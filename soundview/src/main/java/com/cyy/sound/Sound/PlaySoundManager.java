package com.cyy.sound.Sound;

import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created by study on 17/4/20.
 *
 * 播放文件帮助类
 *
 * 本类 为单利的
 */

public class PlaySoundManager {

    public interface PlaySoundListener{
        void complete();
    }

    private Hepler hepler;

    private PlaySoundListener playSoundListener;

    public PlaySoundManager(){
        hepler = Hepler.getInstance();
    }

    public void setPlaySoundListener(PlaySoundListener playSoundListener) {
        this.playSoundListener = playSoundListener;
    }

    public void playSound(File file){
        if (file!=null){
            if (hepler.getMediaPlayer()!=null){
                hepler.getMediaPlayer().reset();
            }
            try {
                hepler.getMediaPlayer().setDataSource(file.getAbsolutePath());
                hepler.getMediaPlayer().prepare();
                hepler.getMediaPlayer().start();
                setOnCompletionListener();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setOnCompletionListener(){
        if (hepler.getMediaPlayer()!=null){
            hepler.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (playSoundListener!=null){
                        playSoundListener.complete();
                    }
                    hepler.release();
                }
            });
        }
    }

    public void release(){
        hepler.release();
    }

    private static class Hepler{
        private static Hepler instance;
        private MediaPlayer mediaPlayer;
        private Hepler(){}

        public static Hepler getInstance(){
            if (instance == null){
                synchronized (Hepler.class){
                    if (instance == null){
                        instance = new Hepler();
                    }
                }
            }
            return instance;
        }

        public MediaPlayer getMediaPlayer() {
            if (mediaPlayer == null)mediaPlayer = new MediaPlayer();
            return mediaPlayer;
        }

        public void release(){
            if (mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }


}
