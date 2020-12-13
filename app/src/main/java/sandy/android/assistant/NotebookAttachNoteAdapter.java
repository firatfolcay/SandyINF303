package sandy.android.assistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;

import java.util.ArrayList;

public class NotebookAttachNoteAdapter extends RecyclerView.Adapter<NotebookAttachNoteAdapter.MyViewHolder> {

    DatabaseManagement db;
    ArrayList<Note> notesToAttachList;
    LayoutInflater inflater;
    Editor editor;
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    public NotebookAttachNoteAdapter(Context context, ArrayList<Note> notes, DatabaseManagement db, MainActivity ma) {
        inflater = LayoutInflater.from(context);
        this.notesToAttachList = notes;
        this.db = db;
        this.activity = (Activity) context;
        this.mainActivity = ma;
    }

    public NotebookAttachNoteAdapter(Context context, ArrayList<Note> notes, DatabaseManagement db) {
        inflater = LayoutInflater.from(context);
        this.notesToAttachList = notes;
        this.db = db;
        this.activity = (Activity) context;
    }


    @Override
    public NotebookAttachNoteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notebook_attach_note_cardview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotebookAttachNoteAdapter.MyViewHolder holder, int position) {
        Note selectedNote = notesToAttachList.get(position);
        holder.setData(selectedNote, position);

    }

    @Override
    public int getItemCount() {
        return notesToAttachList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteTitle, noteDescription;
        CheckBox noteToAttachCheckBox;
        LinearLayout noteToAttachLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            noteTitle = (TextView) itemView.findViewById(R.id.noteTitle);
            noteDescription = (TextView) itemView.findViewById(R.id.noteDescription);

            noteToAttachLinearLayout = (LinearLayout) itemView.findViewById(R.id.noteToAttachLinearLayout);
            noteToAttachLinearLayout.setOnClickListener(this);

            noteToAttachCheckBox = (CheckBox) itemView.findViewById(R.id.noteToAttachCheckBox);

        }

        public void setData(Note selectedNote, int position) {

            this.noteTitle.setText(selectedNote.getTitle());
            this.noteDescription.setText(selectedNote.getSaveDate());

        }


        @Override
        public void onClick(View v) {
            if (v == noteToAttachLinearLayout) {
                noteToAttachCheckBox.setChecked(!noteToAttachCheckBox.isChecked());
            }

        }

        public void notifyChanged() {
            notifyDataSetChanged();
        }





    }
}
