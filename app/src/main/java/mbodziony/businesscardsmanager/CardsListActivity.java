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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_list);

        cardsListView = (ListView) findViewById(R.id.cardsListView);

        cardList = new ArrayList<Card>();
        cardList.add(new Card(null,"Bill Gates","500-100-100","022 512-00-90","343434553","marek@gmail.com","www.google.pl","Google inc.",
                "Warszawa, ul. Chłodna 13","IT developer","marekFacebook","marek@tweeter","marekSkype","other informations"));
        cardList.add(new Card(null,"Steve Jobs","500-100-100","022 512-00-90","343434553","marek@gmail.com","www.google.pl","Google inc.",
                "Warszawa, ul. Chłodna 13","IT developer","marekFacebook","marek@tweeter","marekSkype","other informations"));

        cardsAdapter = new CardsAdapter(this, cardList);
        cardsListView.setAdapter(cardsAdapter);

        cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

            }
        });
    }
}
