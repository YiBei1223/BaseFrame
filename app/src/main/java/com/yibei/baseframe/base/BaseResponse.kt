package com.yibei.baseframe.base



import java.io.Serializable

/**
 * Create by YiBei on 2019/6/6
 * Description : 返回体基础类
 */
class BaseResponse<T> : Serializable {

    var isSuccess: Boolean = true

    var msg: String? = null

    var data: T? = null

}
