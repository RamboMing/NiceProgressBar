package com.safewaychina.niceprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: lanming
 * @date: 2016-04-07
 */
public class NiceHorizontalProgressBar extends ProgressBar {


    private static final int DEFAULT_COLOR_UNREACHED = Color.parseColor("#c8c8c8");
    private static final int DEFAULT_COLOR_REACHED = Color.parseColor("#0000ff");
    private static final int DEFAULT_HEIGHT_UNREACHED_BAR = 5;
    private static final int DEFAULT_HEIGHT_REACHED_BAR = 5;
    private static final int DEFAULT_SIZE_TEXT = 10;
    private static final int DEFAULT_OFFSET_TEXT = 5;
    private static final int DEFAULT_COLOR_TEXT = DEFAULT_COLOR_REACHED;

    protected int mUnReachedColor = DEFAULT_COLOR_UNREACHED;
    protected int mReachedColor = DEFAULT_COLOR_REACHED;

    protected int mUnReachedHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_BAR);
    protected int mReachedHeight = dp2px(DEFAULT_HEIGHT_REACHED_BAR);

    protected int mTextSize = sp2px(DEFAULT_SIZE_TEXT);
    protected int mTextOffset = dp2px(DEFAULT_OFFSET_TEXT);
    protected int mTextColor = DEFAULT_COLOR_TEXT;

    protected Paint mPaint = new Paint();
    protected int mRealWidth;
    protected boolean mIsDrawText = true;
    protected static final int VISIBLE = 0;

    public NiceHorizontalProgressBar(Context context) {
        this(context, null);
    }

    public NiceHorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NiceHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(true);

        obtainStyledAttributes(attrs);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
    }

    private void obtainStyledAttributes(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBarWithNumber);
        mReachedColor = ta.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_reached_color, DEFAULT_COLOR_REACHED);
        mUnReachedColor = ta.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color, DEFAULT_COLOR_UNREACHED);
        mReachedHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_reached_height, DEFAULT_HEIGHT_REACHED_BAR);
        mUnReachedHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_height, DEFAULT_HEIGHT_UNREACHED_BAR);
        mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_size, DEFAULT_SIZE_TEXT);
        mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_offset, DEFAULT_OFFSET_TEXT);
        mTextColor = ta.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_text_color, DEFAULT_COLOR_TEXT);

        int textVisible = ta.getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility, VISIBLE);
        mIsDrawText = textVisible == VISIBLE ? true : false;
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            float textHeight = mPaint.descent() + mPaint.ascent();
            int desireHeight = (int) (getPaddingBottom() + getPaddingTop() + Math.max(Math.max(mReachedHeight, mUnReachedHeight), Math.abs(textHeight)));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(desireHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        boolean noBg = false;
        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        float radio = getProgress() * 1.0f / getMax();
        float progressPosX = mRealWidth * radio;

        if (progressPosX + mTextOffset + textHeight > mRealWidth) {
            progressPosX = mRealWidth - textWidth - mTextOffset;
            noBg = true;
        }
        float end = progressPosX;

        if (end > 0) {
            mPaint.setColor(mReachedColor);
            mPaint.setStrokeWidth(mReachedHeight);
            canvas.drawLine(0, 0, end, 0, mPaint);
        }
        if (mIsDrawText) {
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            canvas.drawText(text, progressPosX, -textHeight, mPaint);
        }
        if (!noBg) {
            float start = progressPosX + mTextOffset + textWidth;
            mPaint.setColor(mUnReachedColor);
            mPaint.setStrokeWidth(mUnReachedHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();
//        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRealWidth = w - getPaddingLeft() - getPaddingRight();
    }

    public int sp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, getContext().getResources().getDisplayMetrics());
    }

    public int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getContext().getResources().getDisplayMetrics());
    }
}
