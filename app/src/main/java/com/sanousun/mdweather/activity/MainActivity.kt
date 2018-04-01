package com.sanousun.mdweather.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.sanousun.mdweather.R
import com.sanousun.mdweather.adapter.DailyAdapter
import com.sanousun.mdweather.adapter.HourlyAdapter
import com.sanousun.mdweather.adapter.SuggestionAdapter
import com.sanousun.mdweather.bg.WeatherView
import com.sanousun.mdweather.model.response.WeatherResponse
import com.sanousun.mdweather.network.WeatherApiService
import com.sanousun.mdweather.rxmethod.ErrorReturn
import com.sanousun.mdweather.rxmethod.RxTransferHelper
import com.sanousun.mdweather.support.util.LogUtil
import com.sanousun.mdweather.widget.SimpleDividerDecoration
import com.sanousun.mdweather.widget.weather.convertToKindRule
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by dashu on 2017/6/25.
 * 天气主页
 */

class MainActivity : BaseActivity() {

    companion object {
        const val EXTRA_CITY = "extra_city"
        fun createIntent(context: Context, city: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_CITY, city)
            return intent
        }
    }

    private val dailyAdapter by lazy { DailyAdapter(this) }
    private val hourlyAdapter by lazy { HourlyAdapter(this) }
    private val suggestAdapter by lazy { SuggestionAdapter(this) }

    private val aMapLocationClient by lazy { AMapLocationClient(this) }
    private val rxPermission by lazy { RxPermissions(this) }

    private var city: String = "杭州市"

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        //初始化每日天气预报
        rv_daily_forecast.layoutManager =
                GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        rv_daily_forecast.adapter = dailyAdapter
        //初始化小时天气预报
        rv_hourly_forecast.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_hourly_forecast.adapter = hourlyAdapter
        //初始化天气指数
        rv_suggest_forecast.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_suggest_forecast.adapter = suggestAdapter
        rv_suggest_forecast.addItemDecoration(SimpleDividerDecoration(this))

        rf_layout.setOnRefreshListener { loadData() }
        weather_bg.setOpenGravitySensor(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            city = savedInstanceState.getString(EXTRA_CITY) ?: city
            loadData()
        } else {
            //启动定位
            rxPermission
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe {
                        if (it) {
                            aMapLocationClient.setLocationListener { aMapLocation: AMapLocation? ->
                                aMapLocation?.let {
                                    if (it.errorCode == 0) {
                                        LogUtil.e("国家：${it.country}，省份：${it.province}\n" +
                                                "城市：${it.city} - ${it.cityCode}，地区：${it.district}")
                                        toolbar.setLogo(R.drawable.ic_action_location)
                                        city = it.city
                                    } else {
                                        LogUtil.e(it.errorInfo)
                                        Toast.makeText(this, "定位失败，默认切换杭州", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                loadData()
                            }
                            val option = AMapLocationClientOption()
                            option.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
                            option.isOnceLocation = true
                            aMapLocationClient.setLocationOption(option)
                            aMapLocationClient.startLocation()
                            rf_layout.isRefreshing = true
                        } else {
                            Toast.makeText(this, "定位权限获取失败，默认切换杭州", Toast.LENGTH_SHORT).show()
                            loadData()
                        }
                    }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        city = intent?.getStringExtra(EXTRA_CITY) ?: city
        toolbar.logo = null
        loadData()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_CITY, city)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_city_list -> {
                val intent = Intent(this, CityListActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_main_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    private fun loadData() {
        rf_layout.isRefreshing = true
        val disposable = WeatherApiService.create()
                .getWeatherByCity(city)
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
                .subscribe { weather ->
                    rf_layout.isRefreshing = false
                    fillContent(weather)
                }
        disposables.add(disposable)
    }

    private fun fillContent(weather: WeatherResponse) {
        toolbar.title = weather.basic?.city
        tv_temp.text = String.format("%d°", weather.now?.tmp)
        tv_weather.text = weather.now?.cond?.txt
        tv_wind.text = String.format("%s %s", weather.now?.wind?.dir, weather.now?.wind?.sc)
        weather_bg.setWeather(
                weather.daily_forecast?.get(0)?.convertToKindRule()
                        ?: WeatherView.WEATHER_KIND_CLEAR_DAY)
        dailyAdapter.clear()
        weather.daily_forecast?.let {
            dailyAdapter.addAll(it)
        }
        hourlyAdapter.clear()
        weather.hourly_forecast?.let {
            hourlyAdapter.addAll(it)
        }
        suggestAdapter.clear()
        weather.suggestion?.let {
            suggestAdapter.addAll(it.getSuggestList())
        }
    }
}