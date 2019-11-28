package com.yibei.baseframe.util

import android.annotation.SuppressLint
import android.app.Activity
import com.blankj.utilcode.util.LogUtils
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Create by YiBei on 2019/7/30
 * Description :权限工具类
 */
@SuppressLint("CheckResult")
class PermissionUtil {
    companion object {

        fun requestPermissionsPlural(
            activity: Activity,
            IPermissionCallBack: IPermissionCallBack,
            strings: Array<String>
        ) {
            val rxPermission = RxPermissions(activity)
            rxPermission.request(*strings).subscribe { aBoolean ->
                if (aBoolean!!) {
                    LogUtils.d("用户通过全部权限请求")
                    IPermissionCallBack.permissioGranted()
                } else {
                    LogUtils.e("有至少一个权限请求未通过", *arrayOfNulls(0))
                    IPermissionCallBack.permissionDisGranted()
                }
            }
        }

    }


}
