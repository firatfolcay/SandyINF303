package sandy.android.assistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    DatabaseManagement db;
    ArrayList<Note> mNoteList;
    LayoutInflater inflater;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        inflater = LayoutInflater.from(context);
        this.mNoteList = notes;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_note_cardview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note selectedNote = mNoteList.get(position);
        holder.setData(selectedNote, position);

    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteTitle, noteDescription;
        ImageView deleteNote;

        public MyViewHolder(View itemView) {
            super(itemView);
            noteTitle = (TextView) itemView.findViewById(R.id.noteTitle);
            noteDescription = (TextView) itemView.findViewById(R.id.noteDescription);
            deleteNote = (ImageView) itemView.findViewById(R.id.deleteNote);
            deleteNote.setOnClickListener(this);

        }

        public void setData(Note selectedNote, int position) {

            this.noteTitle.setText(selectedNote.getTitle());
            this.noteDescription.setText(selectedNote.getSaveDate());


        }


        @Override
        public void onClick(View v) {


        }


    }
}
