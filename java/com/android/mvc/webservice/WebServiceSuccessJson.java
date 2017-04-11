package com.android.mvc.webservice;

//public delegate void webServiceSuccessJson (string text);
public class WebServiceSuccessJson {
	public WebServiceSuccessJson() {

	}

	public WebServiceSuccessJson(Object handler, String callback) {
		this.handler = handler;
		this.callback = callback;
	}

	public Object handler;
	public String callback;
}
