package mbodziony.businesscardsmanager;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] ndefIntentFilters;

    private static boolean closeShareActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setTitle("Udostępnij przez :");

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


    // method to share Card via NFC
    public void shareViaNfc (View view){
        // if device not support NFC - display information about this to the user
        if (nfcAdapter == null) {
            Toast.makeText(this,"NFC not supported at your device!",Toast.LENGTH_SHORT).show();
            finish();
        }
        // if device support NFC but it's disable at the moment - go to NFC Settings
        else if (!nfcAdapter.isEnabled()){
            Toast.makeText(this,"Turn on NFC",Toast.LENGTH_LONG).show();
            closeShareActivity = true;          // user goes to NFC Settings, Activity can be close if come back after turning-on NFC
            Intent gotoNfcSettings = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(gotoNfcSettings);
        }
        // if user choose to share Card via NFC while NFC is on - display info to tap devices together
        else {
            Toast.makeText(this,"Tap your device with other device to share your Card via NFC",Toast.LENGTH_LONG).show();
            finish();
        }
    }


    // method will close this Activity when back from NFC Settings and NFC was turn-on by user
    private void closeThisActivityIfDontNeed(){
        if (nfcAdapter.isEnabled() && closeShareActivity){
            closeShareActivity = false;     // don't close this Activity next time
            Toast.makeText(this,"Tap your devices together to send a Card.\nIt will start automaticly.",Toast.LENGTH_LONG).show();
            finish();
        }
        closeShareActivity = false;         // don't close this Activity next time
    }

    /**
     * Enable foreground dispatcher for handling Intent from NDEF message
     */
    @Override
    public void onResume(){
        super.onResume();
        closeThisActivityIfDontNeed();     // close this Activity when user come back from Settings and turn-on NFC
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

    // private method gets Card from Intent
    private Card getCardFromIntent() {
        Intent intent = getIntent();
        Card card = new Card(intent.getStringExtra("logoPath"), intent.getStringExtra("name"), intent.getStringExtra("mobile"), intent.getStringExtra("phone"),
                intent.getStringExtra("fax"), intent.getStringExtra("email"), intent.getStringExtra("web"),
                intent.getStringExtra("company"), intent.getStringExtra("address"), intent.getStringExtra("job"),
                intent.getStringExtra("facebook"), intent.getStringExtra("tweeter"), intent.getStringExtra("skype"),
                intent.getStringExtra("other"));
        card.setId(intent.getLongExtra("id", 0));
        return card;
    }

    // write Card to NFC tag
    public void writeToNfcTag(View view){
        // if device not support NFC - display information about this to the user
        if (nfcAdapter == null) {
            Toast.makeText(this,"NFC not supported at your device!",Toast.LENGTH_SHORT).show();
            finish();
        }
        // if device support NFC but it's disable at the moment - go to NFC Settings
        else if (!nfcAdapter.isEnabled()){
            Toast.makeText(this,"Turn on NFC",Toast.LENGTH_LONG).show();
            closeShareActivity = true;          // user goes to NFC Settings, Activity can be close if come back after turning-on NFC
            Intent gotoNfcSettings = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(gotoNfcSettings);
        }
        // if user choose to write Card to Tag
        else {
//            Intent writeToTag = putCardInfoToIntent(getCardFromIntent()).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            writeToTag.putExtra("action","writeToTag");
//            startActivity(writeToTag);
            finish();
        }
    }
}
