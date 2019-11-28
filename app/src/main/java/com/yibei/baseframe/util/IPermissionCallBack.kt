package com.yibei.baseframe.util

/**
 * Create by YiBei on 2019/7/30
 * Description : 权限请求返回方法接口
 */
interface IPermissionCallBack {
    fun permissioGranted()

    fun permissionDisGranted()
}