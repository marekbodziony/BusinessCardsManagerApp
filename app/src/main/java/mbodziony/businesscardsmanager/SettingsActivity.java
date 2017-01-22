package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import static android.R.attr.checked;
import static mbodziony.businesscardsmanager.R.id.radioButton1;

public class SettingsActivity extends AppCompatActivity {

    private Button save;
    boolean style1;
    boolean style2;
    boolean style3;
    boolean style4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        save = (Button)findViewById(R.id.saveTheme_btn);
    }

    public void saveTheme(View view){

        // save style value in internal memory
        SharedPreferences settings = getSharedPreferences("Style", 0);
        SharedPreferences.Editor editor = settings.edit();
        // value style is set when radiobutton is selected
        editor.putBoolean("Style1", style1);
        editor.putBoolean("Style2", style2);
        editor.putBoolean("Style3", style3);
        editor.putBoolean("Style4", style4);
        editor.commit(); // Commit the changes
        Intent showWelcomeActivity = new Intent(this,WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(showWelcomeActivity);

    }

    public void onRadioButtonClicked(View view) {
        // check whether the button is currrently checked
        boolean checked = ((RadioButton) view).isChecked();

        // check which radio button was clicked and set bool value that will be used in preferences
        switch(view.getId()) {
            case radioButton1:
                if (checked)
                    style1 = true;
                style2 = false;
                style3 = false;
                style4 = false;
                break;
            case R.id.radioButton2:
                if (checked)
                    style1 = false;
                style2 = true;
                style3 = false;
                style4 = false;
                break;
            case R.id.radioButton3:
                if (checked)
                    style1 = false;
                style2 = false;
                style3 = true;
                style4 = false;
                break;
            case R.id.radioButton4:
                if (checked)
                    style1 = false;
                style2 = false;
                style3 = false;
                style4 = true;
                break;
        }
    }
}