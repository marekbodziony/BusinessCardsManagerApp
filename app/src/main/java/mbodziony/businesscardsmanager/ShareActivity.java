package mbodziony.businesscardsmanager;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private static boolean closeShareActivity = false;

    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setTitle("Udostępnij przez :");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);        // NfcAdapter object to handle NFC service availability
    }


    // method to share Card via NFC
    public void shareViaNfc (View view){
        // if device not support NFC - display information about this to the user
        if (nfcAdapter == null) {
            Toast.makeText(this,"NFC not supported at your device!",Toast.LENGTH_SHORT).show();
            finish();
        }
        // if device support NFC but it's disable at the moment - go to NFC Settings
        else if (!nfcAdapter.isEnabled()){
            Toast.makeText(this,"Turn on NFC",Toast.LENGTH_LONG).show();
            closeShareActivity = true;          // user goes to NFC Settings, Activity can be close if come back after turning-on NFC
            Intent gotoNfcSettings = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(gotoNfcSettings);
        }
        // if user choose to share Card via NFC while NFC is on - display info to tap devices together
        else {
            Toast.makeText(this,"Tap your device with other device to share your Card via NFC",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onResume(){
        closeThisActivityIfDontNeed();     // close this Activity when user come back from Settings and turn-on NFC
        super.onResume();
    }

    // method will close this Activity when back from NFC Settings and NFC was turn-on by user
    private void closeThisActivityIfDontNeed(){
        if (nfcAdapter.isEnabled() && closeShareActivity){
            closeShareActivity = false;     // don't close this Activity next time
            Toast.makeText(this,"Tap your devices together to send a Card.\nIt will start automaticly.",Toast.LENGTH_LONG).show();
            finish();
        }
        closeShareActivity = false;         // don't close this Activity next time
    }

}
