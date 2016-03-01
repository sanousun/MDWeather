package com.sanousun.mdweather.ui.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.sanousun.mdweather.R;

import de.psdev.licensesdialog.LicensesDialog;

public class AboutFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        findPreference("license").setOnPreferenceClickListener(this);
        findPreference("email").setOnPreferenceClickListener(this);
        findPreference("github").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(final Preference preference) {
        final ClipboardManager cm =
                (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        switch (preference.getKey()) {
            case "license":
                new LicensesDialog.Builder(getActivity()).
                        setNotices(R.raw.notices).setIncludeOwnLicense(true).
                        build().showAppCompat();
                break;
            default:
                new AlertDialog.Builder(getActivity()).
                        setMessage("复制到剪贴板？").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ClipData myClip;
                                CharSequence text = preference.getSummary();
                                myClip = ClipData.newPlainText("text", text);
                                cm.setPrimaryClip(myClip);
                            }
                        }).
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
                break;
        }
        return true;

    }
}
