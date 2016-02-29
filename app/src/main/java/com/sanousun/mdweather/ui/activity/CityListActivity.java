package com.sanousun.mdweather.ui.activity;

import android.content.Context;
import android.content.Intent;
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

import com.amap.api.location.AMapLocationClient;
import com.sanousun.mdweather.R;
import com.sanousun.mdweather.app.MyApplication;
import com.sanousun.mdweather.model.CityList;
import com.sanousun.mdweather.model.SimpleWeather;
import com.sanousun.mdweather.rxmethod.CityListEvent;
import com.sanousun.mdweather.rxmethod.Event;
import com.sanousun.mdweather.rxmethod.RxMethod;
import com.sanousun.mdweather.rxmethod.WeatherForListEvent;
import com.sanousun.mdweather.support.util.DensityUtil;
import com.sanousun.mdweather.ui.adapter.CityListAdapter;
import com.sanousun.mdweather.ui.adapter.ItemSwipeHelperCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.Subscribe;

public class CityListActivity extends BaseActivity
        implements CityListAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String LOCAL_CITY = "local_city";

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



    private AMapLocationClient mLocationClient = null;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, CityListActivity.class);
        context.startActivity(intent);
    }

    public static void toActivity(Context context, String localCity) {
        Intent intent = new Intent(context, CityListActivity.class);
        intent.putExtra(LOCAL_CITY, localCity);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_citylist;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onRefresh();
    }

    @Override
    protected void initView() {
        mToolbar.setTitle(R.string.city_list);
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
        mCityIdList = new ArrayList<>();
        if (getIntent() != null) {
            mCityIdList.add(getIntent().getStringExtra(LOCAL_CITY));
        }
        mCityIdList.addAll(MyApplication.getDataSource().getCityIdList());
        mWeatherList = new ArrayList<>();
    }

    @Override
    protected void initEvent() {
        mRefreshLayout.setProgressViewOffset(false, 0, DensityUtil.dip2px(this, 48));
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
    public void onEventMainThread(WeatherForListEvent event) {
        if (event.getEventResult() == Event.FAIL) {
            Toast.makeText(this, "未知网络错误!", Toast.LENGTH_SHORT).show();
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
        if (event.getEventResult() == Event.FAIL) {
            Toast.makeText(this, "未知网络错误!", Toast.LENGTH_SHORT).show();
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
        String cityId = mCityIdList.remove(pos);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
