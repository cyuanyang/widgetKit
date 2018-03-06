package com.tal.imagepicker.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tal.imagepicker.PickerImage;
import com.tal.imagepicker.R;


/**
 * Created by cyy on 2016/7/5.
 *
 * 导航栏
 */
public class NavigationBar extends LinearLayout implements View.OnClickListener{

    private TextView titleView;
    private ImageButton backBtn;
    private LinearLayout titleLayout;
    private Button completeBtn;

    public NavigationBar(Context context) {
        this(context , null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.navi_layout , this);

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        titleView = (TextView) findViewById(R.id.title_view);
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        completeBtn = (Button) findViewById(R.id.completeBtn);

        completeBtn.setOnClickListener(this);
        titleLayout.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setPickModel(int model){
        if (model == PickerImage.PICK_MODE_SINGLE){
            completeBtn.setVisibility(GONE);
        }
    }

    public void setSendText(String text){
        completeBtn.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_btn ){
            if (mListener!=null)mListener.onBackAction();
        }else if(v.getId() == R.id.title_layout){
            if (mListener !=null)mListener.onTitleAction(v);
        }else if (R.id.completeBtn == v.getId()){
            if (mListener != null )mListener.onCompletedAction();
        }
    }

    private OnActionListener mListener;
    public void setmListener(OnActionListener l){
        mListener = l;
    }
    public interface OnActionListener {
        void onTitleAction(View view);
        void onCompletedAction();
        void onBackAction();
    }
}
