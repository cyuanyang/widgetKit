package com.permission.checker.test;

import android.os.Environment;

import java.io.File;

class StorageWriteTest implements IPermissionTest {

    StorageWriteTest() {
    }

    @Override
    public boolean test() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (!directory.exists() || !directory.canWrite()) return false;
        File file = new File(directory, "ANDROID.PERMISSION.TEST");
        if (file.exists()) {
            return file.delete();
        } else {
            return file.createNewFile();
        }
    }
}
