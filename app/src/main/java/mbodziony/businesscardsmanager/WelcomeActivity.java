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
    }

    public void showMyCard(View view){
        Intent showMyCardActivity = new Intent(this,MyCardActivity.class);
        startActivity(showMyCardActivity);
    }
}
