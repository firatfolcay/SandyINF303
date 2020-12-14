package sandy.android.assistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;

import java.util.ArrayList;

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.MyViewHolder> {

    DatabaseManagement db;
    //ArrayList<Notebook> notebookArrayList;
    Notebook notebook;
    ArrayList<Note> notebookNotes;
    LayoutInflater inflater;
    Editor editor;
    Activity activity;

    public NotebookAdapter(Context context, ArrayList<Note> notebookNotes, DatabaseManagement db) {
        inflater = LayoutInflater.from(context);
        this.notebookNotes = notebookNotes;
        this.db = db;
        this.activity = (Activity) context;
    }

    @Override
    public NotebookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notes_of_notebook_cardview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotebookAdapter.MyViewHolder holder, int position) {
        Note selectedNote = notebookNotes.get(position);
        holder.setData(selectedNote, position);
    }

    @Override
    public int getItemCount() {
        return notebookNotes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView notebookTitle;

        LinearLayout notesOfNotebookLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            notebookTitle = (TextView) itemView.findViewById(R.id.noteOfNotebookTitle);

            //notebookDescription = (TextView) itemView.findViewById(R.id.notificationDescription);

            notesOfNotebookLinearLayout = (LinearLayout) itemView.findViewById(R.id.notesOfNotebookLinearLayout);
            notesOfNotebookLinearLayout.setOnClickListener(this);

        }

        public void setData(Note selectedNotebook, int position) {


            this.notebookTitle.setText(selectedNotebook.getTitle());
            //this.notificationDescription.setText(selectedNote.getNotification().getDate());

        }


        @Override
        public void onClick(View v) {
            if (v == notesOfNotebookLinearLayout) {
                openNoteOfNotebook(getLayoutPosition());
            }

        }

        private void openNoteOfNotebook(int position) {
            Note noteToOpen = notebookNotes.get(position);

            Intent intent = new Intent(activity.getApplicationContext(), NotebookActivity.class);
            intent.putExtra("NOTE_ID", noteToOpen.getId());
            activity.startActivityForResult(intent, 0);

        }

        private void refresh(Notebook n){
            notebookNotes = db.getNotesFromNotebook(n);
        }

        /*public void notifyRemoved(int position) {
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notebookArrayList.size());
        }*/

        public void notifyInserted(int position) {
            notifyItemInserted(position);
            notifyItemRangeChanged(position, notebookNotes.size());
        }

        public void notifyChanged() {
            notifyDataSetChanged();
        }


    }
}
