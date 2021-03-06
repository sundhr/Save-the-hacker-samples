package com.blogspot.sundharram.theodolight;


import android.app.Activity;
import android.content.*;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

public class TestPrint extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_print);
        findViewById(R.id.hello_world).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startService(new Intent(this, FusedLocationService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Location location = null;
                String time = "";
                if (intent.getExtras() != null)
                {
                    location = (Location) intent.getExtras().get("LOCATION");
                    time = intent.getExtras().getString("TIME");
                }


                if (location == null)
                    ((TextView) findViewById(R.id.hello_world)).setText("null location received");
                else
                    ((TextView) findViewById(R.id.hello_world)).setText("Fused Location Received =lat :" + location.getLatitude() + ",lng :" + location.getLongitude()+" in "+time);

//                findViewById(R.id.request_location).setEnabled(true);
            }
        }, new IntentFilter(FusedLocationService.LOCATION_RECEIVED));
//        findViewById(R.id.request_location).setEnabled(false);
    }
}