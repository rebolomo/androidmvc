package com.android.mvc.common;

import android.app.Application;
import android.content.Context;

import com.android.mvc.controller.AppScreenCtrl;
import com.android.mvc.controller.LoginScreenCtrl;
import com.android.mvc.controller.RegisterScreenCtrl;
import com.android.mvc.controller.Singleton;
import com.android.mvc.util.Helper;
import com.android.mvc.util.LogTag;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ScreenCtrlManager extends Singleton<ScreenCtrlManager> { 
	
    protected static Context context;
    protected static String appName;
    
    //	All screen ctrls opened.
    private static List<BaseScreenCtrl> mCtrls = Collections
            .synchronizedList(new LinkedList<BaseScreenCtrl>());
    
    public void Destroy() {
    	Singleton.getSingleton(PopupManager.class).Destroy();
    }
    
    //@Override
    public void Create(Context context, Application app) {
        //super.onCreate();
        this.context = context;//this.getApplicationContext();    
        appName =  getAppNameFromSub();
        //registerActivityListener(app);

        //  REBOL note, create all the controls
        //  REBOL note, context is passed at beginning.
        Singleton.getSingleton(PopupManager.class).Create(context);

        //  Any screen ctrls should be reigstered here.
        Singleton.getSingleton(AppScreenCtrl.class).Create(context);
        Singleton.getSingleton(LoginScreenCtrl.class).Create(context);
        Singleton.getSingleton(RegisterScreenCtrl.class).Create(context);
    }

    public static String getAppName() {
        return appName;
    }

    public static Context getContext() {
        return context;
    }

    protected String  getAppNameFromSub() {
    	return "test";
    }

    public void pushCtrl(BaseScreenCtrl ctrl) {
        mCtrls.add(ctrl);
        Helper.logcat(LogTag.Control, "activityList:size:"+mCtrls.size());
    }

    public void popCtrl(BaseScreenCtrl ctrl) {
        mCtrls.remove(ctrl);
        Helper.logcat(LogTag.Control,"activityList:size:"+mCtrls.size());
    }

    public static BaseScreenCtrl currentCtrl() {
        if (mCtrls == null||mCtrls.isEmpty()) {
            return null;
        }
        BaseScreenCtrl ctrl = mCtrls.get(mCtrls.size()-1);
        return ctrl;
    }

    public static void finishCurrentCtrl() {
        if (mCtrls == null||mCtrls.isEmpty()) {
            return;
        }
        BaseScreenCtrl ctrl = mCtrls.get(mCtrls.size()-1);
        finishCtrl(ctrl);
    }

    public static void finishCtrl(BaseScreenCtrl ctrl) {
        if (mCtrls == null||mCtrls.isEmpty()) {
            return;
        }
        if (ctrl != null) {
            mCtrls.remove(ctrl);
            //activity.finish();
            ctrl = null;
        }
    }
    /*
    public String getRunningActivityName() {  
    	ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
    	String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();  
    	return runningActivity;  
    }// getRunningActivityName  
    */
}