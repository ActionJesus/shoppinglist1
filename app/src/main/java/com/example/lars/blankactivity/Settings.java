package com.example.lars.blankactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }

    public void ok(){

        Intent data = new Intent();


        EditText s = (EditText) findViewById(R.id.changeText);
        String ss = s.getText().toString();
        data.putExtra("myData1", ss);


// Activity finished ok, return the data
        setResult(RESULT_OK);

        super.onBackPressed();
    }
}
