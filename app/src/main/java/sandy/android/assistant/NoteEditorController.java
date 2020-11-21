package sandy.android.assistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
    DatabaseManagement db;
    boolean isFABOpen = false;

    Button button_db;
    FloatingActionButton fab_noteeditor_options;
    FloatingActionButton fab_noteeditor_options_addimage;
    FloatingActionButton fab_noteeditor_options_timer;
    FloatingActionButton fab_noteeditor_options_calendar;

    EditText noteeditor_multiline_text;
    EditText noteeditor_title_text;

    ImageView imageView_colorpicker_red;
    ImageView imageView_colorpicker_black;
    ImageView imageView_back;
    ImageView imageView_save_note;

    int multiline_height;
    int multiline_width;

    String htmlstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);
        DatabaseTest dbt = new DatabaseTest(this);

        isFABOpen = false;      //initialization of attributes that will be used during run of onCreate method
        htmlstring = "";
        button_db = (Button) findViewById(R.id.button_db);
        fab_noteeditor_options = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options);      //initialization of attributes that are referenced into note_editor.xml
        fab_noteeditor_options_addimage = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_addimage);
        fab_noteeditor_options_timer = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_timer);
        fab_noteeditor_options_calendar = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_calendar);

        noteeditor_multiline_text = (EditText) findViewById(R.id.noteeditor_multiline_text);
        noteeditor_title_text = (EditText) findViewById(R.id.noteeditor_title_text);

        imageView_colorpicker_red = (ImageView) findViewById(R.id.imageView_colorpicker_red);
        imageView_colorpicker_black = (ImageView) findViewById(R.id.imageView_colorpicker_black);

        imageView_back = (ImageView) findViewById(R.id.imageView_back);
        imageView_save_note = (ImageView) findViewById(R.id.imageView_save_note);

        fab_noteeditor_options.setOnClickListener(new View.OnClickListener() {      //onClick listener for Noteeditor options floating action button
            @Override
            public void onClick(View v) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {      //onClick listener for Noteeditor back button
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView_colorpicker_black.setOnClickListener(new View.OnClickListener() {     //onClick listener for black color picker
            @Override
            public void onClick(View v) {
                noteeditor_multiline_text.setTextColor(getResources().getColor(R.color.black));
            }
        });

        imageView_colorpicker_red.setOnClickListener(new View.OnClickListener() {       //onClick listener for red color picker
            @Override
            public void onClick(View v) {
                noteeditor_multiline_text.setTextColor(getResources().getColor(R.color.red));
            }
        });

        imageView_save_note.setOnClickListener(new View.OnClickListener() {     //onClick listener for save note button in noteeditor.
            @Override
            public void onClick(View v) {
                SpannableStringBuilder builder = new SpannableStringBuilder(noteeditor_multiline_text.getText());
                htmlstring = Html.toHtml(builder);
                System.out.println("last HTML: " + htmlstring);
                if (noteeditor_multiline_text.getText().length() < 1) {
                    //todo
                }
                else {
                    dbt.insertNote(noteeditor_title_text.getText().toString(), htmlstring);
                }
            }
        });

        /*button_db.setOnClickListener(new View.OnClickListener() {       //onClick listener for fetchContent() database test operation
            @Override
            public void onClick(View v) {
                String fromHtml = dbt.fetchContent();
                System.out.println("fromHTML: " + fromHtml);
                noteeditor_multiline_text.setText(Html.fromHtml(fromHtml));
            }
        });*/

        fab_noteeditor_options_addimage.setOnClickListener(new View.OnClickListener() {     //onClick listener for add image function
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);       //initialization of new intent that launches External Storage browser
                startActivityForResult(intent, 0);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //operations to select new image from external storage browser
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            //System.out.println("targetUri: " + targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));       //fetches bitmap from InputStream
                float ratio = bitmap.getWidth() / bitmap.getHeight();                                   //aspect ratio function for scaling of bitmap
                int bitmapHeight = (int) (noteeditor_multiline_text.getHeight() * 0.25);
                int bitmapWidth = (int) (bitmapHeight * ratio);
                bitmap = Bitmap.createScaledBitmap(bitmap,                                          //creates new scaled bitmap
                        bitmapWidth,
                        bitmapHeight,
                        false);

                Drawable d = new BitmapDrawable(getResources(), bitmap);                        //converts bitmap to drawable format and calls addImageInText method
                addImageInEditText(d);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFABMenu(){         //method that makes sub-FAB menus visible
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

    private void closeFABMenu(){        //method that makes sub-FAB menus invisible
        System.out.println("closeFABMenu");
        isFABOpen=false;
        /*fab_noteeditor_options_addimage.animate().translationY(0);
        fab_noteeditor_options_timer.animate().translationY(0);
        fab_noteeditor_options_calendar.animate().translationY(0);*/
        fab_noteeditor_options_addimage.setVisibility(View.INVISIBLE);
        fab_noteeditor_options_timer.setVisibility(View.INVISIBLE);
        fab_noteeditor_options_calendar.setVisibility(View.INVISIBLE);
    }


    private void addImageInEditText(Drawable drawable) {        //method to add selected image from external storage inside Note Editor EditText component
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

    private void deleteImageFromEditText() {         //method to delete selected image from Note Editor EditText component
        String msgEditText = noteeditor_multiline_text.getText().toString();
        if (msgEditText.length() > 0) {
            int selectionCursorPos = noteeditor_multiline_text.getSelectionStart();
            int endPosition = noteeditor_multiline_text.getText().length();

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
