package com.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by shawn on 2018/2/22.
 *
 *
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class PermissionActivity extends Activity {

    private ArrayList<String> permissions;
    private static PermissionListener permissionListener;

    public static void startRequestPermissionActivity(Context context , ArrayList<String> permissions ,PermissionListener listener){
        Intent intent = new Intent(context , PermissionActivity.class);
        Bundle options = new Bundle();
        options.putStringArrayList("permission",permissions);
        intent.putExtras(options);
        ContextCompat.startActivity(context ,intent , null);
        permissionListener = listener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enterAnim();
        invasionStatusBar(this);
        permissions = getIntent().getStringArrayListExtra("permission");

        if (permissions != null && permissions.size() != 0) {
            requestPermissions(permissions.toArray(new String[]{}), 1);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionListener.onRequestPermissionsResult(permissions , grantResults);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        permissionListener = null;
    }

    @Override
    public void finish() {
        super.finish();
        exitAnim();
    }

    private void enterAnim(){
        overridePendingTransition(R.anim.open_in, R.anim.open_in);
    }

    private void exitAnim(){
        overridePendingTransition(R.anim.open_in, R.anim.open_in);
    }

    /**
     * 状态栏颜色透明
     */
    private static void invasionStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
