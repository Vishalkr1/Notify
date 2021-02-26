package com.example.notify.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notify.R;
import com.example.notify.db.NotesDatabase;
import com.example.notify.db.model.NoteModel;
import com.example.notify.utils.BitmapUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ViewNoteActivity extends AppCompatActivity {
    ImageView image;
    TextView title, date, description;
    private NotesDatabase database;
    private NoteModel note;
    private FloatingActionButton delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        initialiseViews();
        displayNote();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteNote(note);
            }
        });
    }

    private void DeleteNote(NoteModel note) {
        database.getNotesDao().deleteNote(note);
        database.getNotesDao().getNotes().remove(note);
        startActivity(new Intent(ViewNoteActivity.this, NoteListActivity.class));
    }

    private void displayNote() {
        if((note = (NoteModel) getIntent().getSerializableExtra("note"))!=null){
            title.setText(note.getTitle());
            description.setText(note.getContent());
            date.setText(note.getDate());

            if(note.getBackground()!=null) {
                Bitmap bmp = BitmapUtil.StringToBitMap(note.getBackground());
                Log.d("TAG", "displayNote: " + bmp);
                {
                    image.setImageBitmap(bmp);
                }
            }
            else{
                image.setImageResource(R.drawable.sample);
            }
        }
    }

    private void initialiseViews() {
        delete = findViewById(R.id.delete_note);
        image = findViewById(R.id.taskImage);
        title = findViewById(R.id.title);
        description = findViewById(R.id.note);
        date = findViewById(R.id.date);
        database = NotesDatabase.getInstance(this);
//        image.setImageResource(R.drawable.sample);
    }
}