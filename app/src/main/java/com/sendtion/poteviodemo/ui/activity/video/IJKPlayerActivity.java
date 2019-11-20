package com.sendtion.poteviodemo.ui.activity.video;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.github.ybq.android.spinkit.SpinKitView;
import com.sendtion.poteviodemo.R;
import com.sendtion.poteviodemo.base.BaseActivity;
import com.sendtion.poteviodemo.util.StatusBarUtils;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * IJKPlayer播放视频
 * gsyVideoPlayer或者dkplayer
 */
public class IJKPlayerActivity extends BaseActivity {

    @BindView(R.id.gsy_player)
    StandardGSYVideoPlayer gsyVideoPlayer;
    @BindView(R.id.spin_kit_view)
    SpinKitView spinKitView;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijk_player);
    }

    @Override
    protected void initStatusBar() {
        StatusBarUtils.setFullScreen(this);
    }

    @Override
    protected void initView() {
        //rtsp://192.168.2.1:554/test.h264

        //rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov
        //https://www.apple.com/105/media/cn/mac/family/2018/46c4b917_abfd_45a3_9b51_4e3054191797/films/bruce/mac-bruce-tpl-cn-2018_1280x720h.mp4
        //https://www.apple.com/105/media/us/iphone-x/2017/01df5b43-28e4-4848-bf20-490c34a926a7/films/feature/iphone-x-feature-tpl-cc-us-20170912_1280x720h.mp4
        //https://www.apple.com/105/media/us/iphone-x/2017/01df5b43-28e4-4848-bf20-490c34a926a7/films/feature/iphone-x-feature-tpl-cc-us-20170912_1920x1080h.mp4
        //rtmp://media3.sinovision.net:1935/live/livestream
        //rtmp://media3.sinovision.net:1935/live/livestream
        //rtmp://58.200.131.2:1935/livetv/hunantv
        //http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8
        //https://media.w3.org/2010/05/sintel/trailer.mp4

        url = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
        //url = "rtsp://192.168.2.1:554/test.h264";

//        videoView.setUrl("rtsp://192.168.2.1:554/test.h264"); //设置视频地址
//        StandardVideoController controller = new StandardVideoController(this);
//        controller.setTitle("test"); //设置视频标题
//        controller.setLive();
//        controller.hideNetWarning(); //隐藏网络警告
//        videoView.setVideoController(controller); //设置控制器，如需定制可继承BaseVideoController
//        //videoView.setPlayerFactory(ExoMediaPlayerFactory.create());
//        //videoView.setPlayerFactory(new MyPlayerFactory());
//        //videoView.setUsingSurfaceView(true); //不支持视频截图，默认用的是TextureView支持截图
//        //videoView.setEnableMediaCodec(true);
//        videoView.setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT);
//        videoView.start(); //开始播放，不调用则不自动播放
//        videoView.addOnVideoViewStateChangeListener(new OnVideoViewStateChangeListener() {
//            @Override
//            public void onPlayerStateChanged(int playerState) {
//                Log.e("---", "onPlayerStateChanged: " + playerState);
//                switch (playerState) {
//                    case VideoView.PLAYER_NORMAL://小屏
//                        break;
//                    case VideoView.PLAYER_FULL_SCREEN://全屏
//                        break;
//                }
//            }
//
//            @Override
//            public void onPlayStateChanged(int playState) {
//                Log.e("---", "onPlayStateChanged: " + playState);
//                switch (playState) {
//                    case VideoView.STATE_IDLE://静止状态，此时播放器还未进行初始化
//                        break;
//                    case VideoView.STATE_PREPARING://正在准备播放，调用了prepareAsync()即进入此状态
//                        spinKitView.setVisibility(View.VISIBLE);
//                        break;
//                    case VideoView.STATE_PREPARED://准备完成状态，播放器此时会回调了onPrepared()方法
//                        break;
//                    case VideoView.STATE_PLAYING://正在播放状态，调用了start()方法即进入此状态
//                        spinKitView.setVisibility(View.GONE);
//                        break;
//                    case VideoView.STATE_PAUSED://暂停播放状态，调用了pause()方法即进入此状态
//                        break;
//                    case VideoView.STATE_PLAYBACK_COMPLETED://播放完成状态，播放器此时会回调onCompletion()方法
//                        break;
//                    case VideoView.STATE_BUFFERING://缓冲状态，seekTo方法会触发此状态，播放器此时会回调onInfo()方法
//                        spinKitView.setVisibility(View.VISIBLE);
//                        break;
//                    case VideoView.STATE_BUFFERED://缓冲结束状态，播放器此时会回调onInfo()方法
//                        spinKitView.setVisibility(View.GONE);
//                        break;
//                    case VideoView.STATE_ERROR://播放出错
//                        break;
//                }
//            }
//        });

        //EXOPlayer内核，支持格式更多
        //PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        //系统内核模式
        //PlayerFactory.setPlayManager(SystemPlayerManager.class);
        //ijk内核，默认模式
        PlayerFactory.setPlayManager(IjkPlayerManager.class);

        //exo缓存模式，支持m3u8，只支持exo
        //CacheFactory.setCacheManager(ExoPlayerCacheManager.class);
        //代理缓存模式，支持所有模式，不支持m3u8等，默认
        CacheFactory.setCacheManager(ProxyCacheManager.class);

        //切换渲染模式
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        //默认显示比例
        //GSYVideoType.SCREEN_TYPE_DEFAULT = 0;
        //16:9
        //GSYVideoType.SCREEN_TYPE_16_9 = 1;
        //4:3
        //GSYVideoType.SCREEN_TYPE_4_3 = 2;
        //全屏裁减显示，为了显示正常 CoverImageView 建议使用FrameLayout作为父布局
        //GSYVideoType.SCREEN_TYPE_FULL = 4;
        //全屏拉伸显示，使用这个属性时，surface_container建议使用FrameLayout
        //GSYVideoType.SCREEN_MATCH_FULL = -4;

        //切换绘制模式
        //GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
        //GSYVideoType.setRenderType(GSYVideoType.GLSURFACE);
        GSYVideoType.setRenderType(GSYVideoType.TEXTURE);

        //ijk关闭log
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);

        //exoplayer自定义MediaSource
//        ExoSourceManager.setExoMediaSourceInterceptListener(new ExoMediaSourceInterceptListener() {
//            @Override
//            public MediaSource getMediaSource(String dataSource, boolean preview, boolean cacheEnable, boolean isLooping, File cacheDir) {
//                //可自定义MediaSource
//                return null;
//            }
//        });

        gsyVideoPlayer.setUp(url, true, "测试视频");
        gsyVideoPlayer.startPlayLogic();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {

    }

//    class MyPlayerFactory extends PlayerFactory<IjkPlayer> {
//        @Override
//        public IjkPlayer createPlayer() {
//            return new IjkPlayer() {
//                @Override
//                public void setOptions() {
//                    super.setOptions();
//                    //支持concat
////                    mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "safe", 0);
////                    mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "protocol_whitelist",
////                            "rtmp,concat,ffconcat,file,subfile,http,https,tls,rtp,tcp,udp,crypto,rtsp");
//                    //使用tcp方式拉取rtsp流，默认是通过udp方式
//                    mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
//                }
//            };
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
//        videoView.pause();
        gsyVideoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
//        videoView.resume();
        gsyVideoPlayer.onVideoResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        videoView.release();
        gsyVideoPlayer.release();
    }

}
