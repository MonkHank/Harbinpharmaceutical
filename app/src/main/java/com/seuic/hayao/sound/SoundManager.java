package com.seuic.hayao.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.seuic.hayao.HYApplication;
import com.seuic.hayao.R;


public class SoundManager {

    private final float BEEP_VOLUME = 0.8f;
    private final long VIBRATE_DURATION = 200L;

    private boolean vibrate = false;

    private Context mContext;
    private int loadId1;
    private int loadId2;
    private SoundPool mSoundPool;
    private Vibrator mVibrator;

    private static SoundManager manager;

    public synchronized static SoundManager getInstance() {
        if (manager == null) {
            manager = new SoundManager(HYApplication.getApplication());
        }
        return manager;
    }

    private SoundManager(Context context, boolean vibrate) {
        super();
        this.mContext = context;
        this.vibrate = vibrate;
        initial();
    }

    private SoundManager(Context context) {
        this(context, false);
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    private void initial() {

        if (null == mSoundPool) {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        loadId1 = mSoundPool.load(mContext, R.raw.error1, 1);
        loadId2 = mSoundPool.load(mContext, R.raw.scan, 1);
        // initialVibrator
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void play() {

        // 参数1：播放特效加载后的ID�?
        // 参数2：左声道音量大小(range = 0.0 to 1.0)
        // 参数3：右声道音量大小(range = 0.0 to 1.0)
        // 参数4：特效音乐播放的优先级，因为可以同时播放多个特效音乐
        // 参数5：是否循环播放，0只播放一�?(0 = no loop, -1 = loop forever)
        // 参数6：特效音乐播放的速度�?1F为正常播放，范围 0.5 �? 2.0
        mSoundPool.play(loadId1, BEEP_VOLUME, BEEP_VOLUME, 1, 0, 1f);

        if (vibrate) {
            mVibrator.vibrate(VIBRATE_DURATION);
        }

    }

    public void playScan(){
        mSoundPool.play(loadId2, BEEP_VOLUME, BEEP_VOLUME, 1, 0, 1f);
    }

    public void vibration(){
        mVibrator.vibrate(50);
    }

}
