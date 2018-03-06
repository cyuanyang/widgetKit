package com.tal.imagepicker.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tal.imagepicker.PickerImage;
import com.tal.imagepicker.R;
import com.tal.imagepicker.model.ImageItem;
import com.tal.imagepicker.ui.views.CropImageView;
import com.tal.imagepicker.ui.views.NavigationBar;
import com.tal.imagepicker.utils.BitmapUtil;
import com.tal.imagepicker.utils.ScreenUtils;

import java.io.File;

/**
 * Created by shawn on 2018/1/3.
 * <p>
 * 裁剪 fragment
 */

public class CropImageFragment extends BaseFragment implements NavigationBar.OnActionListener,
        CropImageView.OnBitmapSaveCompleteListener {

    private NavigationBar navigationBar;
    private CropImageView cropImageView;

    private FragmentCallback mCallback;
    private ImageItem imageItem;
    private Bitmap mBitmap;

    public static CropImageFragment newInstance(ImageItem imageItem) {

        Bundle args = new Bundle();
        args.putSerializable("imageItem", imageItem);
        CropImageFragment fragment = new CropImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback){
            mCallback = (FragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageItem = (ImageItem) getArguments().getSerializable("imageItem");
        }

        //初始化缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageItem.getImagePath(), options);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imageItem.getImagePath(), options);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crop_image, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cropImageView.setFocusStyle(CropImageView.Style.RECTANGLE);
        int width = ScreenUtils.getSceenSize(getContext()).width;
        cropImageView.setFocusWidth((int) (width*0.6));
        cropImageView.setFocusHeight((int) (width*0.6));

        cropImageView.setImageBitmap(cropImageView.rotate(mBitmap, BitmapUtil.getBitmapDegree(imageItem.getImagePath())));
        cropImageView.setOnBitmapSaveCompleteListener(this);

        navigationBar.setmListener(this);
        navigationBar.setSendText("使用");
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth;
            } else {
                inSampleSize = height / reqHeight;
            }
        }
        return inSampleSize;
    }


    private void initView(View rootView) {
        cropImageView = (CropImageView) rootView.findViewById(R.id.cropImageView);
        navigationBar = (NavigationBar) rootView.findViewById(R.id.navigation_bar);
    }

    @Override
    public void onTitleAction(View view) {}

    @Override
    public void onCompletedAction() {
        File desFile = null;
        if (PickerImage.saveCropImageFolder == null){
            desFile = getActivity().getExternalCacheDir();
        }
        cropImageView.saveBitmapToFile(desFile , PickerImage.cropW, PickerImage.cropH ,true);
    }

    @Override
    public void onBackAction() {
        if (mCallback!=null)
            mCallback.fragmentBack();
    }

    @Override
    public void onBitmapSaveSuccess(File file) {
        if (file!=null){
            imageItem.setCropImagePath(file.getAbsolutePath());
            if (mCallback!=null)mCallback.cropImageResult(imageItem);
        }else {
            if (mCallback!=null)mCallback.cropImageResult(null);
        }
    }

    @Override
    public void onBitmapSaveError(File file) {
        if (mCallback!=null)mCallback.cropImageResult(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cropImageView.setOnBitmapSaveCompleteListener(null);
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
