package com.android.mvc.controller;

import java.util.Arrays;
import java.util.List;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.mvc.R;
import com.android.mvc.common.BaseScreenCtrl;
import com.android.mvc.message.MobaMessage;
import com.android.mvc.message.MobaMessageFunc;
import com.android.mvc.message.OperationResponse;
import com.android.mvc.message.WebMessageCode;
import com.android.mvc.message.WebMessageManager;
import com.android.mvc.message.MobaMessageManager.MobaMessageType;
import com.android.mvc.model.ModelTools;
import com.android.mvc.model.data.UserData;
import com.android.mvc.util.Helper;
import com.android.mvc.util.LogTag;
import com.android.mvc.view.LoginActivity;
import com.android.mvc.webservice.NetworkHelper;
import com.android.mvc.webservice.WebReturnCode;

public class LoginScreenCtrl 
extends BaseScreenCtrl<LoginScreenCtrl, LoginActivity> 
implements OnClickListener{
    private int loginIndex = 0;
	private String username, password;
    private List<WebMessageCode> listCode;

	@Override
	protected void Init()
    {
		mScreen.SetSigninBtnCallback(this);

		MobaMessageFunc func = new MobaMessageFunc(this, "onOptionsItemSelected");
		mScreen.SetOptionsItemSelectedCallback(func);
		
		func = new MobaMessageFunc(this, "onBackPressed");
		mScreen.SetBackPressedCallback(func);
    }
	

	@Override
	public void RegisterUpdateHandler()
    {
        loginIndex = 0;
		WebMessageManager.AddListener_view(WebMessageCode.METHOD_ACCOUNT_LOGIN, this, 
				"OnGetMsg_METHOD_ACCOUNT_LOGIN");
    }

	@Override
    public void CancelUpdateHandler()
    {
		WebMessageManager.RemoveListener_view(WebMessageCode.METHOD_ACCOUNT_LOGIN, this, 
				"OnGetMsg_METHOD_ACCOUNT_LOGIN");
    }

    public void OnGetMsg (MobaMessage msg)
    {
        int msgID = 0;
        MobaMessageType type = WebMessageManager.ClientMsg_to_RawCode (
                /*Helper.int2enum(msg.ID(), ClientMsg.class)*/msg.ID());//, /*out */msgID);
        msgID = WebMessageManager.retCodeOut;
        
        WebMessageCode code = Helper.int2enum(msgID, WebMessageCode.class);
        if (listCode.contains(code)) {
            String methodName = "OnGetMsg_" + type.toString() + "_" + code.toString();

            try {
                Class<?>[] classes = { MobaMessage.class };
                Helper.invokeMethod(this, methodName, classes, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public Boolean checkUsername() {
        username = mScreen.getUsername();

        mScreen.SetErrorForUsername(null);
        Helper helper = new Helper();

        if (username.length() == 0) {
            mScreen.SetErrorForUsername(mScreen.getString(R.string.error_field_empty));
            return false;
        }

        if (username.length() < 5) {
            mScreen.SetErrorForUsername(mScreen.getString(R.string.error_small_username));
            return false;
        }

        if (!helper.isValidLogin(username) && !helper.isValidEmail(username)) {
            mScreen.SetErrorForUsername(mScreen.getString(R.string.error_wrong_format));
            return false;
        }

        return  true;
    }

    public Boolean checkPassword() {
        password = mScreen.getPassword();

        mScreen.SetErrorForPassword(null);
        Helper helper = new Helper();

        if (username.length() == 0) {
            mScreen.SetErrorForPassword(mScreen.getString(R.string.error_field_empty));
            return false;
        }

        if (password.length() < 6) {
            mScreen.SetErrorForPassword(mScreen.getString(R.string.error_small_password));
            
            return false;
        }

        if (!helper.isValidPassword(password)) {
            mScreen.SetErrorForPassword(mScreen.getString(R.string.error_wrong_format));
            
            return false;
        }

        return  true;
    }

    public void signin() {
    	username = mScreen.getUsername();
        //username = "test1";
    	password = mScreen.getPassword();
        //password = "123456";
        //  REBOL note, make your server ready at first.
        Toast.makeText(mContext, "make your server ready at first", Toast.LENGTH_SHORT).show();
                
    	//NetworkHelper.Instance().Login(username, password);
    }    
    
	private void OnGetMsg_METHOD_ACCOUNT_LOGIN(MobaMessage msg) {
		OperationResponse response = (OperationResponse)msg.Param() ;
        WebMessageCode code = Helper.int2enum(response.OperationCode, WebMessageCode.class);
        WebReturnCode ret = Helper.int2enum(response.ReturnCode, WebReturnCode.class);
        switch (ret)
        {
            case OK:
                {
                	UserData data = ModelTools.Get_userData_X();
                	Toast.makeText(mContext, "Login Success: "+ data.name, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
	}

	private void onBackPressed() {
		//mScreen.finish();
        this.CloseScreen();
	}

    //  REBOL note, equal to onOptionsItemSelected in activity
    private boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {

            case R.id.menu_item1: {	//	forget password
				//Singleton.getSingleton(RecoveryScreenCtrl.class).OpenView();
                return true;
            }

            case android.R.id.home: {

                //finish();
                this.CloseScreen();
                return true;
            }

            default: {
                return false;
            }
        }
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signinBtn:
            //if (!checkUsername() || !checkPassword()) {    //  REBOL todo, uncomment

            //} else {  	
                signin();
            //}
			break;			
		case R.id.signupBtn:
            Singleton.getSingleton(RegisterScreenCtrl.class).OpenScreen();
            break;
		default:
			break;
		}
	}
}
