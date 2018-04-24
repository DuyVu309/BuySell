package com.example.user.banhangonline.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.user.banhangonline.R;

public class SquareFrameLayoutSell extends FrameLayout {
    private Paint edgePaint;
    private int borderRadius;

    public SquareFrameLayoutSell(@NonNull Context context) {
        super(context);
        init();
    }

    public SquareFrameLayoutSell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SquareFrameLayoutSell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SquareFrameLayoutSell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        borderRadius = (int) getResources().getDimension(R.dimen.default_square_border_radius);
        edgePaint = new Paint();
        edgePaint.setAntiAlias(true);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setColor(ContextCompat.getColor(getContext(), R.color.grey_divider));
        edgePaint.setStrokeWidth(borderRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth() * 2 / 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isActivated()) {
            int left = borderRadius;
            int top = borderRadius;
            int bottom = getMeasuredHeight() - borderRadius;
            int right = getMeasuredWidth() - borderRadius;
            canvas.drawRect(left, top, right, bottom, edgePaint);
        }
    }

}
