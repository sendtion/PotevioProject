package com.sendtion.poteviodemo.ui.activity.video;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;
import com.sendtion.poteviodemo.R;
import com.sendtion.poteviodemo.base.BaseActivity;
import com.sendtion.poteviodemo.util.StatusBarUtils;
import com.sendtion.poteviodemo.widget.CustomVideoView;

import butterknife.BindView;

/**
 * 安卓自带播放器ViewView
 */
public class VideoViewActivity extends BaseActivity {

    @BindView(R.id.video_view)
    CustomVideoView videoView;
    @BindView(R.id.spin_kit_view)
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
    }

    @Override
    protected void initStatusBar() {
        StatusBarUtils.setFullScreen(this);
    }

    @Override
    protected void initView() {
        //rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov
        //https://www.apple.com/105/media/cn/mac/family/2018/46c4b917_abfd_45a3_9b51_4e3054191797/films/bruce/mac-bruce-tpl-cn-2018_1280x720h.mp4
        //https://www.apple.com/105/media/us/iphone-x/2017/01df5b43-28e4-4848-bf20-490c34a926a7/films/feature/iphone-x-feature-tpl-cc-us-20170912_1280x720h.mp4
        //https://www.apple.com/105/media/us/iphone-x/2017/01df5b43-28e4-4848-bf20-490c34a926a7/films/feature/iphone-x-feature-tpl-cc-us-20170912_1920x1080h.mp4
        //rtmp://media3.sinovision.net:1935/live/livestream
        //rtmp://media3.sinovision.net:1935/live/livestream
        //rtmp://58.200.131.2:1935/livetv/hunantv
        //http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8
        //https://media.w3.org/2010/05/sintel/trailer.mp4

        String url = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
        //url = "rtsp://192.168.2.1:554/test.h264"; //videoview无法播放此地址

        spinKitView.setVisibility(View.VISIBLE);

        videoView.setOnPreparedListener(mp -> {
            Log.e("---", "onPrepared: " + mp.getDuration());
            spinKitView.setVisibility(View.GONE);
        });
        videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
            Log.e("---", "onError: " + what + ", " + extra);
            return false;
        });
        videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.start();

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        videoView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.stopPlayback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView = null;
    }
}
