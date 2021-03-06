package com.cyy.sound.Sound;

import android.content.Context;
import android.content.res.TypedArray;
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


import com.cyy.sound.R;
import com.cyy.sound.tools.DisplayUtils;

import java.io.File;

/**
 * Created by study on 17/4/19.
 *
 * 声音的可视化View
 */

public class SoundView extends RelativeLayout implements View.OnClickListener,
        PlaySoundManager.PlaySoundListener{

    public interface SoundListener{
        void clickSound(SoundView soundView); //点击开始播放
        void retryAction(SoundView soundView);
        void playComplete(SoundView soundView); //播放完成
    }

    private static final int MIN_WIDTH = 90; //单位dp 声音背景的最小长度
    private static final int MAX_WIDTH = 160; //单位DP 声音背景的最大长度

    private TextView soundTimeView;//声音的时间View
    private View bgView;//声音的背景View 根据声音的长度确定这个View的宽度
    private ImageView soundImageView; //播放声音时的动画View
    private ProgressBar progressBar;//进度条
    private ImageView retryView;//重试

    private Sound sound; //声音文件
    private SoundListener soundListener;
    private boolean isPlaying = false;

    private PlaySoundManager playSoundManager;

    private int soundAnimateRes;
    private int soundAnimateholder;//动画占位图

    public SoundView(Context context) {
        super(context);
        init(null);
    }

    public SoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init( attrs);
    }

    public SoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init( AttributeSet attrs){
        int soundBackgroundDrawable = 0;
        int retryRes = 0 ;
        if (attrs!=null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs , R.styleable.SoundView);
            soundBackgroundDrawable = typedArray.getResourceId(R.styleable.SoundView_soundBackgroundDrawable , 0);
            soundAnimateRes = typedArray.getResourceId(R.styleable.SoundView_soundAnimateDrawablw , 0);
            soundAnimateholder = typedArray.getResourceId(R.styleable.SoundView_soundAnimatePlaceHolder , 0);
            retryRes = typedArray.getResourceId(R.styleable.SoundView_retryImageRes , 0);
            typedArray.recycle();
        }

        LayoutInflater.from(this.getContext()).inflate(R.layout.view_sound , this);
        soundTimeView = (TextView) findViewById(R.id.soundTimeView);
        bgView = findViewById(R.id.bgView);
        bgView.setOnClickListener(this);
        if (soundBackgroundDrawable!=0){
            bgView.setBackgroundResource(soundBackgroundDrawable);
        }
        soundImageView = (ImageView) findViewById(R.id.soundImageView);
        if (soundAnimateholder!=0){
            soundImageView.setImageResource(soundAnimateholder);
        }
        progressBar = (ProgressBar) findViewById(R.id.progressView);
        retryView = (ImageView) findViewById(R.id.retryView);
        retryView.setOnClickListener(this);
        if (retryRes!=0){
            retryView.setImageResource(retryRes);
        }

        //初始化播放
        playSoundManager = new PlaySoundManager();
        playSoundManager.setPlaySoundListener(this);
    }

    public void setSound(Sound sound){
        this.sound = sound;
        soundTimeView.setText(this.sound.soundTime/1000+"");
    }

    public Sound getSound() {
        return sound;
    }

    public void setSoundListener(SoundListener soundListener) {
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
        stopPlayAmin();
    }

    //后台处理完成调用 显示左边的文字
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
            lp.topMargin = DisplayUtils.dip2px(20);
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
        if (soundAnimateRes==0){
            soundImageView.setImageResource(R.drawable.play_sound_anim);
        }else {
            soundImageView.setImageResource(soundAnimateRes);
        }
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
            if (soundAnimateholder==0){
                soundImageView.setImageResource(R.drawable.ic_voice3);
            }else {
                soundImageView.setImageResource(soundAnimateholder);
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bgView){ //点击背景
            if (this.soundListener != null){
                soundListener.clickSound(this);
            }
            startPlayAnim();
            playSoundManager.playSound(sound.getSoundFile());
        }else if (v.getId() == R.id.retryView){
            //点击重试
            if (this.soundListener!=null)soundListener.retryAction(this );
            startPlayAnim();
            playSoundManager.playSound(sound.getSoundFile());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (playSoundManager!=null){
            playSoundManager.release();
        }
    }

    @Override
    public void complete() {
        if (playSoundManager!=null){
            playSoundManager.release();
        }
        if (soundListener!=null){
            soundListener.playComplete(this);
        }
        stopPlayAmin();
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
