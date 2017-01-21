package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
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
import static mbodziony.businesscardsmanager.R.id.radioButton1;

public class SettingsActivity extends AppCompatActivity {

    private Button save;

   // android:onClick="onRadioButtonClicked"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        save = (Button)findViewById(R.id.saveTheme_btn);
            }

    public void saveTheme(View view){

        SharedPreferences settings = getSharedPreferences("Style", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("Style1", true);
        editor.putBoolean("Style2", false);
        editor.putBoolean("Style3", false);
        editor.putBoolean("Style4", false);
//        boolean checked = ((RadioButton) view).isChecked();



//                break;
//            case R.id.radioButton2:
//                if (checked)
//                    editor.putBoolean("Style1", false);
//                editor.putBoolean("Style2", true);
//                editor.putBoolean("Style3", false);
//                editor.putBoolean("Style4", false);
//                break;
//            case R.id.radioButton3:
//                if (checked)
//                    editor.putBoolean("Style1", false);
//                editor.putBoolean("Style2", false);
//                editor.putBoolean("Style3", true);
//                editor.putBoolean("Style4", false);
//                break;
//            case R.id.radioButton4:
//                if (checked)
//                    editor.putBoolean("Style1", false);
//                editor.putBoolean("Style2", false);
//                editor.putBoolean("Style3", false);
//                editor.putBoolean("Style4", true);
//                break;
//        }

        System.out.println("tu sie wywaliło4");
        editor.commit(); // Commit the changes
        Intent showWelcomeActivity = new Intent(this,WelcomeActivity.class);
        startActivity(showWelcomeActivity);

    }


//       //  Check which radio button was clicked
//        switch(view.getId()) {
//            case radioButton1:
//                if (checked)
//                editor.putBoolean("Style1", true);
//                editor.putBoolean("Style2", false);
//                editor.putBoolean("Style3", false);
//                editor.putBoolean("Style4", false);
//                    break;
//            case R.id.radioButton2:
//                if (checked)
//                editor.putBoolean("Style1", false);
//                editor.putBoolean("Style2", true);
//                editor.putBoolean("Style3", false);
//                editor.putBoolean("Style4", false);
//                    break;
//            case R.id.radioButton3:
//                if (checked)
//                editor.putBoolean("Style1", false);
//                editor.putBoolean("Style2", false);
//                editor.putBoolean("Style3", true);
//                editor.putBoolean("Style4", false);
//                break;
//            case R.id.radioButton4:
//                if (checked)
//                editor.putBoolean("Style1", false);
//                editor.putBoolean("Style2", false);
//                editor.putBoolean("Style3", false);
//                editor.putBoolean("Style4", true);
//                break;
//        }
//    }
}
