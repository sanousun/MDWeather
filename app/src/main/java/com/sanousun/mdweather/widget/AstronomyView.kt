package com.sanousun.mdweather.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.sanousun.mdweather.R
import com.sanousun.mdweather.model.Astronomy
import com.sanousun.mdweather.support.util.DensityUtil
import com.sanousun.mdweather.support.util.dpToPx
import com.sanousun.mdweather.support.util.getScreenWidth

/**
 * Created by dashu on 2017/6/17.
 * 天文数值展示自定义view
 */

class AstronomyView : View {

    private val ICON_SUNRISE = R.drawable.icon_sunset
    private val ICON_SUNSET = R.drawable.icon_moonset

    /** 线段的画笔 */
    val curvePaint: Paint = Paint()
    /** 线段的路径*/
    val curvePath: Path = Path()
    /** 文字的画笔 */
    val textPaint: TextPaint = TextPaint()

    /** 绘制中间时间盘的半径*/
    var curveRadio = 0
    /** 绘制时间盘上的时钟半径*/
    var clockRadio = 0
    /** 绘制icon的宽度*/
    var iconWidth = 0

    /** 天文数值信息*/
    var astronomy: Astronomy? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initData(attrs)
    }

    fun initData(attrs: AttributeSet?) {
        val a: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.AstronomyView)
        clockRadio = a.getDimensionPixelSize(
                R.styleable.AstronomyView_asv_clock_radio, context.dpToPx(10))
        iconWidth = a.getDimensionPixelSize(
                R.styleable.AstronomyView_asv_icon_radio, context.dpToPx(10))
        a.recycle()

        curvePaint.style = Paint.Style.STROKE
        curvePaint.strokeWidth = 3f
        curvePaint.isAntiAlias = true

        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = context.getScreenWidth()
        }
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode != MeasureSpec.EXACTLY) {
            curveRadio = heightSize - paddingTop - paddingBottom - clockRadio
            widthSize = curveRadio * 2 + iconWidth * 2 + paddingLeft + paddingRight
        } else {
            val verticalRadio = heightSize - paddingTop - paddingBottom - clockRadio
            val horizontalRadio = (widthSize - paddingLeft - paddingRight - iconWidth * 2) / 2
            curveRadio = minOf(verticalRadio, horizontalRadio)
        }
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCurve(canvas)
        drawIcon(canvas)
        drawNow(canvas)
        drawText(canvas)
    }

    /**
     * 绘制中间的表盘
     */
    fun drawCurve(canvas: Canvas?) {
        val curveLeft = (width - curveRadio).toFloat()
        val curveRight = (width + curveRadio).toFloat()
        val curveTop = (height - curveRadio).toFloat()
        val curveBottom = (height + curveRadio).toFloat()
        val curveRectF = RectF(curveLeft, curveTop, curveRight, curveBottom)
        curvePath.reset()
        curvePath.moveTo(curveLeft, curveBottom)
        curvePath.arcTo(curveRectF, 180F, 180F)
        curvePath.close()
        curvePaint.color = Color.WHITE
        curvePaint.alpha = 0xFF
        canvas?.drawPath(curvePath, curvePaint)
        curvePath.reset()
        val arc = getAngle()
        curvePath.moveTo(curveLeft, curveBottom)
        curvePath.arcTo(curveRectF, 180F, arc)
        curvePath.lineTo((curveLeft + curveRadio * (1 - Math.cos(arc * Math.PI / 180))).toFloat(), curveBottom)
        curvePath.close()
        curvePaint.alpha = 0x90
        canvas?.drawPath(curvePath, curvePaint)
    }

    /**
     * 绘制日出日落的icon
     */
    fun drawIcon(canvas: Canvas?) {
        val sun = BitmapFactory.decodeResource(resources, ICON_SUNRISE)
        val iconTop = paddingBottom.toFloat() - iconWidth
        val iconBottom = paddingBottom.toFloat()
        val sunRectF = RectF(
                paddingLeft.toFloat(), iconTop,
                paddingLeft.toFloat() + iconWidth, iconBottom)
        canvas?.drawBitmap(sun, null, sunRectF, null)
        val moon = BitmapFactory.decodeResource(resources, ICON_SUNSET)
        val moonRectF = RectF(
                (width - paddingRight).toFloat() - iconWidth, iconTop,
                (width - paddingRight).toFloat(), iconBottom)
        canvas?.drawBitmap(moon, null, moonRectF, null)
    }

    /**
     * 绘制当前的表盘
     */
    private fun drawNow(canvas: Canvas?) {
        curvePath.reset()
        curvePaint.alpha = 0x20
        curvePaint.color = Color.WHITE
        val curveLeft = (width - curveRadio).toFloat()
        val curveBottom = (height + curveRadio).toFloat()
        val arc = getAngle()
        if (arc == 0f || arc == 180f) return
        val x = (curveLeft + curveRadio * (1 - Math.cos(arc * Math.PI / 180))).toFloat()
        val y = (curveBottom - curveRadio * Math.sin(arc * Math.PI / 180)).toFloat()
        curvePath.addCircle(x, y, clockRadio.toFloat(), Path.Direction.CW)
        canvas?.drawPath(curvePath, curvePaint)
        textPaint.textSize = DensityUtil.sp2px(context, 16f).toFloat()
        val textRect = Rect()
        val currentHour = astronomy?.getCurrentHour() ?: "00"
        textPaint.getTextBounds(currentHour, 0, currentHour.length, textRect)
        canvas?.drawText(currentHour, x - textRect.width() / 2, y + textRect.height() / 4, textPaint)
    }

    /**
     * 绘制日出日落时间字符串
     */
    private fun drawText(canvas: Canvas?) {
        textPaint.textSize = context.dpToPx(16).toFloat()
        val tRect = Rect()
        val sr = astronomy?.sr ?: "00:00"
        val ss = astronomy?.ss ?: "18:00"
        textPaint.getTextBounds(sr, 0, sr.length, tRect)
        canvas?.drawText(sr,
                paddingLeft.toFloat() + iconWidth / 2 - tRect.width() / 2,
                paddingBottom.toFloat() - iconWidth,
                textPaint)
        canvas?.drawText(ss,
                paddingLeft.toFloat() + clockRadio + iconWidth / 2 - tRect.width() / 2,
                paddingBottom.toFloat() - iconWidth,
                textPaint)
    }

    private fun getAngle(): Float {
        var rate = astronomy?.getCurrentRateForSun() ?: 0F
        if (rate <= 0 || rate >= 180) {
            rate = 0F
        }
        return 180 * rate
    }
}