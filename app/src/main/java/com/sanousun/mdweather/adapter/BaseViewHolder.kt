package com.sanousun.mdweather.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by dashu on 2017/6/22.
 * ViewHolder基类
 */
abstract class BaseViewHolder<in T>(parent: ViewGroup, @LayoutRes layoutId: Int) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    abstract fun bind(t: T)

    val context: Context
        get() = itemView.context
}
