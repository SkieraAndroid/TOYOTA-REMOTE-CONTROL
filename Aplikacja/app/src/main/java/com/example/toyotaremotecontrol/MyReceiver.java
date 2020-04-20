package com.example.toyotaremotecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MyReceiver extends BroadcastReceiver  {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    private static final String CHANNEL_ID = "ID";
    String msg,phoneNo = "";
    String server_number = "+48517858688";
    private final String TASKS_SHARED_PREFS = "DataSharedPrefs";
    private final String TASKS_TEXT_FILE = "app_data.txt";
    private String NUMBER = "number";

    @Override
    public void onReceive(Context context, Intent intent) {
        //retrieving the general action to be performed and display on log

       Log.i(TAG,"Intent Received: " + intent.getAction());
       if(intent.getAction()==SMS_RECEIVED)
       {
           //retreiving a map of extending data from the intent
           Bundle dataBundle = intent.getExtras();
           if(dataBundle!=null)
           {
                //creating PDU(Protocol Data Unit) object - this is a protocol for transfer messages
               Object[] mypdu = (Object[])dataBundle.get("pdus");
               final SmsMessage[] message = new SmsMessage[mypdu.length];

               for(int i = 0; i<mypdu.length; i++)
               {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) // API Level >=23
                    {
                       String format = dataBundle.getString("format");
                       // from PDU we get all object and SmsMessage Object using following lines:
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i],format);
                    }
                    else //API Level < 23
                    {
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }

                   msg = message[i].getMessageBody();
                   phoneNo = message[i].getOriginatingAddress();
               }

               if(phoneNo.equals(server_number))
               {
                   NotificationManagerCompat mNotification = NotificationManagerCompat.from(context);
                           switch (msg)
                   {
                       case "A":
                           Toast.makeText(context,R.string.open_correct, Toast.LENGTH_LONG).show();

                           NotificationCompat.Builder mBuilder =
                                   new NotificationCompat.Builder(context,CHANNEL_ID)
                                           .setSmallIcon(R.drawable.ic_lock_open)
                                           .setContentTitle("Toyota Carina E")
                                           .setContentText("Car was open successfully!")
                                           .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                   .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.unlock));

                           mNotification.notify(1,mBuilder.build());

                           break;
                       case "B":
                           Toast.makeText(context,R.string.close_correct, Toast.LENGTH_LONG).show();
                           NotificationCompat.Builder mBuilder2 =
                                   new NotificationCompat.Builder(context,CHANNEL_ID)
                                           .setSmallIcon(R.drawable.ic_lock_close)
                                           .setContentTitle("Toyota Carina E")
                                           .setContentText("Car was close successfully!")
                                           .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                           .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.lock));

                           mNotification.notify(1,mBuilder2.build());
                           break;
                       default:
                           Toast.makeText(context,R.string.server_error, Toast.LENGTH_LONG).show();

                           NotificationCompat.Builder mBuilder3 =
                                   new NotificationCompat.Builder(context,CHANNEL_ID)
                                           .setSmallIcon(R.drawable.ic_lock_close)
                                           .setContentTitle("Toyota Carina E")
                                           .setContentText("COMMUNICATION ERROR!")
                                           .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                           .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.error));


                           mNotification.notify(1,mBuilder3.build());

                   }


               }



           }
       }





    }




}
