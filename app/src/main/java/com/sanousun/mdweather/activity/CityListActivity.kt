package com.sanousun.mdweather.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.sanousun.mdweather.R
import com.sanousun.mdweather.adapter.CityAdapter
import com.sanousun.mdweather.model.response.CityResponse
import com.sanousun.mdweather.network.WeatherApiService
import com.sanousun.mdweather.rxmethod.ErrorReturn
import com.sanousun.mdweather.rxmethod.RxTransferHelper
import com.sanousun.mdweather.support.db.DataSource
import com.sanousun.mdweather.support.util.toastShort
import com.sanousun.mdweather.widget.SimpleDividerDecoration
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
        fresh_layout.isEnabled = false

        search_view.setVoiceSearch(false)
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fresh_layout.isRefreshing = true
                    val disposable = WeatherApiService.create()
                            .searchCity(it)
                            .compose(RxTransferHelper.composeFilter<CityResponse>(object : ErrorReturn {
                                override fun errorStatus(status: String) {
                                    fresh_layout.isRefreshing = false
                                    toastShort(status)
                                }

                                override fun errorNetwork(th: Throwable) {
                                    fresh_layout.isRefreshing = false
                                    toastShort(th.message ?: "网络错误")
                                }
                            }))
                            .map { cityResponse -> cityResponse.basic }
                            .subscribe { city ->
                                fresh_layout.isRefreshing = false
                                if (city == null) {
                                    toastShort("查询无结果")
                                } else {
                                    cityAdapter.add(city)
                                }
                            }
                    disposables.add(disposable)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                loadCityData()
            }

            override fun onSearchViewShown() {
                cityAdapter.clear()
                cityAdapter.itemClickListener = { city ->
                    DataSource.getInstance(this@CityListActivity)?.save(city)
                    search_view.closeSearch()
                }
            }
        })
        rv_city.layoutManager = LinearLayoutManager(this)
        rv_city.adapter = cityAdapter
        val divider = SimpleDividerDecoration(this)
        divider.includeBottom = true
        rv_city.addItemDecoration(divider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadCityData()
    }

    override fun onBackPressed() {
        if (search_view.isSearchOpen) {
            search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 从数据库中获取城市信息
     */
    fun loadCityData() {
        cityAdapter.itemClickListener = { (id) ->
            startActivity(MainActivity.createIntent(this, id))
        }
        cityAdapter.clear()
        DataSource.getInstance(this)?.getCityList()?.let {
            cityAdapter.addAll(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.menu_city_list, it)
            val item = it.findItem(R.id.action_search)
            search_view.setMenuItem(item)
        }
        return super.onCreateOptionsMenu(menu)
    }
}
