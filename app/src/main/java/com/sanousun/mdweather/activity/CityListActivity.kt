package com.sanousun.mdweather.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.sanousun.mdweather.R
import com.sanousun.mdweather.support.util.LogUtil
import com.sanousun.mdweather.ui.adapter.CityAdapter
import com.sanousun.mdweather.ui.widget.SimpleDividerDecoration
import kotlinx.android.synthetic.main.activity_city_list.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Created by dashu on 2017/6/25.
 * 城市列表界面
 */

class CityListActivity : BaseActivity() {

    val cityAdapter by lazy { CityAdapter(this) }

    override fun setLayoutId(): Int {
        return R.layout.activity_city_list
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView() {
        toolbar.title = "城市列表"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        search_view.setVoiceSearch(false)
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                LogUtil.e("onQueryTextSubmit($query)")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                LogUtil.e("onSearchViewClosed")
            }

            override fun onSearchViewShown() {
                LogUtil.e("onSearchViewShown")
            }
        })
        rv_city.layoutManager = LinearLayoutManager(this)
        rv_city.adapter = cityAdapter
        rv_city.addItemDecoration(SimpleDividerDecoration(this))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_city_list, menu)
        val item = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(item)
        return super.onCreateOptionsMenu(menu)
    }
}
