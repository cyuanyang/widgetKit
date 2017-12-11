package com.xstudy.pagerindicator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by study on 17/11/28.
 *
 *
 */

public class PagerIndicator extends HorizontalScrollView{

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TAG = "PageIndicator";

    public interface OnItemClickListener{
        void onClick(PagerIndicator layout, View view, int index);
    }

    private int itemMargin;
    private int titleSize;
    private int titleColor;
    private int titleSelectedColor;
    private int indicateHeight;
    private @DrawableRes int indicatorDrawable;

    private List<ViewBean> viewBeans = new ArrayList<>();
    private ContentLayout contentLayout; //滑动的内容View
    private LinearLayout contentView; // title 的content view

    private OnItemClickListener mListener;
    private ViewPager viewPager;

    private ViewBean currentViewBean;
    private View indicateView;

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        itemMargin = (int) dp2px(50);
        titleSize = 16;
        titleColor = Color.parseColor("#999999");
        titleSelectedColor = Color.BLACK;
        indicateHeight = (int) dp2px(3);
        indicatorDrawable = R.drawable.indicator_drawable;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 0){
            throw new IllegalStateException("不允许添加view");
        }

        contentLayout = new ContentLayout(getContext());
        addView(contentLayout);
    }

    private void initTextView(){
        if (contentView == null){
            contentView = new LinearLayout(getContext());
            contentLayout.addView(contentView);
        }
        if (contentView.getChildCount() != 0){
            contentView.removeAllViews();
        }

        for (int i = 0; i < viewBeans.size(); i++) {
            ViewBean bean = viewBeans.get(i);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT ,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            if (i < viewBeans.size()-1){
                lp.rightMargin = itemMargin;
            }
            contentView.addView(bean.view , lp);
        }
    }


    private void initIndicateView(){
        indicateView = new View(getContext());
        indicateView.setBackgroundResource(indicatorDrawable);
        LayoutParams lp = new LayoutParams((int) dp2px(15),
                indicateHeight);
        lp.gravity = Gravity.BOTTOM;
        contentLayout.addView(indicateView , lp);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //布局指示View的位置
        if (currentViewBean!=null){
            View view = currentViewBean.view;
            int left = view.getLeft();
            int right = view.getRight();

            int newLeft = (left+right - indicateView.getMeasuredWidth())/2;
            int newRight = newLeft+indicateView.getMeasuredWidth();
            indicateView.layout(newLeft , indicateView.getTop() ,newRight , indicateView.getBottom());
        }
    }

    private void select(int index){
        if (currentViewBean!=null){
            if (index == currentViewBean.index){
                return;
            }
            currentViewBean.isSelected = false;
            currentViewBean.view.setTextColor(titleColor);

        }

        currentViewBean = viewBeans.get(index);
        currentViewBean.isSelected = true;
        //切换viewpager
        viewPager.setCurrentItem(currentViewBean.index);
        //切换title
        currentViewBean.view.setTextColor(titleSelectedColor);

        //指示view 切换
        indicateView.requestLayout();
    }

    public void setListener(OnItemClickListener l) {
        this.mListener = l;
    }

    public void setViewPager(ViewPager viewPager){
        if (viewPager==null){
            throw new NullPointerException("viewpager = null");
        }
        this.viewPager = viewPager;
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter!=null){
            for (int i = 0; i < adapter.getCount(); i++) {
                CharSequence text = adapter.getPageTitle(i);
                ViewBean viewBean = new ViewBean();
                viewBean.index = i;
                viewBean.setView(getTitleView(text));
                viewBeans.add(viewBean);
            }
        }

        viewPager.addOnPageChangeListener(new PageChangedListener());

        initTextView();
        initIndicateView();
        select(viewPager.getCurrentItem());
    }

    private TextView getTitleView(CharSequence t){
        TextView textView = new TextView(getContext());
        textView.setText(t);
        textView.setTextSize(titleSize);
        textView.setTextColor(titleColor);
        textView.setOnClickListener(listener);
        textView.setGravity(Gravity.CENTER);
        if (DEBUG){
            textView.setBackgroundColor(Color.RED);
        }

        return textView;
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewBean viewBean = (ViewBean) v.getTag();

            select(viewBean.index);

            if (mListener!=null){
                mListener.onClick(PagerIndicator.this , v , viewBean.index);
            }
        }
    };

    private float dp2px(int px){
        Context context = getContext();
        Resources r;
        if (context == null){
            r = Resources.getSystem();
        }else {
            r = context.getResources();
        }

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP ,px , r.getDisplayMetrics());
    }

    class PageChangedListener extends ViewPager.SimpleOnPageChangeListener{

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            select(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);


        }
    }

    class ViewBean{
        private TextView view;
        int index;
        boolean isSelected;

        public void setView(TextView view) {
            this.view = view;
            view.setTag(this);
        }

        public TextView getView() {
            return view;
        }
    }
}
