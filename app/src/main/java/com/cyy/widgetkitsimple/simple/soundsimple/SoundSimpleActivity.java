package com.cyy.widgetkitsimple.simple.soundsimple;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.cyy.sound.Sound.SoundView;
import com.cyy.sound.Sound.SpeakButton;
import com.cyy.widgetkitsimple.R;
import com.cyy.widgetkitsimple.base.BaseActivity;

import java.io.File;
import java.util.logging.Handler;

public class SoundSimpleActivity extends BaseActivity implements SpeakButton.SpeakCallback
 {

    protected SpeakButton speakBtn;
    protected LinearLayout soundViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_sound_simple);
        initView();
        speakBtn.setSpeakCallback(this);
    }

    private void initView() {
        speakBtn = (SpeakButton) findViewById(R.id.speakBtn);
        soundViewContainer = (LinearLayout) findViewById(R.id.soundViewContainer);
    }

    @Override
    public void endSpeak(File sourceFile, int during) {
        SoundView soundView = new SoundView(this);
        soundView.setSound(new SoundView.Sound(during , sourceFile));
        soundView.setParentView(soundViewContainer);

        dealSound(soundView , sourceFile);

    }
    //模拟后台处理声音
    private void dealSound(final SoundView soundView  , File sourceFile){
        soundView.showProgressView();
        soundView.postDelayed(new Runnable() {
            @Override
            public void run() {
                soundView.showSoundTextView();
            }
        },2000);
    }

     @Override
     public void speakTooShort() {

     }
 }
