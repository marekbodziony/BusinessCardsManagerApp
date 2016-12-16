package mbodziony.businesscardsmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class EditMyCardActivity extends AppCompatActivity {

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

    private Card myCard;
    private Intent myCardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_card);

        myCardIntent = getIntent();

        id = myCardIntent.getLongExtra("id",0);
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

    // get card info from Intent object
    public void getCardInfoFromIntent(){

        if (myCardIntent.getStringExtra("logo_path") != null){
            logoImgPath = myCardIntent.getStringExtra("logo_path");
            logo.setImageURI(Uri.parse(logoImgPath));
        }
        name.setText(myCardIntent.getStringExtra("name"));
        name.setHint("");
        mobile.setText(myCardIntent.getStringExtra("mobile"));
        mobile.setHint("");
        phone.setText(myCardIntent.getStringExtra("phone"));
        phone.setHint("");
        fax.setText(myCardIntent.getStringExtra("fax"));
        fax.setHint("");
        email.setText(myCardIntent.getStringExtra("email"));
        email.setHint("");
        web.setText(myCardIntent.getStringExtra("web"));
        web.setHint("");
        company.setText(myCardIntent.getStringExtra("company"));
        company.setHint("");
        address.setText(myCardIntent.getStringExtra("address"));
        address.setHint("");
        job.setText(myCardIntent.getStringExtra("job"));
        job.setHint("");
        facebook.setText(myCardIntent.getStringExtra("facebook"));
        facebook.setHint("");
        tweeter.setText(myCardIntent.getStringExtra("tweeter"));
        tweeter.setHint("");
        skype.setText(myCardIntent.getStringExtra("skype"));
        skype.setHint("");
        other.setText(myCardIntent.getStringExtra("other"));
        other.setHint("");
    }

    // cancel
    public void cancel(View view){
        Toast.makeText(getApplicationContext(),"CANCEL",Toast.LENGTH_SHORT).show();
        finish();
    }
    // save
    public void save(View view){
        Toast.makeText(getApplicationContext(),"SAVED",Toast.LENGTH_SHORT).show();
        myCardIntent.setClass(this,MyCardActivity.class);
        putCardInfoToIntent();
        startActivity(myCardIntent);
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
        myCardIntent.putExtra("save",1);    // indicates that intent came from Card edition Activity
        myCardIntent.putExtra("id",id);
        myCardIntent.putExtra("logo_path",""+logoImgPath);
        myCardIntent.putExtra("name",""+name.getText());
        myCardIntent.putExtra("mobile",""+mobile.getText());
        myCardIntent.putExtra("phone",""+phone.getText());
        myCardIntent.putExtra("fax",""+fax.getText());
        myCardIntent.putExtra("email",""+email.getText());
        myCardIntent.putExtra("web",""+web.getText());
        myCardIntent.putExtra("company",""+company.getText());
        myCardIntent.putExtra("address",""+address.getText());
        myCardIntent.putExtra("job",""+job.getText());
        myCardIntent.putExtra("facebook",""+facebook.getText());
        myCardIntent.putExtra("tweeter",""+tweeter.getText());
        myCardIntent.putExtra("skype",""+skype.getText());
        myCardIntent.putExtra("other",""+other.getText());
    }
}
