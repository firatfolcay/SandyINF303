package sandy.android.assistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;

import java.time.Duration;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    DatabaseManagement db;
    ArrayList<Note> mNoteList;
    LayoutInflater inflater;
    Editor editor;
    Activity activity;
    MainActivity mainActivity = new MainActivity();
    NoteEditorActivity noteEditorActivity = new NoteEditorActivity();

    public NoteAdapter(Context context, ArrayList<Note> notes, DatabaseManagement db, MainActivity ma) {
        inflater = LayoutInflater.from(context);
        this.mNoteList = notes;
        this.db = db;
        this.activity = (Activity) context;
        this.mainActivity = ma;
    }

    public NoteAdapter(Context context, ArrayList<Note> notes, DatabaseManagement db) {
        inflater = LayoutInflater.from(context);
        this.mNoteList = notes;
        this.db = db;
        this.activity = (Activity) context;
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
        LinearLayout noteSelectionLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            noteTitle = (TextView) itemView.findViewById(R.id.noteTitle);
            noteDescription = (TextView) itemView.findViewById(R.id.noteDescription);

            noteSelectionLinearLayout = (LinearLayout) itemView.findViewById(R.id.noteSelectionLinearLayout);
            noteSelectionLinearLayout.setOnClickListener(this);

            deleteNote = (ImageView) itemView.findViewById(R.id.deleteNote);
            deleteNote.setOnClickListener(this);

        }

        public void setData(Note selectedNote, int position) {

            this.noteTitle.setText(selectedNote.getTitle());
            this.noteDescription.setText(selectedNote.getSaveDate());

        }


        @Override
        public void onClick(View v) {
            if (v == deleteNote) {
                deleteNote(getLayoutPosition());
            }
            else if (v == noteSelectionLinearLayout) {
                openNote(getLayoutPosition());
            }

        }

        private void deleteNote(int position) {
            Boolean returnVal = false;
            Note noteToDelete = mNoteList.get(position);
            returnVal = db.deleteNote(noteToDelete);
            if (returnVal) {
                mNoteList.remove(position);
                mNoteList = db.getAllNotes();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mNoteList.size());
            }
            else {
                Toast.makeText(activity, "Note couldn't be deleted.", Toast.LENGTH_LONG);
            }
        }

        private void openNote(int position) {
            Note noteToOpen = mNoteList.get(position);
            ArrayList<Note> allNotesFromDB = db.getAllNotes();

            for (int i = 0; i < allNotesFromDB.size(); i++) {
                if (noteToOpen.getId() == allNotesFromDB.get(i).getId()){
                    Intent intent = new Intent(mainActivity, NoteEditorActivity.class);
                    intent.putExtra("NOTES_FROM_DB_TITLE", allNotesFromDB.get(i).getTitle());
                    intent.putExtra("NOTES_FROM_DB_CONTENT", allNotesFromDB.get(i).getContent());
                    mainActivity.startActivity(intent);
                }
                else {
                    Toast.makeText(activity, "Selected note couldn't be found in DB.", Toast.LENGTH_LONG);
                }
            }
        }





    }
}
