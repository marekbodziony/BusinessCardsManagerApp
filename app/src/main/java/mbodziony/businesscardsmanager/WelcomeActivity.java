package mbodziony.businesscardsmanager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

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

    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] ndefIntentFilters;

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

        /**
         * NfcAdapter and Intent filter for disabling duplicate app running when receiving Intent from NDEF message
         */
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this,0,new Intent(this,getClass()).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter ndefFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefFilter.addDataType("text/plain");                   // set payload type which interests us in NDEF message
            ndefIntentFilters = new IntentFilter[]{ndefFilter};
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
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
    /**
     * Enable foreground dispatcher for handling Intent from NDEF message
     */
    @Override
    public void onResume(){
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this,nfcPendingIntent,ndefIntentFilters,null);
    }
    /**
     * Disable foreground dispatcher for handling Intent from NDEF message
     */
    @Override
    public void onPause(){
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }
    /**
     * Handle Intent from NDEF message - forward to CardListActivity
    */
    @Override
    public void onNewIntent (Intent intent){
        Card c = getCardFromNdefMessage(intent);
        startActivity(putCardInfoToIntent(c));
    }

    /**
     * Method gets Card info from NDEF message
     *
     * @param intent The Intent from NDEF message
     * @return card Card object
     */
    private Card getCardFromNdefMessage(Intent intent){
        Card card = null;
        NdefMessage[] msgs = null;
        Parcelable[] rawMsg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsg != null){
            msgs = new NdefMessage[rawMsg.length];
            for (int i = 0; i < rawMsg.length; i++){
                msgs[i] = (NdefMessage)rawMsg[i];
            }
        }
        // if there will be 3 NDEF records in NDEF message take payload from first two records (card details and logoImg)
        if (msgs[0].getRecords().length == 3){
            card = getCardFromJSON(msgs[0].getRecords()[0].getPayload());
            card.setLogoImgPath(new String(msgs[0].getRecords()[1].getPayload()));
            card.setId(0);
        }
        // if there will be 2 NDEF records in NDEF message take payload from first record (card details)
        else if (msgs[0].getRecords().length == 2){
            card = getCardFromJSON(msgs[0].getRecords()[0].getPayload());
            card.setId(0);
        }
        // if there will less than 2 NDEF records in NDEF message set default values
        else{
            card = new Card("null","card_name","card_mobile","card_phone","card_fax","card_email","card_web","card_company",
                    "card_address","card_job","card_facebook","card_tweeter","card_skype","card_other");
            card.setId(0);
        }

        Log.d("CardNFC","Received NDEF message (" + msgs[0].getRecords().length + " records)");

        return card;
    }

    /**
     * Method gets Card information from JSON file
     *
     * @param payload_card_details Information in byte[] with Card details
     * @return card Card object
     */
    private Card getCardFromJSON(byte[] payload_card_details){

        Card card = null;
        try{
            JSONObject cardJSON = new JSONObject(new String(payload_card_details));
            String logoPath = cardJSON.getString("logoPath");
            String name = cardJSON.getString("name");
            String mobile = cardJSON.getString("mobile");
            String phone = cardJSON.getString("phone");
            String fax = cardJSON.getString("fax");
            String email = cardJSON.getString("email");
            String web = cardJSON.getString("web");
            String company = cardJSON.getString("company");
            String address = cardJSON.getString("address");
            String job = cardJSON.getString("job");
            String facebook = cardJSON.getString("facebook");
            String tweeter = cardJSON.getString("tweeter");
            String skype = cardJSON.getString("skype");
            String other = cardJSON.getString("other");

            card = new Card(logoPath,name,mobile,phone,fax,email,web,company,address,job,facebook,tweeter,skype,other);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d("CardNFC","Card from JSON created");
        return card;
    }
    /**
     * Method puts Card details to Intent
     *
     * @param card Card object
     * @return intent Intent object witch will be sent to CardsListActivity
     */
    // private method put MyCard data (fields) to Intent object
    private Intent putCardInfoToIntent(Card card){
        Intent i = new Intent(this,CardsListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("action","newNFC");
        i.putExtra("logoPath","null");  // receiving Logo via NFC not supported in this app version
        i.putExtra("name",card.getName());
        i.putExtra("mobile",card.getMobile());
        i.putExtra("phone",card.getPhone());
        i.putExtra("fax",card.getFax());
        i.putExtra("email",card.getEmail());
        i.putExtra("web",card.getWeb());
        i.putExtra("company",card.getCompany());
        i.putExtra("address",card.getAddress());
        i.putExtra("job",card.getJob());
        i.putExtra("facebook",card.getFacebook());
        i.putExtra("tweeter",card.getTweeter());
        i.putExtra("skype",card.getSkype());
        i.putExtra("other",card.getOther());
        return i;
    }
}