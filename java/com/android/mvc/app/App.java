package com.android.mvc.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.android.mvc.common.ScreenCtrlManager;
import com.android.mvc.controller.Singleton;
import com.android.mvc.message.MobaMessageManager;
import com.android.mvc.message.WebMessageManager;
import com.android.mvc.model.ModelManager;
import com.android.mvc.util.Constants;

public class App extends Application implements Constants {

	public static final String TAG = App.class.getSimpleName();

	private static App mInstance;

	@Override
	public void onCreate() {

		super.onCreate();
        mInstance = this;

        InitBaseManager();
        Singleton.getSingleton(ScreenCtrlManager.class).Create(this.getApplicationContext(), this);
	}

	//	REBOL todo, application destroy
    //@Override
    public void onDestroy() {
        Singleton.getSingleton(ScreenCtrlManager.class).Destroy();
    }
    
    private void InitBaseManager()
    {
        ModelManager.Instance();//.Init();
        
        WebMessageManager.Instance().SingletonCreate();
       
        final MobaMessageManager mgr = new MobaMessageManager();
        final int TIME = 30;
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                // handler message loop
                try {
                    handler.postDelayed(this, TIME);
                    mgr.Update();
                    System.out.println("do...");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("exception...");
                }
            }
        };
    }

    public boolean isConnected() {
    	
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	
    	NetworkInfo netInfo = cm.getActiveNetworkInfo();
    	
    	if (netInfo != null && netInfo.isConnectedOrConnecting()) {
    		
    		return true;
    	}
    	
    	return false;
    }
    
    public static synchronized App getInstance() {
		return mInstance;
	}
}