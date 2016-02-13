package com.sanousun.mdweather.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.sanousun.mdweather.R;
import com.sanousun.mdweather.support.util.DensityUtil;

/**
 * 七天天气的显示view
 */
public class SevenDaysWeatherView extends View {

    public static final String[] WEEKDAYS =
            {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static final int WEATHER_COUNT = 7;

    private WeatherHolder[] mWeather;

    private Context mContext;

    private Path mHTempLine;
    private Path mLTempLine;
    private Paint mTempLinePaint;

    private Path mTempAreaFill;
    private Paint mTempAreaFillPaint;
    //分割线
    private Paint mCuttingLinePaint;

    private Paint mPointPaint;
    private Paint mTextPaint;

    private int mViewTop;
    private int mViewBottom;
    private int mViewLeft;
    private int mViewRight;
    private int mWeatherItemWidth;
    //每摄氏度的高度，用于确定温度点在表中的高度
    private int mTempHeight;
    private int mLowTemp;

    private int mDayY;
    private int mWeekdayY;
    private int mIconY;
    private int mTempAreaHeight;

    public SevenDaysWeatherView(Context context) {
        this(context, null);
    }

    public SevenDaysWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SevenDaysWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initDefaultData();
        initView();
    }

    private void initDefaultData() {
        mWeather = new WeatherHolder[7];
        for (int i = 0; i < 7; i++) {
            WeatherHolder w = new WeatherHolder();
            w.setDay("1/" + (i + 1));
            w.setWeek(WEEKDAYS[i]);
            w.setType("晴");
            w.setWeatherIcon(R.drawable.weather_fine);
            w.setMaxTemp(22);
            w.setMinTemp(18);
            mWeather[i] = w;
        }
    }

    /**
     * View的自定义初始化工作
     */
    private void initView() {

        mHTempLine = new Path();
        mLTempLine = new Path();
        mTempLinePaint = new Paint();
        mTempLinePaint.setStyle(Paint.Style.STROKE);
        mTempLinePaint.setColor(Color.WHITE);
        mTempLinePaint.setStrokeWidth(2f);
        mTempLinePaint.setAlpha(120);
        mTempLinePaint.setAntiAlias(true);

        mTempAreaFill = new Path();
        mTempAreaFillPaint = new Paint();
        mTempAreaFillPaint.setColor(Color.WHITE);
        mTempAreaFillPaint.setAlpha(40);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);

        mPointPaint = new Paint();
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setAntiAlias(true);

