package com.dragon.planking.chart;

import java.util.ArrayList;

import com.dragon.planking.MenuActivity;
import com.dragon.planking.R;
import com.dragon.planking.model.DayData;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * 线框图
 * 
 * @author dragon
 * 
 */
public class LineDiagram extends View {

//	private List<Integer> milliliter;
    private Context mcontext;
	private float tb;
	private float interval_width;
	private float interval_height;
	private float interval_padding;
	private float interval_textsize;
	private Paint paint_date, paint_brokenLine, paint_dottedline,
			paint_brokenline_big , framPanint;


	private Bitmap bitmap_point;
	private float dotted_text;
	private int daydatalistsize ;
	private ArrayList<DayData> daydatalist;

	public float getDotted_text() {
		return dotted_text;
	}

	public void setDotted_text(float dotted_text) {
		this.dotted_text = dotted_text;
	}

	private int fineLineColor = 0x5faaaaaa; // 灰色
	private int blueLineColor = 0xff00ffff; // 蓝色
//	private int orangeLineColor = 0xffd56f2b; // 橙色

	public LineDiagram(Context context, ArrayList<DayData> daydatalist) {
		super(context);
		mcontext = context;
		init(daydatalist);
	}

	public void init(ArrayList<DayData> daydatalist) {

	    this.daydatalist = daydatalist;
		this.daydatalistsize = daydatalist.size();
		Resources res = getResources();
		tb = res.getDimension(R.dimen.historyscore_tb);
		interval_width = tb * 6.0f;
		interval_height = tb * 20.0f;
//		interval_left = tb * 0.5f;
		interval_padding = 3f*tb;
		interval_textsize = (float) (1.2*tb);
		
		paint_date = new Paint();
		paint_date.setStrokeWidth(tb * 0.1f);
		paint_date.setTextSize(interval_textsize);
		paint_date.setColor(fineLineColor);
		paint_date.setTextAlign(Paint.Align.CENTER);
		
		paint_brokenLine = new Paint();
		paint_brokenLine.setStrokeWidth(tb * 0.1f);
		paint_brokenLine.setColor(blueLineColor);
		paint_brokenLine.setAntiAlias(true);

		paint_dottedline = new Paint();
		paint_dottedline.setStyle(Paint.Style.STROKE);
		paint_dottedline.setColor(fineLineColor);

		paint_brokenline_big = new Paint();
		paint_brokenline_big.setStrokeWidth(tb * 0.4f);
		paint_brokenline_big.setColor(fineLineColor);
		paint_brokenline_big.setAntiAlias(true);
		
		framPanint = new Paint(); //渐变
        framPanint.setAntiAlias(true);
        framPanint.setStrokeWidth(2f);

		bitmap_point = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_point_blue);
		setLayoutParams(new LayoutParams(
				(int) ((this.daydatalistsize) * interval_width+interval_padding*2),
				(int) (interval_height+interval_padding*2)));
	}

	protected void onDraw(Canvas c) {
		if (daydatalistsize == 0)
			return;
		drawStraightLine(c);
		drawBrokenLine(c);
		drawDate(c);
	}

	/**
	 * 绘制竖线
	 * 
	 * @param c
	 */
	public void drawStraightLine(Canvas c) {
		paint_dottedline.setColor(fineLineColor);
//		int count_line = 0;
		for (int i = 0; i < daydatalistsize; i++) {
//			if (count_line == 0) {
//				c.drawLine(interval_left_right * i, 0, interval_left_right * i,
//						getHeight(), paint_date);
//			}
//			if (count_line == 2) {
//				c.drawLine(interval_left_right * i, tb * 1.5f,
//						interval_left_right * i, getHeight(), paint_date);
//			}
//			if (count_line == 1 || count_line == 3) {
				Path path = new Path();
				path.moveTo(interval_width * (i)+interval_padding, interval_padding);
				path.lineTo(interval_width *(i)+interval_padding, getHeight()-interval_padding);
				PathEffect effects = new DashPathEffect(new float[] { tb * 0.3f,
						tb * 0.3f, tb * 0.3f, tb * 0.3f }, tb * 0.1f);
				paint_dottedline.setPathEffect(effects);
				c.drawPath(path, paint_dottedline);
//			}
//			count_line++;
//			if (count_line >= 4) {
//				count_line = 0;
//			}
		}
//		c.drawLine(0, getHeight() - tb * 0.2f, getWidth(), getHeight() - tb
//				* 0.2f, paint_brokenline_big);
	}

	/**
	 * 绘制折线
	 * 
	 * @param c
	 */
	public void drawBrokenLine(Canvas c) {
//		int index = 0;
//		float temp_x = 0;
//		float temp_y = 0;
		long dayMax = (mcontext.getSharedPreferences(MenuActivity.DataSave, Context.MODE_PRIVATE)
                 .getLong("dayMax", 1));
		System.out.println("dayMax==="+dayMax);
		float base = interval_height/dayMax;

		
		System.out.println("getHeight()=="+getHeight());
		System.out.println("base=="+base);
        Shader mShader = new LinearGradient(interval_padding
                , getHeight() -  (base * daydatalist.get(0).getCount()) - interval_padding
                , interval_padding, getHeight(), new int[] {
            Color.argb(50, 0, 255, 255), Color.argb(25, 0, 255, 255),
            Color.argb(0, 0, 255, 255) }, null, Shader.TileMode.CLAMP);
        framPanint.setShader(mShader);
        Path path = new Path();
        path.moveTo(interval_padding, getHeight() -  (base * daydatalist.get(0).getCount()) - interval_padding);
		for (int i = 0; i < daydatalistsize; i++) {
			float x1 ;
			float y1 ;
			float x2 =0;
			float y2 =0;
			x1 = interval_width * i+interval_padding;
			y1 = getHeight() -  (base * daydatalist.get(i).getCount()) - interval_padding;
			
			    
                path.lineTo(x1, y1);

			if(i == daydatalistsize -1)  //渐变区域，最后一个点，划区域
			{
		        path.lineTo(x1, getHeight());
		        path.lineTo(interval_padding, getHeight());
		        path.close();
		        c.drawPath(path, framPanint);
			}
			if(i != daydatalistsize -1)  //当在最后一个点时，不需要下个线
			{
		         x2 = interval_width * (i+1)+interval_padding;
		         y2 = getHeight() -  (base * daydatalist.get(i + 1).getCount())- interval_padding;  
			}
			if(i != daydatalistsize-1)
			{
			    c.drawLine(x1, y1, x2, y2, paint_brokenLine);
			}
			
			
			        c.drawText(""+ daydatalist.get(i).getCount(),
			        x1 ,y1- bitmap_point.getHeight() / 2, paint_date);

					c.drawBitmap(bitmap_point,
							x1 - bitmap_point.getWidth() / 2,
							y1 - bitmap_point.getHeight() / 2, null);

		}
		

	}

	/**
	 * 绘制时间
	 * 
	 * @param c
	 */
	public void drawDate(Canvas c) {
		String date = "";
		for (int i = 0; i < daydatalistsize; i ++) {
		    String year = daydatalist.get(i).getYear()+"";
		    year = year.substring(2,year.length());
			date =year+"-"+setSmallNum(daydatalist.get(i).getMonth())
			        +"-"+setSmallNum(daydatalist.get(i).getDay());
			
			c.drawText(date, interval_width * i+interval_padding,
			        getHeight()- interval_textsize, paint_date);
		}

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
