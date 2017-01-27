package mbodziony.businesscardsmanager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
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

import org.json.JSONObject;

import static android.R.attr.checked;
import static mbodziony.businesscardsmanager.R.id.radioButton1;

public class SettingsActivity extends AppCompatActivity {

    private Button save;
    boolean style1;
    boolean style2;
    boolean style3;
    boolean style4;

    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] ndefIntentFilters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        save = (Button)findViewById(R.id.saveTheme_btn);

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