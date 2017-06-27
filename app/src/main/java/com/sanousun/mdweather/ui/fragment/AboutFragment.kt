package com.sanousun.mdweather.ui.fragment

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment

import com.sanousun.mdweather.R

import de.psdev.licensesdialog.LicensesDialog

class AboutFragment : PreferenceFragment(), Preference.OnPreferenceClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
        findPreference("license").onPreferenceClickListener = this
        findPreference("email").onPreferenceClickListener = this
        findPreference("github").onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        val cm = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        when (preference.key) {
            "license" ->
                LicensesDialog.Builder(activity)
                        .setNotices(R.raw.notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .showAppCompat()
            else ->
                AlertDialog.Builder(activity)
                        .setMessage("复制到剪贴板？")
                        .setPositiveButton("确定", { _, _ ->
                            val myClip: ClipData
                            val text = preference.summary
                            myClip = ClipData.newPlainText("text", text)
                            cm.primaryClip = myClip
                        })
                        .setNegativeButton("取消", null)
                        .show()
        }
        return true
    }

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }
}
