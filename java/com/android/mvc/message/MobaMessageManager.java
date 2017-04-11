package com.android.mvc.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.android.mvc.util.Helper;

/// <summary>
/// message manager
/// </summary>
public class MobaMessageManager {
	// / <summary>
	// / message type
	// / </summary>
	public enum MobaMessageType {
		
		Client(0), // message code from client
		WebCode(1); // message code from server side.
		
		private int value = 0;

	    private MobaMessageType(int value) { 
	        this.value = value;
	    }
	}

	// #region members
	private static 
	Map<MobaMessageType, Map<Integer, List<MobaMessageFunc>>> 
	mMessageFuncMap = new HashMap<MobaMessageType, Map<Integer, List<MobaMessageFunc>>>();

	private static Queue<MobaMessage> mMessageQueue = new LinkedList<MobaMessage>();

	// #endregion

	// #region Awake & Start
	void MobaMessageManager() {
		// mMessageFuncMap.Clear();
		// mMessageQueue.Clear();
	}

	// #endregion

	// #region message register and unregister

	// / <summary>
	// / Regists the message.
	// / </summary>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgFunc">Message func.</param>
	public static void RegistMessage(WebMessageCode msgID, MobaMessageFunc msgFunc) {
		RegistMessage(MobaMessageType.WebCode, Helper.enum2int(msgID), msgFunc);
	}

	// / <summary>
	// / Uns the regist message.
	// / </summary>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgFunc">Message func.</param>
	public static void UnRegistMessage(WebMessageCode msgID, MobaMessageFunc msgFunc) {
		UnRegistMessage(MobaMessageType.WebCode, Helper.enum2int(msgID), msgFunc);
	}

	// / <summary>
	// / Regists the message.
	// / </summary>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgFunc">Message func.</param>
	public static void RegistMessage(/*ClientMsg*/int msgID, MobaMessageFunc msgFunc) {
		RegistMessage(MobaMessageType.Client, Helper.enum2int(msgID), msgFunc);
	}

	// / <summary>
	// / Uns the regist message.
	// / </summary>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgFunc">Message func.</param>
	public static void UnRegistMessage(/*ClientMsg*/int msgID, MobaMessageFunc msgFunc) {
		UnRegistMessage(MobaMessageType.Client, Helper.enum2int(msgID), msgFunc);
	}

	/*	REBOL rmv, no use now.
	public static int CallbackCount(int msgID) {
		Map<Integer, MobaMessageFunc> funcMap = mMessageFuncMap.get(MobaMessageType.Client);
		
		if (funcMap.containsKey(msgID)) {
			//	REBOL TODO
			if (funcMap.get(msgID) != null && funcMap.get(msgID).GetInvocationList ().Length > 0) {
				return funcMap [msgID].GetInvocationList ().Length;
			}
		}

		return 0;
	}
	*/
	// #endregion

	// #region internal
	private static void RegistMessage(MobaMessageType type, int msgID, MobaMessageFunc msgFunc) {
		if (mMessageFuncMap.containsKey(type) == false) {
			mMessageFuncMap.put(type, new HashMap<Integer, List<MobaMessageFunc>>());
		}

		Map<Integer, List<MobaMessageFunc>> funcMap = mMessageFuncMap.get(type);

		if (funcMap.containsKey(msgID)) {
			funcMap.get(msgID).add(msgFunc);
		} else {
			List<MobaMessageFunc> funcList = new ArrayList<MobaMessageFunc>();
			funcList.add(msgFunc);
			funcMap.put(msgID, funcList);
		}
	}

	private static void UnRegistMessage(MobaMessageType msgType, int msgID, MobaMessageFunc msgFunc) {
		if (mMessageFuncMap.containsKey(msgType) == false)
			return;

		Map<Integer, List<MobaMessageFunc>> funcMap = mMessageFuncMap.get(msgType);
		if (funcMap.containsKey(msgID)) {
			// REBOL note, find the callback
			List<MobaMessageFunc> funcList = funcMap.get(msgID);
			for (int i = 0; i < funcList.size(); i++) {
				MobaMessageFunc func = funcList.get(i);
				if (func.handler == msgFunc.handler && func.callback == msgFunc.callback) {
					funcList.remove(i);
					break;
				}
			}
			// funcMap.get(msgID).remove(msgFunc);
		}
	}

