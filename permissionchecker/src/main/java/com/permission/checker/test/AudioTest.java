package com.permission.checker.test;

import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.File;

/**
 * Created by shawn on 2018/2/22.
 *
 * 录音测试
 */

class AudioTest implements IPermissionTest {
    private MediaRecorder mRecorder;
    private File mTempFile = null;

    public AudioTest(){
        mRecorder = new MediaRecorder();
    }

    @Override
    public boolean test() throws Throwable{
        try {
            mTempFile = File.createTempFile("permission", "test");
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mTempFile.getAbsolutePath());
            mRecorder.prepare();
            mRecorder.start();
            return true;
        } finally {
            stop();
        }
    }

    private void stop() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
            } catch (Exception ignored) {
            }
            try {
                mRecorder.release();
            } catch (Exception ignored) {
            }
        }

        if (mTempFile != null && mTempFile.exists()) {
            // noinspection
            mTempFile.delete();
        }
    }
}
