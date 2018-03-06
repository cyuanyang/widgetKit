package com.tal.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tal.imagepicker.model.ImageItem;
import com.tal.imagepicker.ui.PickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyy on 2016/7/5.
 *
 * todo 改成单利模式
 */
public class PickerImage {

    //单选
    public final static int PICK_MODE_SINGLE = 0x0001;

    //多选
    public final static int PICK_MODE_MULTIPLE = 0x0002;

    public final static int PICK_MODE_CROP = 0x0003;

    private final static int REQUEST_CODE = 0x0001;

    //选择的配置
    public static int model = PICK_MODE_SINGLE;
    public static int max = 9; //选择的最大照片数
    public static int cropW = 500, cropH = 500;
    public static boolean isOriginal; //是否原图 这个变量是全局的 选择一个地方原图，意味着所有的图片全是原图

    public static boolean translucent = true; // 沉浸式处理

    //保存截图的附文件夹 默认保存在 ../Android/data/(packagename)/cache/下面
    public static File saveCropImageFolder;
    //非原图 压缩图片保存的文件夹  默认保存在 ../Android/data/(packagename)/cache/下面
    public static File compressImageFolder;

    private PickedCompleteListener pickedCompleteListener;

    private PickerImage(int model , int max , File saveCropImageFolder,int cropW ,int cropH,
                        PickedCompleteListener pickedCompleteListener){
        PickerImage.model = model;
        PickerImage.cropH = cropH;
        PickerImage.cropW = cropW;
        if (max>0){
            PickerImage.max = max;
        }
        PickerImage.saveCropImageFolder = saveCropImageFolder;
        this.pickedCompleteListener = pickedCompleteListener;
    }

    /**
     * 进入选择
     */
    public void pick(Context context){
        if (context instanceof Activity){
            Intent intent = new Intent(context , PickerActivity.class);
            ((Activity) context).startActivityForResult(intent , REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                ArrayList<ImageItem> result = (ArrayList<ImageItem>) data.getSerializableExtra("data");
                if (pickedCompleteListener!=null){
                    pickedCompleteListener.picked(result , model , PickerImage.isOriginal);
                }
            }else {
                if (pickedCompleteListener!=null){
                    pickedCompleteListener.cancel();
                }
            }
        }
    }

    public static class Builder{

        int model;
        int max;
        File saveCropImagePath; //保存截图图片的路径
        PickedCompleteListener pickedCompleteListener;

        int cropW,cropH; //剪切的宽和高

        /**
         * 设置选择的类型
         * @param model PICK_MODE_SINGLE
         * @return ""
         */
        public Builder setModel(int model){
            this.model = model;
            return this;
        }

        /**
         * 设置多选的最大个数
         * 在多选的模式下才能生效
         */
        public Builder setMax(int max){
            this.max = max;
            return this;
        }

        public Builder setPickedCompleteListener(PickedCompleteListener l){
            this.pickedCompleteListener = l;
            return this;
        }

        public Builder setSaveCropImagePath(File saveCropImagePath) {
            this.saveCropImagePath = saveCropImagePath;
            return this;
        }

        public Builder setCropH(int cropH) {
            this.cropH = cropH;
            return this;
        }

        public Builder setCropW(int cropW) {
            this.cropW = cropW;
            return this;
        }

        public PickerImage build(){
            return new PickerImage(this.model ,
                    this.max,
                    saveCropImageFolder,
                    cropW ,
                    cropH,
                    this.pickedCompleteListener);
        }
    }

    public interface PickedCompleteListener{
        void picked(List<ImageItem> pickedItems , int model , boolean isOrigin);
        void cancel();
    }

}
