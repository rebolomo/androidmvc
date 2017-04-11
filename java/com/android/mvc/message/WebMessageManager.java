package com.android.mvc.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.mvc.message.MobaMessageManager;
import com.android.mvc.message.MobaMessageManager.MobaMessageType;
import com.android.mvc.model.ModelManager;
import com.android.mvc.util.Helper;
import com.android.mvc.webservice.WebReturnCode;

public class WebMessageManager// : Singleton<WebMessageManager>
{
	static WebMessageManager mInstance;

	public static WebMessageManager Instance() {
		if (null == mInstance) {
			mInstance = new WebMessageManager ();
		}
		return mInstance;
	}

	public void SingletonCreate ()
	{
		WebMessageCode codes[] = WebMessageCode.values();
		int count = codes.length;
		for(int i = 0; i < count; i++) {
			WebMessageCode code = codes[i];

			//MobaMessageManager.RegistMessage (code, OnGetMsg);
			MobaMessageFunc func = new MobaMessageFunc(this, "OnGetMsg");
			MobaMessageManager.RegistMessage(code, func);		
		}
	}

	public  void SingletonDestroy ()
	{
		WebMessageCode codes[] = WebMessageCode.values();
		int count = codes.length;
		for(int i = 0; i < count; i++) {
			WebMessageCode code = codes[i];

			//MobaMessageManager.RegistMessage (code, OnGetMsg);
			MobaMessageFunc func = new MobaMessageFunc(this, "OnGetMsg");
			MobaMessageManager.UnRegistMessage(code, func);		
		}
	}

	//#endregion
	private static Map<WebMessageCode, List<MobaMessageFunc>> dicWebMessagePreHandler 
	= new HashMap<WebMessageCode, List<MobaMessageFunc>> ();

	public WebMessageManager ()
	{
	}

	/// pre handle of the message, and dispatch of the message.
	/// convert message from server to view/model message in client.
	/*private static*/public void OnGetMsg (MobaMessage msg)
	{
		if (null == msg) {
			return;
		}
		/*ClientMsg*/int subMsgID_toModel = 0;//Helper.int2enum(0, ClientMsg.class);
		/*ClientMsg*/int subMsgID_toView = 0;//Helper.int2enum(0, ClientMsg.class);
		switch (msg.mType) {
		case WebCode:
			subMsgID_toModel = Master_to_NotifyModel (Helper.int2enum(msg.mID, WebMessageCode.class));
			subMsgID_toView = Master_to_NotifyView (Helper.int2enum(msg.mID, WebMessageCode.class));
			break;
		default:
			break;
		}

		//	server message to client message
		//  notify model layer
		MobaMessage newMsgToModel = MobaMessageManager.GetMessage (
				/*(ClientMsg)*/subMsgID_toModel, msg.mParam, 0);
		MobaMessageManager.ExecuteMsg (newMsgToModel);

		//	notify view layer
		if (msg.isFinal) {
			MobaMessage newMsgToView = MobaMessageManager.GetMessage (
					/*(ClientMsg)*/subMsgID_toView, msg.mParam, 0);
			MobaMessageManager.ExecuteMsg (newMsgToView);
		}
	}

	public static void AddListener_model (WebMessageCode code, /*MobaMessageFunc func)*/
			Object handler, String callback) 
	{
		//Helper.logcat("WebMessage", " AddListener_model £º code = " + code);
		MobaMessageFunc func = new MobaMessageFunc(handler, callback);
		MobaMessageManager.RegistMessage (Master_to_NotifyModel (code), func);
	}

	public static void RemoveListener_model (WebMessageCode code, /*MobaMessageFunc func)*/
			Object handler, String callback) 
	{
		//Helper.logcat ("WebMessage", " RemoveListener_model £º code = " + code);
		MobaMessageFunc func = new MobaMessageFunc(handler, callback);
		MobaMessageManager.UnRegistMessage (Master_to_NotifyModel (code), func);
	}

	public static void AddListener_view (WebMessageCode code, Object handler, String callback)
	//MobaMessageFunc func)
	{
		//Helper.logcat ("WebMessage", " AddListener_view £º code = " + code);
		MobaMessageFunc func = new MobaMessageFunc(handler, callback);
		MobaMessageManager.RegistMessage (Master_to_NotifyView (code), func);
	}

	public static void RemoveListener_view (WebMessageCode code, Object handler, String callback) 
	//MobaMessageFunc func)
	{
		//Helper.logcat ("WebMessage", " RemoveListener_view £º code = " + code);
		MobaMessageFunc func = new MobaMessageFunc(handler, callback);
		MobaMessageManager.UnRegistMessage (Master_to_NotifyView (code), func);
	}

	public static int retCodeOut = 0;
	public static MobaMessageType ClientMsg_to_RawCode (/*ClientMsg*/int code/*, out int retCode*/)
	{
		MobaMessageType type = MobaMessageType.Client;
		int retCode = 0;
		int intCode = code;//Helper.enum2int(code);
		int beginModel = Helper.enum2int(ClientMsg.NotifyModel_master_begin);
		int endModel = Helper.enum2int(ClientMsg.NotifyModel_master_end);
		
		int beginView = Helper.enum2int(ClientMsg.NotifyView_master_begin);
		int endView = Helper.enum2int(ClientMsg.NotifyView_game_begin);
		
		if (intCode >= beginModel && intCode < endModel) {
			retCode = Helper.enum2int(NotifyModel_to_Master (code));
			type = MobaMessageType.WebCode;
		} else if (intCode >= beginView && intCode < endView) {
			retCode = Helper.enum2int(NotifyView_to_Master (code));
		} 
		
		WebMessageManager.retCodeOut = retCode;
		return type;
	}

	public static WebMessageCode NotifyModel_to_Master (/*ClientMsg*/int code)
	{
		int intCode = code;//Helper.enum2int(code);
		intCode = intCode - Helper.enum2int(ClientMsg.NotifyModel_master_begin);
		return Helper.int2enum(intCode, WebMessageCode.class);
	}

	public static WebMessageCode NotifyView_to_Master (/*ClientMsg*/int code)
	{
		int intCode = code;//Helper.enum2int(code);
		intCode = intCode - Helper.enum2int(ClientMsg.NotifyView_master_begin);
		return Helper.int2enum(intCode, WebMessageCode.class);
	}

	public static /*ClientMsg*/int Master_to_NotifyModel (WebMessageCode code)
	{
		int intCode = Helper.enum2int(ClientMsg.NotifyModel_master_begin);
		intCode = intCode + Helper.enum2int(code);
		return intCode;//Helper.int2enum(intCode, ClientMsg.class);
	}

	public static /*ClientMsg*/int Master_to_NotifyView (WebMessageCode code)
	{
		int intCode = Helper.enum2int(ClientMsg.NotifyView_master_begin);
		intCode = intCode + Helper.enum2int(code);
		return intCode;//Helper.int2enum(intCode, ClientMsg.class);
	}

	public static void SendMsg (WebMessageCode code, OperationResponse param) {
		SendMsg(code, param, false);
	}

	public static void SendMsg (WebMessageCode code, OperationResponse param, boolean isfinal)
	{
		WebReturnCode ret = Helper.int2enum(param.ReturnCode, WebReturnCode.class);
		//Helper.logcat ("WebMessage", " SendMsg £º code = " + code + " ret = " + ret);
		MobaMessage msg = MobaMessageManager.GetMessage (code, param, isfinal);
		MobaMessageManager.ExecuteMsg (msg);
	}
}