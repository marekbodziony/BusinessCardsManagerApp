package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddCardActivity extends AppCompatActivity {

    private Button home;
    private Button back;
    private Button addnew1;
    private Button addnew2;
    private Button bluetooth1;
    private Button bluetooth2;
    private Button NFC1;
    private Button NFC2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screen);

        home = (Button)findViewById(R.id.home_btn);
        back = (Button)findViewById(R.id.back_btn);
        addnew1 = (Button)findViewById(R.id.create_new_btn);
        addnew2 = (Button)findViewById(R.id.create_new_btnbox);
        bluetooth1 = (Button)findViewById(R.id.add_bluetooth_btn);
        bluetooth2 = (Button)findViewById(R.id.add_bluetooth_btnbox);
        NFC1 = (Button)findViewById(R.id.add_NFC_btn);
        NFC2 = (Button)findViewById(R.id.add_NFC_btnbox);
    }

    public void showHomeScreen (View view){
        Intent showHomeScreenActivity = new Intent(this,WelcomeActivity.class);
        startActivity(showHomeScreenActivity);
    }

    public void createNewCard(View view){
        Intent createNewCardIntent = new Intent(this,EditCardActivity.class);
        createNewCardIntent.putExtra("action","new");
        startActivity(createNewCardIntent);
    }
}
