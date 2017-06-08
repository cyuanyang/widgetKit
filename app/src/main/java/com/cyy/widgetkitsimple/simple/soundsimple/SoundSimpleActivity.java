package com.cyy.widgetkitsimple.simple.soundsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cyy.sound.Sound.SpeakButton;
import com.cyy.widgetkitsimple.R;

public class SoundSimpleActivity extends AppCompatActivity {

    protected SpeakButton speakBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_sound_simple);
        initView();


    }

    private void initView() {
        speakBtn = (SpeakButton) findViewById(R.id.speakBtn);
    }
}
