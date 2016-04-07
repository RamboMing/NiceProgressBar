package com.safewaychina.niceprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: lanming
 * @date: 2016-04-07
 */
public class NiceRoundProgressBar extends NiceHorizontalProgressBar {
    private int mRadius = dp2px(30);

    public NiceRoundProgressBar(Context context) {
        this(context, null);
    }

    public NiceRoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NiceRoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mReachedHeight = (int) (mUnReachedHeight * 1.5f);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithNumber, defStyleAttr, 0);
        mRadius = (int) ta.getDimension(R.styleable.RoundProgressBarWithNumber_radius, 30);
        ta.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int paintWidth = Math.max(mReachedHeight, mReachedHeight);
        if (widthMode != MeasureSpec.EXACTLY) {
            int desireWidth = getPaddingLeft() + getPaddingRight() + mRadius * 2 + paintWidth;
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(desireWidth, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            int desireHeight = getPaddingTop() + getPaddingBottom() + mRadius * 2 + paintWidth;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(desireHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;
        canvas.translate(getPaddingLeft(), getPaddingTop());
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setColor(mUnReachedColor);
        mPaint.setStrokeWidth(mUnReachedHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        mPaint.setColor(mReachedColor);
        mPaint.setStrokeWidth(mReachedHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0, sweepAngle, false, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);
        canvas.restore();
//        super.onDraw(canvas);
    }
}
