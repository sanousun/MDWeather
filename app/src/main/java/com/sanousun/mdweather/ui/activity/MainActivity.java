package com.sanousun.mdweather.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.sanousun.mdweather.R;
import com.sanousun.mdweather.app.WeatherApiUtil;
import com.sanousun.mdweather.model.SimpleWeatherBean;
import com.sanousun.mdweather.model.WeatherBean;
import com.sanousun.mdweather.rxmethod.SimpleErrorVerify;
import com.sanousun.mdweather.support.util.DensityUtil;
import com.sanousun.mdweather.support.util.NetworkUtil;
import com.sanousun.mdweather.support.util.StringUtil;
import com.sanousun.mdweather.support.util.WeatherIconUtil;
import com.sanousun.mdweather.ui.widget.AirQualityIndexView;
import com.sanousun.mdweather.ui.widget.SevenDaysWeatherView;
import com.sanousun.mdweather.ui.widget.SevenDaysWeatherView.WeatherHolder;
import com.sanousun.mdweather.ui.widget.SunRiseToSetView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import rx.Subscription;

import static com.sanousun.mdweather.ui.widget.SunRiseToSetView.Clock;

// TODO: 2016/2/1 后续考虑加入fragmentAdapter
public class MainActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        AMapLocationListener {

    private static final String EXTRA_IS_LOCATION = "is_location";
    private static final String EXTRA_SIMPLE_WEATHER = "simple_weather";

    private boolean isNight;
    private String mWeatherType;
    //应用于定位服务
    private AMapLocationClient mLocationClient;
    private boolean isLocal;
    private boolean isLaunched;

    //用于判断两次返回键的间隔时间
    private long currentTime = 0;

    private SimpleWeatherBean mSimpleWeather;
    private WeatherBean mWeather;

    @Bind(R.id.main_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.main_appbar_layout)
    AppBarLayout mAppbarLayout;
    @Bind(R.id.main_collapsing_tb_layout)
    CollapsingToolbarLayout mCollapsingTBLayout;

    //------header container---------
    @Bind(R.id.main_header_container)
    RelativeLayout mHeaderContainer;
    @Bind(R.id.main_tv_temp)
    TextView mTempText;
    @Bind(R.id.main_tv_type)
    TextView mWeatherTypeText;
    @Bind(R.id.main_tv_temp_hl)
    TextView mTempHLText;
    @Bind(R.id.main_tv_wind)
    TextView mWindText;

    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;

    //-------content container--------
    @Bind(R.id.main_content_container)
    View mMainContainer;
    @Bind(R.id.main_view_sun_rise2set)
    SunRiseToSetView mSunR2SView;
    @Bind(R.id.main_view_seven_day)
    SevenDaysWeatherView m7dWeatherView;
    @Bind(R.id.main_view_aqi)
    AirQualityIndexView mAqiView;
    //---------weather index----------
    @Bind(R.id.main_index_container)
    LinearLayout mIndexContainer;
    private Map<String, View> mIndexViews = new HashMap<>();
    @Bind(R.id.main_view_gm)
    View mGanMaoView;
    @Bind(R.id.main_view_fs)
    View mFangSheView;
    @Bind(R.id.main_view_ct)
    View mChuanYiView;
    @Bind(R.id.main_view_xc)
    View mXiCheView;
    @Bind(R.id.main_view_ls)
    View mLiangShaiView;
    @Bind(R.id.main_view_yd)
    View mYunDongView;
    //---------------------------------------------------------------------------------------------

