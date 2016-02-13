package com.sanousun.mdweather.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import android.view.View;

import static android.graphics.Paint.*;
import static java.lang.Math.*;

/**
 * Created by sanousun on 2016/1/1.
 */
public class TemperatureCurveView extends View {

    private float smooth_value = 1.0f;
    private int[] temperatures;
    private String[] clocks;
    private Point[] points;
    private int view_width;
    private int view_height;
    private int n;

    public void setPoints(int[] temperatures, String[] clocks) {
        this.temperatures = temperatures;
        this.clocks = clocks;
    }

    //绘制温度趋势曲线
    private Paint curvePaint = new Paint();
    private Path curvePath = new Path();
    //绘制温度曲线下方填充
    private Paint fillPaint = new Paint();
    private Path fillPath = new Path();
    //绘制温度点
    private Paint pointPaint = new Paint();
    //绘制时间文字
    //绘制温度文字

    public TemperatureCurveView(Context context) {
        this(context, null);
    }

    public TemperatureCurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperatureCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //View创建的初始化工作
    private void init() {
        curvePaint.setStyle(Style.STROKE);
        curvePaint.setColor(Color.GRAY);
        curvePaint.setStrokeWidth(2f);
        curvePaint.setAntiAlias(true);
        curvePaint.setShadowLayer(30f, 0, 30, Color.BLACK);

        pointPaint.setStrokeJoin(Join.ROUND);
        pointPaint.setStrokeCap(Cap.ROUND);
        pointPaint.setStrokeWidth(10f);
        pointPaint.setColor(Color.WHITE);
        pointPaint.setAntiAlias(true);
        pointPaint.setShadowLayer(20f, 0, 0, Color.WHITE);

        fillPaint.setColor(Color.parseColor("#668000"));
        fillPaint.setAlpha(100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        adjustPoints();
        curvePath.reset();
        fillPath.reset();
        drawTemperatureCurve();
        fillPath.addPath(curvePath);
        fillPath.lineTo(view_width, view_height);
        fillPath.lineTo(0, view_height);
        fillPath.lineTo(0, points[0].getY());
        fillPath.close();
        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(curvePath, curvePaint);
        drawPoint(canvas);
    }

    //点集大于四个点
    public void adjustPoints() {

        view_width = getMeasuredWidth();
        view_height = getMeasuredHeight();

        n = temperatures.length;
        Point[] points = new Point[n];
        float width = view_width / (n * 2);

        int Max = -80, Min = 80;
        for (int tem : temperatures) {
            if (tem > Max) Max = tem;
            if (tem < Min) Min = tem;
        }
        float height = (view_height * 0.8f) /
                ((Max - Min) < 1 ? (2) : (Max - Min + 2));

        for (int i = 0; i < temperatures.length; i++) {
            Point point = new Point();
            point.setX(width * (2 * i + 1));
            point.setY(view_height * 0.9f - (temperatures[i] - Min + 1) * height);
            point.setClock(clocks[i]);
            point.setTemperature(temperatures[i]);
            points[i] = point;
        }

        this.points = points;
    }

    private void drawTemperatureCurve() {
        Point start = new Point();
        start.setX(0);
        start.setY(points[0].getY());
        Point end = new Point();
        end.setX(view_width);
        end.setY(points[points.length - 1].getY());
        curvePath.moveTo(start.getX(), start.getY());
        curvePath.lineTo(points[0].getX(), points[0].getY());
        buildCurvePath(start, points[0], points[1], points[2]);
        for (int i = 1; i < points.length - 2; i++) {
            buildCurvePath(points[i - 1], points[i], points[i + 1], points[i + 2]);
        }
        buildCurvePath(points[n - 3], points[n - 2], points[n - 1], end);
        curvePath.lineTo(end.getX(), end.getY());
    }

    private void drawPoint(Canvas canvas) {
        for (Point p : points) {
            canvas.drawPoint(p.getX(), p.getY(), pointPaint);
            drawLineFromPoint(canvas, p);
        }
    }

    private void drawLineFromPoint(Canvas canvas, Point p) {
        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(1f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(100);
        canvas.drawLine(p.getX(), p.getY(), p.getX(), view_height, paint);
    }

    /**
     * 在两点之间以贝塞尔曲线画平滑曲线
     *
     * @param p1,p2 目标点
     * @param p0,p3 目标点的前后点
     */
    private void buildCurvePath(Point p0, Point p1, Point p2, Point p3) {
        float xc1 = (p0.getX() + p1.getX()) / 2.0f;
        float yc1 = (p0.getY() + p1.getY()) / 2.0f;
        float xc2 = (p1.getX() + p2.getX()) / 2.0f;
        float yc2 = (p1.getY() + p2.getY()) / 2.0f;
        float xc3 = (p2.getX() + p3.getX()) / 2.0f;
        float yc3 = (p2.getY() + p3.getY()) / 2.0f;

        float len1 = (float) sqrt((p1.getX() - p0.getX()) * (p1.getX() - p0.getX()) +
                (p1.getY() - p0.getY()) * (p1.getY() - p0.getY()));
        float len2 = (float) sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) +
                (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));
        float len3 = (float) sqrt((p3.getX() - p2.getX()) * (p3.getX() - p2.getX()) +
                (p3.getY() - p2.getY()) * (p3.getY() - p2.getY()));

        float k1 = len1 / (len1 + len2);
        float k2 = len2 / (len2 + len3);

        float xm1 = xc1 + (xc2 - xc1) * k1;
        float ym1 = yc1 + (yc2 - yc1) * k1;

        float xm2 = xc2 + (xc3 - xc2) * k2;
        float ym2 = yc2 + (yc3 - yc2) * k2;

        // Resulting control points. Here smooth_value is mentioned
        // above coefficient K whose value should be in range [0...1].
        float ctrl1_x = xm1 + (xc2 - xm1) * smooth_value + p1.getX() - xm1;
        float ctrl1_y = ym1 + (yc2 - ym1) * smooth_value + p1.getY() - ym1;

        float ctrl2_x = xm2 + (xc2 - xm2) * smooth_value + p2.getX() - xm2;
        float ctrl2_y = ym2 + (yc2 - ym2) * smooth_value + p2.getY() - ym2;

        curvePath.cubicTo(ctrl1_x, ctrl1_y, ctrl2_x, ctrl2_y, p2.getX(), p2.getY());
    }

    static class Point {
        private String clock;
        private int temperature;
        private float x;
        private float y;

        public String getClock() {
            return clock;
        }

        public void setClock(String clock) {
            this.clock = clock;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
