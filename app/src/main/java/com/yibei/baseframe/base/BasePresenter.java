package com.yibei.baseframe.base;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.fubang.fish.util.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;

import org.jetbrains.annotations.NotNull;


/**
 * Create by YiBei on 2019/6/6
 * Description : 基类Presenter
 */
public class BasePresenter<T extends BaseContract.IView> implements BaseContract.IPresenter<T> {
    protected T mView;
    private LifecycleOwner mLifecycleOwner;


    public BasePresenter(T view){
        this.mView = view;
    }

    protected  <T> AutoDisposeConverter<T> bindToLife() {
        if (null == mLifecycleOwner)
            throw new NullPointerException("mLifecycleOwner == null");
        return RxLifecycleUtils.bindLifecycle(mLifecycleOwner);
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
    }

    @Override
    public void onCreate(LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        if (mView != null) {
            mView = null;
        }
    }

    @Override
    public void onLifecycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {

    }
}
