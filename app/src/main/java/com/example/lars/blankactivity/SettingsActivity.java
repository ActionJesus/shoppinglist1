package com.example.lars.blankactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SettingsActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment())
                .commit();
        //note - there is not setContentView and no xml layout
        //for this activity. Because that is defined 100 %
        //in the fragment
    }
    public void ok(){

        Intent data = new Intent();
        data.putExtra("myData1", "Data 1 value");

// Activity finished ok, return the data
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
}