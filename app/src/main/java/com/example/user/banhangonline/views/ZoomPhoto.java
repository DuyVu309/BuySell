package com.example.user.banhangonline.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import uk.co.senab.photoview.PhotoView;

public class ZoomPhoto extends PhotoView {

    public ZoomPhoto(Context context) {
        super(context);
    }

    public ZoomPhoto(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public ZoomPhoto(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);

        Matrix m = getDisplayMatrix();
        float[] values = new float[9];
        m.getValues(values);

        if (values[5] < 0.0f) {
            m.postTranslate(0, -2 * values[5]);
            setDisplayMatrix(m);
        }
    }
}