	// #endregion

	// #region message dispatch and execute
	// / <summary>
	// / message be sent-> message manager loop-> check if delay -> execute the message callback
	// / </summary>
	// / <param name="msg"></param>
	public static void DispatchMsg(MobaMessage msg) {
		mMessageQueue.add(msg);
	}

	// / <summary>
	// / execute message at once
	// / </summary>
	// / <param name="msg"></param>
	public static void ExecuteMsg(MobaMessage msg) {
		//Helper.logcat("MsgHandler",
		//		"==>MobaMessageManager ExecuteMsg : type = " + msg.MessageType() + " id = " + msg.ID());
		if (mMessageFuncMap.containsKey(msg.MessageType())) {
			if (mMessageFuncMap.get(msg.MessageType()).containsKey(msg.ID())) {
				List<MobaMessageFunc> func = mMessageFuncMap.get(msg.MessageType()).get(msg.ID());
				if (func != null) {
					// func.Invoke (msg);
					for (int i = 0; i < func.size(); i++) {
						Class<?>[] classes = { MobaMessage.class };
						try {
							Helper.invokeMethod(func.get(i).handler, func.get(i).callback, classes, msg);
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	// #endregion

	// #region create message

	// / <summary>
	// / Gets the message.
	// / </summary>
	// / <returns>The message.</returns>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgParam">Message parameter.</param>
	public static MobaMessage GetMessage(ClientMsg msgID, Object msgParam) {
		return GetMessage(MobaMessageType.Client, Helper.enum2int(msgID), msgParam, 0.0f, 0, false);
	}

	// / <summary>
	// / Gets the message.
	// / </summary>
	// / <returns>The message.</returns>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgParam">Message parameter.</param>
	// / <param name="unit_id">Unit_id.</param>
	public static MobaMessage GetMessage(ClientMsg msgID, Object msgParam, int unit_id) {
		return GetMessage(MobaMessageType.Client, Helper.enum2int(msgID), msgParam, 0.0f, unit_id, false);
	}

	// / <summary>
	// / Gets the message.
	// / </summary>
	// / <returns>The message.</returns>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgParam">Message parameter.</param>
	// / <param name="delayTime">Delay time.</param>
	public static MobaMessage GetMessage(/*ClientMsg*/int msgID, Object msgParam, float delayTime) {
		return GetMessage(MobaMessageType.Client, msgID, msgParam, delayTime, 0, false);
	}

	// / <summary>
	// / </summary>
	// / <returns>The message.</returns>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgParam">Message parameter.</param>
	public static MobaMessage GetMessage(WebMessageCode msgID, Object msgParam, boolean isfinal/* = false*/) {
		return GetMessage(MobaMessageType.WebCode, Helper.enum2int(msgID), msgParam, 0.0f, 0, isfinal);
	}

	// / <summary>
	// / Gets the message.
	// / </summary>
	// / <returns>The message.</returns>
	// / <param name="msgType">Message type.</param>
	// / <param name="msgID">Message I.</param>
	// / <param name="msgParam">Message parameter.</param>
	// / <param name="msgDelayTime">Message delay time.</param>
	// / <param name="unit_id">Unit_id.</param>
	private static MobaMessage GetMessage(MobaMessageType msgType, int msgID, Object msgParam, float msgDelayTime,
			int unit_id/*=0*/, boolean isfinal/*=false*/) {
		MobaMessage msg = new MobaMessage(msgType, msgID, msgParam, msgDelayTime, unit_id, isfinal);
		return msg;
	}

	// #endregion

	// #region Update
	public void Update() {
		//Helper.logcat("message", "MobaMessageManager.Update");
		int msgCount = mMessageQueue.size();
		for (int i = 0; i < msgCount; i++) {
			MobaMessage msg = mMessageQueue.remove();
			if (msg.IsDelayExec()) {
				mMessageQueue.add(msg);
			} else {
				ExecuteMsg(msg);
			}
		}
	}
	// #endregion
}
