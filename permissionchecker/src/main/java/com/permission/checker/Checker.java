package com.permission.checker;

import android.content.Context;

import com.permission.checker.test.StrictChecker;

/**
 * Created by shawn on 2018/2/22.
 *
 *
 */

public class Checker implements IChecker {

    private IChecker standardChecker = new StandardChecker();
    private IChecker strictChecker = new StrictChecker();

    @Override
    public boolean check(Context context ,String permission) {
        return standardChecker.check(context , permission)
                && strictChecker.check(context , permission);
    }

}
