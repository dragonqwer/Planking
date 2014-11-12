package com.dragon.planking.widget;


import com.dragon.planking.MenuActivity;
import com.dragon.planking.R;

import com.dragon.planking.chart.Histogram;
import com.dragon.planking.chart.LineDiagram;
import com.dragon.planking.model.DayData;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class ProfileFragment extends Fragment
{

    private static String TAG ="ProfileFragment";
    
    private View parentView;
    private FrameLayout linediagram;
    private LinearLayout histogram;
    
//********test************//
//    private Button b1;
//    private ImageView iv;
//********test************//
   
    private HorizontalScrollView testsroll;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.profile, container, false);
        initViews();
        return parentView;
    }
    
    
    private void initViews()
    {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        ResideMenu resideMenu = parentActivity.getResideMenu();
        
        testsroll= (HorizontalScrollView) parentView.findViewById(R.id.testsroll);
        linediagram= (FrameLayout) parentView.findViewById(R.id.linediagram);
        histogram= (LinearLayout) parentView.findViewById(R.id.histogram);
        
        linediagram.addView(new LineDiagram(parentActivity,parentActivity.daydatalist));
        
        for(DayData dd : parentActivity.daydatalist)
        {
            Log.v(TAG , "I=="+dd.toString());
            histogram.addView(new Histogram(parentActivity, dd));
        }
        
        resideMenu.addIgnoredView(linediagram);
        resideMenu.addIgnoredView(histogram);
//********test*****截屏*******//        
//        b1 = (Button)parentView.findViewById(R.id.button1);
//        iv = (ImageView)parentView.findViewById(R.id.imageView1);
//        
//        b1.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                // TODO Auto-generated method stub
//                testsroll.setDrawingCacheEnabled(true);
//                Bitmap obmp = Bitmap.createBitmap(testsroll.getDrawingCache());
//                System.out.println("obmp.getByteCount()=="+obmp.getByteCount());
//                parentView.setBackgroundDrawable(new BitmapDrawable(obmp));
//                
//                testsroll.setDrawingCacheEnabled(false);
//            }
//        });
//********test************//
    }

}
