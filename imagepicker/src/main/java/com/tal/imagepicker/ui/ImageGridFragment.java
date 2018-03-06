package com.tal.imagepicker.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.tal.imagepicker.FileFilter;
import com.tal.imagepicker.NormalLoadImage;
import com.tal.imagepicker.PickerImage;
import com.tal.imagepicker.R;
import com.tal.imagepicker.model.DirPopModel;
import com.tal.imagepicker.model.ImageItem;
import com.tal.imagepicker.ui.pop.DirPopView;
import com.tal.imagepicker.ui.views.BottomView;
import com.tal.imagepicker.ui.views.NavigationBar;
import com.tal.imagepicker.ui.views.ToggleView;
import com.tal.imagepicker.utils.SearchImageHelper;
import com.tal.imagepicker.utils.ToastHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by shawn on 2017/12/29.
 *
 */

public class ImageGridFragment extends BaseFragment implements SearchImageHelper.CallBack ,
        GridAdapter.OnSelectedChagedDelegate,
        DirPopView.OnDirChangedDelegate ,Callback.BottomCallback, AdapterView.OnItemClickListener ,
        NavigationBar.OnActionListener {

    protected GridView mainGridView;
    protected DirPopView dirPopView;
    protected NavigationBar navigationBar;
    protected BottomView bottomLayout;

    private GridAdapter adapter;

    /**
     * 加载图片后的数据源
     *
     * todo 考虑交给Activity 来管理比较合适 这样就可以在预览和本页面共享数据源
     */
    private ArrayList<ImageItem> imagesItem;
    private List<ImageItem> tempList;

    /**
     * 所有包含图片的文件夹
     */
    protected List<DirPopModel> mImagesDir = new ArrayList<>();
    /**
     * grid view 当前显示的图片所在的文件夹  默认显示图片数量最多的
     */
    protected File currentImageDir;
    /**
     * currentImageDir 对应的数量
     */
    protected int imageCount;

    /**
     * 选择的图片的数量 切换文件夹后也是这个 这个不变
     ***/
    private Set<ImageItem> pickedImageItem = new HashSet<>(PickerImage.max); //选择的images
    /***key > 文件夹的绝对路径  value 选择的image在数组中的位置***/
    private HashMap<String, Set<Integer>> pickedDirMap;

    private Runnable completeRunnable = new Runnable() {
        @Override
        public void run() {
            //设置文件夹列表的数据
            date2DirPopView();
            date2View();
        }
    };

    private Handler handler = new Handler();

    private FragmentCallback mCallback;

    private void date2DirPopView() {
        dirPopView.setListViewData(mImagesDir);
        dirPopView.setOnDirChangedDelegate(this);
    }

    public static ImageGridFragment newInstance() {

        Bundle args = new Bundle();

        ImageGridFragment fragment = new ImageGridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback){
            this.mCallback = (FragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_grid, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navigationBar.setTitle("图片");
        navigationBar.setPickModel(mCallback.getPicModel());
        navigationBar.setmListener(this);
        bottomLayout.setCallback(this);
        bottomLayout.setPickModel(mCallback.getPicModel());

        dealSGridScroll();

        SearchImageHelper.newInstance(this).search(this.getContext());
    }

    /**
     * 处理grid view的滚动事件
     */
    private void dealSGridScroll() {
        mainGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    //加载图片
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initView(View rootView) {
        mainGridView = (GridView) rootView.findViewById(R.id.main_gridView);
        dirPopView = (DirPopView) rootView.findViewById(R.id.pop_container);
        navigationBar = (NavigationBar) rootView.findViewById(R.id.navigation_bar);
        bottomLayout = (BottomView) rootView.findViewById(R.id.bottom_layout);

        mainGridView.setOnItemClickListener(this);

    }

    @Override
    public void externalStorageError() {
        ToastHelper.show("外部 存状态不可用", this.getActivity());
    }

    @Override
    public void complete(List<DirPopModel> imagesDir, int max, File maxDir) {
        //加载完成后回调 默认把图片数量最多的一个文件夹设置为当前要显示图片的文件夹
        setCurrentImageDir(maxDir);
        imageCount = max;
        mImagesDir.addAll(imagesDir);
        handler.post(completeRunnable);
    }

    /**
     * 将数据放入临时的list
     *
     * @param currentDir File
     */
    private void setCurrentImageDir(File currentDir) {
        this.currentImageDir = currentDir;
        ///根据这个文件夹所包含的图片文件初始化model
        if (tempList == null) tempList = new ArrayList<>();
        tempList.clear();
        File[] files = currentImageDir.listFiles(new FileFilter());
        for (File file : files) {
            ImageItem imageItem = new ImageItem();
            imageItem.setImageFile(file);
            tempList.add(imageItem);
        }
        //排序
        Collections.sort(tempList);
    }

    private void date2View() {
        if (imagesItem == null) imagesItem = new ArrayList<>();
        imagesItem.clear();
        imagesItem.addAll(tempList);
        tempList.clear();
        //查数据 切换文件夹时的
        if (imagesItem.size() > 0 && pickedDirMap != null) {
            Set<Integer> obj = pickedDirMap.get(imagesItem.get(0).getParentDir());
            if (obj != null) {
                for (Integer i : obj) {
                    imagesItem.get(i).setSelect(true);
                }
            }
        }
        if (adapter == null) {
            adapter = new GridAdapter
                    (ImageGridFragment.this.getContext(), imagesItem, new NormalLoadImage() , mCallback.getPicModel());
            mainGridView.setAdapter(adapter);
            adapter.setPickDelegate(this);
        } else {
            adapter.notifyDataSetChanged();
        }
        //底部数字
//        bottomLayout.setImageCount(imageCount);
        //当前文件夹的名字
        String imgDir = currentImageDir.getAbsolutePath();
        String dirName = imgDir.substring(imgDir.lastIndexOf("/") + 1, imgDir.length());
        bottomLayout.setDirName(dirName);
    }

    public void previewPickValueChanged(ImageItem imageItem ,int pos, boolean select){
        imageItem.setSelect(select);
        adapter.notifyDataSetChanged();
        pickChanged(pos, select);
    }

    /**
     * 添加或者移除选择的图片的位置 注意：只是一个位置参数 这个位置是在数组@Link(imagesItem)中的位置
     * @param pos imagesItem  在这个数组中的位置
     * @param select 选择或者移除
     */
    public void pickChanged(Integer pos , boolean select){
        if (select){
            //selected
            pickedImageItem.add(imagesItem.get(pos));
        }else {
            //当前文件夹
            if (pickedImageItem.contains(imagesItem.get(pos))){
                removePickedImage(imagesItem.get(pos) , pos);
            }else{
                ///这个地方逻辑有点复杂
                //先确定当前文件夹有没有选择照片 还有可能切换后 虽然位置一样但是实例不一样 选择比较名字
                if (imagesItem.size() >0 && pickedDirMap!=null){
                    Set<Integer> obj = pickedDirMap.get(imagesItem.get(0).getParentDir());
                    if (obj!=null){//当前文件夹选择了照片
                        //遍历选择的image 确定是否有名字相同的 则标记为相同的image
                        ImageItem equipImageItem = null;
                        for (ImageItem imageItem : pickedImageItem){
                            if (imageItem.getImageName().equals(imagesItem.get(pos).getImageName())){
                                equipImageItem = imageItem;
                                break;
                            }
                        }
                        if (equipImageItem!=null){
                            removePickedImage(equipImageItem , pos);
                        }
                    }
                }
            }
        }
        //更新底部和上面的UI
        bottomLayout.setPreviewViewNum(pickedImageItem.size());
        if (pickedImageItem.size()==0){
            navigationBar.setSendText("发送");
        }else{
            navigationBar.setSendText("发送("+pickedImageItem.size()+")");
        }
    }

    private void removePickedImage(ImageItem imageItem , int pos){
        pickedImageItem.remove(imageItem);
        //移除map中的位置
        if (pickedDirMap !=null){
            String dirPath = imageItem.getParentDir();
            if (pickedDirMap.containsKey(dirPath)){
                Set<Integer> pickedPos = pickedDirMap.get(dirPath);
                if (pickedPos.contains(pos)){
                    pickedPos.remove(pos);
                }
            }
        }
    }

    @Override
    public void onPickedChanged(ToggleView toggleView, ImageItem imageItem, int position, boolean select) {
        if (select && pickedImageItem.size() >= PickerImage.max){
            Toast.makeText(this.getContext() , "最多选取"+PickerImage.max+"张" , Toast.LENGTH_SHORT).show();
            toggleView.select(false);
            return;
        }
        imageItem.setSelect(select);
        adapter.notifyDataSetChanged();
        pickChanged(position , select);
    }

    @Override
    public void onDirChanged(DirPopModel model) {
        if (pickedDirMap == null)pickedDirMap = new HashMap<>();
        for (ImageItem imageItem : pickedImageItem){
            if (!imagesItem.contains(imageItem))
                continue;

            String parentPath = imageItem.getParentDir();
            Set<Integer> obj = pickedDirMap.get(parentPath) == null ? new HashSet<Integer>():pickedDirMap.get(parentPath);
            obj.add(imagesItem.indexOf(imageItem));
            pickedDirMap.put(parentPath , obj);
        }
        //替换数据
        setCurrentImageDir(new File(model.getImagePath()).getParentFile());
        date2View();
    }

    @Override
    public void dirNameEvent() {
        if (dirPopView.isShow()){
            dirPopView.dissmiss();
        }else {
            dirPopView.show();
        }
    }

    @Override
    public void originalImage(boolean original) {

    }

    @Override
    public void previewImageEvent() {

    }

    @Override
    public void onTitleAction(View view) {}

    @Override
    public void onCompletedAction() {
        //点击导航栏的完成 多选会出现
        mCallback.pickCompleted(new ArrayList<>(pickedImageItem));
    }

    @Override
    public void onBackAction() {
        if (mCallback!=null)mCallback.fragmentBack();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCallback.getPicModel() == PickerImage.PICK_MODE_SINGLE){
            //单选模式 进入预览
            if (mCallback!=null)mCallback.preview(imagesItem , pickedImageItem ,  position);
        }else if (mCallback.getPicModel() == PickerImage.PICK_MODE_MULTIPLE){
            //多选模式
            if (mCallback!=null)mCallback.preview(imagesItem , pickedImageItem , position);
        }else if (mCallback.getPicModel() == PickerImage.PICK_MODE_CROP){
            //裁剪
            ImageItem imageItem = (ImageItem) parent.getItemAtPosition(position);
            if (mCallback!=null)mCallback.cropImage(imageItem);
        }
    }

    private ArrayList<ImageItem> getPickedListByPos(int pos){
        ArrayList<ImageItem> pickedList = new ArrayList<>();
        pickedList.add(imagesItem.get(pos));
        return pickedList;
    }

}
