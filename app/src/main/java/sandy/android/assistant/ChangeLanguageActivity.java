package sandy.android.assistant;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import com.akexorcist.localizationactivity.ui.LocalizationActivity;

public class ChangeLanguageActivity extends LocalizationActivity {
    Button button_en;
    Button button_tr;
    Button button_save;
    Button button_cancel;

    String selectedLang = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language);

        button_en = findViewById(R.id.button_en);
        button_tr = findViewById(R.id.button_tr);
        button_save = findViewById(R.id.button_save_lang);
        button_cancel = findViewById(R.id.button_cancel_lang);

        button_en.setOnClickListener(l->{
            button_en.setBackgroundColor(getResources().getColor(R.color.darkBlue));
            button_en.setTextColor(getResources().getColor(R.color.white));

            button_tr.setBackgroundColor(getResources().getColor(R.color.white));
            button_tr.setTextColor(getResources().getColor(R.color.black));

            selectedLang = button_en.getText().toString();
        });

        button_tr.setOnClickListener(l->{
            button_tr.setBackgroundColor(getResources().getColor(R.color.darkBlue));
            button_tr.setTextColor(getResources().getColor(R.color.white));

            button_en.setBackgroundColor(getResources().getColor(R.color.white));
            button_en.setTextColor(getResources().getColor(R.color.black));

            selectedLang = button_tr.getText().toString();
        });

        button_save.setOnClickListener(l->{
            if(selectedLang.contentEquals("")){
                //
            }
            else if(selectedLang.contentEquals(getResources().getString(R.string.language_en))){
                setLanguage("en");
            }
            else if(selectedLang.contentEquals(getResources().getString(R.string.language_tr))){
                setLanguage("tr");
            }

            setResult(RESULT_OK);
            finish();
        });

        button_cancel.setOnClickListener(l->{
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
