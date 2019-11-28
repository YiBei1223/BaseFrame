package com.yibei.baseframe.net.http


import com.yibei.baseframe.net.interceptor.HeadRequestInterceptor
import com.yibei.baseframe.net.interceptor.LogInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Create by YiBei on 2019/6/6
 * Description : http配置
 */
object HttpConfig {
    private const val CONNECT_TIMEOUT = 60L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L
//    private val cookieStore: HashMap<String, List<Cookie>> = HashMap()


    //OkHttpClient实例 带拦截器
    val okHttpClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(HeadRequestInterceptor())
                    .addInterceptor(LogInterceptor())

                    .build()
        }


    //OkHttpClient实例 不带拦截器
    val okHttpClientEmpty: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build()
    }

}
