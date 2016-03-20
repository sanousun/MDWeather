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
import com.sanousun.mdweather.model.SimpleWeatherBean;
import com.sanousun.mdweather.model.WeatherBean;
import com.sanousun.mdweather.rxmethod.ErrorVerify;
import com.sanousun.mdweather.rxmethod.RxMethod;
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
import de.greenrobot.event.Subscribe;

import static com.sanousun.mdweather.ui.widget.SunRiseToSetView.Clock;
import static com.sanousun.mdweather.ui.widget.SunRiseToSetView.VISIBLE;

// TODO: 2016/2/1 后续考虑加入fragmentAdapter
public class MainActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        AppBarLayout.OnOffsetChangedListener,
        AMapLocationListener {

    private static final String IS_LOCATION = "is_location?";
    private static final String CITY_ID = "city_id";
    private static final String CITY_NAME = "city_name";
    private static final String WEATHER_TYPE = "weather_type";
    private static final String IS_NIGHT = "is_night?";

    private String mCityId;
    private String mCityName;
    private boolean isNight;
    private String mWeatherType;
    //应用于定位服务
    private AMapLocationClient mLocationClient;
    private boolean isLocal;
    private boolean isLaunched;

    private ErrorVerify errorVerify;

    //用于判断两次返回键的间隔时间
    private long currentTime = 0;

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
    //--------------------------------

    //-------content container--------
    @Bind(R.id.main_content_container)
    LinearLayout mContentContainer;
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

    public static void startActivity(
            Activity activity, boolean isLocation,
            String cityId, String cityName,
            String weatherType, Boolean isNight) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(IS_LOCATION, isLocation);
        intent.putExtra(CITY_ID, cityId);
        intent.putExtra(CITY_NAME, cityName);
        intent.putExtra(WEATHER_TYPE, weatherType);
        intent.putExtra(IS_NIGHT, isNight);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
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
            isLocal = intent.getBooleanExtra(IS_LOCATION, false);
            mCityId = intent.getStringExtra(CITY_ID);
            mCityName = intent.getStringExtra(CITY_NAME);
            mWeatherType = intent.getStringExtra(WEATHER_TYPE);
            isNight = intent.getBooleanExtra(IS_NIGHT, false);
            setActionBar();
            setBackground();
            onRefresh();
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
            mRefreshLayout.setProgressViewOffset(false, 0, DensityUtil.dip2px(this, 48));
            mRefreshLayout.setRefreshing(true);
        }
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
    }

    @Override
    protected void initData() {
        mIndexViews.put("gm", mGanMaoView);
        mIndexViews.put("fs", mFangSheView);
        mIndexViews.put("ct", mChuanYiView);
        mIndexViews.put("xc", mXiCheView);
        mIndexViews.put("ls", mLiangShaiView);
        mIndexViews.put("yd", mYunDongView);

        errorVerify = new SimpleErrorVerify(getApplicationContext()) {
            @Override
            public void callback() {
                mRefreshLayout.setRefreshing(false);
            }
        };
    }

    @Override
    protected void initEvent() {
        mRefreshLayout.setOnRefreshListener(this);
        mAppbarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mCompositeSubscription.add(
                RxMethod.getSimpleWeather(mCityId, errorVerify));
    }

    @Override
    public void onOffsetChanged(AppBarLayout al, int o) {
        mRefreshLayout.setEnabled(o == 0);
    }

    //----------------------------------------Event Bus-------------------------------------------
    @Subscribe
    public void onEventMainThread(SimpleWeatherBean simpleWeatherBean) {
        updateHeader(simpleWeatherBean);
        mCityId = simpleWeatherBean.getCitycode();
        mCompositeSubscription.add(
                RxMethod.getWeather(mCityId, errorVerify));
    }

    @Subscribe
    public void onEventMainThread(WeatherBean weatherBean) {
        updateContent(weatherBean);
        mContentContainer.setVisibility(VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }
    //-------------------------------------------Event Bus----------------------------------------

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mCityName);
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

    private void updateHeader(SimpleWeatherBean s) {
        if (s == null) return;
        //得到RunRiseToSetView所需要的数据
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Clock now = new Clock(hour, minute);
        Clock sunrise = new Clock(s.getRiseH(), s.getRiseM());
        Clock sunset = new Clock(s.getSetH(), s.getSetM());
        //判断当前时间是否是晚上
        isNight = s.isNight();
        mWeatherType = s.getWeather();
        setActionBar();
        setBackground();
        mTempText.setText(String.format("%s°", s.getTemp()));
        mWeatherTypeText.setText(s.getWeather());
        mTempHLText.setText(String.format("%s°/%s°", s.getH_tmp(), s.getL_tmp()));
        mWindText.setText(String.format("%s %s", s.getWD(), s.getWS()));

        mSunR2SView.setNowClock(sunrise, sunset, now);
    }

    private void updateContent(WeatherBean w) {
        if (w == null) return;
        //得到SevenDayWeatherView所需要的数据
        WeatherHolder[] whs = new WeatherHolder[7];
        int k = 0;
        while (k < 2) {
            WeatherBean.HistoryEntity historyEntity =
                    w.getHistory().get(w.getHistory().size() + k - 2);
            WeatherHolder wh = new WeatherHolder();
            wh.setDay(StringUtil.getDay(historyEntity.getDate()));
            wh.setWeek(StringUtil.getWeekDay(historyEntity.getWeek()));
            wh.setType(historyEntity.getType());
            wh.setWeatherIcon(WeatherIconUtil.getIconResId(this, wh.getType()));
            wh.setMaxTemp(StringUtil.getTemp(historyEntity.getHightemp()));
            wh.setMinTemp(StringUtil.getTemp(historyEntity.getLowtemp()));
            whs[k++] = wh;
        }
        WeatherBean.TodayEntity todayEntity = w.getToday();
        WeatherHolder holder = new WeatherHolder();
        holder.setDay(StringUtil.getDay(todayEntity.getDate()));
        holder.setWeek("今日");
        holder.setType(todayEntity.getType());
        holder.setWeatherIcon(WeatherIconUtil.getIconResId(this, holder.getType()));
        holder.setMaxTemp(StringUtil.getTemp(todayEntity.getHightemp()));
        holder.setMinTemp(StringUtil.getTemp(todayEntity.getLowtemp()));
        whs[k++] = holder;
        while (k < 7) {
            WeatherBean.ForecastEntity forecastEntity =
                    w.getForecast().get(k - 3);
            WeatherHolder wh = new WeatherHolder();
            wh.setDay(StringUtil.getDay(forecastEntity.getDate()));
            wh.setWeek(StringUtil.getWeekDay(forecastEntity.getWeek()));
            wh.setType(forecastEntity.getType());
            wh.setWeatherIcon(WeatherIconUtil.getIconResId(this, wh.getType()));
            wh.setMaxTemp(StringUtil.getTemp(forecastEntity.getHightemp()));
            wh.setMinTemp(StringUtil.getTemp(forecastEntity.getLowtemp()));
            whs[k++] = wh;
        }
        m7dWeatherView.setWeatherData(whs);

        mAqiView.setAqi(StringUtil.getAqi(todayEntity.getAqi()));

        //设置生活指数
        for (WeatherBean.TodayEntity.IndexEntity index : w.getToday().getIndex()) {
            View v = mIndexViews.get(index.getCode());
            ((ImageView) v.findViewById(R.id.view_index_iv_icon)).
                    setImageResource(WeatherIconUtil.getIndexResId(this, index.getCode()));
            String text = index.getName() +
                    (TextUtils.isEmpty(index.getIndex()) ?
                            "" : ("--" + index.getIndex()));
            ((TextView) v.findViewById(R.id.view_index_tv_name)).
                    setText(text);
            ((TextView) v.findViewById(R.id.view_index_tv_index)).
                    setText(index.getDetails());
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
                if (mCityId == null) {
                    break;
                }
                if (isLaunched) {
                    CityListActivity.toActivity(this, mCityId);
                } else {
                    CityListActivity.toActivity(this);
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
                mCityName = city.substring(0, city.length() - 1);
                mCompositeSubscription.add(
                        RxMethod.getWeatherForLoc(mCityName, errorVerify));
            } else {
                //显示错误信息
                if (NetworkUtil.isNetworkConnected(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),
                            aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "网络未连接", Toast.LENGTH_SHORT).show();
                }
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!isLaunched) {
            super.onBackPressed();
        }
        if ((System.currentTimeMillis() - currentTime) < 2000) {
            this.finish();
        } else {
            currentTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出！", Toast.LENGTH_SHORT).show();
        }
    }
}
