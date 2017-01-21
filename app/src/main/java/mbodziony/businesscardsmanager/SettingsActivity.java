package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import static android.R.attr.checked;

public class SettingsActivity extends AppCompatActivity {

    private Button save;
    String str; // store the text corresponding to  the RadioButton which is clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        save = (Button)findViewById(R.id.saveTheme_btn);

            }

    public void saveTheme(View view){
        Intent showWelcomeActivity = new Intent(this,WelcomeActivity.class);
        showWelcomeActivity.putExtra("radioChosen", str); // pass "str" to the next Activity
        startActivity(showWelcomeActivity);
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButton1:
                if (checked)
                    str = "button1Text";
                System.out.println("coś jest zaznaczone");
                break;
            case R.id.radioButton2:
                if (checked) str = "button2Text";
                break;
            case R.id.radioButton3:
                if (checked) str = "button3Text";
                break;
            case R.id.radioButton4:
                if (checked) str = "button3Text";
                break;
        }
    }
}
