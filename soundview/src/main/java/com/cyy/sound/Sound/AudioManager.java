package com.cyy.sound.Sound;

import android.media.MediaRecorder;
import android.util.Log;

import com.cyy.sound.WidgetKit;
import com.cyy.sound.tools.FileUtils;
import com.cyy.sound.tools.MD5;

import java.io.File;

/**
 * Created by study on 17/4/18.
 *
 * 录音和播放声音控制
 */

public class AudioManager {

    private File dirFile ;
    private MediaRecorder mRecorder;
    private File recordFile; //录音文件
    private boolean isRecording;//是否在录音

    public AudioManager(){
        dirFile = WidgetKit.getWidgetKit().getApp().getExternalFilesDir("audios");
    }
    /**
     * 开始录音
     * @return 返回录音存放的位置  抛异常会返回null
     */
    public File record(){
        String path = dirFile.getAbsolutePath();
        recordFile = FileUtils.createFile(path , getFileName());

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mRecorder.setOutputFile(recordFile.getAbsolutePath());
        try {
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
            return recordFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //一般在0-30左右
    public int get(){
        if (mRecorder!=null && isRecording){
            int ratio = mRecorder.getMaxAmplitude() / 150;
            Log.e("dv" , "ratio=="+ratio);
            int db = 0;// 分贝
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            Log.e("dv" , "db=="+db);
            return db;
        }else {
            return 0;
        }
    }

    public void cancel(){
        release();
        if (recordFile!=null && recordFile.exists()){
            recordFile.delete();
        }
    }

    public void release(){
        if (mRecorder != null){
            mRecorder.release();
            mRecorder = null;
        }
        isRecording = false;
    }

    private String getFileName(){
        return MD5.encode(System.currentTimeMillis()+"");
    }

    public boolean isRecording() {
        return isRecording;
    }
}
