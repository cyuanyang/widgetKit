package com.permission.checker;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

/**
 * Created by shawn on 2018/2/22.
 * 用api检查权限
 *
 * 测试中发现有些手机不管有没有拒绝都是返回true
 */

public class StandardChecker implements IChecker {

    @Override
    public boolean check(Context context , String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        int result = context.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid());
        if (result == PackageManager.PERMISSION_DENIED) {
            return false;
        }

        String op = AppOpsManager.permissionToOp(permission);
        if (TextUtils.isEmpty(op)) {
            return true;
        }

        AppOpsManager appOpsManager = context.getSystemService(AppOpsManager.class);
        result = appOpsManager.noteProxyOp(op, context.getPackageName());
        if (result != AppOpsManager.MODE_ALLOWED) {
            return false;
        }
        return true;
    }
}
