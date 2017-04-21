package edu.csulb.android.bluetoothandwifi;

import android.app.Activity;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;

    private static final int REQUEST_CONNECT_DEVICE = 3;

    private BluetoothChatService mChatService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BluetoothConnection();

        Button connectWiFi = (Button) findViewById(R.id.btn_connect_wifi);
        Button connectBT = (Button) findViewById(R.id.btn_connect_bt);

        connectBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bluetoothIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivityForResult(bluetoothIntent, REQUEST_CONNECT_DEVICE);
            }
        });

    }

    public void TextMessage(View view) {
        Intent textMessageIntent = new Intent(MainActivity.this, TextActivity.class);
        startActivity(textMessageIntent);
    }

    public void PhotoMessage(View view) {
        Intent photoMessageIntent = new Intent(MainActivity.this, PhotoActivity.class);
        startActivity(photoMessageIntent);
    }

    public void VoiceMessage(View view) {
        Intent voiceMessageIntent = new Intent(MainActivity.this, VoiceActivity.class);
        startActivity(voiceMessageIntent);
    }

    public void BluetoothConnection() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (mChatService == null) {
                setupChat();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
                    setupChat();
                } else {
                    Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void setupChat() {

    }
}
