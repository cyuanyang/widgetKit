package com.permission.checker.test;

import android.Manifest;
import android.content.Context;

import com.permission.checker.IChecker;

/**
 * Created by shawn on 2018/2/22.
 *
 * 与 StandardChecker 配合使用 解决所有的手机判断权限问题
 *
 * 原理是 执行操作判断是否有数据生成 有的话则有权限 没有数据或者报错没有权限
 */

public class StrictChecker implements IChecker {

    @Override
    public boolean check(Context context, String permission) {
        try {
            if (Manifest.permission.RECORD_AUDIO.equals(permission)){
                return checkAudio();
            }else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)){
                return checkStorageWrite();
            }else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)){
                return checkStorageRead();
            }
        }catch (Throwable e){
            return false;
        }
        return false;
    }

    //测试录音
    private boolean checkAudio() throws Throwable{
        AudioTest audioChecker = new AudioTest();
        return audioChecker.test();
    }

    //测试写文件
    private boolean checkStorageWrite()throws Throwable {
        StorageWriteTest storageWriteTest = new StorageWriteTest();
        return storageWriteTest.test();
    }

    //测试读文件
    private boolean checkStorageRead()throws Throwable {
        StorageReadTest storageReadTest = new StorageReadTest();
        return storageReadTest.test();
    }
}
