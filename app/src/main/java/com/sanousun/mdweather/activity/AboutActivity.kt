package com.sanousun.mdweather.activity

import android.os.Bundle
import com.sanousun.mdweather.R
import com.sanousun.mdweather.ui.fragment.AboutFragment
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Created by dashu on 2017/6/27.
 * 关于界面
 */

class AboutActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_about
    }

    override fun initData(savedInstanceState: Bundle?) {
        //null
    }

    override fun initView() {
        toolbar.title = "关于"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fragmentManager
                .beginTransaction()
                .add(R.id.about_container, AboutFragment.newInstance())
                .commit()
    }
}
