package com.cyy.widgetkit.tools;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created by huanghua on 16/5/4.
 *
 */
public class FileUtils {

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 将content写到文件中
     *
     * @param dirPath  文件路径
     * @param fileName 文件名
     * @param content
     * @param append   是否追加写文件
     */
    public static void write(String dirPath, String fileName, String content, boolean append) {
        BufferedWriter out = null;
        try {
            File file = new File(dirPath, fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
//                file.mkdirs();
                file.createNewFile();
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append)));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isExist(String fileName) {
        File file = new File(fileName);
        if (file != null && file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 读取文件里面的内容
     *
     * @param file
     * @return
     */
    public static String read(File file) {
        String result = "";
        if (!file.exists()) {
            return result;
        }
        BufferedReader reader = null;
        try {
            String line = "";
            reader = new BufferedReader(new FileReader(file));
            while (!TextUtils.isEmpty((line = reader.readLine()))) {
                result = result + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static File createFile(String dirPath, String fileName){
        File file = new File(dirPath, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if(file.exists()){
            file.delete();
        }
//        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
        return file;
    }

    public static String getSdCardPath(Context context) {
        File path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = context.getCacheDir();
        }
        File newPath = new File(path, "crazy_teacher_video_qiniu");
        if (!newPath.exists()) {
            newPath.mkdir();
        }
        return newPath.toString();
    }

    ///内存卡的剩余容量
    ////单位 B  -1没有内存卡
    public static long availableSDCardCapacity() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            if (Build.VERSION.SDK_INT >= 18){
                return sf.getFreeBytes();
            }else {
                return sf.getBlockSize()*sf.getAvailableBlocks();
            }
        }else {
            return -1;
        }
    }

    ///格式化一个大小 最终返回整数 舍弃小数部分
    public static String formattorSizeInt(long size){
        String suffix = "B";
        if (size>1023){
            size = size/1024;
            suffix = "K";
        }
        if (size>1023){
            size = size/1024;
            suffix = "M";
        }
        if (size>1023){
            size = size/1024;
            suffix = "G";
        }
        if (size>1023){
            size = size/1024;
            suffix = "T";
        }
        return size+suffix;
    }

    public static String availableSDCardCapacityFormate(Context context){
        long capacity = availableSDCardCapacity();
        if (capacity >= 0){
            if (capacity<1024){
                return "可用内存0M";
            }else {
                String capacityStr = Formatter.formatFileSize(context , capacity);
//                capacityStr = capacityStr.replace(" " ,"");
//                capacityStr = capacityStr.replace("GB" ,"G");
//                capacityStr = capacityStr.replace("MB" ,"M");
//                capacityStr = capacityStr.replace("KB","K");
                return "可用内存"+capacityStr;
            }
        }else {
            return "外部存储设备不可用";
        }
    }
}
