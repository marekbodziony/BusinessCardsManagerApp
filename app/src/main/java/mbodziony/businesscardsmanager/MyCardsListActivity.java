package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MyCardsListActivity extends AppCompatActivity {

    private DatabaseManager dbManager;
    private Intent myCardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards_list);

        myCardIntent = getIntent();

        dbManager = new DatabaseManager(getApplicationContext());
        dbManager.open();                   // open connection to database

        serveMyCardInDatabaseIfNeeded();     // save, edit or delete myCard from database if user select specific action
        showMyCardIfExists();               // checks if there is myCard element in database, if yes it shows it (goes to ShowCardActivity)
    }

    // create new myCard (go to EditCardActivity)
    public void createNewMyCard(View view){
        myCardIntent = new Intent(this,EditCardActivity.class);
        myCardIntent.putExtra("action","newMyCard");
        startActivity(myCardIntent);
    }

    // method checks if there is myCard element in database, if yes it shows myCard (goes to ShowCardActivity)
    private void showMyCardIfExists(){
        Cursor cardCursor = dbManager.getAllCardsFromDB("mycards");
        //if (cardCursor != null) Toast.makeText(getApplicationContext(),"Records in DB  = " + cardCursor.getCount(),Toast.LENGTH_SHORT).show();
        if (cardCursor != null && cardCursor.getCount() == 1){
            cardCursor.moveToFirst();

            Intent myCardIntent = new Intent(this,ShowCardActivity.class);
            myCardIntent.putExtra("action","myCard");
            myCardIntent.putExtra("id",cardCursor.getLong(0));
            myCardIntent.putExtra("logoPath",""+cardCursor.getString(1));
            myCardIntent.putExtra("name",""+cardCursor.getString(2));
            myCardIntent.putExtra("mobile",""+cardCursor.getString(3));
            myCardIntent.putExtra("phone",""+cardCursor.getString(4));
            myCardIntent.putExtra("fax",""+cardCursor.getString(5));
            myCardIntent.putExtra("email",""+cardCursor.getString(6));
            myCardIntent.putExtra("web",""+cardCursor.getString(7));
            myCardIntent.putExtra("company",""+cardCursor.getString(8));
            myCardIntent.putExtra("address",""+cardCursor.getString(9));
            myCardIntent.putExtra("job",""+cardCursor.getString(10));
            myCardIntent.putExtra("facebook",""+cardCursor.getString(11));
            myCardIntent.putExtra("tweeter",""+cardCursor.getString(12));
            myCardIntent.putExtra("skype",""+cardCursor.getString(13));
            myCardIntent.putExtra("other",""+cardCursor.getString(14));
            startActivity(myCardIntent);
        }
    }

    // method saves myCard into database if there is need (Intent from EditCardActivity)
    private void serveMyCardInDatabaseIfNeeded(){
        myCardIntent = getIntent();

        Card card = new Card(myCardIntent.getStringExtra("logoPath"),myCardIntent.getStringExtra("name"),myCardIntent.getStringExtra("mobile"),
                myCardIntent.getStringExtra("phone"),myCardIntent.getStringExtra("fax"),myCardIntent.getStringExtra("email"),myCardIntent.getStringExtra("web"),
                myCardIntent.getStringExtra("company"),myCardIntent.getStringExtra("address"),myCardIntent.getStringExtra("job"),
                myCardIntent.getStringExtra("facebook"),myCardIntent.getStringExtra("tweeter"),myCardIntent.getStringExtra("skype"),
                myCardIntent.getStringExtra("other"));
        card.setId(myCardIntent.getLongExtra("id",0));
        // add new Card to database
        if (myCardIntent.getStringExtra("action").equals("newMyCard")){
            dbManager.insertNewCard("mycards",card);
            Toast.makeText(getApplicationContext(),"Card SAVED",Toast.LENGTH_SHORT).show();
        }
        // edit Card details in database
        else if (myCardIntent.getStringExtra("action").equals("editMyCard")){
            dbManager.updateCard("mycards",card);
            Toast.makeText(getApplicationContext(),"Card edited",Toast.LENGTH_SHORT).show();
        }
        // delete Card from database
        else if (myCardIntent.getStringExtra("action").equals("delete")){
            dbManager.deleteCard("mycards",card.getId());
            Toast.makeText(getApplicationContext(),"Card deleted!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy(){
        if (dbManager != null) dbManager.close();   // close connection to database
        super.onDestroy();
    }

    // when "back" button is pressed go back to WelcomeActivity (Main screen)
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
