package com.permission;

import android.support.annotation.NonNull;

/**
 * Created by shawn on 2018/2/22.
 *
 */

public interface PermissionListener {

    void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults);
}
