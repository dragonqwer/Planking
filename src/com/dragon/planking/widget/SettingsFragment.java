package com.dragon.planking.widget;

import com.dragon.planking.MenuActivity;
import com.dragon.planking.R;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午3:28
 * Mail: specialcyci@gmail.com
 */

public class SettingsFragment extends Fragment
{

	private View parentView;
	private ResideMenu resideMenu;
	
	FeedbackAgent fb;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	parentView = inflater.inflate(R.layout.settings, container, false);
    	initfeedback();
    	initViews();
    	
        return parentView;
    }
    
    private void initfeedback()
    {
    	fb = new FeedbackAgent(getActivity());
    	// check if the app developer has replied to the feedback or not.
        fb.sync();
    }
    private void initViews()
    {
    	  MenuActivity parentActivity = (MenuActivity) getActivity();
          resideMenu = parentActivity.getResideMenu();

          parentView.findViewById(R.id.settings_Feedback).setOnClickListener(new View.OnClickListener()
          {
              @Override
              public void onClick(View view)
              {
            	  fb.startFeedbackActivity();
              }
          });
          parentView.findViewById(R.id.settings_update).setOnClickListener(new View.OnClickListener()
          {
        	  @Override
        	  public void onClick(View view)
        	  {
        		  UmengUpdateAgent.setDefault();
        		  UmengUpdateAgent.forceUpdate(getActivity());
        		  UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

					@Override
					public void onUpdateReturned(int arg0, UpdateResponse arg1) {
						// TODO Auto-generated method stub
						Log.v("test", "updateStatus==="+arg0);
					       switch (arg0) {
                           case 0: // 有更新
                               UmengUpdateAgent.showUpdateDialog(getActivity(), arg1);
                               break;
                           case 1: // 无更新
                               Toast.makeText(getActivity(), "当前已是最新版.", Toast.LENGTH_SHORT).show();
                               break;
                           case 2: // 如果设置为wifi下更新且wifi无法打开时调用
                               break;
                           case 3: // 连接超时
                      
                               break;
                           }
					}
        			  
        		  });
        		  
        		  UmengUpdateAgent.setDownloadListener(new UmengDownloadListener(){

					@Override
					public void OnDownloadEnd(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.v("test", "OnDownloadEnd arg0==="+arg0);
						Log.v("test", "OnDownloadEnd arg1==="+arg1);
					}

					@Override
					public void OnDownloadStart() {
						// TODO Auto-generated method stub
						Log.v("test", "OnDownloadStart");
					}

					@Override
					public void OnDownloadUpdate(int arg0) {
						// TODO Auto-generated method stub
						Log.v("test", "OnDownloadUpdate arg0==="+arg0);
					}
        			  
        		  });
        		  
        	  }
          });
    }

}
