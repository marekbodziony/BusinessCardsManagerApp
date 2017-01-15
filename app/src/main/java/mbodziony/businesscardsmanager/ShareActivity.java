package mbodziony.businesscardsmanager;

import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothServerSocket;
        import android.bluetooth.BluetoothSocket;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import java.io.File;
        import java.util.List;

        import android.bluetooth.BluetoothAdapter;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.content.pm.ResolveInfo;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.support.v7.app.ActionBarActivity;
        import android.view.View;
        import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
        import java.util.Set;
        import java.util.UUID;

public class ShareActivity extends AppCompatActivity {

    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;
    private Button shareBluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        shareBluetooth = (Button)findViewById(R.id.bluetooth_btn);

        setTitle("Udostępnij przez :");
    }


    public void onBluetoothpress(View view) {

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter == null) {
            Toast.makeText(this, "Bluetooth nie jest obsługiwany na urządzeniu", Toast.LENGTH_LONG).show();
        } else {
            enableBluetooth();
        }
    }

    public void enableBluetooth() {

        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);

        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU) {

//            Gson gson = new Gson();
//            String json = gson.toJson();


            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            File f = new File(Environment.getExternalStorageDirectory(), "jason.json");
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

            Log.d("TAG","JSON file created!");
            Log.d("TAG",cardJSON.toString());

            return cardJSON.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}



