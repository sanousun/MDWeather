package com.sanousun.mdweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanousun.mdweather.R;
import com.sanousun.mdweather.model.SimpleWeather;
import com.sanousun.mdweather.support.util.WeatherIconUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CityListAdapter
        extends RecyclerView.Adapter<CityListAdapter.MyViewHolder>
        implements ItemSwipeHelperCallBack.ItemSwipeHelperAdapter {

    private Context mContext;
    private List<SimpleWeather> mWeatherList = new ArrayList<>();
    private OnItemClickListener mListener;

    public CityListAdapter(Context context) {
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void add(SimpleWeather data) {
        mWeatherList.add(data);
        notifyItemInserted(mWeatherList.size());
    }

    public void add(List<SimpleWeather> data) {
        for (int i = 0; i < data.size(); i++) {
            mWeatherList.add(data.get(i));
            notifyItemInserted(i);
        }
    }

    public void removeAll() {
        mWeatherList.clear();
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        mWeatherList.remove(pos);
        notifyItemRemoved(pos);
        mListener.itemRemove(pos);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_city_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.update();
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    @Override
    public void onItemDismiss(int pos) {
        if (pos == 0) return;
        this.remove(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @Bind(R.id.city_item_container)
        RelativeLayout mContainer;
        @Bind(R.id.city_item_tv_city)
        TextView mCityName;
        @Bind(R.id.city_item_iv_location)
        ImageView mLocationIcon;
        @Bind(R.id.city_item_tv_temp)
        TextView mWeatherTemp;
        @Bind(R.id.city_item_tv_type)
        TextView mWeatherType;

        SimpleWeather mWeather;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void update() {
            int pos = getLayoutPosition();
            mWeather = mWeatherList.get(pos);
            //根据天气，设置背景图片
            int resId = WeatherIconUtil.
                    getBackgroundResId(mContext,
                            mWeather.getRetData().getWeather(),
                            mWeather.getRetData().isNight());
            mContainer.setBackgroundResource(resId);
            //判断是否属于本地天气，是则将定位icon显示
            mLocationIcon.setVisibility(
                    pos == 0 ? View.VISIBLE : View.GONE);
            mCityName.setText(mWeather.getRetData().getCity());
            mWeatherTemp.setText(String.format("%s°", mWeather.getRetData().getTemp()));
            mWeatherType.setText(mWeather.getRetData().getWeather());
        }

        @Override
        public void onClick(View view) {
            if (mListener != null)
                mListener.itemClick(getLayoutPosition());
        }
    }

    public interface OnItemClickListener {
        void itemClick(int pos);

        void itemRemove(int pos);
    }
}
