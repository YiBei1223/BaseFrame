package com.yibei.baseframe.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class KeyBoardUtils {

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService ( Context.INPUT_METHOD_SERVICE );
        imm.toggleSoftInput ( 0, InputMethodManager.HIDE_NOT_ALWAYS );
    }

    public static void showSoftInputFromWindow(Activity context,EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService ( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow ( view.getWindowToken (), 0 ); //强制隐藏键盘
    }


    public static boolean isShowSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService ( Context.INPUT_METHOD_SERVICE );
        //获取状态信息
        return imm.isActive ();//true 打开
    }

    /**
     * 打卡软键盘
     */
    public static void openKeybord(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService ( Context.INPUT_METHOD_SERVICE );
        imm.toggleSoftInput ( InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY );
    }

    /**
     * 键盘如果是打开状态 关闭
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if(imm.isActive()){
            //拿到view的token 不为空
            if (activity.getCurrentFocus()!=null) {
                if( activity.getCurrentFocus().getWindowToken()!=null)
                {
                    //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService ( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow ( activity.getCurrentFocus ().getWindowToken (), InputMethodManager.HIDE_NOT_ALWAYS );
    }


    public static void showKeyboard(Activity activity, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService ( Context.INPUT_METHOD_SERVICE );
        if (null == imm) return;
        if (isShow) {
            if (activity.getCurrentFocus () != null) {
                //有焦点打开
                imm.showSoftInput ( activity.getCurrentFocus (), 0 );
            } else {
                //无焦点打开
                imm.toggleSoftInput ( InputMethodManager.SHOW_FORCED, 0 );
            }
        } else {
            if (activity.getCurrentFocus () != null) {
                //有焦点关闭
                imm.hideSoftInputFromWindow ( activity.getCurrentFocus ().getWindowToken (), InputMethodManager.HIDE_NOT_ALWAYS );
            } else {
                //无焦点关闭
                imm.toggleSoftInput ( InputMethodManager.HIDE_IMPLICIT_ONLY, 0 );
            }
        }
    }
}


