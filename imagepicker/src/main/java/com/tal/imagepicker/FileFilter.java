package com.tal.imagepicker;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by cyy on 2016/7/5.
 *
 */
public class FileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String filename) {
        if (filename.endsWith(".png")
                        || filename.endsWith(".jpg")
                        || filename.endsWith(".jpeg")){
            if(filename.startsWith(".")){
                //去掉隐藏文件
                return false;
            }
            return true;
        }
        return false;
    }
}
