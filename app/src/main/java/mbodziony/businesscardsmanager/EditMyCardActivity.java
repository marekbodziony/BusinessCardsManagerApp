package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditMyCardActivity extends AppCompatActivity {

    private Button cancel;
    private Button save;

    private ImageView logo;
    private EditText name;
    private EditText mobile;
    private EditText phone;
    private EditText fax;
    private EditText email;
    private EditText web;
    private EditText company;
    private EditText address;
    private EditText job;
    private EditText facebook;
    private EditText tweeter;
    private EditText skype;
    private EditText other;

    private Intent editMyCardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_card);

        editMyCardIntent = getIntent();

        logo = (ImageView)findViewById(R.id.myCard_logo);
        name = (EditText)findViewById(R.id.myCard_nameVal);
        mobile = (EditText)findViewById(R.id.myCard_mobileVal);
        phone = (EditText)findViewById(R.id.myCard_phoneVal);
        fax = (EditText)findViewById(R.id.myCard_faxVal);
        email = (EditText)findViewById(R.id.myCard_emailVal);
        web = (EditText)findViewById(R.id.myCard_webVal);
        company = (EditText)findViewById(R.id.myCard_companyVal);
        address = (EditText)findViewById(R.id.myCard_addressVal);
        job = (EditText)findViewById(R.id.myCard_jobVal);
        facebook = (EditText)findViewById(R.id.myCard_facebookVal);
        tweeter = (EditText)findViewById(R.id.myCard_tweeterVal);
        skype = (EditText)findViewById(R.id.myCard_skypeVal);
        other = (EditText)findViewById(R.id.myCard_otherVal);

        getCardInfoFromIntent();

        cancel = (Button)findViewById(R.id.editMyCard_cancelBtn);
        save = (Button)findViewById(R.id.editMyCard_saveBtn);
    }

    // get card info from Intent object
    public void getCardInfoFromIntent(){
        name.setHint(editMyCardIntent.getStringExtra("name"));
        mobile.setHint(editMyCardIntent.getStringExtra("mobile"));
        phone.setHint(editMyCardIntent.getStringExtra("phone"));
        fax.setHint(editMyCardIntent.getStringExtra("fax"));
        email.setHint(editMyCardIntent.getStringExtra("email"));
        web.setHint(editMyCardIntent.getStringExtra("web"));
        company.setHint(editMyCardIntent.getStringExtra("company"));
        address.setHint(editMyCardIntent.getStringExtra("address"));
        job.setHint(editMyCardIntent.getStringExtra("job"));
        facebook.setHint(editMyCardIntent.getStringExtra("facebook"));
        tweeter.setHint(editMyCardIntent.getStringExtra("tweeter"));
        skype.setHint(editMyCardIntent.getStringExtra("skype"));
        other.setHint(editMyCardIntent.getStringExtra("other"));
    }

    // cancel
    public void cancel(View view){
        Toast.makeText(getApplicationContext(),"CANCEL",Toast.LENGTH_SHORT).show();
    }
    // save
    public void save(View view){
        Toast.makeText(getApplicationContext(),"SAVE",Toast.LENGTH_SHORT).show();
    }

}
