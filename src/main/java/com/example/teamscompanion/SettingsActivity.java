package com.example.teamscompanion;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    boolean tcSet, teamsSet = false;
    SharedPreferences sharedPreferences;


    public void tcSettings(View view) {

        tcSet = true;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);


    }


    public void teamsSettings(View view) {

        teamsSet = true;

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", "com.microsoft.teams", null);
        intent.setData(uri);
        startActivity(intent);
    }


    public void nextClicked(View view) {

        if (tcSet && teamsSet) {
            sharedPreferences.edit().putBoolean("isOpenedFirstTime", false).apply();
            Toast.makeText(getApplicationContext(), "Clear & Restart the app to activate permissions!", Toast.LENGTH_LONG).show();
            finish();
        }  else {
            Toast.makeText(getApplicationContext(), "Please allow both permissions!", Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        setTitle("Configure");


    }
}