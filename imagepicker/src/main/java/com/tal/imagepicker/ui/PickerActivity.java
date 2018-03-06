package com.tal.imagepicker.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tal.imagepicker.PickerImage;
import com.tal.imagepicker.R;
import com.tal.imagepicker.model.ImageItem;
import com.tal.imagepicker.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by shawn on 2017/12/29.
 * <p>
 * <p>
 * 1。图片的加载放在了 ImageGridFragment 当中 ，主要将图片加载到数组当中
 * <p>
 * 2。预览的图片的的fragment数据源是从 ImageGridFragment 传入的
 * <p>
 * 3。多选时，已经选择的照片也被传入到预览fragment中，根据这些图片确定预览的UI
 * <p>
 * 4。预览是，改变图片的选择状态时，需要同步 ImageGridFragment 中的选择图片的数据
 */

public class PickerActivity extends FragmentActivity implements FragmentCallback {

    public final static String FILTER_ORIGINAL = "com.tal.imagepicker.FILTER_ORIGINAL";

    private static int model;
    protected View statusBarPlaceView;

    private List<BaseFragment> fragments = new ArrayList<>();

    private ImageGridFragment imageGridFragment;
    private InnerHandler handler = new InnerHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_picker);
        initView();
        model = PickerImage.model;

        FragmentManager fragmentManager = getSupportFragmentManager();
        imageGridFragment = ImageGridFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, imageGridFragment)
                .commitAllowingStateLoss();

        fragments.add(imageGridFragment);

        if (PickerImage.translucent) {
            fullScreen();
        }else {
            statusBarPlaceView.setVisibility(View.GONE);
        }

    }

    private void fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    @Override
    public void pickCompleted(final ArrayList<ImageItem> imageItems) {
        if (!PickerImage.isOriginal) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.obtainMessage(1).sendToTarget();
                    File file = PickerActivity.this.getExternalCacheDir();
                    //压缩处理
                    for (ImageItem i : imageItems) {
                        String compressPath = ImageUtils.compress(
                                PickerActivity.this,
                                i.getImagePath(),
                                file.getAbsolutePath(),
                                1280,
                                200 * 1024);

                        i.setCompressImagePath(compressPath);
                    }
                    handler.obtainMessage(2, imageItems).sendToTarget();
                }
            }).start();
        } else {
            handler.obtainMessage(2, imageItems).sendToTarget();
        }
    }


    @Override
    public void preview(ArrayList<ImageItem> imageItems, Set<ImageItem> pickItems, int currentPosition) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PreviewImageFragment previewImageFragment = PreviewImageFragment.newInstance(imageItems, new ArrayList<>(pickItems), currentPosition);
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, previewImageFragment)
                .hide(getTopFragment())
                .show(previewImageFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
        fragments.add(previewImageFragment);
    }

    @Override
    public void cropImage(ImageItem imageItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CropImageFragment cropImageFragment = CropImageFragment.newInstance(imageItem);
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, cropImageFragment)
                .hide(getTopFragment())
                .show(cropImageFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
        fragments.add(cropImageFragment);
    }

    @Override
    public void cropImageResult(ImageItem imageItem) {
        ArrayList<ImageItem> imageItems = new ArrayList<>(1);
        imageItems.add(imageItem);
        Intent intent = new Intent();
        intent.putExtra("data", imageItems);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void fragmentBack() {
        onBackPressed();
    }

    @Override
    public void previewPickChanged(ImageItem imageItem, int pos, boolean select) {
        imageGridFragment.previewPickValueChanged(imageItem, pos, select);
    }

    @Override
    public int getPicModel() {
        return model;
    }

    private BaseFragment getTopFragment() {
        return fragments.get(fragments.size() - 1);
    }

    @Override
    public void onBackPressed() {
        if (getTopFragment() == null || !getTopFragment().onBackPressed()) {
            if (fragments.size() <= 1) {
                super.onBackPressed();
            } else {
                BaseFragment top = getTopFragment();
                fragments.remove(top);
                getSupportFragmentManager().beginTransaction()
                        .remove(top)
                        .show(getTopFragment())
                        .commitAllowingStateLoss();
            }
        }
    }

    private void initView() {
        statusBarPlaceView = (View) findViewById(R.id.statusBarPlaceView);
    }

    private class InnerHandler extends Handler {

//        static int SHOW_DIALOG = 1;
//        static int COMPLETE = 2;

        ProgressDialog dialog;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                //完成
                ArrayList<ImageItem> imageItems = (ArrayList<ImageItem>) msg.obj;
                Intent intent = new Intent();
                intent.putExtra("data", imageItems);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                dialog = ProgressDialog.show(PickerActivity.this, "", "");
            }
        }
    }
}
