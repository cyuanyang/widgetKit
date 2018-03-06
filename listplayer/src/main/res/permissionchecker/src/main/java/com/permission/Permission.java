package com.permission;

import android.app.Activity;

/**
 * Created by shawn on 2018/2/22.
 *
 */

public class Permission {

    public static IRequest with(Activity activity){
        return new Request(activity);
    }

}
