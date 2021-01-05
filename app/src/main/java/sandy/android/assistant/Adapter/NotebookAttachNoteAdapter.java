package sandy.android.assistant.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;

import java.util.ArrayList;

import sandy.android.assistant.Controller.MainActivity;
import sandy.android.assistant.Model.DatabaseManagement;
import sandy.android.assistant.Model.Note;
import sandy.android.assistant.R;

public class NotebookAttachNoteAdapter extends RecyclerView.Adapter<NotebookAttachNoteAdapter.MyViewHolder> {

    DatabaseManagement db;
    ArrayList<Note> notesToAttachList;
    LayoutInflater inflater;
    Activity activity;
    MainActivity mainActivity = new MainActivity();
    MyViewHolder holder;
    public ArrayList<ArrayList> checkedItemsList = new ArrayList<ArrayList>();

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
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotebookAttachNoteAdapter.MyViewHolder holder, int position) {
        Note selectedNote = notesToAttachList.get(position);
        holder.setData(selectedNote, position);
        checkedItemsList.add(holder.getCheckedItems());

    }

    @Override
    public int getItemCount() {
        return notesToAttachList.size();
    }

    public ArrayList<Note> getNotesToAttachList() {

        return notesToAttachList;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteTitle, noteDescription;
        CheckBox noteToAttachCheckBox;
        LinearLayout noteToAttachLinearLayout;
        ArrayList<Integer> checkedItemsOfHolder = new ArrayList<Integer>();

        public MyViewHolder(View itemView) {
            super(itemView);

            noteTitle = (TextView) itemView.findViewById(R.id.noteToAttachTitle);
            noteDescription = (TextView) itemView.findViewById(R.id.noteToAttachDescription);

            noteToAttachLinearLayout = (LinearLayout) itemView.findViewById(R.id.noteToAttachLinearLayout);
            noteToAttachLinearLayout.setOnClickListener(this);

            noteToAttachCheckBox = (CheckBox) itemView.findViewById(R.id.noteToAttachCheckBox);

            noteToAttachCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int adapterPosition = getAdapterPosition();
                    if (isChecked) {
                        checkedItemsOfHolder.add(Integer.valueOf(adapterPosition));
                    }
                    else {
                        checkedItemsOfHolder.remove(Integer.valueOf(adapterPosition));
                    }

                }
            });

        }

        public void setData(Note selectedNote, int position) {

            this.noteTitle.setText(selectedNote.getTitle());
            this.noteDescription.setText(selectedNote.getSaveDate());

        }

        public ArrayList<Integer> getCheckedItems() {
            return checkedItemsOfHolder;
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
