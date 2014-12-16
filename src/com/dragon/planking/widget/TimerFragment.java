package com.dragon.planking.widget;

import com.dragon.planking.R;
import com.dragon.planking.MenuActivity;
import com.dragon.planking.model.Data;
import com.dragon.planking.widget.CountdownFragment.CountdownTimer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 需要添加暂停状态
 * @author zhengzelong
 *
 */
public class TimerFragment extends Fragment implements OnClickListener
{
	private static String TAG ="TimerFragment";
    private View parentView;
    private ImageView timer_btn_start;
    private TextView timer_min;
    private TextView timer_sec;  
    
    private AnimationSet in, out;
    private ScaleAnimation scale_in, scale_out;
    private AlphaAnimation fade_in, fade_out;
    
    private Timer timer;
    private boolean isbtn_stop = false;  //计时图标btn_start_hover，就是false；
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.timer, container, false);
        initViews();
        return parentView;
    }
    
    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        initAnim();
    }

    private void initViews()
    {
        MenuActivity parentActivity = (MenuActivity) getActivity();
       
        
        timer_btn_start = (ImageView)parentView.findViewById(R.id.timer_btn_start);
        timer_btn_start.setOnClickListener(this); 
        timer_min = (TextView)parentView.findViewById(R.id.timer_min);
        timer_sec = (TextView)parentView.findViewById(R.id.timer_sec);
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
                timer_btn_start.setVisibility(View.INVISIBLE);
                if(isbtn_stop){
                	timer_btn_start.setBackgroundResource(R.drawable.btn_stop);
                }else{
                	timer_btn_start.setBackgroundResource(R.drawable.btn_start_hover);
                }
                timer_btn_start.setVisibility(View.VISIBLE);
                timer_btn_start.startAnimation(in);

            }
        });
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

        timer_min.setText(strminute);
        timer_sec.setText(strsecond);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.timer_btn_start)
		{
		    if(timer == null)
            {

                timer = new Timer(1000);
                isbtn_stop = true;
                timer_btn_start.startAnimation(out);
                timer.start();
            }else{
            	timer.cancel();
            	timer = null;
                long myminute = 0;
                long mysecond = 0;
                
                referhtime(myminute, mysecond);
                isbtn_stop = false;
                timer_btn_start.startAnimation(out);
//                coundown_btn_start.setBackgroundResource(R.drawable.btn_start_hover);
            }
		}
	}
	
    class Timer extends AdvancedTimer {

        public Timer(long countDownInterval) {
            super(countDownInterval);
        }
        
        @Override
        public void onStart()
        {
            // TODO Auto-generated method stub
            Log.v(TAG, "onStart()");
            
//            coundown_btn_start.setBackgroundResource(R.drawable.btn_stop);
        }

        // 倒计时的回调

		@Override
		public void onTick(long mRemainTime) {
			// TODO Auto-generated method stub
		     long myminute = ((mRemainTime / 1000)) / 60;
	            long mysecond = mRemainTime / 1000 - myminute * 60;
	            
	            referhtime(myminute,mysecond);
		}
    }

}
