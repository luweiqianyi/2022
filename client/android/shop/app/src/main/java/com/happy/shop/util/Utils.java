package com.happy.shop.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.StringRes;

import com.happy.shop.designpattern.SingleTon;

public class Utils extends SingleTon {
    private Utils(){
        super();
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
}
