package com.tal.imagepicker.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by shawn on 2018/1/2.
 *
 */

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseFragmentCallback){
//            mCallback = (BaseFragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mCallback = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected boolean onBackPressed(){
        return false;
    }

    public interface BaseFragmentCallback{
    }
}
