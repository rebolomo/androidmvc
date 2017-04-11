package com.android.mvc.controller;

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
import com.android.mvc.model.ModelTools;
import com.android.mvc.model.data.UserData;
import com.android.mvc.util.Helper;
import com.android.mvc.view.RegisterActivity;
import com.android.mvc.webservice.NetworkHelper;
import com.android.mvc.webservice.WebReturnCode;

public class RegisterScreenCtrl 
extends BaseScreenCtrl<RegisterScreenCtrl, RegisterActivity> 
implements OnClickListener{
	private String username, password, email;
	
	@Override
	protected void Init()
    {	
		mScreen.SetSignupBtnCallback(this);
        mScreen.SetTermLabelCallback(this);

        MobaMessageFunc func = new MobaMessageFunc(this, "onOptionsItemSelected");
        mScreen.SetOptionsItemSelectedCallback(func);
   
        func = new MobaMessageFunc(this, "onBackPressed");
        mScreen.SetBackPressedCallback(func);
    }

    private boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home: {

                this.CloseScreen();
                return true;
            }

            default: {
                return false;
            }
        }
    }

    private void onBackPressed() {
        //mScreen.finish();
        this.CloseScreen();
    }

	@Override
	public void RegisterUpdateHandler()
    {
		super.RegisterUpdateHandler();
		WebMessageManager.AddListener_view(WebMessageCode.METHOD_ACCOUNT_SIGNUP, this, 
				"OnGetMsg_METHOD_ACCOUNT_SIGNUP");
    }

	@Override
    public void CancelUpdateHandler()
    {
		super.CancelUpdateHandler();
		WebMessageManager.RemoveListener_view(WebMessageCode.METHOD_ACCOUNT_SIGNUP, this, 
				"OnGetMsg_METHOD_ACCOUNT_SIGNUP");
    }
	
	private void OnGetMsg_METHOD_ACCOUNT_SIGNUP(MobaMessage msg) {
		OperationResponse response = (OperationResponse)msg.Param() ;
        WebMessageCode code = Helper.int2enum(response.OperationCode, WebMessageCode.class);
        WebReturnCode ret = Helper.int2enum(response.ReturnCode, WebReturnCode.class);
        switch (ret)
        {
            case OK:
                {
                	UserData data = ModelTools.Get_userData_X();
                	Toast.makeText(mContext, "Register Success: " + data.name, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//	REBOL note, 
		switch (v.getId()) {
		case R.id.signupBtn:
            if (!checkUsername() || !checkPassword() || !checkEmail()) {


            } else {
            	//	REBOL note, test   	
                register();
            }
		
			break;			
		case R.id.signinBtn:
			Singleton.getSingleton(LoginScreenCtrl.class).OpenScreen();
			break;	
		default:
			break;
		}
	}
	
    public void register() {
        //username = mScreen.getUsername();
        username = "test2";
        //password = mScreen.getPassword();
        password = "123456";
        NetworkHelper.Instance().Register(username, password);
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

    public Boolean checkEmail() {

        email = mScreen.getEmail(); 

        Helper helper = new Helper();

        if (email.length() == 0) {
            mScreen.SetErrorForEmail(mScreen.getString(R.string.error_field_empty));
            
            return false;
        }

        if (!helper.isValidEmail(email)) {
            mScreen.SetErrorForEmail(mScreen.getString(R.string.error_wrong_format));
            
            return false;
        }

        mScreen.SetErrorForEmail(null);
            
        return true;
    }

    public Boolean verifyRegForm() {
        mScreen.SetErrorForUsername(null);
        mScreen.SetErrorForPassword(null);
        mScreen.SetErrorForEmail(null);

        Helper helper = new Helper();

        if (username.length() == 0) {
            mScreen.SetErrorForUsername(mScreen.getString(R.string.error_field_empty));
            
            return false;
        }

        if (username.length() < 5) {
            mScreen.SetErrorForUsername(mScreen.getString(R.string.error_small_username));
            
            return false;
        }

        if (!helper.isValidLogin(username)) {
            mScreen.SetErrorForUsername(mScreen.getString(R.string.error_wrong_format));
            
            return false;
        }

        if (password.length() == 0) {
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

        if (email.length() == 0) {
            mScreen.SetErrorForEmail(mScreen.getString(R.string.error_field_empty));
            
            return false;
        }

        if (!helper.isValidEmail(email)) {
            mScreen.SetErrorForEmail(mScreen.getString(R.string.error_wrong_format));
            
            return false;
        }

        return true;
    }
}
