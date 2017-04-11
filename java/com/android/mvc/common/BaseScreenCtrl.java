package com.android.mvc.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.mvc.controller.Singleton;
import com.android.mvc.message.MobaMessage;
import com.android.mvc.message.UIMessageManager;
import com.android.mvc.message.UIMessageType;

public class BaseScreenCtrl<K, T extends ActivityBase> extends Singleton<K> {
	protected T mScreen;
	protected Context mContext;
	public Object Screen() { return mScreen; } 
	
	protected void ScreenTo(Object value) { mScreen = (T)value; }

	public void Create (Context context)
	{
		mContext = context;
	}

    public /*virtual*/ void OpenScreen() {
    	//	REBOL note, cancel current ctrl listener
        BaseScreenCtrl currentCtrl = Singleton.getSingleton(ScreenCtrlManager.class).currentCtrl();
        if(currentCtrl != null) { 
        	currentCtrl.CancelUpdateHandler();
        }
    	//	manage the ctrl
        Singleton.getSingleton(ScreenCtrlManager.class).pushCtrl(this);
        //	change activity
        //	REBOL note, 
        Class<T> entityClass = null;
        Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            entityClass = (Class<T>)p[1];
        }

        RegisterPreHandler();
		//  REBOL note, all ctrl have its own activity, open it now
        ChangeActivity(entityClass);    
    }	

    public /*virtual*/ void CloseScreen()
    {	
        HandleBeforeCloseView(); //
        CancelUpdateHandler(); //
        
        Singleton.getSingleton(ScreenCtrlManager.class).popCtrl(this);
 
        BaseScreenCtrl currentCtrl = Singleton.getSingleton(ScreenCtrlManager.class).currentCtrl();
        if(currentCtrl != null) {
        	//	REBOL note, register the resume listener, then we finish current activity, the previous activity will be resumed.
        	currentCtrl.RegisterResumeHandler();
        }

        //  REBOL note, finish activity here, the last activity will be resumed，thus send resume message to currentCtrl
        Destroy();
    }

    protected /*virtual*/ void InitWrapper(MobaMessage msg)
    {
    	CancelPreHandler();
    	CancelResumeHandler();
        Activity activity = (Activity)msg.Param();
        String id = activity.toString();
        mScreen = (T) activity;

        Init();
        RegisterUpdateHandler();
        //  REBOL note, can update after activity created
        Update();
    }

    protected /*virtual*/ void Init()
    {

    }

    /// <summary>
    ///	 Play effect, animation, etc
    /// </summary>
    protected /*virtual*/ void HandleAfterOpenView()
    {
    }

    /// <summary>
    ///	 Stop effect, animation, etc
    /// </summary>
    protected /*virtual*/ void HandleBeforeCloseView()
    {
    }

    /// <summary>
    /// Update the view
    /// </summary>
    protected /*virtual*/ void Update()
    {
    }

    /// <summary>
    /// Finish the activity now
    /// </summary>
    protected /*virtual*/ void Destroy()
    {
    	/*
    	if(mScreen == null) {
    		return;
    	}
    	*/
        Activity a = (Activity)mScreen;
        String id = a.toString();
        a.finish();
        /*
        //	REBOL note, the current activity
        String currentActivityName = 
        		Singleton.getSingleton(ScreenCtrlManager.class).getRunningActivityName();
        Helper.logcat(LogTag.Control, currentActivityName);
        */
        //mScreen = null;
    }

    /// <summary>
    /// Register callback for activity created event
    /// </summary>
    public /*virtual*/ void RegisterPreHandler()
    {
        //  REBOL note, most of the activities send message at the end of OnCreate, we can then get the view controls in the callback.
    	UIMessageManager.AddListener(UIMessageType.UI_Activity_CreateFinished, this, "InitWrapper");
    }
    
    public /*virtual*/ void RegisterResumeHandler()
    {
    	UIMessageManager.AddListener(UIMessageType.UI_Activity_ResumeFinished, this, "InitWrapper");
    }

    public /*virtual*/ void CancelPreHandler()
    {
        UIMessageManager.RemoveListener(UIMessageType.UI_Activity_CreateFinished, this, "InitWrapper");
    }
    
    public /*virtual*/ void CancelResumeHandler()
    {
    	UIMessageManager.RemoveListener(UIMessageType.UI_Activity_ResumeFinished, this, "InitWrapper");
    }

    //  All child screen ctrl override this function.
    public /*virtual*/ void RegisterUpdateHandler()
    {
        
    }

    public /*virtual*/ void CancelUpdateHandler()
    {
        
    }

    //  Will call onCreate, and the UI_Activity_CreateFinished message triggered.
    protected void ChangeActivity(Class c) {
        Intent startActivity = new Intent();  
        startActivity.setClass(this.mContext, c); 
        startActivity.setAction(c.getName()); 
        startActivity.setFlags( 
                  Intent.FLAG_ACTIVITY_NEW_TASK 
                  | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
        mContext.startActivity(startActivity); 
    }
}