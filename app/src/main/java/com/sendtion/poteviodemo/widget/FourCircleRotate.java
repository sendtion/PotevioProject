package com.sendtion.poteviodemo.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.lang.ref.WeakReference;

/**
 * 四圆动画，
 */
public class FourCircleRotate extends View {
    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;
    private ValueAnimator valueAnimator;
    private int delay = 4000;
    private float R = 0;

    private float where = 0;
    private int show = 0;
    private AnimationHandler mHandler;

    public FourCircleRotate(Context context) {
        this(context, null);
    }

    public FourCircleRotate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FourCircleRotate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new AnimationHandler(this);
        paint1 = new Paint();
        paint2 = new Paint();
        paint3 = new Paint();
        paint4 = new Paint();
        paint5 = new Paint();
        paint1.setColor(Color.parseColor("#38a2f0"));
        paint2.setColor(Color.parseColor("#38a2f0"));
        paint3.setColor(Color.parseColor("#38a2f0"));
        paint4.setColor(Color.parseColor("#38a2f0"));
        paint5.setColor(Color.parseColor("#38a2f0"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //显示固定的圆，逐个出现
        if (show <= 4) {
            canvas.drawCircle(R, R, R, paint2);
        }
        if (show <= 3) {
            canvas.drawCircle(getWidth() - R, R, R, paint3);
        }
        if (show <= 2) {
            canvas.drawCircle(getWidth() - R, getHeight() - R, R, paint4);
        }
        if (show <= 1) {
            canvas.drawCircle(R, getHeight() - R, R, paint5);
        }
        //逐个消失
        if (show > 4) {
            if (show <= 5) {
                canvas.drawCircle(getWidth() - R, R, R, paint3);
            }
            if (show <= 6) {
                canvas.drawCircle(getWidth() - R, getHeight() - R, R, paint4);
            }
            if (show <= 7) {
                canvas.drawCircle(R, getHeight() - R, R, paint5);
            }
        }
        //移动
        if (where < 1 && where > 0) {
            canvas.drawCircle(R + (getWidth() - 2 * R) * where, R, R, paint1);
            show = 4;
        }
        if (where < 2 && where > 1) {
            canvas.drawCircle(getHeight() - R, R + (getHeight() - 2 * R) * (where - 1), R, paint1);
            show = 3;
        }
        if (where < 3 && where > 2) {
            canvas.drawCircle(getWidth() - R - (getWidth() - 2 * R) * (where - 2), getHeight() - R, R, paint1);
            show = 2;
        }
        if (where < 4 && where > 3) {
            canvas.drawCircle(R, getHeight() - R - (getHeight() - 2 * R) * (where - 3), R, paint1);
            show = 1;
        }
        if (where > 4 && where < 5) {
            canvas.drawCircle(R + (getWidth() - 2 * R) * (where - 4), R, R, paint1);
            show = 5;
        }
        if (where > 5 && where < 6) {
            canvas.drawCircle(getHeight() - R, R + (getHeight() - 2 * R) * (where - 5), R, paint1);
            show = 6;
        }
        if (where > 6 && where < 7) {
            canvas.drawCircle(getWidth() - R - (getWidth() - 2 * R) * (where - 6), getHeight() - R, R, paint1);
            show = 7;
        }
        if (where > 7 && where < 8) {
            canvas.drawCircle(R, getHeight() - R - (getHeight() - 2 * R) * (where - 7), R, paint1);
            show = 8;
        }

    }


    public void startAnimation() {
        if (valueAnimator == null) {
            valueAnimator = getValueAnimator();
        } else {
            if (!valueAnimator.isRunning()) {
                valueAnimator.start();
            }
        }
        R = getWidth() / (float) 6;
    }

    private static class AnimationHandler extends Handler {
        WeakReference<FourCircleRotate> mSoftReference;

        private AnimationHandler(FourCircleRotate fourCircleRotate) {
            mSoftReference = new WeakReference<>(fourCircleRotate);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    FourCircleRotate fourCircleRotate = mSoftReference.get();
                    if (fourCircleRotate != null) {
                        fourCircleRotate.startAnimation();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static class UpdateListener implements ValueAnimator.AnimatorUpdateListener {
        WeakReference<FourCircleRotate> mSoftReference;

        private UpdateListener(FourCircleRotate fourCircleRotate) {
            mSoftReference = new WeakReference<>(fourCircleRotate);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            FourCircleRotate fourCircleRotate = mSoftReference.get();
            if (fourCircleRotate != null) {
                fourCircleRotate.where = (float) animation.getAnimatedValue();
                fourCircleRotate.invalidate();
            }
        }
    }

    private static class AnimatorListener implements Animator.AnimatorListener {
        WeakReference<FourCircleRotate> mSoftReference;
        private boolean isCancel = false;

        private AnimatorListener(FourCircleRotate fourCircleRotate) {
            mSoftReference = new WeakReference<>(fourCircleRotate);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            isCancel = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!isCancel) {
                FourCircleRotate fourCircleRotate = mSoftReference.get();
                if (fourCircleRotate != null) {
                    fourCircleRotate.mHandler.sendEmptyMessage(1);
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isCancel = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    public void stop() {
        mHandler.removeMessages(1);
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.removeAllListeners();
            valueAnimator = null;
        }
    }

    private ValueAnimator getValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 8f);
        valueAnimator.addUpdateListener(new UpdateListener(this));
        valueAnimator.addListener(new AnimatorListener(this));
        valueAnimator.setDuration(delay);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        return valueAnimator;
    }
}
