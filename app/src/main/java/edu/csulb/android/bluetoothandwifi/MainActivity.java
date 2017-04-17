package edu.csulb.android.bluetoothandwifi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}
