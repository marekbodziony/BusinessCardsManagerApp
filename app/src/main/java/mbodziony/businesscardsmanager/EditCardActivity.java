package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditCardActivity extends AppCompatActivity {

    private Button cancel;
    private Button save;
    private Button photo;
    private String logoImgPath;

    private long id;
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

    //private Card myCard;
    private Intent editCardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        editCardIntent = getIntent();

        id = editCardIntent.getLongExtra("id",0);
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
        photo = (Button)findViewById(R.id.takePhotoBtn);
    }

    // get card info from Intent object and display on screen
    public void getCardInfoFromIntent(){

        if (!editCardIntent.getStringExtra("logoPath").equals("null")){
            logoImgPath = editCardIntent.getStringExtra("logoPath");
            logo.setImageURI(Uri.parse(logoImgPath));
        }
        else{ logo.setImageResource(R.drawable.person_x311);}
        name.setText(editCardIntent.getStringExtra("name"));
        name.setHint("");
        mobile.setText(editCardIntent.getStringExtra("mobile"));
        mobile.setHint("");
        phone.setText(editCardIntent.getStringExtra("phone"));
        phone.setHint("");
        fax.setText(editCardIntent.getStringExtra("fax"));
        fax.setHint("");
        email.setText(editCardIntent.getStringExtra("email"));
        email.setHint("");
        web.setText(editCardIntent.getStringExtra("web"));
        web.setHint("");
        company.setText(editCardIntent.getStringExtra("company"));
        company.setHint("");
        address.setText(editCardIntent.getStringExtra("address"));
        address.setHint("");
        job.setText(editCardIntent.getStringExtra("job"));
        job.setHint("");
        facebook.setText(editCardIntent.getStringExtra("facebook"));
        facebook.setHint("");
        tweeter.setText(editCardIntent.getStringExtra("tweeter"));
        tweeter.setHint("");
        skype.setText(editCardIntent.getStringExtra("skype"));
        skype.setHint("");
        other.setText(editCardIntent.getStringExtra("other"));
        other.setHint("");
    }

    // cancel
    public void cancel(View view){
        Toast.makeText(getApplicationContext(),"CANCEL",Toast.LENGTH_SHORT).show();
        finish();
    }
    // save
    public void save(View view){

        if (!isCardReadyToSave()) return;   // check if fields name and/or mobile phone are not empty

        if (editCardIntent.getStringExtra("action").equals("new") || editCardIntent.getStringExtra("action").equals("edit")){
            editCardIntent.setClass(this,CardsListActivity.class);
        }
        else if (editCardIntent.getStringExtra("action").equals("newMyCard") || editCardIntent.getStringExtra("action").equals("editMyCard")){
            editCardIntent.setClass(this,MyCardsListActivity.class);
        }
        putCardInfoToIntent();
        startActivity(editCardIntent);
    }

    // method checks if name and/or mobile phone are not empty
    private boolean isCardReadyToSave(){
        boolean isReadyToSave = true;
        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(),"Nie podano nazwiska",Toast.LENGTH_SHORT).show();
            isReadyToSave = false;
        }
        if (mobile.length() == 0 ) {
            Toast.makeText(getApplicationContext(),"Nie podano telefonu komórkowego",Toast.LENGTH_SHORT).show();
            isReadyToSave = false;
        }
        return isReadyToSave;
    }


    // take photo
    public void takePhoto (View view){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhotoIntent,1);
    }
    // load image from gallery
    public void loadImageFromGallery(View view){
        Intent loadImgIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(loadImgIntent,1);
    }
    // delete logo from MyCard (set default)
    public void deleteLogo(View view){
        //logoImgPath = null;
        logo.setImageResource(R.drawable.person_x311);
        logoImgPath = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int respondCode, Intent data){
        // set taken photo or loaded image to logo ImageView
        if (requestCode == 1 && respondCode == RESULT_OK){
            logoImgPath = data.getData().toString();
            logo.setImageURI(Uri.parse(logoImgPath));
        }
    }

    // private method put MyCard data (fields) to Intent object
    private void putCardInfoToIntent(){
        editCardIntent.putExtra("id",id);
        editCardIntent.putExtra("logoPath",""+logoImgPath);
        editCardIntent.putExtra("name",""+name.getText());
        editCardIntent.putExtra("mobile",""+mobile.getText());
        editCardIntent.putExtra("phone",""+phone.getText());
        editCardIntent.putExtra("fax",""+fax.getText());
        editCardIntent.putExtra("email",""+email.getText());
        editCardIntent.putExtra("web",""+web.getText());
        editCardIntent.putExtra("company",""+company.getText());
        editCardIntent.putExtra("address",""+address.getText());
        editCardIntent.putExtra("job",""+job.getText());
        editCardIntent.putExtra("facebook",""+facebook.getText());
        editCardIntent.putExtra("tweeter",""+tweeter.getText());
        editCardIntent.putExtra("skype",""+skype.getText());
        editCardIntent.putExtra("other",""+other.getText());
    }
}
