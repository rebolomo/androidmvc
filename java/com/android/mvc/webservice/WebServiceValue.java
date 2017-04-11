package com.android.mvc.webservice;

import java.util.Map;

import com.android.mvc.message.WebMessageCode;

public class WebServiceValue
{
	public WebServiceValue (WebServiceType type, boolean isJson, String bURL, Map pList, 
			String authKey, String authValue)
	{
		_type = type;
		_isJson = isJson;
		_baseURL = bURL;
		_paramList = pList;
		_authKey = authKey;
		_authValue = authValue;
	}

	/// <summary>
	/// http request type
	/// </summary>
	public WebServiceType _type;
	/// <summary>
	/// is json or not
	/// </summary>
	public boolean _isJson;
	/// <summary>
	/// api url
	/// </summary>
	public String _baseURL;
	/// <summary>
	/// api code in client
	/// </summary>
	public WebMessageCode _webCode;
	/// <summary>
	/// param
	/// </summary>
	public Map _paramList;
	/// <summary>
	/// auth
	/// </summary>
	public String _authKey;
	public String _authValue;
}