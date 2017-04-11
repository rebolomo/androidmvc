package com.android.mvc.webservice;

import com.android.mvc.message.WebMessageCode;

/// <summary>
/// web success callback
/// </summary>
//public delegate void WebRequestCallback<T> (WebMessageCode code,T backObj,WebReturnCode ret);
public class WebRequestCallback<T> {
	public WebRequestCallback() {

	}

	public WebRequestCallback(Object handler, String callback) {
		this.handler = handler;
		this.callback = callback;
	}

	public Object handler;
	public String callback;
}
