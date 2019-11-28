package com.yibei.baseframe.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.fubang.fish.base.BaseFragment


/**
 * Create by YiBei on 2019/6/6
 * Description : BaseActivity
 */
abstract class BaseActivity : AppCompatActivity(), BaseFragment.OnFragmentInteractionListener {
    /**
     * 设置Activity的布局ID
     *
     * @return
     */
    protected abstract val activityLayoutID: Int
    protected abstract fun initView()

    override fun onFragmentInteraction(uri: Uri) {
    }

    @SuppressLint("PrivateApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLayoutID)
        initView()
    }

    /**
     * 全透状态栏
     */
    private fun setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            val window = window
            window.clearFlags(FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            window.addFlags(FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private var contentViewGroup:View?=null

    protected fun setFitSystemWindow(fitSystemWindow: Boolean) {
        if (contentViewGroup == null) {
            contentViewGroup = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
        }
        contentViewGroup!!.fitsSystemWindows = fitSystemWindow
    }


    /**
     * 为了兼容4.4的抽屉布局->透明状态栏
     */
    private fun setDrawerLayoutFitSystemWindow(){
        if (Build.VERSION.SDK_INT == 19) {//19表示4.4
            val statusBarHeight = getStatusBarHeight()
            if (contentViewGroup == null) {
                contentViewGroup = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
            }
            if (contentViewGroup is DrawerLayout) {
                val drawerLayout = contentViewGroup as DrawerLayout
                drawerLayout.clipToPadding = true
                drawerLayout.fitsSystemWindows = false
                for (i in 0 until drawerLayout.childCount) {
                    val child = drawerLayout.getChildAt(i)
                    child.fitsSystemWindows = false
                    child.setPadding(0, statusBarHeight, 0, 0)
                }

            }
        }
    }

    //利用反射获取状态栏高度
    private fun getStatusBarHeight(): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
