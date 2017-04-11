package com.android.mvc.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.mvc.message.MobaMessageFunc;
import com.android.mvc.message.UIMessageManager;
import com.android.mvc.message.UIMessageType;
import com.android.mvc.util.Constants;
import com.android.mvc.util.Helper;

//	Since the version 22.1.0, the class ActionBarActivity is deprecated. We should use AppCompatActivity.
//	REBOL note, all activities are based from this ActivityBase
public class ActivityBase extends AppCompatActivity implements Constants {
    protected String TAG = "ActivityBase";
    
    private MobaMessageFunc backPressedCallback;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        UIMessageManager.SendMsg(UIMessageType.UI_Activity_ResumeFinished, this);
    }
    
    protected void OnCreateFinished() {
    	UIMessageManager.SendMsg(UIMessageType.UI_Activity_CreateFinished, this);
    }
    
    @Override
    public void onBackPressed(){
        try {
            Helper.invokeMethod(backPressedCallback.handler, backPressedCallback.callback, null);
        } catch (Exception e) {
        }
    }
    
	public void SetBackPressedCallback(MobaMessageFunc callback) {
    	backPressedCallback = callback;
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
	}
}