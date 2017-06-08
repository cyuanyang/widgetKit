package com.cyy.sound.Sound;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.cyy.sound.R;

import java.io.File;

/**
 * Created by study on 17/4/18.
 * 按住说话按钮
 */

public class SpeakButton extends TextView {

    private final static int CANCEL_DISTANCE = 200; //上滑取消录制的最小距离
    private final static int MIN_SOUND = 1000; //最小的时间的间隔
    private long soundTime; //录音的时间

    private AudioManager audioManager;
    private File file ;

    private SoundPopWindow popWindow;

    private boolean isCancelRecord; //是否取消录制

    public SpeakButton(Context context) {
        super(context);
        init();
    }

    public SpeakButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpeakButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    private void init(){
        //初始化播放器
        this.audioManager = new AudioManager();
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_sound_popwindow , null);
        popWindow = new SoundPopWindow(contentView);
    }

    //取消录制
    private void cancelRecord(){
        audioManager.cancel();
        removeCallbacks(updateMicRunnable);
    }

    /**
     * 开始录制
     */
    private void beginRecord(){
        isCancelRecord = false;
        popWindow.show(this);
        file = audioManager.record();
        soundTime = System.currentTimeMillis();
        updateMicStatus();

    }

    private Runnable updateMicRunnable = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };
    private void updateMicStatus(){
        int db = audioManager.get();
        popWindow.changVolumeStatue(db);
        postDelayed(updateMicRunnable, 200);
    }

    /**
     * 结束录制
     */
    private void endRecord(){
        if (!isCancelRecord && file != null){
            int during = (int) (System.currentTimeMillis()-soundTime);
            if (during > MIN_SOUND){
                popWindow.dismiss();
                audioManager.release();
                if (speakCallback!=null)speakCallback.endSpeak(file , during);
            }else {
                popWindow.speakTooShort();
            }
        }else {
            popWindow.dismiss();
            cancelRecord();
        }

        removeCallbacks(updateMicRunnable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                setText(R.string.speak_txt_pressed);
                beginRecord();
//                StateListDrawable drawable = (StateListDrawable) getBackground();
//                drawable.setState(new int[]{android.R.attr.state_focused,android.R.attr.state_pressed});
                setPressed(true);
                break;

            case MotionEvent.ACTION_MOVE:
                isCancelRecord = event.getY()<-CANCEL_DISTANCE;
                popWindow.cancelRecord(isCancelRecord);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                endRecord();
                setText(R.string.speak_txt_normal);
                setPressed(false);
                break;
        }
        return true;
    }


    private SpeakCallback speakCallback;
    public void setSpeakCallback(SpeakCallback speakCallback) {
        this.speakCallback = speakCallback;
    }

    /**
     * 按下说话回调监听
     */
    public interface SpeakCallback{
        void endSpeak(File sourceFile, int during); //结束录音
    }

}
