package com.example.notify.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notify.R;
import com.example.notify.db.NotesDatabase;
import com.example.notify.db.model.NoteModel;
import com.example.notify.utils.AppUtil;
import com.example.notify.utils.BitmapUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class AddNoteActivity extends AppCompatActivity {
    EditText title, description;
    TextView date_created;
    FloatingActionButton save, capture;
    Bitmap upload_photo;
    ImageView photo;
    NoteModel note;
    NotesDatabase database ;
    private String imageSource = null;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initialiseViews();

        capture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                CaptureImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            upload_photo = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(upload_photo);
            //                InputStream is = getContentResolver().openInputStream(imageUri);
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageSource = BitmapUtil.BitMapToString(upload_photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CaptureImage() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private void initialiseViews() {
//        background = Bitmap.createBitmap()
        capture = findViewById(R.id.capture);
        photo = findViewById(R.id.photo);
        title = findViewById(R.id.add_title);
        description = findViewById(R.id.add_note);
        save = findViewById(R.id.fab2);
        date_created = findViewById(R.id.date_created);
        database = NotesDatabase.getInstance(this);
        date_created.setText(AppUtil.getFormattedDateString(AppUtil.getCurrentDateTime()));
        upload_photo = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
    }


    public void AddNote(View view) {
        if(TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(description.getText())){
            Toast.makeText(AddNoteActivity.this, "Empty fields not allowed", Toast.LENGTH_SHORT).show();
        }else {
            final String note_title = title.getText().toString();
            final String note_description = description.getText().toString();
            note = new NoteModel(note_description, note_title, AppUtil.getFormattedDateString(AppUtil.getCurrentDateTime()));
//                    int width = background.getWidth();
//                    int height = background.getHeight();

//                    {
//                    int size = background.getRowBytes() * background.getHeight();
//                    ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//                    background.copyPixelsToBuffer(byteBuffer);
//                    byte[] byteArray = byteBuffer.array();
//                    note.setBackground(byteArray);}
            if(imageSource!=null){
                note.setBackground(imageSource);
            }
            new InsertTask(AddNoteActivity.this, note).execute();
        }
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<AddNoteActivity> activityReference;
        private NoteModel note;

        // only retain a weak reference to the activity
        InsertTask(AddNoteActivity context, NoteModel note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented note id
            long j = activityReference.get().database.getNotesDao().insertNote(note);
            note.setNote_id(j);
            Log.e("ID ", "doInBackground: " + j);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(note, 1);
                activityReference.get().finish();
            }
        }
    }
    private void setResult(NoteModel note, int flag) {
        setResult(flag, new Intent().putExtra("note", note));
        finish();
    }

}