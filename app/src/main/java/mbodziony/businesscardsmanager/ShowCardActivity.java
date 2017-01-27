package mbodziony.businesscardsmanager;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ShowCardActivity extends AppCompatActivity {

    private Card myCard;
    private Intent cardIntent;
    JSONObject cardJSON = new JSONObject();

    private long id;
    private ImageView logo;
    private TextView name;
    private TextView mobileTxt;
    private TextView mobile;
    private TextView phoneTxt;
    private TextView phone;
    private TextView faxTxt;
    private TextView fax;
    private TextView emailTxt;
    private TextView email;
    private TextView webTxt;
    private TextView web;
    private TextView companyTxt;
    private TextView company;
    private TextView addressTxt;
    private TextView address;
    private TextView jobTxt;
    private TextView job;
    private TextView facebookTxt;
    private TextView facebook;
    private TextView tweeterTxt;
    private TextView tweeter;
    private TextView skypeTxt;
    private TextView skype;
    private TextView otherTxt;
    private TextView other;

    private Button shareBluetooth;
    // set how long device should be visible
    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;

    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] ndefIntentFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card);

        logo = (ImageView)findViewById(R.id.myCard_logo);
        name = (TextView)findViewById(R.id.myCard_nameVal);
        mobileTxt = (TextView)findViewById(R.id.myCard_phoneTxt);
        mobile = (TextView)findViewById(R.id.myCard_mobileVal);
        phoneTxt = (TextView)findViewById(R.id.myCard_phoneTxt);
        phone = (TextView)findViewById(R.id.myCard_phoneVal);
        faxTxt = (TextView)findViewById(R.id.myCard_faxTxt);
        fax = (TextView)findViewById(R.id.myCard_faxVal);
        emailTxt = (TextView)findViewById(R.id.myCard_emailTxt);
        email = (TextView)findViewById(R.id.myCard_emailVal);
        webTxt = (TextView)findViewById(R.id.myCard_webTxt);
        web = (TextView)findViewById(R.id.myCard_webVal);
        companyTxt = (TextView)findViewById(R.id.myCard_companyTxt);
        company = (TextView)findViewById(R.id.myCard_companyVal);
        addressTxt = (TextView)findViewById(R.id.myCard_addressTxt);
        address = (TextView)findViewById(R.id.myCard_addressVal);
        jobTxt = (TextView)findViewById(R.id.myCard_jobTxt);
        job = (TextView)findViewById(R.id.myCard_jobVal);
        facebookTxt = (TextView)findViewById(R.id.myCard_facebookTxt);
        facebook = (TextView)findViewById(R.id.myCard_facebookVal);
        tweeterTxt = (TextView)findViewById(R.id.myCard_tweeterTxt);
        tweeter = (TextView)findViewById(R.id.myCard_tweeterVal);
        skypeTxt = (TextView)findViewById(R.id.myCard_skypeTxt);
        skype = (TextView)findViewById(R.id.myCard_skypeVal);
        otherTxt = (TextView)findViewById(R.id.myCard_otherTxt);
        other = (TextView)findViewById(R.id.myCard_otherVal);

        shareBluetooth = (Button)findViewById(R.id.bluetooth_btn);

        // set values of Card object taken from Intent (and hide empty fields)
        setMyCardValues();

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

    // delete MyCard
    public void deleteMyCard(View view){
        new AlertDialog.Builder(this).setTitle("Usunąć wizytówkę ?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String action = cardIntent.getStringExtra("action");
                        if (action.equals("myCard")) cardIntent.setClass(getApplicationContext(), MyCardsListActivity.class);
                        else if (action.equals("cardFromList")) cardIntent.setClass(getApplicationContext(), CardsListActivity.class);
                        putCardInfoToIntent();
                        cardIntent.putExtra("action","delete");     // Card from this Intent should be deleted in database
                        cardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(cardIntent);
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .show();
    }
    // edit MyCard
    public void editMyCard(View view){
        cardIntent.setClass(this, EditCardActivity.class);
        putCardInfoToIntent();
        startActivity(cardIntent);
    }

    // private method to set all values of MyCard taken from Intent
    private void setMyCardValues(){
        cardIntent = getIntent();
        //if user edited Card or create new Card then take values from Intent
        myCard = new Card(cardIntent.getStringExtra("logoPath"), cardIntent.getStringExtra("name"), cardIntent.getStringExtra("mobile"), cardIntent.getStringExtra("phone"),
                cardIntent.getStringExtra("fax"), cardIntent.getStringExtra("email"), cardIntent.getStringExtra("web"),
                cardIntent.getStringExtra("company"), cardIntent.getStringExtra("address"), cardIntent.getStringExtra("job"),
                cardIntent.getStringExtra("facebook"), cardIntent.getStringExtra("tweeter"), cardIntent.getStringExtra("skype"),
                cardIntent.getStringExtra("other"));
        myCard.setId(cardIntent.getLongExtra("id",0));

        if (myCard.getLogoImgPath() == null || myCard.getLogoImgPath().equals("null")) logo.setImageResource(R.drawable.person_x311);
        else logo.setImageURI(Uri.parse(myCard.getLogoImgPath()));
        name.setText(myCard.getName());
        mobile.setText(myCard.getMobile());
        phone.setText(myCard.getPhone());
        fax.setText(myCard.getFax());
        email.setText(myCard.getEmail());
        web.setText(myCard.getWeb());
        company.setText(myCard.getCompany());
        address.setText(myCard.getAddress());
        job.setText(myCard.getJob());
        facebook.setText(myCard.getFacebook());
        tweeter.setText(myCard.getTweeter());
        skype.setText(myCard.getSkype());
        other.setText(myCard.getOther());

        hideEmptyFields();          // hide empty fields
    }

    // private method fos hiding MyCard empty fields
    private void hideEmptyFields(){
        if (mobile.length() == 0) {
            mobileTxt.setVisibility(View.GONE);
            mobile.setVisibility(View.GONE);
        }
        if (phone.length() == 0) {
            phoneTxt.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
        }
        if (fax.length() == 0) {
            faxTxt.setVisibility(View.GONE);
            fax.setVisibility(View.GONE);
        }
        if (email.length() == 0) {
            emailTxt.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        }
        if (web.length() == 0) {
            webTxt.setVisibility(View.GONE);
            web.setVisibility(View.GONE);
        }
        if (company.length() == 0) {
            companyTxt.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
        }
        if (address.length() == 0) {
            addressTxt.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
        }
        if (job.length() == 0) {
            jobTxt.setVisibility(View.GONE);
            job.setVisibility(View.GONE);
        }
        if (facebook.length() == 0) {
            facebookTxt.setVisibility(View.GONE);
            facebook.setVisibility(View.GONE);
        }
        if (tweeter.length() == 0) {
            tweeterTxt.setVisibility(View.GONE);
            tweeter.setVisibility(View.GONE);
        }
        if (skype.length() == 0) {
            skypeTxt.setVisibility(View.GONE);
            skype.setVisibility(View.GONE);
        }
        if (other.length() == 0) {
            otherTxt.setVisibility(View.GONE);
            other.setVisibility(View.GONE);
        }
    }

    // private method puts Card data (fields) to Intent object
    private void putCardInfoToIntent(){
        if (cardIntent.getStringExtra("action").equals("myCard")) {cardIntent.putExtra("action","editMyCard");}
        else if (cardIntent.getStringExtra("action").equals("cardFromList")) {cardIntent.putExtra("action","edit");}
        cardIntent.putExtra("id",myCard.getId());
        cardIntent.putExtra("logoPath",myCard.getLogoImgPath());
        cardIntent.putExtra("name",myCard.getName());
        cardIntent.putExtra("mobile",myCard.getMobile());
        cardIntent.putExtra("phone",myCard.getPhone());
        cardIntent.putExtra("fax",myCard.getFax());
        cardIntent.putExtra("email",myCard.getEmail());
        cardIntent.putExtra("web",myCard.getWeb());
        cardIntent.putExtra("company",myCard.getCompany());
        cardIntent.putExtra("address",myCard.getAddress());
        cardIntent.putExtra("job",myCard.getJob());
        cardIntent.putExtra("facebook",myCard.getFacebook());
        cardIntent.putExtra("tweeter",myCard.getTweeter());
        cardIntent.putExtra("skype",myCard.getSkype());
        cardIntent.putExtra("other",myCard.getOther());
    }

    // when "back" button is pressed go back to proper Activity
    @Override
    public void onBackPressed(){
        cardIntent = getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(cardIntent.getStringExtra("action").equals("myCard") || cardIntent.getStringExtra("action").equals("editMyCard")){
            cardIntent.setClass(this,WelcomeActivity.class);
        }
        else if(cardIntent.getStringExtra("action").equals("cardFromList") || cardIntent.getStringExtra("action").equals("edit")
                || cardIntent.getStringExtra("action").equals("writeToTag")){
            cardIntent.setClass(this,CardsListActivity.class);
        }
        else{
            Toast.makeText(getApplicationContext(),"BACK button not defined\naction="+cardIntent.getStringExtra("action"),Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(cardIntent);
    }

    @Override
    public void onResume(){
        super.onResume();
        // if NFC is enable on device set NDEF message ready for sharing via NFC
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            nfcAdapter.setNdefPushMessage(putCardContentToNdefMessage(), this);
        }
        nfcAdapter.enableForegroundDispatch(this,nfcPendingIntent,ndefIntentFilters,null);
    }

    // share Card with other Android devices
    public void shareCard(View view){
        Intent shareCardIntent = getIntent().setClass(this,ShareActivity.class);
        putCardInfoToIntent();
        startActivity(shareCardIntent);
    }

    // put Card content to NDEF message (for sharing via NFC)
    private NdefMessage putCardContentToNdefMessage(){

        byte[] payload_card_details = cardToJSON(myCard).getBytes();
        // at this moment there's no sending logo via NFC functionality, so payload_card_logo is set to null
        byte[] payload_card_logo = null;
        if (payload_card_logo == null) payload_card_logo = "null".getBytes();   // if logo payload is null set default value of logo

        // create NDEF records and put card payload
        NdefRecord record1 = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT,new byte[0],payload_card_details);
        NdefRecord record2 = NdefRecord.createMime("image/jpeg",payload_card_logo);

        // put NDEF records to NDEF message
        NdefMessage msg = new NdefMessage(new NdefRecord[]{record1, record2, NdefRecord.createApplicationRecord("mbodziony.businesscardsmanager")});

        Log.d("CardNFC","NDEF message created (" + msg.getRecords().length + " records)");

        return  msg;
    }

    // put Card information to JSON file (it will be converted to byte[] for sending in NDEF record payload)
    private String cardToJSON(Card card){

        try {
            JSONObject cardJSON = new JSONObject();
            cardJSON.put("logoPath",card.getLogoImgPath());
            cardJSON.put("name",card.getName());
            cardJSON.put("mobile",card.getMobile());
            cardJSON.put("phone",card.getPhone());
            cardJSON.put("fax",card.getFax());
            cardJSON.put("email",card.getEmail());
            cardJSON.put("web",card.getWeb());
            cardJSON.put("company",card.getCompany());
            cardJSON.put("address",card.getAddress());
            cardJSON.put("job",card.getJob());
            cardJSON.put("facebook",card.getFacebook());
            cardJSON.put("tweeter",card.getTweeter());
            cardJSON.put("skype",card.getSkype());
            cardJSON.put("other",card.getOther());

            Log.d("CardNFC","JSON file created!");

            return cardJSON.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    // share Card via Bluetooth
    public void shareViaBluetooth(View view){

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        // if adapter was not found bluetooth is not available on the device
        if(btAdapter == null) {
            Toast.makeText(this, "Bluetooth nie jest obsługiwany na urządzeniu", Toast.LENGTH_LONG).show();
        } else {
            enableBluetooth();
        }
    }

    public void enableBluetooth() {

        // ask for permission to enable Bluetooth and make device visible
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU) {

            // set type of file to be sent via Bluetooth
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");

            // convert card to json format
            try {

                cardJSON.put("logoPath", myCard.getLogoImgPath());
                cardJSON.put("name", myCard.getName());
                cardJSON.put("mobile", myCard.getMobile());
                cardJSON.put("phone", myCard.getPhone());
                cardJSON.put("fax", myCard.getFax());
                cardJSON.put("email", myCard.getEmail());
                cardJSON.put("web", myCard.getWeb());
                cardJSON.put("company", myCard.getCompany());
                cardJSON.put("address", myCard.getAddress());
                cardJSON.put("job", myCard.getJob());
                cardJSON.put("facebook", myCard.getFacebook());
                cardJSON.put("tweeter", myCard.getTweeter());
                cardJSON.put("skype", myCard.getSkype());
                cardJSON.put("other", myCard.getOther());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // for debugging
            System.out.println("The directory is: " + Environment.getExternalStorageDirectory());

            // write json file to bluetoothCard.json
            try (FileWriter file = new FileWriter("/storage/emulated/0/bluetoothCard.json")) {
                // convert json file to string
                file.write(cardJSON.toString());
                // for debugging
                System.out.println("Udało się skopiować obiekt json do pliku ...");
                System.out.println("\nJSON Object: " + cardJSON);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // prepare file to send and send file if bluetooth was found and was not canceled during the process
            File f = new File(Environment.getExternalStorageDirectory(), "bluetoothCard.json");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            PackageManager pm = getPackageManager();
            List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);

            if(appsList.size() > 0) {
                String packageName = null;
                String className = null;
                boolean found = false;

                for(ResolveInfo info : appsList) {
                    packageName = info.activityInfo.packageName;
                    if(packageName.equals("com.android.bluetooth")) {
                        className = info.activityInfo.name;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(this, "Bluetooth nie został znaleziony",
                            Toast.LENGTH_LONG).show();
                } else {
                    intent.setClassName(packageName, className);
                    startActivity(intent);
                }
            }
        } else {
            Toast.makeText(this, "Bluetooth został anulowany", Toast.LENGTH_LONG)
                    .show();
        }
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
