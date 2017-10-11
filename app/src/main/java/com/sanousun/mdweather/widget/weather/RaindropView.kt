package com.sanousun.mdweather.widget.weather

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Created by dashu on 2017/7/3.
 * 雨滴的view
 */

class RaindropView : View {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initData(context, attrs)
    }

    fun initData(context: Context, attrs: AttributeSet?) {

    }
}
