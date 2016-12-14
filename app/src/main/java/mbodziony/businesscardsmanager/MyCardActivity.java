package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCardActivity extends AppCompatActivity {

    private Card myCard;
    private Card myCard1;
    private Intent editMyCardIntent;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);

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

        // Card object for testing
        myCard = new Card(null,"Jan Kowalski","500-100-100","022 512-00-90","343434553","marek@gmail.com","www.google.pl","Google inc.",
              "Warszawa, ul. Chłodna 13","IT developer","marekFacebook","marek@tweeter","marekSkype","other informations");
//        myCard1 = new Card(null,"Jan Kowalski","500-100-100","","","marek@gmail.com","www.google.pl","Google inc.",
//                "Warszawa, ul. Chłodna 13","IT developer","marekFacebook","","","other informations");

        // set values of Card object (and hide empty fields)
        setMyCardValues();
    }

    // delete MyCard
    public void deleteMyCard(View view){
        Toast.makeText(getApplicationContext(),"DELETE",Toast.LENGTH_SHORT).show();
    }
    // edit MyCard
    public void editMyCard(View view){
        editMyCardIntent = new Intent(this, EditMyCardActivity.class);
        putCardInfoToIntent();
        startActivity(editMyCardIntent);
    }

    // private method to set all values of MyCard
    private void setMyCardValues(){
        editMyCardIntent = getIntent();
        //if user edited Card or create new Card then take values from Intent
        if (editMyCardIntent != null && editMyCardIntent.getIntExtra("save",0) == 1){
            myCard = new Card(null, editMyCardIntent.getStringExtra("name"), editMyCardIntent.getStringExtra("mobile"), editMyCardIntent.getStringExtra("phone"),
                    editMyCardIntent.getStringExtra("fax"), editMyCardIntent.getStringExtra("email"), editMyCardIntent.getStringExtra("web"),
                    editMyCardIntent.getStringExtra("company"), editMyCardIntent.getStringExtra("address"), editMyCardIntent.getStringExtra("job"),
                    editMyCardIntent.getStringExtra("facebook"), editMyCardIntent.getStringExtra("tweeter"), editMyCardIntent.getStringExtra("skype"),
                    editMyCardIntent.getStringExtra("other"));
        }

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

        // hide empty fields
        hideEmptyFields();
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

    // private method put MyCard data (fields) to Intent object
    private void putCardInfoToIntent(){
        editMyCardIntent.putExtra("id",myCard.getId());
        editMyCardIntent.putExtra("name",myCard.getName());
        editMyCardIntent.putExtra("mobile",myCard.getMobile());
        editMyCardIntent.putExtra("phone",myCard.getMobile());
        editMyCardIntent.putExtra("fax",myCard.getFax());
        editMyCardIntent.putExtra("email",myCard.getEmail());
        editMyCardIntent.putExtra("web",myCard.getWeb());
        editMyCardIntent.putExtra("company",myCard.getCompany());
        editMyCardIntent.putExtra("address",myCard.getAddress());
        editMyCardIntent.putExtra("job",myCard.getJob());
        editMyCardIntent.putExtra("facebook",myCard.getFacebook());
        editMyCardIntent.putExtra("tweeter",myCard.getTweeter());
        editMyCardIntent.putExtra("skype",myCard.getSkype());
        editMyCardIntent.putExtra("other",myCard.getOther());
    }
}
