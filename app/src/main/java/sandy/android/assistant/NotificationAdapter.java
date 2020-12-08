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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    DatabaseManagement db;
    ArrayList<Note> mNoteWithNotificationList;
    LayoutInflater inflater;
    Editor editor;
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    public NotificationAdapter(Context context, ArrayList<Note> notesWithNotification, DatabaseManagement db, MainActivity ma) {
        inflater = LayoutInflater.from(context);
        this.mNoteWithNotificationList = notesWithNotification;
        this.db = db;
        this.activity = (Activity) context;
        this.mainActivity = ma;
    }

    public NotificationAdapter(Context context, ArrayList<Note> notesWithNotification, DatabaseManagement db) {
        inflater = LayoutInflater.from(context);
        this.mNoteWithNotificationList = notesWithNotification;
        this.db = db;
        this.activity = (Activity) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification_cardview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note selectedNote = mNoteWithNotificationList.get(position);
        holder.setData(selectedNote, position);
    }

    @Override
    public int getItemCount() {
        return mNoteWithNotificationList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView notificationTitle, notificationDescription;
        ImageView deleteNotification;
        LinearLayout notificationSelectionLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            notificationTitle = (TextView) itemView.findViewById(R.id.notificationTitle);
            notificationDescription = (TextView) itemView.findViewById(R.id.notificationDescription);

            notificationSelectionLinearLayout = (LinearLayout) itemView.findViewById(R.id.notificationSelectionLinearLayout);
            notificationSelectionLinearLayout.setOnClickListener(this);

            deleteNotification = (ImageView) itemView.findViewById(R.id.deleteNotification);
            deleteNotification.setOnClickListener(this);

        }

        public void setData(Note selectedNote, int position) {

            if (selectedNote.getNotification() != null) {
                this.notificationTitle.setText(selectedNote.getTitle());
                this.notificationDescription.setText(selectedNote.getNotification().getDate());
            }
        }


        @Override
        public void onClick(View v) {
            if (v == deleteNotification) {
                deleteNotification(getLayoutPosition());
            }
            /*else if (v == notificationSelectionLinearLayout) {
                openNotification(getLayoutPosition());
            }*/

        }

        private void deleteNotification(int position) {
            Boolean returnVal = false;
            Notification notificationToDelete = mNoteWithNotificationList.get(position).getNotification();
            returnVal = db.deleteNotification(notificationToDelete);
            if (returnVal) {
                mNoteWithNotificationList.remove(position);
                mNoteWithNotificationList = db.getAllNotes();
                notifyRemoved(position);
            }
            else {
                Toast.makeText(activity, "Notification couldn't be deleted.", Toast.LENGTH_LONG);
            }
        }

        /*private void openNote(int position) {
            Note noteToOpen = mNoteList.get(position);

            try {
                db.getNoteFromNoteId(noteToOpen.getId());
                Intent intent = new Intent(mainActivity, NoteEditorActivity.class);
                intent.putExtra("NOTE_ID",noteToOpen.getId());
                mainActivity.startActivity(intent);
            }
            catch(Exception e){
                Toast.makeText(activity, "Selected note couldn't be found.", Toast.LENGTH_LONG);
            }
        }*/

        public void notifyRemoved(int position) {
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mNoteWithNotificationList.size());
        }

        public void notifyInserted(int position) {
            notifyItemInserted(position);
            notifyItemRangeChanged(position, mNoteWithNotificationList.size());
        }

        public void notifyChanged() {
            notifyDataSetChanged();
        }





    }

}
