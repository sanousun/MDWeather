package com.sanousun.mdweather.adapter

import android.content.Context
import android.view.ViewGroup
import com.sanousun.mdweather.R
import com.sanousun.mdweather.model.BasicCity
import kotlinx.android.synthetic.main.item_location.view.*

/**
 * Created by dashu on 2017/6/22.
 * 已添加城市列表适配器
 */

class CityAdapter(context: Context) :
        BaseAdapter<BasicCity, CityAdapter.CityHolder>(context) {

    var itemClickListener: ((city: BasicCity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        return CityHolder(parent, R.layout.item_location)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(getItemData(position)) }
    }

    class CityHolder(parent: ViewGroup, layoutId: Int) :
            BaseViewHolder<BasicCity>(parent, layoutId) {
        override fun bind(t: BasicCity) {
            itemView.tv_city.text = t.city
            itemView.tv_location.text = t.getLocationName()
        }
    }
}
