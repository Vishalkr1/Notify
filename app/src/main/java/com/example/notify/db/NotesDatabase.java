package com.example.notify.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.notify.db.Dao.NotesDao;
import com.example.notify.db.model.NoteModel;
import com.example.notify.utils.DateConverter;


@Database(entities = {NoteModel.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao getNotesDao();

    private static NotesDatabase noteDB;

    public static NotesDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }

    private static NotesDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                NotesDatabase.class,
                "Notes_table").allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        noteDB = null;
    }
}
