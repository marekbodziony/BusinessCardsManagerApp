package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Activity represents main screen of application "Business Cards Manager".
 *
 * This activity navigates to different application screens depending on user choice.
 *
 * @author Marek Bodziony, Jacek Kułak.
 * Release date: 23.01.2017
 *
 */
public class WelcomeActivity extends AppCompatActivity {
    /**
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);

        /**
         * An ImageView corresponding with application logo
         */
        ImageView img = (ImageView) findViewById(R.id.imageView);

        /**
         * Object "settings" will carry information about actually chosen application color style
         */
        SharedPreferences settings = getSharedPreferences("Style", 0);
        /**
         * Default boolean value used to carry information about application color style1
         */
        boolean Style1 = settings.getBoolean("Style1", false);
        /**
         * Default boolean value used to carry information about application color style2
         */
        boolean Style2 = settings.getBoolean("Style2", false);
        /**
         * Default boolean value used to carry information about application color style3
         */
        boolean Style3 = settings.getBoolean("Style3", false);
        /**
         * Default boolean value used to carry information about application color style4
         */
        boolean Style4 = settings.getBoolean("Style4", false);

        /**
         * Set particular logo depending of application color style selected
         */
        if(Style1==true)
            img.setImageResource(R.drawable.welcome_logo_green);
        if(Style2==true)
            img.setImageResource(R.drawable.welcome_logo_orange);
        if(Style3==true)
            img.setImageResource(R.drawable.welcome_logo_blue);
        if(Style4==true)
            img.setImageResource(R.drawable.welcome_logo_x256);


        // to prevent lunching two applications at the same time when receiving NDEF message via NFC
        // Android will open this app once again because filter is set in Manifest.xml, so current version should be finished
        String intentSrc = getIntent().toString();
        // if Intent was sent via NFC (flg=0x10400000) Android will open app again with Main screen (WelcomeActivity)
        if (intentSrc.contains("flg=0x10400000")){
            Log.d("CardSettings","WelcomeActivity receive Intent from NFC! Activity was finished");
            finish();
        }
        // if Intent was sent via NFC to another Activity  and now it comes back with parameter "action" set to "NFC"
        if (intentSrc.contains("flg=0x4000000")){
            if (getIntent().getStringExtra("action") != null && getIntent().getStringExtra("action").equals("NFC")) {
                Log.d("CardSettings", "WelcomeActivity finished! Intent = " + getIntent().getStringExtra("action"));
                finish();
            }
        }
    }

    /**
     * Method that shows MyCard screen.
     * It creates Intent with details of "MyCardsListActivity.class", put in it information about action performed and start this Intent.
     *
     * @param view The button which was clicked
     */
    public void showMyCard(View view){
        Intent showMyCardActivity = new Intent(this,MyCardsListActivity.class);
        showMyCardActivity.putExtra("action","myCard");
        startActivity(showMyCardActivity);
    }
    /**
     * Method that shows Cards List screen.
     * It creates Intent with details of "CardsListActivity.class", put in it information about action performed and start this Intent.
     *
     * @param view The button which was clicked
     */
    public void showCardsList(View view){
        Intent showCardsListActivity = new Intent(this,CardsListActivity.class);
        showCardsListActivity.putExtra("action","list");
        startActivity(showCardsListActivity);
    }
    /**
     * Method that shows screen where new Card can be added.
     * It creates Intent with details of "EditCardActivity.class", put in it information about action performed and start this Intent.
     *
     * @param view The button which was clicked
     */
    public void showEditScreen (View view){
        Intent showEditScreenActivity = new Intent(this,EditCardActivity.class);
        showEditScreenActivity.putExtra("action","new");
        startActivity(showEditScreenActivity);
    }
    /**
     * Method that shows application color style settings screen.
     * It creates Intent with details of "EditCardActivity.class", put in it information about action performed and start this Intent.
     *
     * @param view The button which was clicked
     */
    public void showSettingsScreen (View view){
        Intent showSettingsScreenActivity = new Intent(this,SettingsActivity.class);
        showSettingsScreenActivity.putExtra("action","settings");
        startActivity(showSettingsScreenActivity);
    }
    /**
     * Method will close application when "turn off" button is clicked.
     *
     * @param view The button which was clicked
     */
    public void turnOff (View view) {
        finish();
    }

    /**
     * Method will close application when "back" button is clicked.
     */
    @Override
    public void onBackPressed(){
        finish();
    }

}