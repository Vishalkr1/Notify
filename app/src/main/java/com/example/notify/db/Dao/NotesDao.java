package com.example.notify.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.notify.db.model.NoteModel;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM Notes_table order by note_id ASC")
    List<NoteModel> getNotes();

    @Query("SELECT * FROM Notes_table WHERE note_id =:noteId")
    NoteModel getNote(int noteId);

    @Insert
    long insertNote(NoteModel note);

    @Update
    void updateNote(NoteModel newNote);

    @Delete
    void deleteNote(NoteModel note);

    @Delete
    void deleteAll(NoteModel... notes);


}
