package com.example.teamscompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView hoursTextView, minsTextView, colonTextView;
    Button stopButton;
    SharedPreferences sharedPreferences;
    ImageView clockImageView;
    AlarmManager alarmManager;
    SimpleDateFormat simpleDateFormat;
    PendingIntent pendingIntent, pendingIntent2;
    Intent intentAlarm;



    @Override
    protected void onResume() {
        super.onResume();

//        Log.i("Main", "inside onResume");
        updateGui();

    }

    @Override
    protected void onPause() {
        super.onPause();

//        Log.i("Main", "inside onPause");
        if (!sharedPreferences.getBoolean("isFinished", true)) {
            Toast.makeText(getApplicationContext(), "You can also clear the app!", Toast.LENGTH_SHORT).show();

        }
    }


    public void updateGui() {
        // here we calculate totalMinsInCountdown

//        Log.i("Info","inside updateGui");

        if (sharedPreferences.getBoolean("isFinished", true)) {

//            Log.i("Main", "calling stopbutton");
            stopTimer();
        }

        else {

            String endTime = sharedPreferences.getString("endTime", "null");

//            Log.i("Main", nowTime);
//            Log.i("Main", endTime);


            try {

                Calendar C = Calendar.getInstance();
                int nowHrs = C.get(Calendar.HOUR_OF_DAY); // 24 hrs time
                int nowMins = C.get(Calendar.MINUTE);
                int nowSecs = C.get(Calendar.SECOND);

                String startTime = Integer.toString(nowHrs) + ":" + Integer.toString(nowMins) + ":" + Integer.toString(nowSecs);

                assert endTime != null;
                Date endDate = simpleDateFormat.parse(endTime);
                Date nowDate = simpleDateFormat.parse(startTime);
                assert endDate != null;
                assert nowDate != null;
                int secs = (int) ((endDate.getTime() - nowDate.getTime())/1000);
                int totalMinsInCountdown = secs / 60;

             //   Log.i("Main totalMinincountdown", Integer.toString(totalMinsInCountdown));

                int hours = totalMinsInCountdown / 60;
                int mins = totalMinsInCountdown % 60;
      //          Log.i("hours", Integer.toString(hours));
       //         Log.i("Mins", Integer.toString(mins));
                String hoursString = (hours < 10) ? "0" + Integer.toString(hours) : Integer.toString(hours);
                String minsString = (mins < 10) ? "0" + Integer.toString(mins) : Integer.toString(mins);

                hoursTextView.setText(hoursString);
                minsTextView.setText(minsString);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

//    @Override
//    protected void onDestroy() {
//    //    stopService(new Intent(this, BroadcastService.class));
//        super.onDestroy();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hoursTextView = (TextView) findViewById(R.id.hoursTextView);
        minsTextView = (TextView) findViewById(R.id.minsTextView);
        colonTextView = (TextView) findViewById(R.id.colonTextView);
        stopButton = (Button) findViewById(R.id.stopButton);
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);  // can also explicitly give the packageName String.
        clockImageView = (ImageView) findViewById(R.id.clockImageView);
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intentAlarm =  new Intent(this, MyAlarm.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, 0);
        pendingIntent2 = PendingIntent.getBroadcast(this, 1, intentAlarm, 0);
        clockImageView.setOnClickListener(this);

        // directly start the activity if there is already a value in the sharedPreferences.

        // if app is opened for first time

        if (!sharedPreferences.getBoolean("isFinished", true)) {
            clockImageView.setVisibility(View.INVISIBLE);
            hoursTextView.setVisibility(View.VISIBLE);
            minsTextView.setVisibility(View.VISIBLE);
            colonTextView.setAlpha(1);
            stopButton.setVisibility(View.VISIBLE);
            updateGui();
        }


    }


    public void stopTimerHardReset(View view) {
        hoursTextView.setVisibility(View.INVISIBLE);
        minsTextView.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        colonTextView.setAlpha(0);
        clockImageView.setVisibility(View.VISIBLE);

        // null the value of sharedpreferences.

        sharedPreferences.edit().putBoolean("isFinished", true).apply();
        sharedPreferences.edit().putBoolean("deadLock", false).apply();

//        Log.i("Main", "Stopping both timers");
        if(pendingIntent != null) alarmManager.cancel(pendingIntent);
        if(pendingIntent2 != null) alarmManager.cancel(pendingIntent2);
    }

    // set function
    public void stopTimer() {
 //       Log.i("Main", "Stop timer function");
        hoursTextView.setVisibility(View.INVISIBLE);
        minsTextView.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        colonTextView.setAlpha(0);
        clockImageView.setVisibility(View.VISIBLE);


    }



    // create a menu


    // set function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // set function
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.whatsapp) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareSubject = "Help!";
            String shareBody = "Hello, soo rha hu bhai.. \nMera Naam liya to please mujhe msg ya call kar dena.. Thanks 😅\uD83D\uDE42 !!";

            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            sharingIntent.setPackage("com.whatsapp");
            // give options to select sharing app
            startActivity(sharingIntent);

        }

        // open settings for microsoft teams
        else if (item.getItemId() == R.id.teams) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", "com.microsoft.teams", null);
            intent.setData(uri);
            startActivity(intent);
        }

        else if (item.getItemId() == R.id.share){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareSubject = "Help!";
            String shareBody = "Hello, I am a little busy, plz DM me if my name gets called..\n Thanks \uD83D\uDE42!!";

            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            // give options to select sharing app
            startActivity(Intent.createChooser(sharingIntent, "Notify friend using"));
            startActivity(sharingIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    // set function

    public void showTimePickerDialogue() {
        Calendar C = Calendar.getInstance();
        final int startHrs = C.get(Calendar.HOUR_OF_DAY); // 24 hrs time
        final int startMins = C.get(Calendar.MINUTE);
        final int startSecs = C.get(Calendar.SECOND);

        TimePickerDialog timerPickerDialogue = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int endHrs, int endMins){



                        // set the visibility of the timer and button
                        clockImageView.setVisibility(View.INVISIBLE);
                        hoursTextView.setVisibility(View.VISIBLE);
                        minsTextView.setVisibility(View.VISIBLE);
                        colonTextView.setAlpha(1);
                        stopButton.setVisibility(View.VISIBLE);


                        String startTime = Integer.toString(startHrs) + ":" + Integer.toString(startMins) + ":" + Integer.toString(startSecs);
                        String endTime = Integer.toString(endHrs) + ":" + Integer.toString(endMins) + ":00";


                        try {

                            Date startDate = simpleDateFormat.parse(startTime);

                            Date endDate = simpleDateFormat.parse(endTime);



                            // now dates are ready lets check if the difference is positive


                            assert endDate != null;

                            // Log.i("SystemTime", Long.toString(System.currentTimeMillis()));

                            // Log.i("Endtime", Long.toString(endDate.getTime()));

                            assert startDate != null;
                           // Log.i("StartTime", Long.toString(startDate.getTime()));


                            long diffInMillis = endDate.getTime() - startDate.getTime();

//                            Log.i("Main/endtime", Long.toString(endDate.getTime()));
//
//                            Log.i("Main/startTime", Long.toString(startDate.getTime()));
//
//                            Log.i("Main/diff", Long.toString(diffInMillis));


                            if (diffInMillis >= 0) {

                                sharedPreferences.edit().putString("endTime", endTime).apply();
                                sharedPreferences.edit().putBoolean("isFinished", false).apply();
                                // if the timer starts from deadLock disable it


                                sharedPreferences.edit().putBoolean("deadLock", false).apply();


                                if(pendingIntent2 != null) alarmManager.cancel(pendingIntent2);


                                diffInMillis += System.currentTimeMillis();

                                setAlarm(diffInMillis);

                                updateGui();


                            } else {
                                Toast.makeText(MainActivity.this, "Select a proper time!", Toast.LENGTH_SHORT).show();
                                showTimePickerDialogue();
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                }, startHrs, startMins, true);
        timerPickerDialogue.show();
    }


    // set function
    private void setAlarm(long timeInMillis) {

//        Log.i("Main", "Alarm set");


        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        long extraTime = timeInMillis + 180000;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, extraTime, pendingIntent2);
   //     Log.i("Info", "AlarmSet");

    }



    // set function

    @Override
    public void onClick(View view) {

    //    Log.i("Main", "clock clicked");

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {

             showTimePickerDialogue();

        } else {
            Toast.makeText(getApplicationContext(), "Turning on Wifi, Switch off hotspot!", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }
    }
}