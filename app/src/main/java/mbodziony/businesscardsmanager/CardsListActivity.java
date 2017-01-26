package mbodziony.businesscardsmanager;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardsListActivity extends AppCompatActivity {

    private ListView cardsListView;
    private List<Card> cardList;
    private CardsAdapter cardsAdapter;
    private DatabaseManager dbManager;
    private Intent cardIntent;

    private TextView noCardsTxt;
    private TextView noCardsAddNewTxt;
    private Button newCardBtn;
    private Button bluetoothBtn;

    private BluetoothAdapter btAdapter;

    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] ndefIntentFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_list);

        noCardsTxt = (TextView)findViewById(R.id.no_cards_on_list);
        noCardsAddNewTxt = (TextView)findViewById(R.id.no_cards_add_new);
        newCardBtn = (Button)findViewById(R.id.no_cards_add_new_btn);
        bluetoothBtn = (Button)findViewById(R.id.readfromBT);

        cardsListView = (ListView) findViewById(R.id.cardsListView);
        cardList = new ArrayList<Card>();

        dbManager = new DatabaseManager(getApplicationContext());
        dbManager.open();                   // open connection to database

        cardIntent = getIntent();           // get Intent

        // for NFC sharing
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

        serveCardInDatabaseIfNeeded();      // save, edit or delete Card in database (depend of Intent action)
        getCardsFromDatabase();             // get Cards list from database and populate cards list

        cardsAdapter = new CardsAdapter(this, cardList);
        cardsListView.setAdapter(cardsAdapter);

        // show Card (go to ShowCardActivity) when user click Card on cards list
        cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Card card = cardList.get(pos);
                showSelectedCard(card);
            }
        });

        // add context popup menu to cards list
        registerForContextMenu(cardsListView);
    }

    @Override
    public void onResume(){
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this,nfcPendingIntent,ndefIntentFilters,null);
    }

    @Override
    public void onPause(){
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent (Intent intent){
        Log.d("CardNFC","New Intent received");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            Log.d("CardNFC","NDEF Intent received");
            Card card = getCardFromNdefMessage(intent);
            dbManager.insertNewCard("cards",card);
            getCardsFromDatabase();
            card.setId(cardList.get(cardList.size()-1).getId());
            Toast.makeText(getApplicationContext(),"Card SAVED",Toast.LENGTH_SHORT).show();
            showSelectedCard(card);
        }
    }

    // save new Card, edit Card details or delete Card from database (depending on Intent action)
    private void serveCardInDatabaseIfNeeded(){
        Card card = null;
        String action = cardIntent.getAction();
        // if Card details received form other device via NFC
        if (action != null && action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Toast.makeText(this,"NDEF message received!",Toast.LENGTH_SHORT).show();
            card = getCardFromNdefMessage(cardIntent);          // get Card object from NDEF message
            cardIntent = new Intent();                          // Consume this intent
            cardIntent.putExtra("action","newNFC");             // set Intent action to "new" (new Card received via NFC)
        }
        // if Card details received form this application Activities
        else {
            card = new Card(cardIntent.getStringExtra("logoPath"), cardIntent.getStringExtra("name"), cardIntent.getStringExtra("mobile"),
                    cardIntent.getStringExtra("phone"), cardIntent.getStringExtra("fax"), cardIntent.getStringExtra("email"), cardIntent.getStringExtra("web"),
                    cardIntent.getStringExtra("company"), cardIntent.getStringExtra("address"), cardIntent.getStringExtra("job"),
                    cardIntent.getStringExtra("facebook"), cardIntent.getStringExtra("tweeter"), cardIntent.getStringExtra("skype"),
                    cardIntent.getStringExtra("other"));
            card.setId(cardIntent.getLongExtra("id", 0));
        }
        // add new Card to database
        if (cardIntent.getStringExtra("action").equals("new")){
            dbManager.insertNewCard("cards",card);
            Toast.makeText(getApplicationContext(),"Card SAVED",Toast.LENGTH_SHORT).show();
        }
        // when new Card received via NFC - add new Card to database and show it (go to ShowCardActivity)
        if (cardIntent.getStringExtra("action").equals("newNFC")){
            dbManager.insertNewCard("cards",card);
            getCardsFromDatabase();
            card.setId(cardList.get(cardList.size()-1).getId());
            Toast.makeText(getApplicationContext(),"Card SAVED",Toast.LENGTH_SHORT).show();
            showSelectedCard(card);
        }
        // edit Card details in database
        else if (cardIntent.getStringExtra("action").equals("edit")){
            dbManager.updateCard("cards",card);
            Toast.makeText(getApplicationContext(),"Card edited",Toast.LENGTH_SHORT).show();
        }
        // delete Card from database
        else if (cardIntent.getStringExtra("action").equals("delete")){
            dbManager.deleteCard("cards",card.getId());
            Toast.makeText(getApplicationContext(),"Card deleted!",Toast.LENGTH_SHORT).show();
        }
    }

    // method gets all Cards from database and populate cards list
    private void getCardsFromDatabase(){
        Cursor cardCursor = dbManager.getAllCardsFromDB("cards");

        if (cardCursor != null && cardCursor.getCount() > 0) {
            cardCursor.moveToFirst();
            do{
                long id = cardCursor.getLong(0);
                String logoPath = cardCursor.getString(1);
                String name = cardCursor.getString(2);
                String mobile = cardCursor.getString(3);
                String phone = cardCursor.getString(4);
                String fax = cardCursor.getString(5);
                String email = cardCursor.getString(6);
                String web = cardCursor.getString(7);
                String company = cardCursor.getString(8);
                String address = cardCursor.getString(9);
                String job = cardCursor.getString(10);
                String facebook = cardCursor.getString(11);
                String tweeter = cardCursor.getString(12);
                String skype = cardCursor.getString(13);
                String other = cardCursor.getString(14);

                Card card = new Card(logoPath,name,mobile,phone,fax,email,web,company,address,job,facebook,tweeter,skype,other);
                card.setId(id);
                cardList.add(card);
            }
            while (cardCursor.moveToNext());
        }
        else {
            showNoCardsInfoIfListEmpty();       // if no cards element in database display "no cards" info and show "add new" button
        }

    }

    // show selected Card (go to ShowCardActivity)
    private void showSelectedCard(Card card){
        Intent showCardIntent = new Intent(this,ShowCardActivity.class);
        showCardIntent.putExtra("action","cardFromList");
        showCardIntent.putExtra("id",card.getId());
        showCardIntent.putExtra("logoPath",""+card.getLogoImgPath());
        showCardIntent.putExtra("name",""+card.getName());
        showCardIntent.putExtra("mobile",""+card.getMobile());
        showCardIntent.putExtra("phone",""+card.getPhone());
        showCardIntent.putExtra("fax",""+card.getFax());
        showCardIntent.putExtra("email",""+card.getEmail());
        showCardIntent.putExtra("web",""+card.getWeb());
        showCardIntent.putExtra("company",""+card.getCompany());
        showCardIntent.putExtra("address",""+card.getAddress());
        showCardIntent.putExtra("job",""+card.getJob());
        showCardIntent.putExtra("facebook",""+card.getFacebook());
        showCardIntent.putExtra("tweeter",""+card.getTweeter());
        showCardIntent.putExtra("skype",""+card.getSkype());
        showCardIntent.putExtra("other",""+card.getOther());
        startActivity(showCardIntent);
    }
    // edit selected Card (go to EditCardActivity)
    private void editSelectedCard(Card card){
        Intent editCardIntent = new Intent(this,EditCardActivity.class);
        editCardIntent.putExtra("action","edit");
        editCardIntent.putExtra("id",card.getId());
        editCardIntent.putExtra("logoPath",""+card.getLogoImgPath());
        editCardIntent.putExtra("name",""+card.getName());
        editCardIntent.putExtra("mobile",""+card.getMobile());
        editCardIntent.putExtra("phone",""+card.getPhone());
        editCardIntent.putExtra("fax",""+card.getFax());
        editCardIntent.putExtra("email",""+card.getEmail());
        editCardIntent.putExtra("web",""+card.getWeb());
        editCardIntent.putExtra("company",""+card.getCompany());
        editCardIntent.putExtra("address",""+card.getAddress());
        editCardIntent.putExtra("job",""+card.getJob());
        editCardIntent.putExtra("facebook",""+card.getFacebook());
        editCardIntent.putExtra("tweeter",""+card.getTweeter());
        editCardIntent.putExtra("skype",""+card.getSkype());
        editCardIntent.putExtra("other",""+card.getOther());
        startActivity(editCardIntent);
    }

    // method displays "no cards" info and shows "add new" button
    private void showNoCardsInfoIfListEmpty(){
        noCardsTxt.setVisibility(View.VISIBLE); ;
        noCardsAddNewTxt.setVisibility(View.VISIBLE);
        newCardBtn.setVisibility(View.VISIBLE);
    }


    // Close connection to database when Activity is closed - onDestroy()
    @Override
    public void onDestroy(){
        if (dbManager != null) dbManager.close();   // close connection to database
        super.onDestroy();
    }

    // create a popup menu, it will be displayed when user long press Card item on a cards list
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu, menu);

    }

    // what to do when user click one of popup menu items
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int i = (int)info.id;
        long id = cardList.get(i).getId();

        switch (item.getItemId()) {
            // show selected Card (go to ShowCardActivity)
            case R.id.menu_show:
                showSelectedCard(cardList.get(i));
                return true;
            // edit selected Card (go to EditCardActivity)
            case R.id.menu_edit:
                editSelectedCard(cardList.get(i));
                return true;
            // delete selected Card
            case R.id.menu_delete:
                new AlertDialog.Builder(this).setTitle("Usunąć wizytówkę ?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                dbManager.deleteCard("cards",cardList.get(i).getId());
                                cardList.remove(i);
                                cardsListView.setAdapter(cardsAdapter);
                                Toast.makeText(getApplicationContext(),"Card deleted!",Toast.LENGTH_SHORT).show();
                                if(cardList.size() == 0) showNoCardsInfoIfListEmpty();
                                return;
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                return;
                            }
                        })
                        .show();
            default:
                return super.onContextItemSelected(item);
        }
    }

    // create new Card if user clicked (+) - go to EditCardActivity
    public void createNewCard(View view){
        cardIntent = new Intent(this,EditCardActivity.class);
        cardIntent.putExtra("action","new");
        startActivity(cardIntent);
    }

    // when "back" button is pressed go back to WelcomeActivity (Main screen)
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    // get Card info from NDEF message
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

    // get Card information from JSON
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

    // read json file send via Bluetooth from other device and save to database
    public void readFromBluetoothFile(View view)  {
        // find location of json file in phone memory, if file was not sent/saved communicate this to the user
        File f = null;
        try {
            File jsonLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            f = new File(jsonLocation,"bluetoothCard.json");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"File is not in download directory, please re-send",Toast.LENGTH_SHORT).show();
        }

        //info for debugging if file exists
        if (f.exists()) {
            System.out.println("it exists");
        }

        // convert jason file to byte array
        byte[] bytesArray = new byte[(int) f.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //read file into bytes[]
            fis.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //      String action = cardIntent.getAction();

        // create card from bytes array pulled from .json file and save it to database
        dbManager.insertNewCard("cards",getCardFromJSON(bytesArray));
        // inform user that card was correctly saved
        Toast.makeText(getApplicationContext(),"Card SAVED",Toast.LENGTH_SHORT).show();
        // delete file received by bluetooth (no longer needed)
        f.delete();
        Toast.makeText(getApplicationContext(),"Bluetooth file deleted",Toast.LENGTH_SHORT).show();
    }


}
