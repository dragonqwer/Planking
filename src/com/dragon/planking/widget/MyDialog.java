package com.dragon.planking.widget;

import com.dragon.planking.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

//自定义Dialog
public class MyDialog extends Dialog{
    
    private Window window = null;
    
    public MyDialog(Context context)
    {  
        super(context);
    }
    
    public void showDialog(int layoutResID){
        windowDeploy();
        setContentView(layoutResID);
        //设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(true);
        show();
    }
    
    //设置窗口显示
    public void windowDeploy(){
        window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.alpha = 0.6f; //设置透明度
        wl.height =LayoutParams.WRAP_CONTENT;
        wl.width = LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.CENTER; 
        window.setAttributes(wl);
    }

}
