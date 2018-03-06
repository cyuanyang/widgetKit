package com.tal.imagepicker.ui;

import com.tal.imagepicker.model.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by shawn on 2018/1/2.
 *
 * fragment 和 activity 交互用的接口
 */

public interface FragmentCallback extends BaseFragment.BaseFragmentCallback{

    void preview(ArrayList<ImageItem> imageItems , Set<ImageItem> pickItems , int currentPosition);  //预览

    void cropImage(ImageItem imageItem); //裁剪图片

    void cropImageResult(ImageItem imageItem); //裁剪的结果 若果imageItem=null 裁剪失败

    void pickCompleted(ArrayList<ImageItem> imageItems); //选择完成

    void fragmentBack(); //返回

    int getPicModel();

    void previewPickChanged(ImageItem imageItem ,int pos, boolean select);
}
