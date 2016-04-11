package com.zolo.acem;

import android.preference.PreferenceActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.LayoutInflater;
import android.view.View;
/**
 * Created by Mighty on 4/10/2016.
 */
public class Preferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final AlertDialog.Builder aboutbuilder = new AlertDialog.Builder(this);



        getPreferenceManager()
                .findPreference("about")
                .setOnPreferenceClickListener(
                        new OnPreferenceClickListener() {
                            public boolean onPreferenceClick(Preference preference) {

                                aboutbuilder.setTitle(R.string.about);
                                LayoutInflater inflater = getLayoutInflater();
                                View radioLayout = inflater.inflate(R.layout.about, null);
                                aboutbuilder.setView(radioLayout);

                                AlertDialog aboutbuilderDialog = aboutbuilder.create();
                                aboutbuilderDialog.show();
                                return true;

                            }
                        });

    }
}
