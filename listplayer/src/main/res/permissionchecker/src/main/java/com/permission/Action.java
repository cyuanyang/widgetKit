package com.permission;

import java.util.List;

/**
 * Created by shawn on 2018/2/22.
 *
 */

public interface Action {

    void onAction(List<String> permissions);
}
