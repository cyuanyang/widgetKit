package com.permission;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.permission.checker.Checker;
import com.permission.checker.IChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shawn on 2018/2/22.
 *
 * 请求权限
 */

public class Request implements IRequest , PermissionListener {

    private IChecker mChecker ;

    private Context mContext;

    private List<String> permissions = new ArrayList<>();
    private Action onDenied;
    private Action onGranted;

    public Request(Context context){
        mContext = context;
        mChecker = new Checker();
    }

    @Override
    public IRequest permissions(List<String> permissions) {
        this.permissions.addAll(permissions);
        return this;
    }

    @Override
    public IRequest onDenied(Action onDenied) {
        this.onDenied = onDenied;
        return this;
    }

    @Override
    public IRequest onGranted(Action onGranted) {
        this.onGranted = onGranted;
        return this;
    }

    @Override
    public void start() {
        ArrayList<String> denyPermissions = hasDeniedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (denyPermissions.size() == 0 && onGranted!=null){
                onGranted.onAction(permissions);
            }else {
                PermissionActivity.startRequestPermissionActivity(mContext , denyPermissions , this);
            }
        }else {
            if (denyPermissions == null || denyPermissions.size() == 0){
                if (onGranted != null){
                    onGranted.onAction(permissions);
                }
            }else {
                if (onDenied != null){
                    onDenied.onAction(permissions);
                }
            }
        }
    }

    /**
     * 返回拒绝的权限
     * @return 返回被拒绝的权限的list
     */
    private ArrayList<String> hasDeniedPermissions(List<String> permissions){
        ArrayList<String> denyList = new ArrayList<>();
        for (String permission : permissions) {
            if (!mChecker.check(mContext , permission)){
                denyList.add(permission);
            }
        }
        return denyList;
    }

    @Override
    public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> permissionList = Arrays.asList(permissions);
        ArrayList<String> denyPermissions = hasDeniedPermissions(permissionList);
        if (denyPermissions.size() == 0){
            if (onGranted!=null){
                onGranted.onAction(permissionList);
            }
        }else {
            if (onDenied!=null){
                onDenied.onAction(denyPermissions);
            }
        }
    }
}
