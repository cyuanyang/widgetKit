package com.permission;

import java.util.List;

/**
 * Created by shawn on 2018/2/22.
 *
 *
 */

public interface IRequest {

    IRequest permissions(List<String> permissions);

    IRequest onDenied(Action onDenied);

    IRequest onGranted(Action onGranted);

    void start();

}
