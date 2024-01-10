package com.ma.optionsinline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OptionsInLine extends View {

    private int optionNum = 7;
    private static int defaultOptionIndex = 0;
    private static int textColor = 0xff000000;

    private static Paint paint = new Paint();

    private String[] optionTags = {"W3", "W2", "W1", "O", "C1", "C2", "C3"};
    private static int selectedIndex = 0;
    private float optionHeight = 0f;

    public OptionsInLine(Context context) {
        super(context);
        init();
    }

    public OptionsInLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionsInLine);
        optionNum = typedArray.getInt(R.styleable.OptionsInLine_OptionsInLine_optionNum, optionNum);
        defaultOptionIndex = typedArray.getInt(R.styleable.OptionsInLine_OptionsInLine_defaultOptionIndex, defaultOptionIndex);
        textColor = typedArray.getColor(R.styleable.OptionsInLine_OptionsInLine_textColor, textColor);
        typedArray.recycle();

        init();
    }

    public OptionsInLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public OptionsInLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setTextSize(20);

        if (defaultOptionIndex >= optionNum) defaultOptionIndex = 0;

        selectedIndex = 4;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        final int width = getWidth() - paddingLeft - paddingRight;
        final int height = getHeight() - paddingTop - paddingBottom;

        optionHeight = (height / 2f) * 0.1f;
        final float textHeight = (height / 2f) * 0.4f;

        //背景线
        canvas.drawLine(paddingLeft, paddingTop + height / 2f, width + paddingLeft, paddingTop + height / 2f, paint);

        if (optionNum == 1) {
            //项目分隔线
            canvas.drawLine(paddingLeft + width / 2f, paddingTop + height / 2f, paddingLeft + width / 2f, paddingTop + height / 2f - optionHeight, paint);
            if (optionTags.length == optionNum) {
                float measuredTextWidth = paint.measureText(optionTags[0]) / 2f;
                float measuredTextHeight = paint.getFontSpacing() * 0.3f;
                canvas.drawText(optionTags[0], paddingLeft + width / 2f - measuredTextWidth, paddingTop + height / 2f + textHeight - measuredTextHeight, paint);
            }
            canvas.drawCircle(paddingLeft + width / 2f, paddingTop + height / 2f, optionHeight, paint);
        } else if (optionNum > 1) {
            int tmpOptionNum = optionNum - 2;
            //项目分隔线 第一条跟最后一条
            canvas.drawLine(paddingLeft, paddingTop + height / 2f, paddingLeft, paddingTop + height / 2f - optionHeight, paint);
            canvas.drawLine(paddingLeft + width, paddingTop + height / 2f, paddingLeft + width, paddingTop + height / 2f - optionHeight, paint);
            if (optionTags.length == optionNum) {
                float measuredTextWidth = paint.measureText(optionTags[0]) / 2f;
                float measuredTextHeight = paint.getFontSpacing() * 0.3f;
                canvas.drawText(optionTags[0], paddingLeft - measuredTextWidth, paddingTop + height / 2f + textHeight - measuredTextHeight, paint);
                measuredTextWidth = paint.measureText(optionTags[optionTags.length - 1]) / 2f;
                canvas.drawText(optionTags[optionTags.length - 1], paddingLeft + width - measuredTextWidth, paddingTop + height / 2f + textHeight - measuredTextHeight, paint);
            }
            if (selectedIndex == 0) {
                canvas.drawCircle(paddingLeft, paddingTop + height / 2f, optionHeight, paint);
            } else if (selectedIndex == optionNum - 1) {
                canvas.drawCircle(paddingLeft + width, paddingTop + height / 2f, optionHeight, paint);
            }

            if (tmpOptionNum > 0) {
                tmpOptionNum++;
                float tmpOptionGap = (float) width / tmpOptionNum;
                for (int i = 0; i < tmpOptionNum - 1; i++) {
                    //项目分隔线
                    canvas.drawLine(paddingLeft + tmpOptionGap * (i + 1), paddingTop + height / 2f, paddingLeft + tmpOptionGap * (i + 1), paddingTop + height / 2f - optionHeight, paint);
                    if (optionTags.length == optionNum) {
                        float measuredTextWidth = paint.measureText(optionTags[i + 1]) / 2f;
                        float measuredTextHeight = paint.getFontSpacing() * 0.3f;
                        canvas.drawText(optionTags[i + 1], paddingLeft + tmpOptionGap * (i + 1) - measuredTextWidth, paddingTop + height / 2f + textHeight - measuredTextHeight, paint);
                    }
                    if (selectedIndex == i + 1) {
                        canvas.drawCircle(paddingLeft + tmpOptionGap * (i + 1), paddingTop + height / 2f, optionHeight, paint);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //屏蔽父级触摸事件
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //第一个点 index 0
                if (event.getX() >= getPaddingLeft() - optionHeight * 2 && event.getX() <= getPaddingLeft() + optionHeight * 2) {
                    setSelectedIndex(0);
                    return true;
                    //最后一个点 index optionNum - 1
                } else if (event.getX() >= getWidth() - getPaddingRight() - optionHeight * 2 && event.getX() <= getWidth() - getPaddingRight() + optionHeight * 2) {
                    setSelectedIndex(optionNum - 1);
                    return true;
                } else {
                    int tmpNum = optionNum - 1;
                    float tmpOptionGap = (float) (getWidth() - getPaddingRight() - getPaddingLeft()) / tmpNum;
                    for (int i = 0; i < tmpNum - 1; i++) {
                        if (event.getX() >= getPaddingLeft() + tmpOptionGap * (i + 1) - optionHeight * 2 && event.getX() <= getPaddingLeft() + tmpOptionGap * (i + 1) + optionHeight * 2) {
                            setSelectedIndex(i + 1);
                            return true;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onTouchEvent(event);
    }

    public int getOptionNum() {
        return optionNum;
    }

    public void setOptionNum(int optionNum) {
        this.optionNum = optionNum;
        invalidate();

    }

    public String[] getOptionTags() {
        return optionTags;
    }

    public void setOptionTags(String[] optionTags) {
        this.optionTags = optionTags;
        invalidate();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        OptionsInLine.selectedIndex = selectedIndex;
        invalidate();
    }
}
