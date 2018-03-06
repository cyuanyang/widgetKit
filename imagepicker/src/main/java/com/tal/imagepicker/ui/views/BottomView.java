package com.tal.imagepicker.ui.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tal.imagepicker.PickerImage;
import com.tal.imagepicker.R;
import com.tal.imagepicker.ui.Callback;
import com.tal.imagepicker.ui.PickerActivity;


/**
 * Created by cyy on 2016/7/5.
 *
 */
public class BottomView extends RelativeLayout implements View.OnClickListener,PickView.OnValveChangedListener {

    private TextView dirNameView;
    private TextView previewView;
    private PickView originalCheckBox;

    private Callback.BottomCallback callback;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPick = intent.getBooleanExtra("data" , false);
            originalCheckBox.select(isPick , false);
        }
    };

    public BottomView(Context context) {
        this(context, null);
    }

    public BottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        previewView.setEnabled(false);
    }

    @Override
    protected void onAttachedToWindow() {
        IntentFilter filter = new IntentFilter(PickerActivity.FILTER_ORIGINAL);
        getContext().registerReceiver(mReceiver,filter);

        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(mReceiver);
    }

    private void initView() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.bottom_layout, this);
        dirNameView = (TextView) findViewById(R.id.dirNameView);
        previewView = (TextView) findViewById(R.id.previewImageView);
        originalCheckBox = (PickView) findViewById(R.id.originalCheckBox);

        dirNameView.setOnClickListener(this);
        previewView.setOnClickListener(this);
        originalCheckBox.setOnValveChangedListener(this);
    }

    public void setPickModel(int model){
        if (model == PickerImage.PICK_MODE_SINGLE){
            previewView.setVisibility(GONE);
        }
        if (model == PickerImage.PICK_MODE_CROP){
            originalCheckBox.setVisibility(GONE);
            previewView.setVisibility(GONE);
        }
    }

    public void setCallback(Callback.BottomCallback callback) {
        this.callback = callback;
    }

    /**
     * 设置目录名字
     * @param dirName ""
     */
    public void setDirName(String dirName){
        dirNameView.setText(dirName);
    }

    /**
     * 设置预览的文字数量
     * @param num 数量
     */
    public void setPreviewViewNum(int num){
        if (num <= 0){
            previewView.setEnabled(false);
            previewView.setText("预览");
        }else {
            previewView.setEnabled(true);
            previewView.setText("预览("+num+")");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dirNameView){
            if (callback!=null)callback.dirNameEvent();
        }else if (v.getId() == R.id.originalCheckBox){
            if (callback!=null)callback.originalImage(true);
        }else if (v.getId() == R.id.previewImageView){
            if (callback!=null)callback.previewImageEvent();
        }
    }

    @Override
    public void onValueChanged(View view, boolean isPick) {
        PickerImage.isOriginal = isPick;
        if (getContext()!=null){
            getContext().sendBroadcast(new Intent(PickerActivity.FILTER_ORIGINAL).putExtra("data" , isPick));
        }
    }
}
