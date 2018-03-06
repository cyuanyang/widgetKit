package com.tal.imagepicker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cyy on 2016/7/4.
 * 主要是对大图片处理成需要的小图片
 */
public class ImageUtils {

    /**
     * 压缩图片到指定规格
     *
     * 出入的图片内存大小小于 maxSize 则不会进行压缩处理
     *
     * 压缩是一个耗时的 需要在线程中操作
     *
     * @param path 图片的路径
     * @param max 图片的最大边
     * @param maxSize 压缩图片的最大大小
     */
    public static String compress(Context context ,String path ,String outFolder,int max , long maxSize){

        File  originImageFile = new File(path);

        //原图片本来就小于 指定的大小 则不需要在压缩处理 直接将原图地址返回即可
        if (originImageFile.length()<maxSize){
            return path;
        }

        //将图片加载到内存中
        Bitmap bitmap = decodeSampleImageFromUri(path , max , max);
        if (outFolder == null){
            throw new IllegalStateException("压缩的文件夹必须要传入");
        }
        File dir = new File(outFolder);
        File saveImageFile = createFile(dir , "temp_" , ".jpg");
        saveOutput(context , bitmap , saveImageFile , maxSize);
        bitmap.recycle();
        return saveImageFile.getAbsolutePath();
    }

    /**
     * 将图片保存在本地
     *
     * 保存本地会检测图片的大小 不符合会压缩质量
     */
    private static void saveOutput(Context context , Bitmap image,File saveFile , long maxSize) {
        OutputStream outputStream = null;
        int quality = 100;
        try {
            outputStream = context.getContentResolver().openOutputStream(Uri.fromFile(saveFile));
            if (outputStream != null) image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            //todo 图片压缩到指定大小
//            while ((saveFile.length() > maxSize || quality == 100) && quality > 30 ){
//                if (outputStream != null) image.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
//                quality = quality - 10;
//            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        try {
            File nomedia = new File(folder, ".nomedia");  //在当前文件夹底下创建一个 .nomedia 文件
            if (!nomedia.exists()) nomedia.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    public static Bitmap decodeSampleImageFromUri(String path , int reqWidth , int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path , options);
        options.inSampleSize = Math.round(calculateSampleImageSize(options , reqWidth , reqHeight));
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeFile(path , options);
    }

    /**
     * 大图到小图的尺寸
     * @return f
     */
    public static float calculateSampleImageSize(BitmapFactory.Options options , int reqWidth , int reqHeight){
        int width = options.outWidth ;
        int height = options.outHeight;
        float inSampleScale = 1;
        if (width > reqWidth && height > reqHeight){
            float widthRadio = Math.round(width*1.0f/reqHeight);
            float heightRadio = Math.round(height*1.0f/reqHeight);
            inSampleScale = Math.max(widthRadio , heightRadio);
        }
        return inSampleScale;
    }


}
