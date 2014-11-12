package com.dragon.planking.chart;

import java.util.List;

import com.dragon.planking.MenuActivity;
import com.dragon.planking.R;
import com.dragon.planking.model.DayData;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * 柱状图
 * 
 * @author Administrator
 * 
 */
public class Histogram extends View {
    
    private static String TAG ="Histogram";
    
	private DayData daydata;
	private float tb;
	private float interval_width;
	private float interval_height;
	private float interval_padding;
	private float interval_space;
	private Paint paint_date, paint_rectf_blue;

	private int fineLineColor = 0x5faaaaaa; // 灰色
	private int blueLineColor = 0xff00ffff; // 蓝色

	private long result_max = 0;
	
	public Histogram(Context context, DayData daydata) {
		super(context);
		init(daydata);
		result_max = (context.getSharedPreferences(MenuActivity.DataSave, Context.MODE_PRIVATE)
                .getLong("Max", 1));
		Log.v(TAG, "result_max=="+result_max);
//		this.setPadding(left, top, right, bottom);
	}

	public void init(DayData daydata) {
		this.daydata = daydata;
		Resources res = getResources();
		tb = res.getDimension(R.dimen.historyscore_tb);
		interval_width = tb * 1.6f;
		interval_height = tb * 20.0f;
		interval_space = tb *0.2f;
		interval_padding = 3f*tb - (interval_width/2);
		paint_date = new Paint();
		paint_date.setStrokeWidth(tb * 0.05f);
		paint_date.setTextSize(tb * 1.2f);
		paint_date.setColor(fineLineColor);
		paint_date.setTextAlign(Align.CENTER);

//		paint_rectf_gray = new Paint();
//		paint_rectf_gray.setStrokeWidth(tb * 0.05f);
//		paint_rectf_gray.setColor(fineLineColor);
//		paint_rectf_gray.setStyle(Style.FILL);
//		paint_rectf_gray.setAntiAlias(true);

		paint_rectf_blue = new Paint();
		paint_rectf_blue.setStrokeWidth(tb * 0.05f);
		paint_rectf_blue.setColor(blueLineColor);
		paint_rectf_blue.setStyle(Style.FILL);
		paint_rectf_blue.setAntiAlias(true);

		setLayoutParams(new LayoutParams(
				(int) (this.daydata.getDatalist().size() * interval_width +interval_padding*2),
				(int) (interval_height+interval_padding*2)));
	}

	protected void onDraw(Canvas c) {

		drawDate(c);
		drawRectf(c);
	}

	/**
	 * 绘制矩形
	 * 
	 * @param c
	 */
	public void drawRectf(Canvas c) {
		for (int i = 0; i < daydata.getDatalist().size(); i++) {
		    Log.v(TAG , "I=="+i);
		    drawFectfs(c,i);
			
		}
	}
	
	
	
	
	public void drawFectfs(Canvas c,int i)
	{
//	    RectF f = new RectF();
//        f.set(tb * 0.2f + (interval_left_right+2*tb) * i,
//                getHeight() - tb * 12.0f,
//                tb * 2.2f + (interval_left_right+2*tb) * i, 
//                getHeight() - tb * 2.0f);
//        c.drawRoundRect(f, tb * 0.2f, tb * 0.2f, paint_rectf_gray);
	    Log.v(TAG, "daydata.getDatalist().get(i).getResult()=="+daydata.getDatalist().get(i).getResult());
        float base = (daydata.getDatalist().get(i).getResult())* (interval_height/ result_max);
        Log.v(TAG, "daydata.getDatalist().get(i).getResult()/ result_max=="+daydata.getDatalist().get(i).getResult()/ result_max);
        Log.v(TAG, "base=="+base);
        RectF f = new RectF();
        f.set(interval_padding + interval_width*i+interval_space, 
                getHeight()- interval_padding - base, 
                interval_padding+ interval_width*(i+1),
                getHeight()- interval_padding );
        c.drawRoundRect(f, tb * 0.3f, tb * 0.3f, paint_rectf_blue);
	}

	/**
	 * 绘制日期
	 * 
	 * @param c
	 */
	public void drawDate(Canvas c) {
//			String date = daydata.getData();
			String year = daydata.getYear()+"";
	        year = year.substring(2,year.length());
	        String date =year+"-"+setSmallNum(daydata.getMonth())+"-"+setSmallNum(daydata.getDay());
			c.drawText(date, getWidth()/2,
					getHeight() - (interval_padding/2), paint_date);

	}
	/**
	 * 0到9显示00~09
	 * @param num
	 * @return
	 */
	private String setSmallNum(int num)
	{  String num_s ;
	     if (num >= 0 && num <= 9) {
	         num_s = "0" + num;
	        }else{
	            num_s = num +"";
	        }
	     
	    return num_s;
	}
}
