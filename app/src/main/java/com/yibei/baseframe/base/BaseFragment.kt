package com.fubang.fish.base

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.yibei.baseframe.base.BaseActivity
import com.yibei.baseframe.base.BaseContract


/**
 * Create by YiBei on 2019/6/6
 * Description : 全局基类Fragment，所有的Fragment继承这个Fragment
 */
abstract class BaseFragment<P : BaseContract.IPresenter<*>> : Fragment(), BaseContract.IView {
    private lateinit var mView: View
    lateinit var mActivity: BaseActivity
    lateinit var mContext: Context
    lateinit var mPresenter: P

    private var listener: OnFragmentInteractionListener? = null


    /**
     * 获取Fragment的LayoutID
     *
     * @return
     */
    protected abstract val fragmentLayoutID: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = initPresenter()
        mPresenter.setLifecycleOwner(this@BaseFragment)
        lifecycle.addObserver(mPresenter)
        if (savedInstanceState != null) {
            val isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN)
            val ft = fragmentManager!!.beginTransaction()
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(fragmentLayoutID, container, false)
        mActivity = (this@BaseFragment.activity as BaseActivity?)!!
        mContext = mActivity
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).show(true)
        //绑定
        initView(view, savedInstanceState)
    }

    /**
     * 初始化Presenter
     *
     * @return
     */
    protected abstract fun initPresenter(): P

    /**
     * 初始化View
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract fun initView(view: View, savedInstanceState: Bundle?)


    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showSuccess(message: String) {
        ToastUtils.showShort(message)
    }

    override fun showFailed(message: String) {
        ToastUtils.showShort(message)
    }

    override fun showNoNet() {}

    override fun onRetry() {
        ToastUtils.showShort("onRetry")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    //定义一个接口 用于Navigation Activity和Fragment之间的传值
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private const val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    }

    //利用反射获取状态栏高度
    fun getStatusBarHeight(): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
