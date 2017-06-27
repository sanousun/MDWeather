package com.sanousun.mdweather.adapter

import android.content.Context
import android.view.ViewGroup
import com.sanousun.mdweather.R
import com.sanousun.mdweather.model.Weather
import com.sanousun.mdweather.support.util.changeFormat
import com.sanousun.mdweather.support.util.getDayOfWeekString
import kotlinx.android.synthetic.main.item_daily_forecast.view.*

/**
 * Created by dashu on 2017/6/24.
 * 每日天气预报的列表
 */

class DailyAdapter(context: Context) :
        BaseAdapter<Weather, DailyAdapter.DailyHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHolder {
        return DailyHolder(parent, R.layout.item_daily_forecast)
    }

    class DailyHolder(parent: ViewGroup, layoutId: Int) :
            BaseViewHolder<Weather>(parent, layoutId) {

        override fun bind(t: Weather) {
            itemView.tv_date.text = t.date.changeFormat("yyyy-MM-dd", "MM/dd")
            itemView.tv_weekday.text = t.date.getDayOfWeekString("yyyy-MM-dd")
            itemView.iv_weather.text = t.getWeatherCondition()
            itemView.cv_temp.text = String.format("%d°-%d°", t.tmp.min, t.tmp.max)
        }
    }
}