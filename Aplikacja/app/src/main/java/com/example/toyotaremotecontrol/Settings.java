package com.example.toyotaremotecontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Settings extends AppCompatActivity {

    private final String TASKS_SHARED_PREFS = "DataSharedPrefs";
    private final String TASKS_TEXT_FILE = "app_data.txt";
    private String NUMBER = "number";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);
        restoreDataFromFile();
        EditText number_text = findViewById(R.id.editText);
        number_text.setText(NUMBER);

        final FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        fab.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                EditText number = findViewById(R.id.editText);
                String phone_number = number.getText().toString();

                if(phone_number.length()==9 && phone_number.matches("([5-8]?[0-9]{8})")) {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.change_correct), Toast.LENGTH_LONG);

                    toast.show();
                    NUMBER ="+48"+phone_number;
                    saveDataToFile();

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



    private void saveDataToFile()
    {
        try(FileOutputStream fileOutputStream = openFileOutput(TASKS_TEXT_FILE,MODE_PRIVATE))
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutputStream.getFD()));

            String delim = ";";

            String line = NUMBER + delim;

            writer.write(line);
            writer.newLine();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                NUMBER = line2[0];

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
