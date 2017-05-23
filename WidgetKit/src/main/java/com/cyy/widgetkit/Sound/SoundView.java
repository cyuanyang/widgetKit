package com.cyy.widgetkit.Sound;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.cyy.widgetkit.R;
import com.cyy.widgetkit.tools.DisplayUtils;

import java.io.File;

/**
 * Created by study on 17/4/19.
 *
 * 声音的可视化View
 */

public class SoundView extends RelativeLayout implements View.OnClickListener{

    public interface ClickSoundListener{
        void clickSound(SoundView soundView);
        void retryAction(SoundView soundView);
    }

    private static final int MIN_WIDTH = 90; //单位dp
    private static final int MAX_WIDTH = 160; //单位DP

    private TextView soundTimeView;//声音的时间View
    private View bgView;//声音的背景View 根据声音的长度确定这个View的宽度
    private ImageView soundImageView; //播放声音时的动画View
    private ProgressBar progressBar;//进度条
    private ImageView retryView;//重试

    private Sound sound; //声音文件
    private ClickSoundListener soundListener;
    private boolean isPlaying = false;

    public SoundView(Context context) {
        super(context);
        init();
    }

    public SoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_sound , this);
        soundTimeView = (TextView) findViewById(R.id.soundTimeView);
        bgView = findViewById(R.id.bgView);
        bgView.setOnClickListener(this);
        soundImageView = (ImageView) findViewById(R.id.soundImageView);
        progressBar = (ProgressBar) findViewById(R.id.progressView);
        retryView = (ImageView) findViewById(R.id.retryView);
        retryView.setOnClickListener(this);
    }

    public void setSound(Sound sound){
        this.sound = sound;
        soundTimeView.setText(this.sound.soundTime/1000+"");
    }

    public Sound getSound() {
        return sound;
    }

    public void setSoundListener(ClickSoundListener soundListener) {
        this.soundListener = soundListener;
    }

    /**
     *开始等待 开始上传操作的时候调用
     *
     */
    public void showProgressView(){
        progressBar.setVisibility(VISIBLE);
        soundTimeView.setVisibility(GONE);
        retryView.setVisibility(GONE);
    }
    //后台对声音的处理出错
    public void showRetryView(){
        retryView.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        soundTimeView.setVisibility(GONE);
    }

    public void showSoundTextView(){
        progressBar.setVisibility(GONE);
        soundTimeView.setVisibility(VISIBLE);
        retryView.setVisibility(GONE);
    }

    public void setParentView(LinearLayout parentView){
        if (sound!=null){
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(time2Width(sound.soundTime) , DisplayUtils.dip2px(40));
            lp.rightMargin = DisplayUtils.dip2px(20);
            parentView.addView(this ,  0, lp );
        }else {
            throw new IllegalArgumentException("先调用setSound");
        }
    }

    //时间转换View的宽度
    private int time2Width(int time){
        int t = time/1000;
        if (t<1){
            t = 1;
        }else if (t>30){
            t = 30;
        }
        return DisplayUtils.dip2px((MAX_WIDTH-MIN_WIDTH)/30*t+MIN_WIDTH);
    }

    //开始播放动画
    private void startPlayAnim(){
        soundImageView.setImageResource(R.drawable.play_sound_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) soundImageView.getDrawable();
        if (animationDrawable!=null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }

    //停止动画
    public void stopPlayAmin(){
        AnimationDrawable animationDrawable = (AnimationDrawable) soundImageView.getDrawable();
        if (animationDrawable!=null && animationDrawable.isRunning()){
            animationDrawable.stop();
            soundImageView.setImageResource(R.drawable.ic_voice3);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bgView){ //点击背景
            if (this.soundListener != null){
                startPlayAnim();
                soundListener.clickSound(this);
            }
        }else if (v.getId() == R.id.retryView){
            //点击重试
            if (this.soundListener!=null)soundListener.retryAction(this );
        }

    }

    public static class Sound{
        private File soundFile; //声音文件
        private int soundTime; //声音的时间  单位毫秒

        public Sound(int soundTime , File soundFile){
            this.soundFile = soundFile;
            this.soundTime = soundTime;
        }

        public File getSoundFile() {
            return soundFile;
        }
    }

}
