package com.yibei.baseframe.net;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by YiBei on 2019/6/6
 * Description : 通用的Rx线程转换类
 */
public class RxSchedulers {

    private static final ObservableTransformer schedulersTransformer = a -> (a).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }
}