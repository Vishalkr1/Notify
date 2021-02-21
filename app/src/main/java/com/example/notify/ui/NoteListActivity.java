package com.example.notify.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.notify.Adapter.NotesAdapter;
import com.example.notify.R;
import com.example.notify.db.NotesDatabase;
import com.example.notify.db.model.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity implements NotesAdapter.OnNoteItemClick{
    private static final String TAG = "NoteList";
    private RecyclerView notesList;
    private NotesDatabase database;
    private List<NoteModel> notes;
    private NotesAdapter adapter;
    private int pos;
    private ImageView emptyList;
    private FloatingActionButton add_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        intitialiseViews();
        displayNotes();
    }


    private void intitialiseViews() {
        emptyList = findViewById(R.id.emptyList);
        add_note = findViewById(R.id.fab);
        notesList = findViewById(R.id.recycler_view);
        notesList.setLayoutManager(new LinearLayoutManager(this));
        notes = new ArrayList<>();
        adapter = new NotesAdapter(notes, this);
        notesList.setAdapter(adapter);
    }

    public void AddNewNote(View view) {
        startActivityForResult(new Intent(NoteListActivity.this, AddNoteActivity.class), 100);
    }


    private void UpdateEmptyView() {
        if(notes.size()==0){
            emptyList.setVisibility(View.VISIBLE);
            notesList.setVisibility(View.GONE);
        }else{
            emptyList.setVisibility(View.GONE);
            notesList.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void displayNotes() {
        Log.d(TAG, "displayNotes: logged ini to display notes");
        database = NotesDatabase.getInstance(NoteListActivity.this);
        new RetrieveTask(this).execute();
    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<NoteModel>> {
        private WeakReference<NoteListActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(NoteListActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<NoteModel> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().database.getNotesDao().getNotes();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<NoteModel> notes) {
            if (notes != null && notes.size() > 0) {
                activityReference.get().notes.clear();
                activityReference.get().notes.addAll(notes);
                activityReference.get().adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                notes.add((NoteModel) data.getSerializableExtra("note"));
            } else if (resultCode == 2) {
                notes.set(pos, (NoteModel) data.getSerializableExtra("note"));
            }
        }
        UpdateEmptyView();
    }

    @Override
    protected void onDestroy() {
        database.cleanUp();
        super.onDestroy();
    }

    @Override
    public void onNoteClick(int pos) {
        startActivityForResult(new Intent(this, ViewNoteActivity.class).putExtra("note",notes.get(pos)), 100);
    }
}