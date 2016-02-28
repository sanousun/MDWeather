package com.sanousun.mdweather.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.sanousun.mdweather.R;
import com.sanousun.mdweather.app.MyApplication;
import com.sanousun.mdweather.model.CityList;
import com.sanousun.mdweather.model.SimpleWeather;
import com.sanousun.mdweather.rxmethod.CityListEvent;
import com.sanousun.mdweather.rxmethod.RxMethod;
import com.sanousun.mdweather.rxmethod.WeatherForListEvent;
import com.sanousun.mdweather.rxmethod.WeatherForLocEvent;
import com.sanousun.mdweather.support.Constant;
import com.sanousun.mdweather.ui.adapter.CityListAdapter;
import com.sanousun.mdweather.ui.adapter.ItemSwipeHelperCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.Subscribe;

public class CityListActivity extends BaseActivity
        implements CityListAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        AMapLocationListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.list_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.list_rv)
    RecyclerView mRecyclerView;

    private List<String> mCityIdList;
    private List<SimpleWeather> mWeatherList;
    private CityListAdapter mAdapter;
    private SearchView mSearchView;
    private MenuItem mSearchItem;

    //用于判断两次返回键的间隔时间
    private long currentTime = 0;

    private AMapLocationClient mLocationClient = null;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_citylist;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CityListAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new ItemSwipeHelperCallBack(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void initData() {

        mCityIdList = MyApplication.getDataSource().getCityIdList();
        mWeatherList = new ArrayList<>();
        //初始化定位服务
        mLocationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(this);
        mLocationClient.startLocation();
    }

    @Override
    protected void initEvent() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    //-----------------------------------------------------------------------------
    //-------------------SwipeRefreshLayout.OnRefreshListener----------------------
    //-----------------------------------------------------------------------------
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mWeatherList = new ArrayList<>();
        mAdapter.removeAll();
        mCompositeSubscription.add(
                RxMethod.getWeatherForList(
                        mCityIdList.get(mWeatherList.size())));
    }
    //-----------------------------------------------------------------------------
    //--------------------SwipeRefreshLayout.OnRefreshListener---------------------
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    //---------------------------------Event Bus-----------------------------------
    //-----------------------------------------------------------------------------

    @Subscribe
    public void onEventMainThread(WeatherForLocEvent event) {
        if (event.getEventResult() == Constant.Result.FAIL) {
            Toast.makeText(this, "网络错误!", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleWeather simpleWeather = event.getSimpleWeather();
        if (simpleWeather.getErrNum() != 0) {
            Toast.makeText(this, simpleWeather.getErrMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        mCityIdList.add(mWeatherList.size(),
                simpleWeather.getRetData().getCitycode());
        mWeatherList.add(simpleWeather);
        mAdapter.add(simpleWeather);
        //请求收藏的地名
        if (getNextCity() == null) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mCompositeSubscription.add(RxMethod.getWeatherForList(getNextCity()));
        }
    }


    @Subscribe
    public void onEventMainThread(WeatherForListEvent event) {
        if (event.getEventResult() == Constant.Result.FAIL) {
            Toast.makeText(this, "wrong network!", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleWeather simpleWeather = event.getSimpleWeather();
        if (simpleWeather.getErrNum() != 0) {
            Toast.makeText(this, simpleWeather.getErrMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        mWeatherList.add(simpleWeather);
        mAdapter.add(simpleWeather);
        if (getNextCity() == null) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mCompositeSubscription.add(RxMethod.getWeatherForList(getNextCity()));
        }
    }

    @Subscribe
    public void onEventMainThread(CityListEvent event) {
        if (event.getEventResult() == Constant.Result.FAIL) {
            Toast.makeText(this, "wrong network!", Toast.LENGTH_SHORT).show();
            return;
        }
        CityList cityList = event.getCityList();
        if (cityList.getErrNum() != 0) {
            Toast.makeText(this, cityList.getErrMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
        MenuItemCompat.collapseActionView(mSearchItem);

        String cityName = cityList.getRetData().get(0).getName_cn();
        String cityId = cityList.getRetData().get(0).getArea_id();
        if (cityId == null) return;
        MyApplication.getDataSource().insert(cityName, cityId);
        mCityIdList.add(cityId);
        mCompositeSubscription.add(RxMethod.getWeatherForList(cityId));
    }

    /**
     * 获取下一个city的id,localCityId(mCityIdList.get(0))比较，跳过已经作为自动定位出现的城市
     */
    private String getNextCity() {
        if (mWeatherList.size() >= mCityIdList.size())
            return null;
        int pos = mWeatherList.size();
        if (mCityIdList.get(pos).equals(mCityIdList.get(0)))
            return getNextCity();
        return mCityIdList.get(pos);
    }

    //-----------------------------------------------------------------------------
    //---------------------------------Event Bus-----------------------------------
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    //-------------------CityListAdapter.OnItemClickListener-----------------------
    //-----------------------------------------------------------------------------

    @Override
    public void itemClick(int pos) {
        SimpleWeather.RetDataEntity entity = mWeatherList.get(pos).getRetData();
        MainActivity.startActivity(this, pos == 0,
                entity.getCitycode(), entity.getCity(),
                entity.getWeather(), entity.isNight());
    }

    @Override
    public void itemRemove(int pos) {
        String cityId = mCityIdList.remove(pos - 1);
        MyApplication.getDataSource().delete(cityId);
        mWeatherList.remove(pos);
    }

    //-----------------------------------------------------------------------------
    //-------------------CityListAdapter.OnItemClickListener-----------------------
    //-----------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_citylist, menu);
        mSearchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        mSearchView.setQueryHint("请输入所要查找的城市...");
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (MyApplication.getDataSource().getCityId(query) != null) {
                        Toast.makeText(getApplicationContext(),
                                "城市已存在列表中", Toast.LENGTH_SHORT).show();
                        mSearchView.setQuery(null, false);
                    } else {
                        mCompositeSubscription.add(RxMethod.getCityList(query));
                        mRefreshLayout.setRefreshing(true);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - currentTime) < 500) {
            this.finish();
        } else {
            currentTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出！", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }

    /**
     * 高德地图定位服务的api回调接口
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String city = aMapLocation.getCity();
                String cityName = city.substring(0, city.length() - 1);
                mCompositeSubscription.add(RxMethod.getWeatherForLoc(cityName));
            } else {
                //显示错误信息
                Toast.makeText(getApplicationContext(),
                        aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
