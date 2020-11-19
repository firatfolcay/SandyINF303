package sandy.android.assistant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class NoteEditorController extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView recyclerView;
    View view;
    boolean isFABOpen = false;
    FloatingActionButton fab_noteeditor_options;
    FloatingActionButton fab_noteeditor_options_addimage;
    FloatingActionButton fab_noteeditor_options_timer;
    FloatingActionButton fab_noteeditor_options_calendar;
    EditText noteeditor_multiline_text;
    int multiline_height;
    int multiline_width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);

        isFABOpen = false;
        fab_noteeditor_options = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options); //floating action buttons to expand
        fab_noteeditor_options_addimage = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_addimage);
        fab_noteeditor_options_timer = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_timer);
        fab_noteeditor_options_calendar = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_calendar);
        noteeditor_multiline_text = (EditText) findViewById(R.id.noteeditor_multiline_text);

        fab_noteeditor_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fab_noteeditor_options_addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                float ratio = bitmap.getWidth() / bitmap.getHeight();
                int bitmapHeight = (int) (noteeditor_multiline_text.getHeight() * 0.25);
                int bitmapWidth = (int) (bitmapHeight * ratio);
                bitmap = Bitmap.createScaledBitmap(bitmap,
                        bitmapWidth,
                        bitmapHeight,
                        false);

                Drawable d = new BitmapDrawable(getResources(), bitmap);
                addImageInEditText(d);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFABMenu(){
        System.out.println("showFABMenu");
        isFABOpen=true;
        fab_noteeditor_options_addimage.setVisibility(View.VISIBLE);
        System.out.println("fab visibility:" + fab_noteeditor_options_addimage.getVisibility());
        fab_noteeditor_options_timer.setVisibility(View.VISIBLE);
        fab_noteeditor_options_calendar.setVisibility(View.VISIBLE);
        /*fab_noteeditor_options_addimage.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab_noteeditor_options_timer.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab_noteeditor_options_calendar.animate().translationY(-getResources().getDimension(R.dimen.standard_155));*/
    }

    private void closeFABMenu(){
        System.out.println("closeFABMenu");
        isFABOpen=false;
        /*fab_noteeditor_options_addimage.animate().translationY(0);
        fab_noteeditor_options_timer.animate().translationY(0);
        fab_noteeditor_options_calendar.animate().translationY(0);*/
        fab_noteeditor_options_addimage.setVisibility(View.INVISIBLE);
        fab_noteeditor_options_timer.setVisibility(View.INVISIBLE);
        fab_noteeditor_options_calendar.setVisibility(View.INVISIBLE);
    }


    private void addImageInEditText(Drawable drawable) {
        noteeditor_multiline_text = (EditText) findViewById(R.id.noteeditor_multiline_text);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        int selectionCursorPos = noteeditor_multiline_text.getSelectionStart();
        noteeditor_multiline_text.getText().insert(selectionCursorPos, ".");
        selectionCursorPos = noteeditor_multiline_text.getSelectionStart();
        SpannableStringBuilder builder = new SpannableStringBuilder(noteeditor_multiline_text.getText());
        int startPos = selectionCursorPos - ".".length();
        builder.setSpan(new ImageSpan(drawable), startPos, selectionCursorPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        noteeditor_multiline_text.setText(builder);
        noteeditor_multiline_text.setSelection(selectionCursorPos);
    }

    private void deleteImageFromEditText() {
        String msgEditText = noteeditor_multiline_text.getText().toString();
        System.out.println("text length : " + msgEditText.length());
        if (msgEditText.length() > 0) {
            int selectionCursorPos = noteeditor_multiline_text.getSelectionStart();
            int endPosition = noteeditor_multiline_text.getText().length();
            System.out.println("cursor Pos: " + selectionCursorPos + " endPosition : " + endPosition);

            if (selectionCursorPos > 0) {
                int deletingObjectStartPos = selectionCursorPos - 1;
                noteeditor_multiline_text.getText().delete(deletingObjectStartPos, selectionCursorPos);
                noteeditor_multiline_text.setSelection(deletingObjectStartPos);
            }
        } else {
            noteeditor_multiline_text.setText("");
        }
    }


}
