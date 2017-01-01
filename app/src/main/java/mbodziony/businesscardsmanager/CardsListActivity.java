package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CardsListActivity extends AppCompatActivity {

    private ListView cardsListView;
    private List<Card> cardList;
    private CardsAdapter cardsAdapter;
    private DatabaseManager dbManager;
    private Intent cardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_list);

        cardsListView = (ListView) findViewById(R.id.cardsListView);
        cardList = new ArrayList<Card>();

        dbManager = new DatabaseManager(getApplicationContext());
        dbManager.open();           // open connection to database

        saveCardInDatabase();       // save new Card or edit Card details in databse
        getCardsFromDatabase();     // get Cards list from database and populate cards list


        cardsAdapter = new CardsAdapter(this, cardList);
        cardsListView.setAdapter(cardsAdapter);

        cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                Card card = cardList.get(pos);
                //Toast.makeText(getApplicationContext(),card.getName(),Toast.LENGTH_SHORT).show();
                showSelectedCard(card);
            }
        });
    }

    // save new Card or edit Card details in database
    private void saveCardInDatabase(){
        cardIntent = getIntent();
        // add new Card to database
        if (cardIntent.getStringExtra("action").equals("new")){
            dbManager.insertNewCard("cards",cardIntent.getStringExtra("logoPath"),cardIntent.getStringExtra("name"),cardIntent.getStringExtra("mobile"),
                    cardIntent.getStringExtra("phone"),cardIntent.getStringExtra("fax"),cardIntent.getStringExtra("email"),cardIntent.getStringExtra("web"),
                    cardIntent.getStringExtra("company"),cardIntent.getStringExtra("address"),cardIntent.getStringExtra("job"),
                    cardIntent.getStringExtra("facebook"),cardIntent.getStringExtra("tweeter"),cardIntent.getStringExtra("skype"),
                    cardIntent.getStringExtra("other"));
            Toast.makeText(getApplicationContext(),"Card SAVED",Toast.LENGTH_SHORT).show();
        }
        // edit Card details in database
        else if (cardIntent.getStringExtra("action").equals("edit")){
            Toast.makeText(getApplicationContext(),"Edit Card",Toast.LENGTH_SHORT).show();
        }
    }

    // method gets all Cards from database and populate cards list
    private void getCardsFromDatabase(){
        Cursor cardCursor = dbManager.getAllCardsFromDB("cards");
        int i = 1;
        if (cardCursor != null) {
            cardCursor.moveToFirst();
            Toast.makeText(getApplicationContext(),"Records in DB = " + cardCursor.getCount(),Toast.LENGTH_SHORT).show();

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

    }

    // show selected Card (go to ShowcardActivity)
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

    @Override
    public void onDestroy(){
        if (dbManager != null) dbManager.close();   // close connection to database
        super.onDestroy();
    }
}
