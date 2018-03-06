package com.permission.checker.test;

/**
 * Created by shawn on 2018/2/22.
 *
 * 主动的权限测试
 *
 * 根据操作产生的结果来判定是否拥有权限
 */

interface IPermissionTest {

    boolean test() throws Throwable;
}
