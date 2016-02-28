package com.sanousun.mdweather.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sanousun.mdweather.R;
import com.sanousun.mdweather.support.util.DensityUtil;

public class SunRiseToSetView extends View {

    private static final int ICON_SUNRISE = R.drawable.icon_sunset;
    private static final int ICON_SUNSET = R.drawable.icon_moonset;

    private Clock sunrise;
    private Clock sunset;
    private Clock now;

    private float viewLeft;
    private float curveLeft;
    private float curveTop;
    private float curveRight;
    private float curveBottom;

    private float iconWidth;
    private float curveRadio;
    private float clockRadio;

    private Path curvePath;
    private Paint curvePaint;
    private Path postPath;
    private Paint postPaint;
    private Paint textPaint;
    private Path clockPath;

    public SunRiseToSetView(Context context) {
        this(context, null);
    }

    public SunRiseToSetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunRiseToSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        sunrise = new Clock(6, 0);
        sunset = new Clock(18, 0);
        now = new Clock(0, 0);
        iconWidth = DensityUtil.dip2px(getContext(), 48f);
        clockRadio = DensityUtil.dip2px(getContext(), 16f);

        curvePath = new Path();
        curvePaint = new Paint();
        curvePaint.setStyle(Paint.Style.STROKE);
        curvePaint.setStrokeWidth(3);
        curvePaint.setColor(Color.WHITE);
        curvePaint.setAntiAlias(true);
        curvePaint.setAlpha(150);

        postPath = new Path();
        postPaint = new Paint();
        postPaint.setColor(Color.WHITE);
        postPaint.setAlpha(50);

        clockPath = new Path();

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
    }

    public void setNowClock(Clock sunrise, Clock sunset, Clock now) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.now = now;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int)
                    (((width - getPaddingLeft() - getPaddingRight()) / 2
                            - (clockRadio + iconWidth))
                            + getPaddingBottom() + getPaddingTop() + clockRadio);
        }
        viewLeft = getPaddingLeft();
        curveRadio = Math.min(((width - getPaddingLeft() - getPaddingRight()) / 2
                        - clockRadio - iconWidth),
                height - getPaddingTop() - getPaddingBottom() - clockRadio);
        curveLeft = viewLeft + clockRadio + iconWidth;
        curveTop = getPaddingTop() + clockRadio;
        curveRight = curveLeft + 2 * curveRadio;
        curveBottom = curveTop + curveRadio;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCurve(canvas);
        drawIcon(canvas);
        drawNow(canvas);
        drawText(canvas);
    }

    private void drawCurve(Canvas canvas) {
        RectF curveRectF = new RectF(curveLeft, curveTop,
                curveRight, curveBottom + curveRadio);
        curvePath.reset();
        curvePath.moveTo(curveLeft, curveBottom);
        curvePath.arcTo(curveRectF, 180, 180);
        curvePath.lineTo(curveLeft, curveBottom);
        canvas.drawPath(curvePath, curvePaint);
        postPath.reset();
        float arc = getAngle();
        postPath.moveTo(curveLeft, curveBottom);
        postPath.arcTo(curveRectF, 180, arc);
        postPath.lineTo((float)
                (curveLeft + curveRadio * (1 - Math.cos(arc * Math.PI / 180))), curveBottom);
        postPath.lineTo(curveLeft, curveBottom);
        postPath.close();
        canvas.drawPath(postPath, postPaint);
    }

    private void drawIcon(Canvas canvas) {
        Bitmap sun = BitmapFactory.decodeResource(getResources(), ICON_SUNRISE);
        RectF sunRectF = new RectF(
                viewLeft, curveBottom - iconWidth, viewLeft + iconWidth, curveBottom);
        canvas.drawBitmap(sun, null, sunRectF, null);
        Bitmap moon = BitmapFactory.decodeResource(getResources(), ICON_SUNSET);
        RectF moonRectF = new RectF(curveRight + clockRadio,
                curveBottom - iconWidth, curveRight + clockRadio + iconWidth, curveBottom);
        canvas.drawBitmap(moon, null, moonRectF, null);
    }

    private void drawNow(Canvas canvas) {
        clockPath.reset();
        float arc = getAngle();
        if (arc == 0 || arc == 180) return;
        float x = (float) (curveLeft + curveRadio * (1 - Math.cos(arc * Math.PI / 180)));
        float y = (float) (curveBottom - curveRadio * Math.sin(arc * Math.PI / 180));
        clockPath.addCircle(x, y, clockRadio, Path.Direction.CW);
        canvas.drawPath(clockPath, postPaint);
        textPaint.setTextSize(DensityUtil.sp2px(getContext(), 16f));
        Rect textRect = new Rect();
        textPaint.getTextBounds(
                now.getH(), 0, (now.getH()).length(), textRect);
        canvas.drawText(now.getH(),
                x - textRect.width() / 2, y + textRect.height() / 4, textPaint);
    }

    private void drawText(Canvas canvas) {
        textPaint.setTextSize(DensityUtil.sp2px(getContext(), 16f));
        Rect tRect = new Rect();
        textPaint.getTextBounds(sunrise.toString(), 0, sunrise.toString().length(), tRect);
        canvas.drawText(sunrise.toString(),
                viewLeft + iconWidth / 2 - tRect.width() / 2, curveBottom - iconWidth, textPaint);
        canvas.drawText(sunset.toString(),
                curveRight + clockRadio + iconWidth / 2 - tRect.width() / 2,
                curveBottom - iconWidth, textPaint);
    }


    private float getAngle() {
        int dayHour = sunset.getHour() - sunrise.getHour();
        int dayMinute = sunset.getMinute() - sunrise.getMinute();
        if (dayMinute < 0) {
            dayHour--;
            dayMinute += 60;
        }
        if (now.getHour() < sunrise.getHour() ||
                (now.getHour() == sunrise.getHour() && now.getMinute() < sunrise.getMinute())) {
            return 0;
        }
        if (now.getHour() > sunset.getHour() ||
                (now.getHour() == sunset.getHour() && now.getMinute() > sunset.getMinute())) {
            return 180;
        }
        int postHour = now.getHour() - sunrise.getHour();
        int postMinute = now.getMinute() - sunrise.getMinute();
        if (postMinute < 0) {
            postHour--;
            postMinute += 60;
        }
        return 180 * (postMinute / 60.0f + postHour) / (dayMinute / 60.0f + dayHour);
    }


    public static class Clock {
        private int hour;
        private int minute;

        public Clock(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public String getH() {
            return (hour < 10 ? "0" + hour : "" + hour);
        }

        public String getM() {
            return (minute < 10 ? "0" + minute : "" + minute);
        }


        @Override
        public String toString() {
            return (getH() + ":" + getM());
        }
    }
}
