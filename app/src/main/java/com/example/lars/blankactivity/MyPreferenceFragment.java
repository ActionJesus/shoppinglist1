package com.example.lars.blankactivity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

//extending the standard preference fragment
public class MyPreferenceFragment extends PreferenceFragment {

    //These values are specifed in the prefs.xml file
    //and needs to correspond exactly to those in the prefs.xml file
    private static String SETTINGS_GENDERKEY = "male";
    private static String SETTINGS_NAMEKEY = "name";

    public static boolean isMale(Context context)
    {

        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_GENDERKEY, true);
    }

    public static String getName(Context context)
    {

        return PreferenceManager.getDefaultSharedPreferences(context).getString(SETTINGS_NAMEKEY, "");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adding the prefs
        addPreferencesFromResource(R.xml.prefs);
    }
}