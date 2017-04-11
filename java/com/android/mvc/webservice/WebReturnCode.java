package com.android.mvc.webservice;    
public enum WebReturnCode 
{	OK(0),	TOKEN_ERROR(97),
	URL_ERROR(404),
	NORESULT(405),
	SYSTEM_ERROR(406),
	ERROR_UNAUTHOR_OR_AUTHOR_TIMEOUT(407),
	SERVICE_TIMEOUT(408),
	ERROR(409),
	ACCOUNT_PASSWORD_NOT_MATCH(1001), 
	ACCOUNT_OR_PASSWORD_EMPTY(1002), 
	ACCOUNT_ALREADY_REGISTERED(1003), 
	PERMISSION_LIMITED(1004), 
	INVALID_CREDENTIALS(1005), //'Invalid credentials' 
	ACCOUNT_NOT_FOUND(1006);
	
	private int value = 0;
	private WebReturnCode(int value) {   
		this.value = value;
	}
}