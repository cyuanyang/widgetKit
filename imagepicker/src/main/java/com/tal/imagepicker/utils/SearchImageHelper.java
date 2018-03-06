package com.tal.imagepicker.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tal.imagepicker.Attach;
import com.tal.imagepicker.FileFilter;
import com.tal.imagepicker.model.DirPopModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cyy on 2016/7/4.
 * 加载外部设备的图片
 */
public class SearchImageHelper {

    /**   存放图片所在的文件夹   */
    protected List<DirPopModel> mImagesDir = new ArrayList<>();

    protected File currentImageDir;
    protected int imageCount;

    private CallBack mCallBack;

    public static SearchImageHelper newInstance(@NonNull CallBack callBack){
        return  new SearchImageHelper(callBack);
    }

    public SearchImageHelper(CallBack callBack){
        this.mCallBack = callBack;
    }

    public void search(final Context context){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
            mCallBack.externalStorageError();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                ContentResolver cr = context.getContentResolver();
                Cursor cursor = cr.query(mUri ,
                        null ,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/png" , "image/jpeg"} ,
                        MediaStore.Images.Media.DATE_MODIFIED);

                if (cursor!=null){
                    if (Attach.DEBUG) Log.e("共有图片" , cursor.getCount()+"");

                    Set<String> tempDir = new HashSet<>();
                    while (cursor.moveToNext()){
                        //图片的路径
                        String path = cursor.getString
                                (cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                        File parentFile = new File(path).getParentFile();

                        if (parentFile == null)continue;

                        String imgDir = parentFile.getAbsolutePath();
                        if (tempDir.contains(imgDir)){
                            continue;
                        }else {
                            tempDir.add(imgDir);
                        }
                        String[] imageChildren = parentFile.list(new FileFilter());
                        //没有子文件
                        if (imageChildren == null){
                            continue;
                        }
                        int fileCount  = imageChildren.length;
                        if (imageCount<fileCount){
                            imageCount = fileCount;
                            currentImageDir = parentFile;
                        }
                        //构建DirPopModel类
                        if (imageChildren.length > 0){
                            String firstImagePath = imgDir+"/"+imageChildren[0];
                            String dirName = imgDir.substring(imgDir.lastIndexOf("/")+1, imgDir.length());
                            mImagesDir.add(genDirPopModel(firstImagePath , dirName , fileCount));
                        }
                    }

                    cursor.close();

                    mCallBack.complete(mImagesDir , imageCount , currentImageDir);
                }

            }
        }).start();
    }

    private DirPopModel genDirPopModel(String path , String dirName , int count){
        DirPopModel model = new DirPopModel();
        model.setImagePath(path);
        model.setDirName(dirName);
        model.setImageCount(count+"");

        return model;
    }

    public interface CallBack{
        /**
         * 运行时在主线程
         */
        void externalStorageError();

        /**
         * 运行时没有在主线程
         */
        void complete(List<DirPopModel> imagesDir, int max, File maxDir);
    }

}
