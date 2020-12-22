package sandy.android.assistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;

import java.util.ArrayList;

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.MyViewHolder> {

    DatabaseManagement db;
    //ArrayList<Notebook> notebookArrayList;
    Notebook notebook;
    ArrayList<Note> notebookNotes;
    LayoutInflater inflater;
    Activity activity;
    View root;


    public NotebookAdapter(Context context, ArrayList<Note> notebookNotes, Notebook notebook, View view, DatabaseManagement db) {
        inflater = LayoutInflater.from(context);
        this.notebookNotes = notebookNotes;
        this.notebook = notebook;
        this.root = view;
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
        ImageView deleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            notebookTitle = (TextView) itemView.findViewById(R.id.noteOfNotebookTitle);

            //notebookDescription = (TextView) itemView.findViewById(R.id.notificationDescription);

            notesOfNotebookLinearLayout = (LinearLayout) itemView.findViewById(R.id.notesOfNotebookLinearLayout);
            notesOfNotebookLinearLayout.setOnClickListener(this);

            deleteButton = itemView.findViewById(R.id.notesOfNotebookDelete);
            deleteButton.setOnClickListener(this);

        }

        public void setData(Note selectedNotebook, int position) {


            this.notebookTitle.setText(selectedNotebook.getTitle());
            //this.notificationDescription.setText(selectedNote.getNotification().getDate());

        }


        @Override
        public void onClick(View v) {
            if(v == deleteButton){
                deleteNote(getLayoutPosition());
            }
            if (v == notesOfNotebookLinearLayout) {
                openNote(getLayoutPosition(), root);
            }

        }

        private void openNote(int position, View v) {
            Note note = notebookNotes.get(position);

            View popupView = inflater.inflate(R.layout.notebook_note_popup_view, null);

            Editor renderer = popupView.findViewById(R.id.notebookPopupRenderer);
            renderer.render(note.getContent());

            TextView title = popupView.findViewById(R.id.notebookPopupTitle);
            title.setText(note.getTitle());
            title.setEnabled(false);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        }

        private void deleteNote(int position){
            //if you spam the delete button it might throw out of bounds exception
            try {
                Note note = notebookNotes.get(position);
                db.removeNoteFromNotebook(note);
                notebookNotes.remove(position);
                //refresh();
                notifyRemoved(position);
            }
            catch (Exception e){

            }
        }

        private void refresh(){
            notebookNotes = db.getNotesFromNotebook(notebook);
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

        public void notifyRemoved(int position) {
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notebookNotes.size());
        }


    }
}
