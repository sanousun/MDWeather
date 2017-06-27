package com.sanousun.mdweather.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by dashu on 2017/6/22.
 * Adapter基类
 */
abstract class BaseAdapter<T, V : BaseViewHolder<T>>(val context: Context) : RecyclerView.Adapter<V>() {

    val mData: MutableList<T> = arrayListOf()

    fun clear() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun add(t: T) {
        mData.add(t)
        notifyDataSetChanged()
    }

    fun addAll(ts: List<T>) {
        mData.addAll(ts)
        notifyDataSetChanged()
    }

    fun update(ts: List<T>) {
        clear()
        addAll(ts)
    }

    fun update(t: T, pos: Int) {
        remove(pos)
        insert(t, pos)
    }

    fun insert(t: T, pos: Int) {
        if (pos < 0 || pos >= mData.size)
            return
        mData.add(pos, t)
        notifyItemInserted(pos)
    }

    fun remove(pos: Int) {
        if (pos < 0 || pos >= mData.size)
            return
        mData.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun getItemData(pos: Int): T {
        return mData[pos]
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V

    override fun onBindViewHolder(holder: V, position: Int) {
        holder.bind(mData[holder.layoutPosition])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}
