package com.tal.imagepicker.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tal.imagepicker.R;

/**
 * Created by shawn on 2018/1/3.
 * <p>
 * 选择的View
 */

public class PickView extends LinearLayout {

    public interface OnValveChangedListener{
        void onValueChanged(View view , boolean isPick);
    }

    protected ToggleView toggleView;
    protected TextView pickTextView;

    private OnValveChangedListener listner;
    private boolean isPick = false;

    public PickView(Context context) {
        super(context);
        initView(null);
    }

    public PickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public PickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(@Nullable AttributeSet attrs) {

        String pickText = null;
        if (attrs!=null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs , R.styleable.PickView);
            pickText = typedArray.getString(R.styleable.PickView_pickText);
            typedArray.recycle();
        }

        LayoutInflater.from(getContext()).inflate(R.layout.layout_pick_view, this);
        toggleView = (ToggleView) findViewById(R.id.toggle_view);
        pickTextView = (TextView) findViewById(R.id.pickTextView);

        if (!TextUtils.isEmpty(pickText)){
            pickTextView.setText(pickText);
        }

        setGravity(Gravity.CENTER_VERTICAL);

        toggleView.setOnSelectedChangedListener(new ToggleView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(View v, boolean select) {
                isPick = select;
                if (listner!=null)listner.onValueChanged(PickView.this , isPick);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isPick = !isPick;
                toggleView.select(isPick , true);
                if (listner!=null)listner.onValueChanged(PickView.this , isPick);
            }
        });
    }

    public void setPickText(String text){
        pickTextView.setText(text);
    }
    public void setOnValveChangedListener(OnValveChangedListener l){
        this.listner = l;
    }

    public void select(boolean select , boolean anim){
        toggleView.select(select , anim);
        isPick = select;
    }
}
