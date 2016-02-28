package com.sanousun.mdweather.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.sanousun.mdweather.R;

public class AboutFragment extends PreferenceFragment {

    public static AboutFragment newInstance(){
        return new AboutFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
