package com.happy.shop.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

public class Utils {

    private static volatile Utils mInstance;

    private Utils(){
    }

    public static Utils getInstance(){
        if(mInstance == null){
            synchronized (Utils.class){
                if(mInstance == null){
                    mInstance = new Utils();
                }
            }
        }
        return mInstance;
    }


    /**
     * 外部的类需要实现该接口中的方法以便于实现点击OK或者cancel所产生的行为
     */
    public interface MyDialogInterface{
        void posCallback();
        void negCallback();
    }

    /**
     * 显示弹出对话框
     * @param context 对话框需要在哪个Activity中显示
     * @param messageId 对话框提示信息文字对应的资源id
     * @param bNeedTitle 对话框是否需要标题
     * @param titleId 对话框的标题文字对应的资源id,仅在bNeedTitle为true有效
     * @param posId ok按钮对应的文字的资源id
     * @param negId cancel按钮对应的文字的资源id
     * @param dialogInterface ok和cancel按钮对应的函数行为接口
     */
    public void showStandardDialog(
            Context context,
            @StringRes int messageId,
            boolean bNeedTitle,
            @StringRes int titleId,
            @StringRes int posId,
            @StringRes int negId,
            MyDialogInterface dialogInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(messageId);
        if(bNeedTitle){
            builder.setTitle(titleId);
        }

        // 一种是传统写法
        builder.setPositiveButton(posId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogInterface.posCallback();
            }
        });

        // 另一种是lambda表达式写法
        builder.setNegativeButton(negId, (dialog, id) -> {
                dialogInterface.negCallback();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 显示提示
     * @param context Activity
     * @param charSequence 提示信息文字
     * @param duration 提示持续时间
     * @param bNeedCenter 是否需要居中显示
     */
    public void showToastTip(Context context,CharSequence charSequence,int duration,boolean bNeedCenter){
        Toast toast = Toast.makeText(context, charSequence, duration);
        if(bNeedCenter){
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }

    static long s_StartTime = System.nanoTime()/1000/1000;

    static public long timeGetTime64()
    {
        long longTime = System.nanoTime()/1000/1000;
        long intTime = (long) (longTime - s_StartTime);
        return intTime;
    }

    static public int timeGetTime()
    {
        return (int)timeGetTime64();
    }

    //实现类似Windows下timeGetTime的功能，但是并不是从开机时间开始算，而是从 变量初始化开始算的
    static public int getTickCount()
    {
        return timeGetTime();
    }

    private static int mLastClickTick = 0;

    /**
     * 控制按钮是否允许重复点击
     * @return
     */
    static public boolean allowBtnClickAgain(){
        boolean allowed = true;
        int now = Utils.getTickCount();
        if(now - mLastClickTick < 1000)
        {
            allowed = false;
        }
        else {
            mLastClickTick = now;
        }
        return allowed;
    }

    /**
     * 显示还是隐藏软键盘
     * @param activity 当前弹软键盘所在的Activity
     * @param bShow true: 显示，false: 隐藏
     */
    public void showOrHideSoftKeyboard(AppCompatActivity activity,boolean bShow){
        if(activity != null){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if(bShow){
                imm.showSoftInput(activity.getWindow().getDecorView(),InputMethodManager.SHOW_FORCED); // 强制显示软键盘
            }
            else {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0); //强制隐藏键盘
            }
        }
    }
}
