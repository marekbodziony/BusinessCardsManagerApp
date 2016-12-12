package mbodziony.businesscardsmanager;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MBodziony on 2016-12-12.
 */
public class CardsAdapter extends ArrayAdapter<Card> {

    private Activity context;
    private List<Card> cardsList;

    public CardsAdapter (Activity context, List<Card> cardsList){
        super(context,R.layout.cards_list_item,cardsList);
        this.context = context;
        this.cardsList = cardsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.cards_list_item,null,true);

        TextView name = (TextView) rowView.findViewById(R.id.nameVal);
        TextView mobile = (TextView) rowView.findViewById(R.id.mobileVal);

        name.setText(cardsList.get(position).getName());
        mobile.setText(cardsList.get(position).getPhone());

        return rowView;
    }


}
