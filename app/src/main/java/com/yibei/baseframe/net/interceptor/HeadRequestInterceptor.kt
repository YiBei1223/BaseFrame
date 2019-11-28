package com.yibei.baseframe.net.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Create by YiBei on 2019/6/6
 * Description : 请求头拦截器
 */
class HeadRequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
                .newBuilder()
                //.addHeader("X-APP-TYPE", "android")
                .build()
        Log.i("2333",request.body().toString())
        return chain.proceed(request)
    }
}