    /**
     * 设置转场动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Transition makeEnterTransition() {
        Transition t = new Fade();
        t.setDuration(500);
        return t;
    }

    public static void startActivity(Activity activity, boolean isLocation,
                                     SimpleWeatherBean simpleWeatherBean) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EXTRA_IS_LOCATION, isLocation);
        intent.putExtra(EXTRA_SIMPLE_WEATHER, simpleWeatherBean);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(makeEnterTransition());
        }
        Intent intent = getIntent();
        if (intent.getAction() == null) {
            isLaunched = false;
            isLocal = intent.getBooleanExtra(EXTRA_IS_LOCATION, false);
            mSimpleWeather = intent.getParcelableExtra(EXTRA_SIMPLE_WEATHER);
            isNight = mSimpleWeather.isNight();
            setActionBar();
            setBackground();
            updateHeader();
        } else {
            //初始化定位服务
            isLaunched = true;
            isLocal = true;
            mLocationClient = new AMapLocationClient(getApplicationContext());
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setOnceLocation(true);
            mLocationClient.setLocationOption(option);
            mLocationClient.setLocationListener(this);
            mLocationClient.startLocation();
        }
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        mRefreshLayout.setProgressViewOffset(false, 0, DensityUtil.dip2px(this, 48));
    }

    @Override
    protected void initData() {
        mIndexViews.put("gm", mGanMaoView);
        mIndexViews.put("fs", mFangSheView);
        mIndexViews.put("ct", mChuanYiView);
        mIndexViews.put("xc", mXiCheView);
        mIndexViews.put("ls", mLiangShaiView);
        mIndexViews.put("yd", mYunDongView);
    }

    @Override
    protected void initEvent() {
        mRefreshLayout.setOnRefreshListener(this);
        mAppbarLayout.addOnOffsetChangedListener(
                (appBarLayout, verticalOffset) -> mRefreshLayout.setEnabled(verticalOffset == 0));
    }

    @Override
    public void onRefresh() {
        if (mSimpleWeather == null) return;
        mRefreshLayout.setRefreshing(true);
        Subscription subscription = WeatherApiUtil.getWeatherApi()
                .getSimpleWeather(mSimpleWeather.citycode)
                .compose(WeatherApiUtil.composeFilter(new SimpleErrorVerify(this, mRefreshLayout)))
                .subscribe(simpleWeatherBean -> {
                    mSimpleWeather = simpleWeatherBean;
                    updateHeader();
                });
        mCompositeSubscription.add(subscription);
    }

    private void setActionBar() {
        if (mSimpleWeather == null) return;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mSimpleWeather.city);
            if (isLocal)
                actionBar.setLogo(R.drawable.ic_action_location);
            if (!isLaunched)
                actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setBackground() {
        int bgRes = WeatherIconUtil.getBackgroundResId(this, mWeatherType, isNight);
        if (bgRes != -1) {
            mHeaderContainer.setBackgroundResource(bgRes);
        }
        int colorRes = WeatherIconUtil.getBackColorResId(this, mWeatherType, isNight);
        if (colorRes != -1) {
            int color = ContextCompat.getColor(this, colorRes);
            mSunR2SView.setBackgroundColor(color);
            m7dWeatherView.setBackgroundColor(color);
            mAqiView.setBackgroundColor(color);
            mIndexContainer.setBackgroundColor(color);
            mCollapsingTBLayout.setContentScrimColor(color);
        }
    }

    private void updateHeader() {
        if (mSimpleWeather == null) return;
        //得到RunRiseToSetView所需要的数据
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Clock now = new Clock(hour, minute);
        Clock sunrise = new Clock(mSimpleWeather.getRiseH(), mSimpleWeather.getRiseM());
        Clock sunset = new Clock(mSimpleWeather.getSetH(), mSimpleWeather.getSetM());
        //判断当前时间是否是晚上
        isNight = mSimpleWeather.isNight();
        mWeatherType = mSimpleWeather.weather;
        setActionBar();
        setBackground();
        mTempText.setText(String.format("%s°", mSimpleWeather.temp));
        mWeatherTypeText.setText(mSimpleWeather.weather);
        mTempHLText.setText(String.format("%s°/%s°", mSimpleWeather.h_tmp, mSimpleWeather.l_tmp));
        mWindText.setText(String.format("%s %s", mSimpleWeather.WD, mSimpleWeather.WS));
        mSunR2SView.setNowClock(sunrise, sunset, now);
        loadWeather();
    }

    private void loadWeather() {
        Subscription subscription = WeatherApiUtil.getWeatherApi()
                .getWeather(mSimpleWeather.citycode)
                .compose(WeatherApiUtil.composeFilter(new SimpleErrorVerify(this, mRefreshLayout)))
                .subscribe(weatherBean -> {
                    mWeather = weatherBean;
                    updateContent();
                });
        mCompositeSubscription.add(subscription);
    }

    private void updateContent() {
        mRefreshLayout.setRefreshing(false);
        mMainContainer.setVisibility(View.VISIBLE);
        if (mWeather == null) return;
        //得到SevenDayWeatherView所需要的数据
        WeatherHolder[] whs = new WeatherHolder[7];
        int k = 0;
        while (k < 2) {
            WeatherBean.HistoryEntity historyEntity =
                    mWeather.history.get(mWeather.history.size() + k - 2);
            WeatherHolder wh = new WeatherHolder();
            wh.setDay(StringUtil.getDay(historyEntity.date));
            wh.setWeek(StringUtil.getWeekDay(historyEntity.week));
            wh.setType(historyEntity.type);
            wh.setWeatherIcon(WeatherIconUtil.getIconResId(this, wh.getType()));
            wh.setMaxTemp(StringUtil.getTemp(historyEntity.hightemp));
            wh.setMinTemp(StringUtil.getTemp(historyEntity.lowtemp));
            whs[k++] = wh;
        }
        WeatherBean.TodayEntity todayEntity = mWeather.today;
        WeatherHolder holder = new WeatherHolder();
        holder.setDay(StringUtil.getDay(todayEntity.date));
        holder.setWeek("今日");
        holder.setType(todayEntity.type);
        holder.setWeatherIcon(WeatherIconUtil.getIconResId(this, holder.getType()));
        holder.setMaxTemp(StringUtil.getTemp(todayEntity.hightemp));
        holder.setMinTemp(StringUtil.getTemp(todayEntity.lowtemp));
        whs[k++] = holder;
        while (k < 7) {
            WeatherBean.ForecastEntity forecastEntity =
                    mWeather.forecast.get(k - 3);
            WeatherHolder wh = new WeatherHolder();
            wh.setDay(StringUtil.getDay(forecastEntity.date));
            wh.setWeek(StringUtil.getWeekDay(forecastEntity.week));
            wh.setType(forecastEntity.type);
            wh.setWeatherIcon(WeatherIconUtil.getIconResId(this, wh.getType()));
            wh.setMaxTemp(StringUtil.getTemp(forecastEntity.hightemp));
            wh.setMinTemp(StringUtil.getTemp(forecastEntity.lowtemp));
            whs[k++] = wh;
        }
        m7dWeatherView.setWeatherData(whs);

        mAqiView.setAqi(StringUtil.getAqi(todayEntity.aqi));

        //设置生活指数
        for (WeatherBean.TodayEntity.IndexEntity index : mWeather.today.index) {
            View v = mIndexViews.get(index.code);
            ((ImageView) v.findViewById(R.id.view_index_iv_icon)).
                    setImageResource(WeatherIconUtil.getIndexResId(this, index.code));
            String text = index.name +
                    (TextUtils.isEmpty(index.index) ? "" : ("--" + index.index));
            ((TextView) v.findViewById(R.id.view_index_tv_name)).
                    setText(text);
            ((TextView) v.findViewById(R.id.view_index_tv_index)).
                    setText(index.details);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(this);
                break;
            case R.id.action_city_list:
                if (mSimpleWeather == null) {
                    break;
                }
                if (isLaunched) {
                    CityListActivity.toActivity(this, mSimpleWeather.citycode);
                } else {
                    this.finish();
                }
                break;
            case R.id.action_main_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String city = aMapLocation.getCity();
                String cityName = city.substring(0, city.length() - 1);
                Subscription subscription = WeatherApiUtil.getWeatherApi()
                        .getSimpleWeatherForLoc(cityName)
                        .compose(WeatherApiUtil.composeFilter(new SimpleErrorVerify(this, mRefreshLayout)))
                        .subscribe(simpleWeatherBean -> {
                            mSimpleWeather = simpleWeatherBean;
                            updateHeader();
                        });
                mCompositeSubscription.add(subscription);
            } else {
                //显示错误信息
                if (NetworkUtil.isNetworkConnected(this)) {
                    Toast.makeText(this, aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
                }
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!isLaunched) {
            super.onBackPressed();
            return;
        }
        if ((System.currentTimeMillis() - currentTime) < 2000) {
            finish();
        } else {
            currentTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出！", Toast.LENGTH_SHORT).show();
        }
    }
}
