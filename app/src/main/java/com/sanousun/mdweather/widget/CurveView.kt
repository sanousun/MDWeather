package com.sanousun.mdweather.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.sanousun.mdweather.model.Temperature
import com.sanousun.mdweather.support.util.drawTextWithCenter

/**
 * Created by dashu on 2017/6/23.
 * 展示最高温度最低温度趋势折线的视图
 */

class CurveView : android.view.View {

    /** 表示温度的圆点半径*/
    var pointRadio = 0
    /** 圆点描述文字的大小*/
    var descTextSize = 0
    /** 圆点描述文字的画笔*/
    val descTextPaint: android.text.TextPaint = android.text.TextPaint()
    /** 折线的画笔*/
    val curvePaint: android.graphics.Paint = android.graphics.Paint()
    /** 折线的路径*/
    val curvePath: android.graphics.Path = android.graphics.Path()

    /** 当前温度*/
    var tempNow: Temperature? = null
    /** 前一天的温度，用于展示左折线*/
    var tempPre: Temperature? = null
    /** 后一天的温度，用于展示右折线*/
    var tempNext: Temperature? = null
    /** 温度范围*/
    var tempRange: Temperature? = null

    /** 上顶点位置*/
    var top = 0F
    /** 下顶点位置*/
    var bottom = 0F
    /** 左上顶点位置*/
    var startTop = 0F
    /** 左下顶点位置*/
    var startBottom = 0F
    /** 右上顶点位置*/
    var endTop = 0F
    /** 右下顶点位置*/
    var endBottom = 0F

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initData(attrs)
    }

    fun initData(attrs: AttributeSet?) {
        descTextPaint.textAlign = android.graphics.Paint.Align.CENTER
    }

    fun setTemp(now: Temperature, pre: Temperature?, next: Temperature?, range: Temperature) {
        tempNow = now
        tempPre = pre
        tempNext = next
        tempRange = range
        calcCoordinates()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        drawCurve(canvas)
        drawText(canvas)
    }

    /**
     * 画出折线图
     */
    fun drawCurve(canvas: Canvas?) {
        val midX = width / 2
        curvePath.reset()
        //画出范围曲线
        curvePaint.alpha = 0xFF
        curvePath.moveTo(paddingLeft.toFloat(), startTop)
        curvePath.lineTo(midX.toFloat(), top)
        curvePath.lineTo((width - paddingRight).toFloat(), endTop)
        curvePath.moveTo((width - paddingRight).toFloat(), endBottom)
        curvePath.lineTo(midX.toFloat(), bottom)
        curvePath.lineTo(paddingLeft.toFloat(), startBottom)
        canvas?.drawPath(curvePath, curvePaint)
        //画出范围内蒙层
        curvePath.close()
        curvePaint.alpha = 0x10
        canvas?.drawPath(curvePath, curvePaint)
        //画出温度点
        canvas?.drawCircle(midX.toFloat(), top, pointRadio.toFloat(), curvePaint)
        canvas?.drawCircle(midX.toFloat(), bottom, pointRadio.toFloat(), curvePaint)
    }

    /**
     * 画出点的描述文字
     */
    fun drawText(canvas: Canvas?) {
        val tRect = android.graphics.Rect()
        val min = (tempNow?.min ?: 0).toString()
        val max = (tempNow?.max ?: 0).toString()
        val midWidth = measuredWidth / 2
        descTextPaint.getTextBounds(min, 0, min.length, tRect)
        canvas?.drawTextWithCenter(min, midWidth.toFloat(), top - descTextSize, descTextPaint)
        descTextPaint.getTextBounds(max, 0, max.length, tRect)
        canvas?.drawTextWithCenter(max, midWidth.toFloat(), bottom + descTextSize, descTextPaint)
    }

    /**
     * 根据temp数据计算坐标位置
     */
    fun calcCoordinates() {
        val unit = (measuredHeight - paddingTop - paddingBottom).toFloat() / (tempRange?.getRange() ?: 1)
        val min = tempNow?.min ?: 0
        val max = tempNow?.max ?: 0
        val padding = descTextSize * 2
        //计算各种坐标轴
        top = (min - (tempRange?.min ?: 0)) * unit + padding
        bottom = (min - (tempRange?.min ?: 0)) * unit + padding
        startTop = (tempPre?.min ?: min - (tempRange?.min ?: 0)) * unit + padding
        startBottom = (tempPre?.max ?: max - (tempRange?.min ?: 0)) * unit + padding
        endTop = (tempNext?.min ?: min - (tempRange?.min ?: 0)) * unit + padding
        endBottom = (tempNext?.max ?: max - (tempRange?.min ?: 0)) * unit + padding
    }
}