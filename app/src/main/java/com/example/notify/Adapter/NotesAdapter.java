package com.example.notify.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.R;
import com.example.notify.db.model.NoteModel;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<NoteModel> notes;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnNoteItemClick onNoteItemClick;
    public NotesAdapter(List<NoteModel> notes, Context context) {
        this.notes = notes;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        onNoteItemClick = (OnNoteItemClick) context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_notes,parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NoteModel note = notes.get(position);
        Log.d(TAG, "onBindViewHolder: "+note);
        holder.title.setText(note.getTitle());
        holder.note.setText(note.getContent());
        holder.date.setText(note.getDate());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, note, date;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            note = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }

        @Override
        public void onClick(View view) {
            onNoteItemClick.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteItemClick {
        void onNoteClick(int pos);
    }

}
