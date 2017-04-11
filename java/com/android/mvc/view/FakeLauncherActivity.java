package com.android.mvc.view;

import android.os.Bundle;

import com.android.mvc.common.ActivityBase;
import com.android.mvc.controller.AppScreenCtrl;
import com.android.mvc.controller.Singleton;

public class FakeLauncherActivity extends ActivityBase{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		//	REBOL note, FakeLauncherActivity
        //Singleton.getSingleton(AppScreenCtrl.class).OpenView();
        Singleton.getSingleton(AppScreenCtrl.class).OpenScreen();
    }
}