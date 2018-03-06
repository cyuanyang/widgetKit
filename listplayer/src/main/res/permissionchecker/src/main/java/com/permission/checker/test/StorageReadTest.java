package com.permission.checker.test;

import android.os.Environment;

import java.io.File;

class StorageReadTest implements IPermissionTest {

    StorageReadTest() {

    }

    @Override
    public boolean test() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (directory.exists() && directory.canRead()) {
            long modified = directory.lastModified();
            String[] pathList = directory.list();
            return modified > 0 && pathList != null;
        }
        return false;
    }
}