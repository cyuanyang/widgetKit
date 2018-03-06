package com.tal.imagepicker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tal.imagepicker.Attach;
import com.tal.imagepicker.PickerImage;
import com.tal.imagepicker.R;
import com.tal.imagepicker.model.ImageItem;
import com.tal.imagepicker.ui.views.PickView;
import com.tal.imagepicker.ui.views.ToggleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by shawn on 2018/1/2.
 *
 *
 */

public class PreviewImageFragment extends BaseFragment implements
        SinglePreviewImageFragment.ImageEventListener , View.OnClickListener{

    private final static String CURRENT_PREVIEW_POSITION = "PreviewImageActivity.CURRENT_PREVIEW_POSITION";
    private final static String PREVIEW_IMAGES = "PreviewImageActivity.PREVIEW_IMAGES";
    private final static String PREVIEW_SELECTED_IMAGES = "PreviewImageActivity.PREVIEW_SELECTED_IMAGES";

    public final static String PREVIEW_IMAGE_URL = "PreviewImageFragment.PREVIEW_IMAGE_URL";

    public static PreviewImageFragment newInstance(ArrayList<ImageItem> imageItems,
                                                   ArrayList<ImageItem> pickItems ,int currentPosition) {

        Bundle bundle = new Bundle();
        //将数据源传入
        bundle.putSerializable(PREVIEW_IMAGES, imageItems);
        //当前选择的位置
        bundle.putInt(CURRENT_PREVIEW_POSITION, currentPosition);
        //将选择的图片传入
        bundle.putSerializable(PREVIEW_SELECTED_IMAGES , pickItems);

        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    protected RelativeLayout navLayout;
    protected PickView originalCheckBox;
    protected LinearLayout bottomLayout;

    private FrameLayout previewContainer;
    private ImageButton backBtn;
    private TextView positionView;
    private Button completeAction;
    private PickView selectPickView;
    private ViewPager viewPager;

    private boolean isShowInfoView = true;
    private int currentPosition; //当前的位置
    private List<ImageItem> previewImages; //所有预览的图片
    private List<ImageItem> pickItems; //选择的照片
    private FragmentCallback mCallback;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPick = intent.getBooleanExtra("data" , false);
            originalCheckBox.select(isPick , false);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter(PickerActivity.FILTER_ORIGINAL);
        context.registerReceiver(mReceiver , filter);
        if (context instanceof FragmentCallback){
            mCallback = (FragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        if (getContext()!=null)
            getContext().unregisterReceiver(mReceiver);
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            previewImages = (List<ImageItem>) getArguments().getSerializable(PREVIEW_IMAGES);
            pickItems = (List<ImageItem>) getArguments().getSerializable(PREVIEW_SELECTED_IMAGES);
            currentPosition = getArguments().getInt(CURRENT_PREVIEW_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preview_image, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), previewImages , this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(simpleOnPageChangeListener);

        completeAction.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        showInfoView();

        //title
        positionView.setText((1+currentPosition) +"/"+(1+previewImages.size()));

        //对业务的不同处理
        if (mCallback.getPicModel() == PickerImage.PICK_MODE_SINGLE){
            selectPickView.setVisibility(View.GONE);
        }else if (mCallback.getPicModel() == PickerImage.PICK_MODE_MULTIPLE){
            completeAction.setVisibility(View.GONE);
            //view pager 第一次加载不会调用page select 方法 这里主动调一次
            pageSelected(currentPosition);

            //pick 事件
            selectPickView.setOnValveChangedListener(new PickView.OnValveChangedListener() {
                @Override
                public void onValueChanged(View view, boolean isPick) {
                    dealPreviewPickChangedEvent(isPick);
                }
            });
        }

        originalCheckBox.select(PickerImage.isOriginal , false);
        //原图处理
        originalCheckBox.setOnValveChangedListener(new PickView.OnValveChangedListener() {
            @Override
            public void onValueChanged(View view, boolean isPick) {
                PickerImage.isOriginal = isPick;
                if (getContext()!=null){
                    getContext().sendBroadcast(new Intent(PickerActivity.FILTER_ORIGINAL).putExtra("data" , isPick));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (R.id.complete_action==v.getId()){
            //完成
            if (currentPosition<previewImages.size() && mCallback!=null){
                ArrayList<ImageItem> result = new ArrayList<>(1);
                result.add(previewImages.get(currentPosition));
                mCallback.pickCompleted(result);
            }
        }else if (R.id.back_btn == v.getId()){
            //返回
            if (mCallback!=null)
                mCallback.fragmentBack();
        }
    }

    /**
     * 预览的时候 点击改变了是否选择图片
     *
     * 1。要处理本数据源选择图片的情况
     * 2。要处理 ImageGridFragment 中数据源的情况
     *
     * @param isPick true 选择  false 取消选择
     */
    private void dealPreviewPickChangedEvent(boolean isPick){
        //1.....
        ImageItem imageItem = previewImages.get(currentPosition);
        imageItem.setSelect(isPick);

        //2.....
        if (mCallback!=null)mCallback.previewPickChanged(imageItem ,currentPosition, isPick);
    }

    /**
     * 隐藏信息的View
     */
    private void hideInfoView() {
        navLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        isShowInfoView = false;
    }

    private void showInfoView(){
        navLayout.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
        isShowInfoView = true;
    }

    private void initView(View rootView) {
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        previewContainer = (FrameLayout) rootView.findViewById(R.id.preview_container);
        backBtn = (ImageButton) rootView.findViewById(R.id.back_btn);
        positionView = (TextView) rootView.findViewById(R.id.position_view);
        completeAction = (Button) rootView.findViewById(R.id.complete_action);
        selectPickView = (PickView) rootView.findViewById(R.id.selectPickView);
        navLayout = (RelativeLayout) rootView.findViewById(R.id.navLayout);
        originalCheckBox = (PickView) rootView.findViewById(R.id.originalCheckBox);
        bottomLayout = (LinearLayout) rootView.findViewById(R.id.bottomLayout);
    }

    private void pageSelected(int position){
        //更新底部的是否选择的UI
        ImageItem imageItem = previewImages.get(position);
        selectPickView.select(imageItem.isSelect() , false);
    }

    private ViewPager.SimpleOnPageChangeListener simpleOnPageChangeListener =
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    currentPosition = position;
                    positionView.setText((1+currentPosition) +"/"+(1+previewImages.size()));
                    pageSelected(position);
                }
            };

    @Override
    public void onViewTap(View view) {
        if (isShowInfoView){
            hideInfoView();
        }else
            showInfoView();
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private List<ImageItem> previewImages;
        private SinglePreviewImageFragment.ImageEventListener eventListener;

        public PagerAdapter(FragmentManager fm, List<ImageItem> previewImages ,
                            SinglePreviewImageFragment.ImageEventListener eventListener) {
            super(fm);
            this.previewImages = previewImages;
            this.eventListener = eventListener;
        }

        @Override
        public Fragment getItem(int position) {
            SinglePreviewImageFragment fragment = new SinglePreviewImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(PREVIEW_IMAGE_URL, previewImages.get(position).getImagePath());
            fragment.setArguments(bundle);
            fragment.setImageEventListener(eventListener);
            return fragment;
        }

        @Override
        public int getCount() {
            return previewImages.size();
        }
    }

}
