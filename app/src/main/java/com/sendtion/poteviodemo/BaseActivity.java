package com.sendtion.poteviodemo;

import android.content.pm.ActivityInfo;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sendtion.poteviodemo.util.ActivityUtils;
import com.sendtion.poteviodemo.util.InstallUtils;
import com.sendtion.poteviodemo.util.SPUtils;
import com.sendtion.poteviodemo.util.StatusBarUtil;
import com.sendtion.poteviodemo.widget.HttpLoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Activity基类
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    private HttpLoadingDialog progressLoadingDialog;
    protected Map<Integer,String> analyst = new HashMap<>();

    protected void showProgressDialog(String msg) {
        if (progressLoadingDialog == null) {
            progressLoadingDialog = new HttpLoadingDialog(this, msg);
        }
        progressLoadingDialog.setCancelable(false);
        if (!isFinishing()) {
            progressLoadingDialog.show();
        }
    }

    protected void showProgressDialog() {
        if (progressLoadingDialog == null) {
            progressLoadingDialog = new HttpLoadingDialog(this, "数据加载中...");
        }
        progressLoadingDialog.setCancelable(false);
        if (!isFinishing()) {
            progressLoadingDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (progressLoadingDialog != null && progressLoadingDialog.isShowing()) {
            progressLoadingDialog.dismiss();
        }
    }

    public void setContentView(int layoutId){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.setContentView(layoutId);

        initActivity();
        initStatusBar();
        initView();
        initData();
    }

    private void initActivity(){
        // 添加Activity到堆栈
        ActivityUtils.getInstance().addActivity(this);
        //初始化EventBus
        if (isBindEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //初始化ButterKnife
        ButterKnife.bind(this);
    };

    protected void initStatusBar() {
        //此种方式，在MIUI上白色状态栏不会看不到文字
        StatusBarUtil.addStatusBarView(this, ContextCompat.getColor(this, R.color.color_f));
        StatusBarUtil.setStatusBar(this, false, false);
        StatusBarUtil.setStatusTextColor(true, this);
    }

    protected abstract void initView();

    protected abstract void initData();

    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        //解除对EventBus的绑定
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        // 结束Activity&从堆栈中移除
        ActivityUtils.getInstance().finishActivity(this);
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //判断是否需要安装新版本
        String newAppUrl = SPUtils.getInstance().getString("newAppUrl");
        if (!TextUtils.isEmpty(newAppUrl)){
            InstallUtils.installAPK(this, newAppUrl);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void analystEvent(Integer viewId){
        if (analyst.containsKey(viewId)) {
            MobclickAgent.onEvent(this,analyst.get(viewId));
        }
    }

    protected void analystEvent(String event){
        if (!TextUtils.isEmpty(event)) {
            MobclickAgent.onEvent(this,event);
        }
    }
}
