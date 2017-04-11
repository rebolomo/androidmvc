package com.android.mvc.webservice;

//public delegate void webServiceFailedJson (WebReturnCode error);
public class WebServiceFailedJson {
	public WebServiceFailedJson() {

	}

	public WebServiceFailedJson(Object handler, String callback) {
		this.handler = handler;
		this.callback = callback;
	}

	public Object handler;
	public String callback;
}
