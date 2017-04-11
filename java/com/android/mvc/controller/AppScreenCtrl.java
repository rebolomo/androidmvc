package com.android.mvc.controller;

import com.android.mvc.R;
import com.android.mvc.common.BaseScreenCtrl;
import com.android.mvc.view.AppActivity;

import android.view.View;
import android.view.View.OnClickListener;

public class AppScreenCtrl extends BaseScreenCtrl<AppScreenCtrl, AppActivity> implements OnClickListener{
	
	@Override
	protected void Init()
    {
		//		REBOL note, after activity created, Init will be called
		mScreen.SetLoginBtnCallback(this);
		mScreen.SetSignupBtnCallback(this);
    }
	
	@Override
	public void RegisterUpdateHandler()
    {
		super.RegisterUpdateHandler();
    }

	@Override
    public void CancelUpdateHandler()
    {
		super.CancelUpdateHandler();
    }
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			Singleton.getSingleton(LoginScreenCtrl.class).OpenScreen();
			break;			
		case R.id.signupBtn:
			Singleton.getSingleton(RegisterScreenCtrl.class).OpenScreen();
		default:
			break;
		}
	}
}
