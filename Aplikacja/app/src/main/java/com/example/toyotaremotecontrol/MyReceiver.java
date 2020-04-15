package com.example.toyotaremotecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MyReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg,phoneNo = "";
    String server_number = "+48517858688";


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
                  switch (msg)
                   {
                       case "A":
                           Toast.makeText(context,R.string.open_correct, Toast.LENGTH_LONG).show();
                           break;
                       case "B":
                           Toast.makeText(context,R.string.close_correct, Toast.LENGTH_LONG).show();
                          // Snackbar.make(context, getString(R.string.close_correct),Snackbar.LENGTH_LONG).show();
                           break;
                       default:
                           Toast.makeText(context,R.string.server_error, Toast.LENGTH_LONG).show();
                          // Snackbar.make(context, getString(R.string.server_error),Snackbar.LENGTH_LONG).show();
                   }


               }



           }
       }
    }
}
