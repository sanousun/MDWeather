package com.sanousun.mdweather.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sanousun.mdweather.R;
import com.sanousun.mdweather.app.MyApplication;
import com.sanousun.mdweather.app.WeatherApiUtil;
import com.sanousun.mdweather.model.CityBean;
import com.sanousun.mdweather.model.SimpleWeatherBean;
import com.sanousun.mdweather.rxmethod.SimpleErrorVerify;
import com.sanousun.mdweather.support.util.DensityUtil;
import com.sanousun.mdweather.ui.adapter.CityListAdapter;
import com.sanousun.mdweather.ui.adapter.ItemSwipeHelperCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscription;

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
    private List<SimpleWeatherBean> mWeatherList;
    private CityListAdapter mAdapter;
    private SearchView mSearchView;

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
            String id = getIntent().getStringExtra(LOCAL_CITY);
            if (!TextUtils.isEmpty(id)) {
                mCityIdList.add(id);
            }
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
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mWeatherList.clear();
        mAdapter.removeAll();
        loadData();
    }

    private void loadData() {
        String id = getNextCity();
        if (TextUtils.isEmpty(id)) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mRefreshLayout.setRefreshing(true);
            Subscription subscription = WeatherApiUtil.getWeatherApi()
                    .getSimpleWeather(id)
                    .compose(WeatherApiUtil.composeFilter(new SimpleErrorVerify(this, mRefreshLayout)))
                    .subscribe(simpleWeatherBean -> {
                        mWeatherList.add(simpleWeatherBean);
                        mAdapter.add(simpleWeatherBean);
                        loadData();
                    });
            mCompositeSubscription.add(subscription);
        }
    }

    /**
     * 获取下一个city的id,localCityId(mCityIdList.get(0))比较，跳过已经作为自动定位出现的城市
     */
    private String getNextCity() {
        int pos = mWeatherList.size();
        if (pos >= mCityIdList.size())
            return null;
        if (pos != 0 && (mCityIdList.get(0)).equals(mCityIdList.get(pos))) {
            mCityIdList.remove(pos);
            return getNextCity();
        }
        return mCityIdList.get(pos);
    }

    @Override
    public void itemClick(int pos) {
        SimpleWeatherBean simpleWeather = mWeatherList.get(pos);
        MainActivity.startActivity(this, pos == 0, simpleWeather);
    }

    @Override
    public void itemRemove(int pos) {
        String cityId = mCityIdList.remove(pos);
        MyApplication.getDataSource().delete(cityId);
        mWeatherList.remove(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_citylist, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint("请输入所要查找的城市...");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCity(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchCity(String query) {
        if (MyApplication.getDataSource().getCityId(query) != null) {
            Toast.makeText(CityListActivity.this, "城市已存在列表中", Toast.LENGTH_SHORT).show();
            mSearchView.setQuery(null, false);
        } else {
            Subscription subscription = WeatherApiUtil.getWeatherApi()
                    .getCityList(query)
                    .compose(WeatherApiUtil.composeFilter(
                            new SimpleErrorVerify(CityListActivity.this, mRefreshLayout)))
                    .subscribe(list -> {
                        if (list == null || list.size() == 0) {
                            Toast.makeText(CityListActivity.this, "未查找到城市", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] items = new String[list.size()];
                            for (int i = 0; i < list.size(); i++) {
                                CityBean city = list.get(i);
                                items[i] = city.province_cn + "-" + city.name_cn;
                            }
                            AlertDialog dialog = new AlertDialog.Builder(CityListActivity.this)
                                    .setTitle("请选择所在城市：")
                                    .setItems(items, (dialog1, which) -> {
                                        CityBean cityBean = list.get(which);
                                        if (MyApplication.getDataSource().getCityId(cityBean.area_id) != null) {
                                            Toast.makeText(getApplicationContext(), "城市已存在列表中", Toast.LENGTH_SHORT).show();
                                            mSearchView.setQuery(null, false);
                                        } else {
                                            MyApplication.getDataSource().insert(cityBean.name_cn, cityBean.area_id);
                                            mCityIdList.add(cityBean.area_id);
                                            mSearchView.setQuery(null, false);
                                            loadData();
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }
                    });
            mCompositeSubscription.add(subscription);
            mRefreshLayout.setRefreshing(true);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
