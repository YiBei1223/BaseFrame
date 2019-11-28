package com.fubang.fish.base


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * Create by YiBei on 2019/6/6
 * Description : 基础配置约定
 */
interface BaseContract {

    interface IPresenter<T : IView> : LifecycleObserver {

        fun setLifecycleOwner(lifecycleOwner: LifecycleOwner)

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate(owner: LifecycleOwner)

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(owner: LifecycleOwner)

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onLifecycleChanged(owner: LifecycleOwner,
                               event: Lifecycle.Event)
    }

    interface IView {

        /**
         * 显示进度中
         */
        fun showLoading()

        /**
         * 隐藏进度
         */
        fun hideLoading()

        /**
         * 显示请求成功
         *
         * @param message
         */
        fun showSuccess(message: String)

        /**
         * 失败重试
         *
         * @param message
         */
        fun showFailed(message: String)

        /**
         * 显示当前网络不可用
         */
        fun showNoNet()

        /**
         * 重试
         */
        fun onRetry()

    }

    interface IModel
}
