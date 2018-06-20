package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HGTXxgl on 2018/6/20.
 */

public class ProgressCircle extends View{
    private Paint paint;
    private int strokewidth = 4;
    private int alpha = 50;
    private int progress = 30;
    private Paint mpaint;

    public ProgressCircle(Context context) {
        super(context,null);
    }

    public ProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(strokewidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(alpha);

        mpaint = new Paint();
        mpaint.setColor(Color.RED);
        mpaint.setDither(true);
        mpaint.setAntiAlias(true);
        mpaint.setStrokeCap(Paint.Cap.ROUND);
        mpaint.setStrokeJoin(Paint.Join.ROUND);
        mpaint.setStrokeWidth(strokewidth);
        mpaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = getWidth()/2;
        int cy = getHeight()/2;
        int radius = (cx>cy?cy:cx)-strokewidth;

        canvas.drawCircle(cx,cy,radius,paint);

        RectF oval = new RectF(cx-radius,cy-radius,cx+radius,cy+radius);
        canvas.drawArc(oval,90,(int)(-(3.6)*progress),false,mpaint);
    }


    public  void setprogress( int progresss){
        this.progress = progresss;

        postInvalidate();//这个很重要，不能是Invalidate()；

    }
}
