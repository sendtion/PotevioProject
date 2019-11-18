package com.sendtion.poteviodemo.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sendtion.poteviodemo.R;
import com.sendtion.poteviodemo.util.ActivityUtils;

/**
 * 标题栏的封装
 */
public class TitleBarView extends LinearLayout {
    private TitleViewHolder mViewHolder;
    private View mTitleView;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        if (typedArray != null) {
            boolean leftVisible = typedArray.getBoolean(R.styleable.TitleBarView_leftVisible, true);
            boolean rightVisible = typedArray.getBoolean(R.styleable.TitleBarView_rightVisible, false);
            boolean titleVisible = typedArray.getBoolean(R.styleable.TitleBarView_titleVisible, true);
            String title = typedArray.getString(R.styleable.TitleBarView_titleBarContent);
            String rightContent = typedArray.getString(R.styleable.TitleBarView_titleBarRightContent);
            int color = typedArray.getColor(R.styleable.TitleBarView_titleBarRightContentColor, 0x000000);
            int bgColor = typedArray.getColor(R.styleable.TitleBarView_titleBarBgColor, 0xFFFFFF);
            initViewsVisible(leftVisible, titleVisible, rightVisible);
            setTitleContent(title);
            setTitleRight(rightContent);
            setTitleRightColor(color);
            setBgColor(bgColor);
            typedArray.recycle();
        }
    }

    private void init(final Context context) {
        int layoutId = R.layout.comm_toolbar;
        mTitleView = LayoutInflater.from(context).inflate(layoutId, this, false);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dp_20));
        addView(mTitleView, layoutParams);

        mViewHolder = new TitleViewHolder(this);
        mViewHolder.mLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackLeft != null) {
                    callBackLeft.OnLeftClick(v);
                } else {
                    ActivityUtils.getInstance().finishActivity((Activity) context);
                }
            }
        });
        mViewHolder.mRightContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.OnRightClick(v);
                }
            }
        });
        mViewHolder.mRightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.OnRightClick(v);
                }
            }
        });
    }

    public void initViewsVisible(boolean isLeftVisible, boolean isContentVisible, boolean isRightVisible) {
        // 左侧返回
        mViewHolder.mLeft.setVisibility(isLeftVisible ? View.VISIBLE : View.INVISIBLE);
        // 中间标题
        mViewHolder.mContent.setVisibility(isContentVisible ? View.VISIBLE : View.INVISIBLE);
        // 右侧返回
        mViewHolder.mRightContent.setVisibility(isRightVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setLeftIcon(int resourceId) {
        mViewHolder.mLeft.setImageResource(resourceId);
    }

    public void setTitleContent(String title) {
        if (!TextUtils.isEmpty(title)) {
            mViewHolder.mContent.setText(title);
        }
    }

    public void setTitleContentColor(int color){
        mViewHolder.mContent.setTextColor(color);
    }

    public void setTitleRight(String text) {
        if (!TextUtils.isEmpty(text)) {
            mViewHolder.mRightIcon.setVisibility(GONE);
            mViewHolder.mRightContent.setVisibility(VISIBLE);
            mViewHolder.mRightContent.setText(text);
        }
    }

    public void setIconRight(int resId) {
        mViewHolder.mRightContent.setVisibility(GONE);
        mViewHolder.mRightIcon.setVisibility(VISIBLE);
        mViewHolder.mRightIcon.setImageResource(resId);
    }

    public void setIconContentRight(Activity activity, int resId, int colorId, String text) {
        mViewHolder.mRightContent.setVisibility(GONE);
        mViewHolder.mRightIcon.setVisibility(GONE);
        Drawable drawable = ContextCompat.getDrawable(activity, resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    }

    public void setRightVisibility(boolean isRightVisible) {
        // 右侧返回
        mViewHolder.mRightContent.setVisibility(isRightVisible ? View.VISIBLE : View.INVISIBLE);
        mViewHolder.mRightIcon.setVisibility(isRightVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setBottomLineVisibility(boolean isVisible){
        mViewHolder.mBottomLine.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setTitleRightColor(int color) {
        mViewHolder.mRightContent.setTextColor(color);
    }

    public void setBgColor(int color) {
        mViewHolder.mToolBar.setBackgroundColor(color);
    }

    public void setTitleBackground(int color) {
        mTitleView.setBackgroundColor(color);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void OnRightClick(View v);
    }

    private CallBackLeft callBackLeft;

    public void setCallBackLeft(CallBackLeft callBackLeft) {
        this.callBackLeft = callBackLeft;
    }

    public interface CallBackLeft {
        void OnLeftClick(View view);
    }

    private static class TitleViewHolder {
        private LinearLayout mToolBar;
        private ImageView mRightIcon;
        private ImageView mLeft;
        private TextView mContent;
        private TextView mRightContent;
        private RelativeLayout mBottomLine;

        private TitleViewHolder(View v) {
            mToolBar = ((LinearLayout) v.findViewById(R.id.comm_toolbar));
            mLeft = (ImageView) v.findViewById(R.id.comm_toolbar_back);
            mContent = (TextView) v.findViewById(R.id.comm_toolbar_title);
            mRightContent = (TextView) v.findViewById(R.id.comm_toolbar_right_text);
            mRightIcon = ((ImageView) v.findViewById(R.id.comm_toolbar_right_img));
            mBottomLine = (RelativeLayout) v.findViewById(R.id.comm_toolbar_bottom_line);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}