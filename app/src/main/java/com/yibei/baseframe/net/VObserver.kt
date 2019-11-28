package com.fubang.fish.net

import android.accounts.NetworkErrorException
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.fubang.fish.model.base.BaseResponse
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.adapter.rxjava2.HttpException
import java.net.ConnectException
import java.util.concurrent.TimeoutException

/**
 * Desc: 统一封装的Observer
 * Created by fww on 2019/3/29
 */
abstract class VObserver<T> : Observer<BaseResponse<T>> {
     var mContext: Context = Utils.getApp()

    override fun onSubscribe(d: Disposable) {
        //请求开始
    }

    override fun onNext(tBaseResponse: BaseResponse<T>) {
        //根据业务来分
        if (tBaseResponse.isSuccess) {
            onSuccess(tBaseResponse.data)
        } else {
            onFailure(tBaseResponse.msg, true)
        }

    }

    override fun onError(e: Throwable) {
        try {
            if (e is ConnectException || e is NetworkErrorException) {
                onFailure("本地网络错误 请检查网络", true)
            } else if (e is TimeoutException) {
                onFailure("本地网络请求超时 请稍后再试", true)
            } else if (e is HttpException) {
                if(e.code()==401){
                    val msgIntent = Intent("com.fubang.fish.logout")
                    LocalBroadcastManager.getInstance(Utils.getApp()).sendBroadcast(msgIntent)
                    onFailure("登录过期 请重新登陆", true)
                }
                else
                    onFailure("服务器开了个小差 请稍后再试", true)
            } else {
                onFailure("系统异常", true)
            }
        } catch (e1: Exception) {
            onFailure("系统异常", true)
        }

    }

    override fun onComplete() {
        //请求完成
    }

    /**
     * 返回成功
     * @param t 返回体
     */
    protected abstract fun onSuccess(t: T?)

    protected  abstract fun onFail(message:String?)

    /**
     * 返回失败  子类覆盖时必须super
     * 可对通用问题进行拦截
     * @param message 消息
     * @param isShowMsg 是否显示错误消息
     */
    @CallSuper
    protected fun onFailure( message: String?, isShowMsg: Boolean) {
        LogUtils.e("wrong message is : $message")
        onFail(message)
//        ToastUtils.showLong(message)
        //判断状态码 进行拦截
    }
}

