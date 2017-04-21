package edu.csulb.android.bluetoothandwifi;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {

    private BluetoothAdapter mBtAdapter;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private ArrayAdapter<String> pairedDeviceArrayAdapter;
    private ArrayAdapter<String> mNewDeviceArrayAdapter;
    int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluetooth_device_list);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        discoverDevices();

        mNewDeviceArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        /* Bind the list view for the new devices
        * Set adapter for it
        * */

        ListView newDeviceListView = (ListView) findViewById(R.id.new_devices);
        newDeviceListView.setAdapter(mNewDeviceArrayAdapter);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mNewDeviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

    }

    /*
    * Start device discovery using bluetooth adapter
    * */
    private void discoverDevices() {
        //Toast.makeText(this, "discoverDevices()", Toast.LENGTH_SHORT).show();

        setTitle("Scanning for devices");

        // If we are already discovering bluetooth devices stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);


        // Request device discovery from bluetooth adapter
        mBtAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDeviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle(R.string.select_device);
                if (mNewDeviceArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDeviceArrayAdapter.add(noDevices);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

}