        mCuttingLinePaint = new Paint();
        mCuttingLinePaint.setStyle(Paint.Style.STROKE);
        mCuttingLinePaint.setStrokeWidth(1f);
        mCuttingLinePaint.setColor(Color.WHITE);
        mCuttingLinePaint.setAlpha(80);
    }

    public void setWeatherData(WeatherHolder[] weather) {
        this.mWeather = weather;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mWeatherItemWidth = (width - getPaddingLeft() - getPaddingRight()) / WEATHER_COUNT;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mWeatherItemWidth * 5;
        }
        setMeasuredDimension(width, height);
        mViewTop = getPaddingTop();
        mViewBottom = height - getPaddingBottom();
        mViewLeft = getPaddingLeft();
        mViewRight = width - getPaddingRight();
        mDayY = mViewTop + mWeatherItemWidth / 2;
        mWeekdayY = mDayY + mWeatherItemWidth * 2 / 5;
        mIconY = mWeekdayY + mWeatherItemWidth;
        mTempAreaHeight = mWeatherItemWidth * 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCuttingLine(canvas);
        drawTextAndIcon(canvas);
        drawTempLine(canvas);
        drawTempPoint(canvas);
    }

    private void drawCuttingLine(Canvas canvas) {
        for (int i = 0; i < WEATHER_COUNT - 1; i++) {
            int x = (i + 1) * mWeatherItemWidth;
            canvas.drawLine(x, mViewTop, x, mViewBottom, mCuttingLinePaint);
        }
    }

    private void drawTextAndIcon(Canvas canvas) {
        int x, y, start;
        Rect textRect = new Rect();
        for (int i = 0; i < mWeather.length; i++) {
            start = mWeatherItemWidth * i;

            String day = mWeather[i].getDay();
            mTextPaint.setTextSize(DensityUtil.sp2px(mContext, 15f));
            mTextPaint.getTextBounds(day, 0, day.length(), textRect);
            mTextPaint.setFakeBoldText(i == 2);
            x = start + (mWeatherItemWidth - textRect.width()) / 2;
            y = mDayY;
            canvas.drawText(day, x, y, mTextPaint);

            String week_day = mWeather[i].getWeek();
            mTextPaint.setTextSize(DensityUtil.sp2px(mContext, 16f));
            mTextPaint.getTextBounds(week_day, 0, week_day.length(), textRect);
            x = start + (mWeatherItemWidth - textRect.width()) / 2;
            y = mWeekdayY;
            canvas.drawText(week_day, x, y, mTextPaint);

            int iconWidth = mWeatherItemWidth - DensityUtil.dip2px(mContext, 12f);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), mWeather[i].getWeatherIcon());
            x = start + (mWeatherItemWidth - iconWidth) / 2;
            y = mIconY - iconWidth;
            Rect bm_rect = new Rect(x, y, x + iconWidth, y + iconWidth);
            canvas.drawBitmap(bm, null, bm_rect, null);
        }
    }

    private void drawTempLine(Canvas canvas) {
        mHTempLine.reset();
        mLTempLine.reset();
        mTempAreaFill.reset();
        int highTemp = -80, lowTemp = 80;
        for (WeatherHolder w : mWeather) {
            if (w.getMaxTemp() > highTemp) {
                highTemp = w.getMaxTemp();
            }
            if (w.getMinTemp() < lowTemp) {
                lowTemp = w.getMinTemp();
            }
        }
        mLowTemp = lowTemp;
        //每摄氏度的高度，用于确定温度点在表中的高度
        mTempHeight = (mTempAreaHeight - DensityUtil.dip2px(mContext, 16f)) /
                (highTemp - lowTemp + 3);
        mHTempLine.moveTo(mViewLeft, getYFromBottom(mWeather[0].getMaxTemp() - 1));
        mLTempLine.moveTo(mViewLeft, getYFromBottom(mWeather[0].getMinTemp() - 1));
        mTempAreaFill.moveTo(mViewLeft, getYFromBottom(mWeather[0].getMaxTemp() - 1));
        for (int i = 0; i < mWeather.length; i++) {
            int x = mViewRight / 14 * (2 * i + 1);
            mHTempLine.lineTo(x, getYFromBottom(mWeather[i].getMaxTemp()));
            mTempAreaFill.lineTo(x, getYFromBottom(mWeather[i].getMaxTemp()));
            mLTempLine.lineTo(x, getYFromBottom(mWeather[i].getMinTemp()));
        }
        mHTempLine.lineTo(mViewRight, getYFromBottom(mWeather[6].getMaxTemp() - 1));
        mTempAreaFill.lineTo(mViewRight, getYFromBottom(mWeather[6].getMaxTemp() - 1));
        mLTempLine.lineTo(mViewRight, getYFromBottom(mWeather[6].getMinTemp() - 1));
        canvas.drawPath(mHTempLine, mTempLinePaint);
        canvas.drawPath(mLTempLine, mTempLinePaint);
        mTempAreaFill.lineTo(mViewRight, getYFromBottom(mWeather[6].getMinTemp() - 1));
        for (int i = mWeather.length - 1; i >= 0; i--) {
            int x = mViewRight / 14 * (2 * i + 1);
            mTempAreaFill.lineTo(x, getYFromBottom(mWeather[i].getMinTemp()));
        }
        mTempAreaFill.lineTo(mViewLeft, getYFromBottom(mWeather[0].getMinTemp() - 1));
        mTempAreaFill.lineTo(mViewLeft, getYFromBottom(mWeather[0].getMaxTemp() - 1));
        mTempAreaFill.close();
        canvas.drawPath(mTempAreaFill, mTempAreaFillPaint);
    }

    private int getYFromBottom(int temp) {
        return mViewBottom - DensityUtil.dip2px(mContext, 16f)
                - (temp - mLowTemp + 2) * mTempHeight;
    }

    private void drawTempPoint(Canvas canvas) {
        mTextPaint.setTextSize(DensityUtil.sp2px(mContext, 12f));
        mTextPaint.setFakeBoldText(true);
        for (int i = 0; i < mWeather.length; i++) {
            int x = mViewRight / 14 * (2 * i + 1);
            int yh = getYFromBottom(mWeather[i].getMaxTemp());
            int yl = getYFromBottom(mWeather[i].getMinTemp());
            String text_h = String.format("%d°", mWeather[i].getMaxTemp());
            String text_l = String.format("%d°", mWeather[i].getMinTemp());
            canvas.drawCircle(x, yh, 8, mPointPaint);
            canvas.drawCircle(x, yl, 8, mPointPaint);
            Rect textRect = new Rect();
            mTextPaint.getTextBounds(text_h, 0, text_h.length(), textRect);
            int xt = x - textRect.width() / 2;
            int yt = yh - DensityUtil.dip2px(getContext(), 8f);
            canvas.drawText(text_h, xt, yt, mTextPaint);
            mTextPaint.getTextBounds(text_l, 0, text_l.length(), textRect);
            xt = x - textRect.width() / 2;
            yt = yl + textRect.height() + DensityUtil.dip2px(getContext(), 8f);
            canvas.drawText(text_l, xt, yt, mTextPaint);
        }
    }

    public static class WeatherHolder {
        private String day;
        private String week;
        private String type;
        private int weatherIcon;
        private int MaxTemp;
        private int MinTemp;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getMinTemp() {
            return MinTemp;
        }

        public void setMinTemp(int minTemp) {
            MinTemp = minTemp;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getMaxTemp() {
            return MaxTemp;
        }

        public void setMaxTemp(int maxTemp) {
            MaxTemp = maxTemp;
        }

        public int getWeatherIcon() {
            return weatherIcon;
        }

        public void setWeatherIcon(int weatherIcon) {
            this.weatherIcon = weatherIcon;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week_day) {
            this.week = week_day;
        }
    }
}
