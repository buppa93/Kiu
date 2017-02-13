package com.domain.my.giuseppe.kiu.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.service.LatLngService;

public class SettingsActivity extends AppCompatActivity {

    private static Switch visibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        visibility = (Switch) findViewById(R.id.switch_visibility);
        SharedPreferences sharedPrefs = getSharedPreferences("com.example.xyz", MODE_PRIVATE);
        visibility.setChecked(sharedPrefs.getBoolean("NameOfThingToSave",false));


        //inzializzo e lancio servizio per l' invio periodico della posizione
        final Intent LatLngServiceIntent = new Intent(getApplicationContext(), LatLngService.class);
        visibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                //save the switch state in shared preferences
                if (isChecked==true) {
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave", true);
                    editor.commit();
                    startService(LatLngServiceIntent);
                }
                else if (isChecked==false) {
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave", false);
                    editor.commit();
                    stopService(LatLngServiceIntent);
                }
            }
        });

    }
}
