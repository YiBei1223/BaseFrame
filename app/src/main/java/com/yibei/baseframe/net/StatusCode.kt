package com.fubang.fish.net

/**
 * Create by YiBei on 2019/6/6
 * Description : 状态码约定
 */
object StatusCode {
    /**
     * 数据请求成功
     */
    const val SUCCESS = 0
    /**
     * 网络错误
     */
    const val ERROR_NONETWORK = 4000
    /**
     * 网络超时
     */
    const val ERROR_TIMEOUT = 4001
    /**
     * 异常
     */
    const val ERROR_EXCEPTION = 4004

}
