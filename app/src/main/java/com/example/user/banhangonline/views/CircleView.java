package com.example.user.banhangonline.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.user.banhangonline.R;

public class CircleView extends View {

    private int circleRadius;
    private Paint edgePaint;
    private Paint fillPaint;
    int mAccentColor;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mAccentColor = ContextCompat.getColor(getContext(), R.color.transparent);
        circleRadius = (int) getResources().getDimension(R.dimen.circle_border_radius);

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(mAccentColor);

        edgePaint = new Paint();
        edgePaint.setAntiAlias(true);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setColor(mAccentColor);
        edgePaint.setStrokeWidth(circleRadius);
    }

    public void setColorCircle(int colorCircle) {
        fillPaint.setColor(colorCircle);
        edgePaint.setColor(colorCircle);
        postInvalidate();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int center = (getMeasuredWidth() / 2);
        final int radius = (getMeasuredWidth() / 2) - circleRadius;
        if (isActivated()) canvas.drawCircle(center, center, radius, fillPaint);
        canvas.drawCircle(center, center, radius, edgePaint);
    }
}
