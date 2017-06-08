package com.cyy.sound.Sound;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cyy.sound.R;
import com.cyy.sound.tools.DisplayUtils;

/**
 * Created by study on 17/4/19.
 *
 * 声音提示的window
 */

class SoundPopWindow extends PopupWindow {

    private TextView mMsgView;
    private ImageView soundIconView; //
    private ImageView soundVolumeView; //音量大小的View

    private int status ; //切换录音的状态 0:正常 1:释放 2:太短

    //切换状态的ICON
    private int[] resIcon = {
            R.drawable.ic_microphone,
            R.drawable.ic_release,
            R.drawable.ic_too_short,
    };
    //切换状态的String
    private int[] resString = {
            R.string.speak_msg_up,
            R.string.speak_msg_release,
            R.string.speak_msg_too_short,
    };

    //音量的icon  六个
    private int[] volumeString = {
            R.drawable.ic_volume1,
            R.drawable.ic_volume2,
            R.drawable.ic_volume3,
            R.drawable.ic_volume4,
            R.drawable.ic_volume5,
            R.drawable.ic_volume6,
    };

    SoundPopWindow(View contentView){
        initView(contentView);
    }

    private void initView(View contentView){
        setContentView(contentView);
        setTouchable(false);
        setFocusable(false);
        setWidth(DisplayUtils.dip2px(142));
        setHeight(DisplayUtils.dip2px(153));

        findView(contentView);
    }

    private void findView(View rootView){
        mMsgView = (TextView) rootView.findViewById(R.id.msgView);
        soundIconView = (ImageView) rootView.findViewById(R.id.soundIconView);
        soundVolumeView = (ImageView) rootView.findViewById(R.id.soundVolumeView);
    }

    //改变音量的大小
    public void changVolumeStatue(int db){
        int d = Math.min(db , 30);
        d = d/6;
        soundVolumeView.setImageResource(volumeString[d]);
    }

    void show(View parent){
        changeStatus(0);
        showAtLocation(parent , Gravity.CENTER , 0 , 0 );
    }

    //切换录音的状态 0:正常 1:释放 2:太短
    private void changeStatus(int i){
        if (i != status){
            status = i;
            soundIconView.setImageResource(resIcon[i]);
            mMsgView.setText(resString[i]);
            if (i == 0){
                soundVolumeView.setVisibility(View.VISIBLE);
            }else {
                soundVolumeView.setVisibility(View.GONE);
            }
            if (i == 1){
                mMsgView.setBackgroundResource(R.color.color_b52225alpha_50);
            }else {
                mMsgView.setBackgroundResource(R.color.transparent);
            }
        }
    }

    /**
     * 说话时间太短
     */
    void speakTooShort(){
        changeStatus(2);
        //1秒后消失
        mMsgView.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        } , 1000);
    }

    void cancelRecord(boolean b){
        if (b){
            changeStatus(1);
        }else {
            changeStatus(0);
        }
    }

}
