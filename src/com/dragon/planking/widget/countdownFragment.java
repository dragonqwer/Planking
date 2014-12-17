package com.dragon.planking.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.*;

import net.simonvt.numberpicker.NumberPicker;


import com.dragon.planking.MenuActivity;
import com.dragon.planking.R;
import com.dragon.planking.model.Data;



public class CountdownFragment extends Fragment implements OnClickListener
{
    private static String TAG ="CountdownFragment";
    private View parentView;

    private ImageView coundown_btn_start;
    private TextView coundown_min;
    private TextView coundown_sec;  
    
    private AnimationSet in, out;
    private ScaleAnimation scale_in, scale_out;
    private AlphaAnimation fade_in, fade_out;
    
    private Button coundown_btn_cancel;
    private Button coundown_btn_set;
    private NumberPicker numberPicker_m;
    private NumberPicker numberPicker_s;
    
    private CountdownTimer countdowntimer;
    private SlidingDrawer mDrawer;
    private Long count ;
    private boolean isbtn_stop = false;  //计时图标btn_start_hover，就是false；
    
    private MenuActivity parentActivity;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.countdown, container, false); 
        initView();
        return parentView;
    }
    
    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        initAnim();

    }
    @Override
    public void onPause()
    {
        // TODO Auto-generated method stub
        Log.v(TAG, TAG+"====onPause");
        if(countdowntimer != null)
        {
            countdowntimer.pause();
        }
        super.onPause();
    }
    
    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        Log.v(TAG, TAG+"====onResume");
        if(countdowntimer != null)
        {
            countdowntimer.resume();
        }
        super.onResume();
    }

    private void initAnim()
    {
        // TODO Auto-generated method stub
        in = new AnimationSet(true);
        out = new AnimationSet(true);

        out.setInterpolator(new AccelerateDecelerateInterpolator());
        in.setInterpolator(new AccelerateDecelerateInterpolator());

        scale_in = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale_out = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        fade_in = new AlphaAnimation(0.0f, 1.0f);
        fade_out = new AlphaAnimation(1.0f, 0.0f);

        scale_in.setDuration(150);
        scale_out.setDuration(150);
        fade_in.setDuration(150);
        fade_out.setDuration(150);

        in.addAnimation(scale_in);
        in.addAnimation(fade_in);
        out.addAnimation(fade_out);
        out.addAnimation(scale_out);
        
        out.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                System.out.println("print this");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                coundown_btn_start.setVisibility(View.INVISIBLE);
                if(isbtn_stop){
                    coundown_btn_start.setBackgroundResource(R.drawable.btn_stop);
                }else{
                    coundown_btn_start.setBackgroundResource(R.drawable.btn_start_hover);
                }
                coundown_btn_start.setVisibility(View.VISIBLE);
                coundown_btn_start.startAnimation(in);

            }
        });
    }

    private void initView()
    {
        parentActivity = (MenuActivity) getActivity();
        Typeface typeface=Typeface.createFromAsset(parentActivity.getAssets(),"font/arial.ttf");
        
        coundown_btn_start = (ImageView)parentView.findViewById(R.id.coundown_btn_start);

        coundown_min = (TextView)parentView.findViewById(R.id.coundown_min);
        coundown_sec = (TextView)parentView.findViewById(R.id.coundown_sec);
        
        coundown_btn_cancel = (Button)parentView.findViewById(R.id.coundown_set_time_cancel);
        coundown_btn_set = (Button)parentView.findViewById(R.id.coundown_set_time_ok);
        
        mDrawer=(SlidingDrawer)parentView.findViewById(R.id.slidingdrawer);
        
        coundown_min.setTypeface(typeface);
        coundown_sec.setTypeface(typeface);
        
//        countdown_timesetting.setOnClickListener(this);
        coundown_btn_start.setOnClickListener(this); 
        coundown_btn_cancel.setOnClickListener(this); 
        coundown_btn_set.setOnClickListener(this); 
        
        numberPicker_m = (NumberPicker) parentView.findViewById(R.id.numberPicker_m);
        numberPicker_m.setMaxValue(59);
        numberPicker_m.setMinValue(0);
        numberPicker_m.setFocusable(true);
        numberPicker_m.setFocusableInTouchMode(true);
        
        numberPicker_s = (NumberPicker) parentView.findViewById(R.id.numberPicker_s);
        numberPicker_s.setMaxValue(59);
        numberPicker_s.setMinValue(0);
        numberPicker_s.setFocusable(true);
        numberPicker_s.setFocusableInTouchMode(true);
        
    }

    @Override
    public void onClick(View arg0)
    {
        // TODO Auto-generated method stub
        Log.v(TAG, "onClick==="+arg0.getId());
        switch (arg0.getId())
        {
            case R.id.coundown_btn_start:
                try{
                    if(count!=null)
                    {
//                        addLoadUI();                      
                        if(countdowntimer == null)
                        {
                            Log.v(TAG, "count=="+count);
                            countdowntimer = new CountdownTimer(count, 1000);
                            isbtn_stop = true;
                            coundown_btn_start.startAnimation(out);
                            countdowntimer.start();
                        }else{
                            countdowntimer.cancel();
                            countdowntimer = null;
                            long myminute = ((count / 1000)) / 60;
                            long mysecond = count / 1000 - myminute * 60;
                            
                            referhtime(myminute, mysecond);
                            isbtn_stop = false;
                            coundown_btn_start.startAnimation(out);
//                            coundown_btn_start.setBackgroundResource(R.drawable.btn_start_hover);
                        }
 
                    }else{
                        Log.v(TAG, "count is null add the tip");
                    }
                }
                catch (Exception e)
                {
                    // TODO: handle exception
                    Log.v(TAG, "countdowntimer.start exception");
                    Log.v(TAG, "Exception=="+e);
                }
           
                break;
            case R.id.coundown_set_time_cancel:
                mDrawer.animateClose();
                break;
            case R.id.coundown_set_time_ok:
                long myminute = numberPicker_m.getValue();
                long mysecond = numberPicker_s.getValue();
                count = (long) ((myminute*60+mysecond)*1000);
                
                referhtime(myminute,mysecond);
                
                mDrawer.animateClose();
                isbtn_stop = false;
                coundown_btn_start.startAnimation(out);
//                coundown_btn_start.setBackgroundResource(R.drawable.btn_start_hover);
                break;
                
            default:
                break;
        }
    }

    public void addMaxInSP(Data data)
    {
        SharedPreferences DataSave = parentActivity.getSharedPreferences(MenuActivity.DataSave, Context.MODE_PRIVATE);
        if (DataSave.getLong("Max", -1) < data.getResult())
        {
            DataSave.edit().putLong("Max", data.getResult()).commit();
        }
    }
    
    private void referhtime(long myminute, long mysecond)
    {
        // TODO Auto-generated method stub
        String strminute = String.valueOf(myminute);
        String strsecond = String.valueOf(mysecond);


        if (myminute >= 0 && myminute <= 9) {
            strminute = "0" + myminute;
        }
        if (mysecond >= 0 && mysecond <= 9) {
            strsecond = "0" + mysecond;
        }

        coundown_min.setText(strminute);
        coundown_sec.setText(strsecond);
    }
    
    class CountdownTimer extends AdvancedCountdownTimer {

        public CountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        
        @Override
        public void onStart()
        {
            // TODO Auto-generated method stub
            Log.v(TAG, "onStart()");
            
//            coundown_btn_start.setBackgroundResource(R.drawable.btn_stop);
        }
        
        

        public void onFinish() {
//            countdownTimer_edit_min.setEnabled(true);
//            countdownTimer_edit_s.setEnabled(true);
            numberPicker_m.invalidate();
            numberPicker_s.invalidate();
            long myminute = ((count / 1000)) / 60;
            long mysecond = count / 1000 - myminute * 60;
            
            referhtime(myminute, mysecond);
            isbtn_stop = false;
            coundown_btn_start.startAnimation(out);
//            coundown_btn_start.setBackgroundResource(R.drawable.btn_start_hover);
            countdowntimer = null;
            Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
            t.setToNow(); // 取得系统时间。
            Data data =new Data();
//            data.setDate_y(t.year);
//            data.setDate_mo(t.month+1);
//            data.setDate_d(t.monthDay);
//            data.setDate_h(t.hour);
//            data.setDate_m(t.minute);
//            data.setDate_s(t.second);
//            data.setResult(count/1000);
//            Log.v(TAG, "add data=="+data.toString());
//            parentActivity.dm.addData(data);
//            addMaxInSP(data);   
            for(int i=0;i<10;i++)
            {
                for(int j=0;j<5;j++)
                {
                    data.setDate_y(t.year);
                    data.setDate_mo(t.month+1);
                    data.setDate_d(t.monthDay+i);
                    data.setDate_h(t.hour);
                    data.setDate_m(t.minute);
//                    data.setDate_s(t.second+j);
                    data.setDate_s(t.second+ i);
                    data.setResult(count/1000 + j+i);
//                    data.setResult(count/1000 + i);
                    Log.v(TAG, "add data=="+data.toString());
                    parentActivity.dm.addData(data); 
                    addMaxInSP(data);
                }
            }
        }

        // 倒计时的回调
        public void onTick(long millisUntilFinished, int percent) {

            long myminute = ((millisUntilFinished / 1000)) / 60;
            long mysecond = millisUntilFinished / 1000 - myminute * 60;
            
            referhtime(myminute,mysecond);
        }
    }
}
