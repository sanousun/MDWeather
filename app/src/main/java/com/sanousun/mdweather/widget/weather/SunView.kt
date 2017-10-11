package com.sanousun.mdweather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sanousun.mdweather.R
import com.sanousun.mdweather.support.util.dpToPx

/**
 * Created by dashu on 2017/7/3.
 * 太阳的view
 */

class SunView : View {

    val paint = Paint()

    var midX = 0F
    var midY = 0F
    var radio = 0F

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initData(context, attrs)
    }

    fun initData(context: Context, attrs: AttributeSet?) {
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = context.dpToPx(4).toFloat()
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SunView)
        paint.color = typedArray.getColor(R.styleable.SunView_sv_color, Color.parseColor("#FFD754"))
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val startLeft = paddingLeft.toFloat()
        val startRight = w - paddingRight.toFloat()
        val startTop = paddingTop.toFloat()
        val startBottom = h - paddingBottom.toFloat()
        midX = (startRight + startLeft) / 2
        midY = (startBottom + startTop) / 2
        //半径是view宽的3/10
        radio = (startRight - startLeft) * 3 / 10
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate(midX, midY)
        //画本体圆
        canvas?.drawCircle(0F, 0F, radio, paint)
        //画周围的细线
        val from = radio / 6 * 7
        val to = radio / 6 * 9
        for (arc in 0..360 step 30) {
            val sin = Math.sin(arc / 180.0 * Math.PI).toFloat()
            val cos = Math.cos(arc / 180.0 * Math.PI).toFloat()
            canvas?.drawLine(cos * from, sin * from, cos * to, sin * to, paint)
        }
    }
}