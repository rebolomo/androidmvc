package com.android.mvc.webservice;

import java.util.Map;

import com.android.mvc.message.WebMessageCode;
import com.android.mvc.util.Helper;

public class WebRequest {
	public static String token = "";
}

////////////////////////////////////////////////////////
//	User
class RegisterRequest extends WebServiceBase
{
	public RegisterRequest (String username, String password)
	{
		super(WebAddress.GameAddress().concat("user/register"), 
				Helper.CreateHash(), WebServiceType.POST, username, password);
		code = WebMessageCode.METHOD_ACCOUNT_SIGNUP; 
	}

	//	REBOL note, use base OnSuccess
	@Override
	public void OnSuccess (String jString)
	{
		super.OnSuccess(jString);
	}
}

class LoginRequest extends WebServiceBase
{
	public LoginRequest (String username, String password)
	{
		super(WebAddress.GameAddress().concat("user/login"), 
				Helper.CreateHash(), WebServiceType.POST, username, password);
		code = WebMessageCode.METHOD_ACCOUNT_LOGIN; 
	}
}