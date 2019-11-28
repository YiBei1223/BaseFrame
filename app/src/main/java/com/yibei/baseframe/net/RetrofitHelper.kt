package com.yibei.baseframe.net


import com.yibei.baseframe.AppSP
import com.yibei.baseframe.net.http.HttpConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Create by YiBei on 2019/6/6
 * Description : Retrofit辅助类
 */
class RetrofitHelper private constructor() {

    private val mRetrofit: Retrofit = Retrofit.Builder().baseUrl(AppSP.SERVER_IP)
            .client(HttpConfig.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


    fun getServer(apiService:Any): Any {
        return mRetrofit.create(Any::class.java)
    }

    companion object {
        //单例
        val INSTANCE: RetrofitHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitHelper()
        }

    }

}
