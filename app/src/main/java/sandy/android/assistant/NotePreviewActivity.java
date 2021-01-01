package sandy.android.assistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.irshulx.Editor;

public class NotePreviewActivity extends AppCompatActivity {
    EditText notePreviewTitle;
    Editor notePreviewRenderer;
    ImageView buttonBackToMainFromPreview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_preview_view);

        buttonBackToMainFromPreview = findViewById(R.id.buttonBackToMainActivityFromPreview);
        notePreviewTitle = findViewById(R.id.notePreviewTitle);
        notePreviewRenderer = findViewById(R.id.notePreviewRenderer);
        Bundle b = getIntent().getExtras();
        if(b != null){
            if(b.get("calendar_notification_note_title") != null){
                notePreviewTitle.setText(b.getString("calendar_notification_note_title"));
                notePreviewRenderer.render(b.getString("calendar_notification_note_content"));
            }
        }

        buttonBackToMainFromPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
