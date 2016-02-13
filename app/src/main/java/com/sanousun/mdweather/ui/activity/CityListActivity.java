package com.sanousun.mdweather.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sanousun.mdweather.R;
import com.sanousun.mdweather.app.MyApplication;
import com.sanousun.mdweather.model.CityList;
import com.sanousun.mdweather.model.SimpleWeather;
import com.sanousun.mdweather.rxmethod.CityListEvent;
import com.sanousun.mdweather.rxmethod.RxMethod;
import com.sanousun.mdweather.rxmethod.SimpleWeatherEvent;
import com.sanousun.mdweather.rxmethod.WeatherForListEvent;
import com.sanousun.mdweather.support.Constant;
import com.sanousun.mdweather.ui.adapter.CityListAdapter;
import com.sanousun.mdweather.ui.adapter.ItemSwipeHelperCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.Subscribe;

public class CityListActivity extends BaseActivity
        implements CityListAdapter.OnItemClickListener,
        ItemSwipeHelperCallBack.ItemSwipeHelperAdapter {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.list_rv_main)
    RecyclerView mRecyclerView;

    private List<String> mCityIdList;
    private List<SimpleWeather> mWeatherList;
    private CityListAdapter mAdapter;
    private SearchView mSearchView;

    private int position;

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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CityListAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new ItemSwipeHelperCallBack(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initData() {
        // TODO: 2016/2/1 从数据库中得到cityIdList用于网络请求
        mCityIdList = MyApplication.getDataSource().getCityIdList();
        mWeatherList = new ArrayList<>();
        position = 0;
        if (mCityIdList.size() == 0) {
            Toast.makeText(this, "请添加城市", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("xyz", "mCompositeSubscription.add() --> initData()");
            mCompositeSubscription.add(RxMethod.getWeatherForList(mCityIdList.get(position)));
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("xyz", "onResume():position --> " + position);
    }

    //---------------------------------Event Bus-----------------------------------
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
        position++;
        // TODO: 2016/2/1 下一个网络请求或者并发请求
        Log.i("xyz", "position --> " + position + "  mCityIdList.size() --> " + mCityIdList.size());
        if (position < mCityIdList.size()) {
            Log.i("xyz", "mCompositeSubscription.add() --> onEventMainThread(SimpleWeatherEvent)");
            mCompositeSubscription.add(RxMethod.getWeatherForList(mCityIdList.get(position)));
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
        mSearchView.clearFocus();
        String cityName = cityList.getRetData().get(0).getName_cn();
        String cityId = cityList.getRetData().get(0).getArea_id();
        if (cityId == null) return;
        Log.i("xyz", "cityID --> " + cityId + "  cityName --> " + cityName);
        // TODO: 2016/2/1 加入到数据库中
        MyApplication.getDataSource().insert(cityName, cityId);
        mCityIdList.add(cityId);
        // TODO: 2016/2/1 可以使用AlertDialog选择城市
        Log.i("xyz", "mCompositeSubscription.add() --> onEventMainThread(CityListEvent)");
        mCompositeSubscription.add(RxMethod.getWeatherForList(cityId));
    }
    //---------------------------------Event Bus-----------------------------------

    @Override
    public void itemClick(int pos) {
        MainActivity.startActivity(this,
                mRecyclerView.getChildAt(pos),
                mCityIdList.get(pos),
                mWeatherList.get(pos).getRetData().getCity(),
                mWeatherList.get(pos).getRetData().getWeather(),
                mWeatherList.get(pos).getRetData().isNight());
    }

    @Override
    public void onItemDismiss(int pos) {
        MyApplication.getDataSource().delete(mCityIdList.get(pos));
        mCityIdList.remove(pos);
        mWeatherList.remove(pos);
        mAdapter.remove(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_citylist, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (MyApplication.getDataSource().getCityId(query) != null) {
                        Toast.makeText(getApplicationContext(), "城市已存在列表中", Toast.LENGTH_SHORT).show();
                    } else {
                        mCompositeSubscription.add(RxMethod.getCityList(query));
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        // TODO: 2016/2/1 searchView未知错误，空指针考虑自定义
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
