package com.example.notify.db.model;


import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.notify.utils.AppUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "Notes_table")
public class NoteModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long note_id;

    @ColumnInfo(name = "notes_content")
    private String content;

    private String title;

    private String date;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] background;



    public NoteModel(String content, String title, String date) {
        this.content = content;
        this.title = title;
        this.date = date;
    }

    @Ignore
    public NoteModel() {
    }

    public byte[] getBackground() {
        return background;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public long getNote_id() {
        return note_id;
    }

    public void setNote_id(long note_id) {
        this.note_id = note_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteModel noteModel = (NoteModel) o;
        if (note_id != noteModel.note_id) return false;
        return title != null ? title.equals(noteModel.title) : noteModel.title == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(note_id, content, title, date);
    }

    @Override
    public String toString() {
        return "Note{" +
                "note_id=" + note_id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
