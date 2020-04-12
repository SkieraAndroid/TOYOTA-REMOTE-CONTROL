package com.example.toyotaremotecontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);


        final FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        fab.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                EditText number = findViewById(R.id.editText);
                String phone_number = number.getText().toString();

                if(phone_number.length()==9 && phone_number.matches("([5-8]?[0-9]{8})")) {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.change_correct), Toast.LENGTH_LONG);
                   // toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER, 0, 0);
                    toast.show();

                    Intent exit =
                            new Intent(getApplicationContext(), MainActivity.class);


                    startActivityForResult(exit, 1);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.phone_error), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

        });

    }
}
