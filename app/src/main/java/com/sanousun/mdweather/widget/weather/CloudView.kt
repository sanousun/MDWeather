package com.sanousun.mdweather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.sanousun.mdweather.R
import com.sanousun.mdweather.support.PathParser
import com.sanousun.mdweather.support.util.dpToPx

/**
 * Created by dashu on 2017/7/3.
 * 云的view
 */

class CloudView : View {

    var path = Path()
    val paint = Paint()

    var scale = 0F

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initData(context, attrs)
    }

    fun initData(context: Context, attrs: AttributeSet?) {
        paint.isAntiAlias = true
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CloudView)
        paint.color = typedArray.getColor(R.styleable.CloudView_cv_color, Color.parseColor("#000000"))
        val pathData = typedArray.getString(R.styleable.CloudView_cv_path)
        path = PathParser.createPathFromPathData(pathData)
        typedArray.recycle()
    }

    fun setPathData(pathData: String) {
        path = PathParser.createPathFromPathData(pathData)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var w = MeasureSpec.getSize(widthMeasureSpec)
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            w = context.dpToPx(100)
        }
        setMeasuredDimension(w, w / 2)
        scale = w / 100F
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.scale(scale, scale)
        canvas?.drawPath(path, paint)
    }
}