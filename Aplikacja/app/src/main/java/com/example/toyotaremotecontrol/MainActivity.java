package com.example.toyotaremotecontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static androidx.core.content.ContextCompat.getSystemService;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;

    private static final String CHANNEL_ID = "ID";
    private static final String CHANNEL_NAME = "Notification";
    private static final String CHANNEL_DESC = "Car info";
    public String phoneNumber = "+48517858688";

    private final String TASKS_SHARED_PREFS = "DataSharedPrefs";
    private final String TASKS_TEXT_FILE = "app_data.txt";
    private String NUMBER = "number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


            restoreDataFromFile();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent exit =
                        new Intent(getApplicationContext(),Settings.class);

                startActivityForResult(exit,1);
            }
        });
                // check if the permission is not granted
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
             //check if the permission is not granted then check of the user has denied the permission

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
            //check if the permission is not granted then check of the user has denied the permission

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }

    }

    public void myClickHandler(View view)
    {

        switch (view.getId())
        {
            case R.id.close_img:
                sendTextMessageClose();
                break;
            case R.id.open_img:
                sendTextMessageOpen();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    public void onRequestRECEIVEPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "You didn't get permission to communicate with server!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    protected void sendTextMessageClose()
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null,  getString(R.string.server_close_message), null, null);
        Toast.makeText(getApplicationContext(), R.string.close,
                Toast.LENGTH_LONG).show();
    }

    protected void sendTextMessageOpen()
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null,  getString(R.string.server_open_message), null, null);
        Toast.makeText(getApplicationContext(), R.string.open,
                Toast.LENGTH_LONG).show();
    }


     private void restoreDataFromFile()
     {
        try
        {
            FileInputStream fileInputStream = openFileInput(TASKS_TEXT_FILE);
            BufferedReader reader = new BufferedReader(new FileReader(fileInputStream.getFD()));
            String line;
            String delim = ";";
            NUMBER = "";
            while ((line = reader.readLine()) != null)
            {
                String[] line2 = line.split(delim);
                phoneNumber = line2[0];

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
     }


}
