package com.tal.imagepicker.ui.pop;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.tal.imagepicker.R;
import com.tal.imagepicker.model.DirPopModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cyy on 2016/7/7.
 * 文件夹列表
 */
public class DirPopView extends FrameLayout implements AdapterView.OnItemClickListener
 , View.OnClickListener{

    protected ListView listView;
    private FrameLayout mainLayout;
    private View coverView;
    private View placeView;

    private DirPopAdapter adapter ;
    private List<DirPopModel> dirItems = new ArrayList<>();

    ///////////////////////////////////////////////////////////////////////////
    private final static int DURING_TIME = 300;
    private AnimationSet listViewEnterSet , listViewExitSet;
    private AnimationSet coverEnterSet , coverExitSet;
    private boolean isShow = false;

    public DirPopView(Context context) {
        super(context);
        init();
    }


    public DirPopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DirPopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.dir_pop_view, this);
        initView();
        adapter = new DirPopAdapter(this.getContext() , dirItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    private void initView() {
        listView = (ListView)findViewById(R.id.list_view);
        mainLayout = (FrameLayout) findViewById(R.id.main_layout);
        mainLayout.setOnClickListener(this);

        coverView = findViewById(R.id.coverView);
        placeView = findViewById(R.id.place_view);
    }

    public void setListViewData(List<DirPopModel> models){
        dirItems.addAll(models);
        adapter.notifyDataSetChanged();
    }

    public void selectCountChanged(Map<String , Set<Integer>>  pickedDirMap){
        if (pickedDirMap!=null){
            for (DirPopModel model : dirItems){
                String dirName = model.getImageParentDirPath();
                if (pickedDirMap.containsKey(dirName)){
                    int num = pickedDirMap.get(dirName).size();
                    model.setSelectCount(String.valueOf(num));
                }else {
                    model.setSelectCount("0");
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (delegate!=null)delegate.onDirChanged(dirItems.get(position));
        dissmiss();
    }

    private OnDirChangedDelegate delegate;
    public void setOnDirChangedDelegate(OnDirChangedDelegate d){
        delegate =d;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_layout){
            dissmiss();
        }
    }

    private void enterAnim(){
        placeView.setVisibility(VISIBLE);
        //listView 动画
        if (listViewEnterSet == null){
            listViewEnterSet = new AnimationSet(true);
            TranslateAnimation translateAnimation = new TranslateAnimation
                    (Animation.RELATIVE_TO_SELF ,0,
                            Animation.RELATIVE_TO_SELF , 0,
                            Animation.RELATIVE_TO_SELF , -0.5f ,
                            Animation.RELATIVE_TO_SELF ,0f);
            translateAnimation.setDuration(DURING_TIME);
            listViewEnterSet.setInterpolator(new OvershootInterpolator());
            listViewEnterSet.addAnimation(translateAnimation);
        }
        listView.startAnimation(listViewEnterSet);

        //背景alpha 动画
        if (coverEnterSet == null){
            AlphaAnimation alphaAnimation = new AlphaAnimation(0 , 1);
            alphaAnimation.setDuration(DURING_TIME);
            coverEnterSet = new AnimationSet(true);
            coverEnterSet.setInterpolator(new DecelerateInterpolator());
            coverEnterSet.addAnimation(alphaAnimation);
        }
        coverView.startAnimation(coverEnterSet);
    }

    private void exitAnim(){
        if (listViewExitSet == null){
            listViewExitSet = new AnimationSet(true);
            TranslateAnimation translateAnimation = new TranslateAnimation
                    (Animation.RELATIVE_TO_SELF ,0,
                            Animation.RELATIVE_TO_SELF , 0,
                            Animation.RELATIVE_TO_SELF , 0f ,
                            Animation.RELATIVE_TO_SELF ,-1.0f);
            translateAnimation.setDuration(DURING_TIME);
            listViewExitSet.setInterpolator(new OvershootInterpolator());
            listViewExitSet.addAnimation(translateAnimation);
        }
        listView.startAnimation(listViewExitSet);
        listViewExitSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                placeView.setVisibility(GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                DirPopView.this.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //alpha
        if (coverExitSet == null){
            AlphaAnimation alphaAnimation = new AlphaAnimation(1 , 0);
            alphaAnimation.setDuration(DURING_TIME);
            coverExitSet = new AnimationSet(true);
            coverExitSet.setInterpolator(new AccelerateInterpolator());
            coverExitSet.addAnimation(alphaAnimation);
        }
        coverView.startAnimation(coverExitSet);
    }

    /**
     * show
     */
    public void show(){
        isShow = true;
        this.setVisibility(VISIBLE);
        enterAnim();
    }

    /**
     * dissmiss
     */
    public void dissmiss(){
        isShow = false;
        exitAnim();
    }

    public boolean isShow(){
        return isShow;
    }

    public interface OnDirChangedDelegate{
        void onDirChanged(DirPopModel model);
    }
}
