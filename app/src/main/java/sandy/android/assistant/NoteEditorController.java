package sandy.android.assistant;

import android.os.Bundle;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteEditorController extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    View view;
    boolean isFABOpen = false;
    FloatingActionButton fab_noteeditor_options = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options); //floating action buttons to expand
    FloatingActionButton fab_noteeditor_options_addimage = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_addimage);
    FloatingActionButton fab_noteeditor_options_timer = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_timer);
    FloatingActionButton fab_noteeditor_options_calendar = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_calendar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);

        fab_noteeditor_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
    }

    private void showFABMenu(){
        isFABOpen=true;
        fab_noteeditor_options_addimage.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab_noteeditor_options_timer.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab_noteeditor_options_calendar.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab_noteeditor_options_addimage.animate().translationY(0);
        fab_noteeditor_options_timer.animate().translationY(0);
        fab_noteeditor_options_calendar.animate().translationY(0);
    }
}
