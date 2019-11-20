package com.sendtion.poteviodemo.ui.activity.video;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sendtion.poteviodemo.R;
import com.sendtion.poteviodemo.base.BaseActivity;
import com.sendtion.poteviodemo.util.StatusBarUtils;
import com.vlc.lib.RecordEvent;
import com.vlc.lib.VlcVideoView;
import com.vlc.lib.listener.MediaListenerEvent;
import com.vlc.lib.listener.util.VLCInstance;
import com.vlc.lib.listener.util.VLCOptions;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * vlc播放rtsp流比较合适
 * https://github.com/mengzhidaren/Vlc-sdk-lib/blob/master/demo/src/main/java/com/yyl/vlc/vlc/VlcVideoFragment.java
 */
public class VLCPlayerActivity extends BaseActivity {

    @BindView(R.id.vlc_video_view)
    VlcVideoView vlcVideoView;
    @BindView(R.id.tv_vlc_url)
    TextView tvUrl;
    @BindView(R.id.tv_vlc_record)
    TextView tvRecord;
    @BindView(R.id.layout_loading)
    RelativeLayout spinKitView;

    private RecordEvent recordEvent;
    private File recordFile = new File(Environment.getExternalStorageDirectory(), "yyl");
    private File snapshotFile = new File(Environment.getExternalStorageDirectory(), "yyl.png");
    private String url = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //保持屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_vlc_player);
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

        //url = "rtsp://192.168.2.1:554/test.h264";
        tvUrl.setText(url);
    }

    @Override
    protected void initData() {
        //录制视频
        recordEvent = new RecordEvent();
        spinKitView.setVisibility(View.VISIBLE);
        vlcVideoView.setMediaListenerEvent(new MediaListenerEvent() {
            @Override
            public void eventBuffing(int event, float buffing) {
                Log.e("---", "eventBuffing: " + buffing);
            }

            @Override
            public void eventStop(boolean isPlayError) {
                Log.e("---", "eventStop: isPlayError = " + isPlayError);
            }

            @Override
            public void eventError(int event, boolean show) {
                Log.e("---", "eventError: show = " + show);
                spinKitView.setVisibility(View.GONE);
            }

            @Override
            public void eventPlay(boolean isPlaying) {
                Log.e("---", "eventPlay: isPlaying = " + isPlaying);
                spinKitView.setVisibility(View.GONE);
            }

            @Override
            public void eventPlayInit(boolean openClose) {
                Log.e("---", "eventPlayInit: openClose = " + openClose);
            }
        });
        vlcVideoView.setPath(url);
    }

    @Override
    protected void loadData() {
        startPlay3();
    }

    //打开硬件加速
    private void startPlay1() {
        LibVLC libVLC = VLCInstance.get(this);

        Media media = new Media(libVLC, Uri.parse(url));
        //  VLCOptions.setMediaOptions(media,getActivity(),VLCOptions.HW_ACCELERATION_AUTOMATIC);
//        media.setHWDecoderEnabled(true, true);
        media.setHWDecoderEnabled(false, false);
        vlcVideoView.setMedia(new MediaPlayer(libVLC));
        vlcVideoView.startPlay();
    }

    //    自定义 源文件
    private void startPlay2() {
        ArrayList<String> libOptions = VLCOptions.getLibOptions(this);

        LibVLC libVLC = new LibVLC(this, libOptions);
        Media media = new Media(libVLC, Uri.parse(url));
        media.setHWDecoderEnabled(false, false);
        vlcVideoView.setMedia(new MediaPlayer(media));
        vlcVideoView.startPlay();
    }

    //直播测试 自定义 源文件
    private void startPlay3() {
        ArrayList<String> libOptions = VLCOptions.getLibOptions(this);

        LibVLC libVLC = new LibVLC(this, libOptions);
        Media media = new Media(libVLC, Uri.parse(url));
        media.setHWDecoderEnabled(false, false);
        //media.addOption(":sout-record-dst-prefix=yylpre.mp4");
        //media.addOption(":network-caching=1000");
        //media.addOption(":rtsp-frame-buffer-size=100000");
        //media.addOption(":rtsp-tcp");

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        vlcVideoView.setMedia(mediaPlayer);
        vlcVideoView.startPlay();
    }

    @OnClick({R.id.tv_vlc_snapshot, R.id.tv_vlc_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_vlc_snapshot:
                if (vlcVideoView.isPrepare()) {
                    Media.VideoTrack videoTrack = vlcVideoView.getVideoTrack();
                    if (videoTrack != null) {
                        try {
                            if (!snapshotFile.exists()){
                                snapshotFile.createNewFile();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //原图
                        recordEvent.takeSnapshot(vlcVideoView.getMediaPlayer(), snapshotFile.getAbsolutePath(), 0, 0);
                        //原图的一半
                        //recordEvent.takeSnapshot(vlcVideoView.getMediaPlayer(), snapshotFile.getAbsolutePath(), videoTrack.width / 2, 0);
                    }
                }
                //这个就是截图 保存Bitmap就行了
                //thumbnail.setImageBitmap(vlcVideoView.getBitmap());
                //Bitmap bitmap = vlcVideoView.getBitmap();
                //saveBitmap("", bitmap);
                break;
            case R.id.tv_vlc_record:
                try {
                    if (!recordFile.exists()){
                        recordFile.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (recordEvent.isSuportRecord(vlcVideoView.getMediaPlayer()) && vlcVideoView.isPrepare()){
                    if (recordEvent.isRecording(vlcVideoView.getMediaPlayer())){
                        tvRecord.setText("停止录屏");
                        recordEvent.stopRecord(vlcVideoView.getMediaPlayer());
                    } else {
                        tvRecord.setText("开始录屏");
                        String directory = recordFile.getAbsolutePath();
                        //String std2 = "standard{access=file,mux=mp4,dst='" + directory + "/yyl.mp4'}";
                        recordEvent.startRecord(vlcVideoView.getMediaPlayer(),directory,"yyl.mp4");
                    }
                }
                break;
            default:
                break;
        }
    }

    public static boolean saveBitmap(String savePath, Bitmap mBitmap) {
        try {
            File filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void onResume() {
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        vlcVideoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        vlcVideoView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //手动清空字幕
        vlcVideoView.setAddSlave(null);
        vlcVideoView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vlcVideoView.onDestory();
        vlcVideoView = null;
    }
}
