package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    private Button settings;
    private Button turnOff;
    private Button cards;
    private Button add;
    private Button myCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);
        ImageView img = (ImageView) findViewById(R.id.imageView);
 //       img.setImageResource(R.drawable.welcome_logo_blue);


        myCard = (Button)findViewById(R.id.my_card_btn);
        add =  (Button)findViewById(R.id.add_btn);
        turnOff = (Button)findViewById(R.id.turn_off_btn);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String message= extras.getString("radioChosen");
        }
    }

    public void showMyCard(View view){
        Intent showMyCardActivity = new Intent(this,MyCardsListActivity.class);
        showMyCardActivity.putExtra("action","myCard");
        startActivity(showMyCardActivity);
    }
    //
    public void showCardsList(View view){
        Intent showCardsListActivity = new Intent(this,CardsListActivity.class);
        showCardsListActivity.putExtra("action","list");
        startActivity(showCardsListActivity);
    }

    public void showEditScreen (View view){
        Intent showEditScreenActivity = new Intent(this,EditCardActivity.class);
        startActivity(showEditScreenActivity);
    }

    public void showSettingsScreen (View view){
        Intent showSettingsScreenActivity = new Intent(this,SettingsActivity.class);
        startActivity(showSettingsScreenActivity);
    }

    public void turnOff (View view) {
        finish();
    }

    // when "back" button is pressed do nothing
    @Override
    public void onBackPressed(){
        finish();
    }

}
