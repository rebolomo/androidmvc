package com.android.mvc.message;

import com.android.mvc.util.Helper;

public class UIMessageManager// :Singleton<UIMessageManager>
{
	// / <summary>
	// / message code translate.
	// / </summary>
	// / <returns>The msg_to_ client message.</returns>
	// / <param name="code">Code.</param>
	private static /*ClientMsg*/int UIMsg_to_ClientMsg(UIMessageType code) {
		int intCode = Helper.enum2int(code);
		return intCode;//Helper.int2enum(intCode, ClientMsg.class);
	}

	// #endregion

	// #region called in public
	// / <summary>
	// / Adds the listener.
	// / </summary>
	// / <param name="code">Code.</param>
	// / <param name="func">Func.</param>
	public static void AddListener(UIMessageType code, 
			/*MobaMessageFunc func*/Object handler, String callback) {
		// ClientLogger.Debug ("UIMsg", "==>UIMessageManager AddListener : " +
		// code);
		MobaMessageFunc func = new MobaMessageFunc(handler, callback);
		//Helper.logcat("UIMsg", "==>UIMessageManager AddListener : " + code);
		MobaMessageManager.RegistMessage(UIMsg_to_ClientMsg(code), func);
	}

	// / <summary>
	// / Removes the listener.
	// / </summary>
	// / <param name="code">Code.</param>
	// / <param name="func">Func.</param>
	public static void RemoveListener(UIMessageType code,
			/*MobaMessageFunc func*/Object handler, String callback) {
		// ClientLogger.Debug ("UIMsg", "==>UIMessageManager RemoveListener : "
		// + code);
		MobaMessageFunc func = new MobaMessageFunc(handler, callback);
		//Helper.logcat("UIMsg", "==>UIMessageManager RemoveListener : " + code);
		MobaMessageManager.UnRegistMessage(UIMsg_to_ClientMsg(code), func);
	}

	public static void SendMsg(UIMessageType code)
	{
		SendMsg(code, null, 0.0f);
	}
	
	public static void SendMsg(UIMessageType code, Object param)
	{
		SendMsg(code, param, 0.0f);
	}
	// / <summary>
	// / send message
	// / </summary>
	// / <returns><c>true</c>, if c_ battle_ start was c2ed, <c>false</c>
	// otherwise.</returns>
	public static void SendMsg(UIMessageType code, Object param/*=null*/, float delayTime/* = 0.0f*/) {
		// ClientLogger.Debug ("UIMsg", "==>UIMessageManager SendMsg : " + code
		// + " " + (param != null ? param : ""));
		//Helper.logcat("UIMsg", "==>UIMessageManager SendMsg : " + code + " " + (param != null ? param : ""));
		MobaMessage newMsg = MobaMessageManager.GetMessage(/*(ClientMsg)*/ Helper.enum2int(code), param, delayTime);
		//MobaMessageManager.DispatchMsg(newMsg);
		//		REBOL note, instant mode
		MobaMessageManager.ExecuteMsg(newMsg);
	}
	// #endregion
}
