package com.dragon.planking.widget;

import com.dragon.planking.R;
import com.dragon.planking.MenuActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class AboutFragment extends Fragment
{
    private static String TAG ="AboutFragment";

    private View parentView;
    private ResideMenu resideMenu;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.about, container, false);
        initViews();
        return parentView;
    }
    
    @Override
    public void onPause()
    {
        // TODO Auto-generated method stub
        Log.v(TAG, TAG+"====onPause");
        super.onPause();
    }

    private void initViews()
    {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
    }

}
