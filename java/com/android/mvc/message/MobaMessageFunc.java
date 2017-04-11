package com.android.mvc.message;

/// <summary>
//  call back delegate.
// / </summary>
// / <param name="msg"></param>
//public delegate void MobaMessageFunc (MobaMessage msg);
public class MobaMessageFunc {
	public MobaMessageFunc() {

	}

	public MobaMessageFunc(Object handler, String callback) {
		this.handler = handler;
		this.callback = callback;
	}

	public Object handler;
	public String callback;
}
