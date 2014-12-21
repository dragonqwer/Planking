package com.dragon.planking;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.dragon.planking.R;
import com.dragon.planking.model.Data;
import com.dragon.planking.model.DayData;
import com.dragon.planking.sql.DBManager;
import com.dragon.planking.widget.Abutton;
import com.dragon.planking.widget.CountdownFragment;
import com.dragon.planking.widget.AboutFragment;
import com.dragon.planking.widget.MyDialog;
import com.dragon.planking.widget.TimerFragment;
import com.dragon.planking.widget.DataFragment;
import com.dragon.planking.widget.ResideMenu;
import com.dragon.planking.widget.ResideMenuItem;
import com.dragon.planking.widget.SettingsFragment;
import com.umeng.update.UmengUpdateAgent;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MenuActivity extends FragmentActivity implements View.OnClickListener
{
	
    private static String TAG = "MenuActivity";
    public static String DataSave = "DataSave";
    private  Resources res;
    private ResideMenu resideMenu;
    private MenuActivity mContext;
    private TextView title;

    private ResideMenuItem itemTimer;
    private ResideMenuItem itemCountdown;
    private ResideMenuItem itemData;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemAbout;
    
    private boolean isexit = false;
    private boolean hastask = false;
    
    private Abutton mAbutton;
    
    private TimerFragment mTimerFragment;
    private CountdownFragment mCountdownFragment;
    private DataFragment mDataFragment;
    private SettingsFragment mSettingsFragment;
    private AboutFragment mAboutFragment;
    

    Timer texit = new Timer();

    public DBManager dm;
    
    public ArrayList<DayData> daydatalist;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        res = getResources();
        mContext = this;
        dm =new DBManager(this);
        initMenu();
        Log.v(TAG, "dm.queryDataList()== "+dm.queryDataList().toString());
        daydatalist = getDayDataList(dm.queryDataList());
        
        Log.v(TAG, "daydatalist.size== "+daydatalist.size());
        for(int i=0;i<daydatalist.size();i++)
        {
            Log.v(TAG, "daydata=i= "+daydatalist.get(i).toString());
            Log.v(TAG, "daydata=i=getDatalist().size() "+daydatalist.get(i).getDatalist().size());
        }
        mTimerFragment = new TimerFragment();
        changeFragment(mTimerFragment,R.layout.timer);
        
        UmengUpdateAgent.update(this);  
        UmengUpdateAgent.setUpdateAutoPopup(true); 
    }
    
    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                if(daydatalist.size() != 0)
                { 
                    long dayMax =0;
                    for(int i=0;i<daydatalist.size();i++)
                    {
                        if(daydatalist.get(i).getCount() >dayMax)
                        {
                            dayMax = daydatalist.get(i).getCount();
                        }
                    }
                    
                    SharedPreferences DataSave = MenuActivity.this.
                            getSharedPreferences(MenuActivity.DataSave, Context.MODE_PRIVATE);
                    System.out.println("putlong===dayMax"+dayMax);
                    DataSave.edit().putLong("dayMax", dayMax).commit();
                }
            }
        }).start();
        
        super.onStart();
    }
    
    

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        dm.closeDB();
        super.onDestroy();
    }

    private void initMenu()
    {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.blue);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        // valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.72f);

        // create menu items;
        itemTimer = new ResideMenuItem(this, R.drawable.icon_timer,res.getString(R.string.title_timer) );
        itemCountdown = new ResideMenuItem(this, R.drawable.icon_countdown, res.getString(R.string.title_countdown));
        itemData = new ResideMenuItem(this, R.drawable.icon_data, res.getString(R.string.title_data));
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, res.getString(R.string.title_settings));
        itemAbout= new ResideMenuItem(this, R.drawable.icon_about, res.getString(R.string.title_about));

        itemTimer.setOnClickListener(this);
        itemCountdown.setOnClickListener(this);
        itemData.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemAbout.setOnClickListener(this);

        resideMenu.addMenuItem(itemTimer, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCountdown, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemData, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
       
         resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
         
         title = (TextView) findViewById(R.id.title);
         
         mAbutton = (Abutton)findViewById(R.id.title_bar_left_menu);
         mAbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {   
                Log.v(TAG, "Abutton onclick");
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                
                /****test dialog***/
                MyDialog test =new MyDialog(MenuActivity.this);
                test.showDialog(R.layout.signdialog_layout);
            }
        });
