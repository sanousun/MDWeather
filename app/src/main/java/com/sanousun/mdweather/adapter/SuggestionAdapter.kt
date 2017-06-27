package com.sanousun.mdweather.adapter

import android.content.Context
import android.view.ViewGroup
import com.sanousun.mdweather.R
import com.sanousun.mdweather.model.KV
import kotlinx.android.synthetic.main.item_suggestion.view.*

/**
 * Created by dashu on 2017/6/22.
 * 天气建议的适配器
 */

class SuggestionAdapter(context: Context) :
        BaseAdapter<KV, SuggestionAdapter.SuggestionHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        return SuggestionHolder(parent, R.layout.item_suggestion)
    }

    class SuggestionHolder(parent: ViewGroup, layoutId: Int) :
            BaseViewHolder<KV>(parent, layoutId) {

        override fun bind(t: KV) {
            itemView.tv_brf.text = t.brf
            itemView.tv_txt.text = t.txt
        }
    }
}
