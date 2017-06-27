package com.sanousun.mdweather.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.sanousun.mdweather.R
import com.sanousun.mdweather.model.response.WeatherResponse
import com.sanousun.mdweather.network.WeatherApiService
import com.sanousun.mdweather.rxmethod.ErrorReturn
import com.sanousun.mdweather.rxmethod.RxTransferHelper
import com.sanousun.mdweather.ui.adapter.DailyAdapter
import com.sanousun.mdweather.ui.adapter.HourlyAdapter
import com.sanousun.mdweather.ui.adapter.SuggestionAdapter
import com.sanousun.mdweather.ui.widget.SimpleDividerDecoration
import kotlinx.android.synthetic.main.activity_mains.*

/**
 * Created by dashu on 2017/6/25.
 * 天气主页
 */

class MainActivity : BaseActivity() {

    private val dailyAdapter by lazy { DailyAdapter(this) }
    private val hourlyAdapter by lazy { HourlyAdapter(this) }
    private val suggestAdapter by lazy { SuggestionAdapter(this) }

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {

        } else {

        }
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        //初始化每日天气预报
        rv_daily_forecast.layoutManager =
                GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        rv_daily_forecast.adapter = dailyAdapter
        //初始化小时天气预报
        rv_hourly_forecast.layoutManager =
                GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
        rv_hourly_forecast.adapter = hourlyAdapter
        //初始化天气指数
        rv_suggest_forecast.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_suggest_forecast.adapter = suggestAdapter
        rv_suggest_forecast.addItemDecoration(SimpleDividerDecoration(this))

        rf_layout.setOnRefreshListener { loadData() }
        ab_layout.addOnOffsetChangedListener {
            _, verticalOffset ->
            rf_layout.isEnabled = verticalOffset == 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_city_list -> {
                val intent = Intent(this, CityListActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_main_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    fun loadData() {
        rf_layout.isRefreshing = true
        val subscription = WeatherApiService.create()
                .getWeatherByCity("hangzhou")
                .compose(RxTransferHelper.composeFilter<WeatherResponse>(object : ErrorReturn {
                    override fun errorStatus(status: String) {
                        rf_layout.isRefreshing = false
                        Toast.makeText(this@MainActivity, status, Toast.LENGTH_SHORT).show()
                    }

                    override fun errorNetwork(th: Throwable) {
                        rf_layout.isRefreshing = false
                        Toast.makeText(this@MainActivity, th.message, Toast.LENGTH_SHORT).show()
                    }
                }))
                .subscribe {
                    weather ->
                    rf_layout.isRefreshing = false
                    fillContent(weather)
                }
        subscriptions.add(subscription)
    }

    fun fillContent(weather: WeatherResponse) {
        tv_temp.text = String.format("%d°", weather.now?.tmp)
        tv_weather.text = weather.now?.cond?.txt
        tv_wind.text = String.format("%s %s", weather.now?.wind?.dir, weather.now?.wind?.sc)
        dailyAdapter.clear()
        if (weather.daily_forecast != null) {
            dailyAdapter.addAll(weather.daily_forecast)
        }
        hourlyAdapter.clear()
        if (weather.hourly_forecast != null) {
            hourlyAdapter.addAll(weather.hourly_forecast)
        }
        suggestAdapter.clear()
        if (weather.suggestion != null) {
            suggestAdapter.addAll(weather.suggestion.getSuggestList())
        }
    }
}