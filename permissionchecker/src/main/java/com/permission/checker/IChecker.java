package com.permission.checker;

import android.content.Context;

import java.util.List;

/**
 * Created by shawn on 2018/2/22.
 *
 */

public interface IChecker {

    boolean check(Context context , String permission);
}
