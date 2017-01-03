package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

        myCard = (Button)findViewById(R.id.my_card_btn);
        add =  (Button)findViewById(R.id.add_btn);
        turnOff = (Button)findViewById(R.id.turn_off_btn);
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

    public void showAddScreen (View view){
        Intent showAddScreenActivity = new Intent(this,AddCardActivity.class);
        startActivity(showAddScreenActivity);
    }

    public void turnOff (View view) {

        finish();
    }

    // when "back" button is pressed do nothing
    @Override
    public void onBackPressed(){}

}