//        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view)
    {

        if (view == itemTimer)
        {
            if(mTimerFragment ==null)
            mTimerFragment =new TimerFragment();
            changeFragment(mTimerFragment,R.layout.timer);
        }
        else if (view == itemCountdown)
        {
            if(mCountdownFragment ==null)
            mCountdownFragment = new CountdownFragment();
            changeFragment(mCountdownFragment,R.layout.countdown);
        }
        else if (view == itemData)
        {
            if(mDataFragment ==null)
            mDataFragment = new DataFragment();
            changeFragment(mDataFragment,R.layout.data);
        }
        else if (view == itemSettings)
        {
            if(mSettingsFragment ==null)
            mSettingsFragment = new SettingsFragment();
            changeFragment(mSettingsFragment,R.layout.settings);
        }
        else if (view == itemAbout)
        {
            if(mAboutFragment ==null)
            mAboutFragment = new AboutFragment();
            changeFragment(mAboutFragment,R.layout.about);
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener()
    {
        @Override
        public void openMenu()
        {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "Menu is opened!");
            mAbutton.setActionId(Abutton.BLACK, Abutton.ROTATE_COUNTER_CLOCKWISE);
        }

        @Override
        public void closeMenu()
        {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "Menu is closed!");
            mAbutton.setActionId(Abutton.DRAWER, Abutton.ROTATE_CLOCKWISE);
        }
    };

    private void changeFragment(Fragment targetFragment,int id)
    {
        switch (id)
        {
            case R.layout.timer:
                title.setText(res.getString(R.string.title_timer));
                break;
            case R.layout.countdown:
                title.setText(res.getString(R.string.title_countdown));
                break;
            case R.layout.data:
                title.setText(res.getString(R.string.title_data));
                break;
            case R.layout.settings:
                title.setText(res.getString(R.string.title_settings));
                break;
            case R.layout.about:
                title.setText(res.getString(R.string.title_about));
                break;

            default:
                break;
        }
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
    
    /**
     * 按日期获取日期表（DayData）
     * @param datalist
     * @return
     */
    public ArrayList<DayData> getDayDataList(ArrayList<Data> datalist)
    {
        ArrayList<DayData> daydatalist = new ArrayList<DayData>();  
        
        if(datalist.size()>1)
        {
            DayData daydata =new DayData();
            daydata.setYear(datalist.get(0).getDate_y());
            daydata.setMonth(datalist.get(0).getDate_mo());
            daydata.setDay(datalist.get(0).getDate_d());
            daydata.addData(datalist.get(0));
            while (datalist.size() >=2)
            {
                
                if(isSameDay(datalist.get(0),datalist.get(1)))
                {
                    daydata.addData(datalist.get(1));
                    datalist.remove(0);
                    
                }else{
                    daydatalist.add(daydata);
                    daydata =new DayData();
                    daydata.setYear(datalist.get(1).getDate_y());
                    daydata.setMonth(datalist.get(1).getDate_mo());
                    daydata.setDay(datalist.get(1).getDate_d());
                    daydata.addData(datalist.get(1));
                    datalist.remove(0);
                }
            }
            daydatalist.add(daydata);
        }else if(datalist.size() ==1){
            DayData daydata =new DayData();
            daydata.setYear(datalist.get(0).getDate_y());
            daydata.setMonth(datalist.get(0).getDate_mo());
            daydata.setDay(datalist.get(0).getDate_d());
            daydata.addData(datalist.get(0));
            daydatalist.add(daydata);
        }

        return daydatalist;
    }
    
    /**
     * 用于判断是不是同一天
     * @param fdata
     * @param sdata
     * @return
     */
    public boolean isSameDay(Data fdata,Data sdata)
    {
        boolean issame =false;
        
        if(fdata.getDate_y() == sdata.getDate_y()){
            if(fdata.getDate_mo() == sdata.getDate_mo())
            {
                if(fdata.getDate_d() == sdata.getDate_d())
                {
                    issame =true;
                }else{
                    issame =false;
                }
            }else{
                issame =false;
            }
        }else{
            issame = false;
        }
        
        return issame;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub
        if(KeyEvent.KEYCODE_BACK == keyCode)
        {
            if(resideMenu.isOpened())
            {
                resideMenu.closeMenu();
                return true;
            }else{
                if (isexit == false) {
                    isexit = true;
                    Log.v(TAG, "ISEXIT");
                    Toast.makeText(MenuActivity.this, "再按一次退出程序",
                            Toast.LENGTH_SHORT).show();
                   
                    if (!hastask) {
                        TimerTask task = new TimerTask() {
                            public void run() {
                                isexit = false;
                                hastask = false;
                                Log.v(TAG, "TimerTask.RUN");
                            }
                        };
                        texit.schedule(task, 2000);
                    }
                    hastask = true;
                } else {

                    MenuActivity.this.finish();

                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    // What good method is to access resideMenu？
    public ResideMenu getResideMenu()
    {
        return resideMenu;
    }
}
