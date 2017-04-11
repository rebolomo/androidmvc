package com.android.mvc.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.mvc.message.MobaMessageFunc;
import com.android.mvc.message.WebMessageCode;
import com.android.mvc.util.Helper;
import com.android.mvc.util.LogTag;
	//#region 
	/// <summary>
	/// web base
	/// </summary>
	public class WebServiceBase
	{
		/// <param name="baseURL">Base UR.</param>
		/// <param name="paramList">Parameter list.</param>
		public WebServiceBase (String baseURL, Map paramList, WebServiceType type, String authKey, String authValue) 
		{
			this(false , baseURL , paramList, type, authKey, authValue);
		}
	
		/// <param name="canLoadLevel">continue request when load level</param>
		/// <param name="baseURL">url</param>
		/// <param name="paramList">Parameter list.</param>
		public WebServiceBase (boolean canLoadLevel, String baseURL, 
				Map paramList, WebServiceType type, String authKey, String authValue) 
		{
			this(canLoadLevel , type , true , baseURL , paramList, authKey, authValue);
		}

		/// <param name="canLoadLevel">continue request when load level</param>
		/// <param name="type"></param>
		/// <param name="isJson">json or not</param>
		/// <param name="baseURL">url</param>
		/// <param name="paramList">Parameter list.</param>
		public WebServiceBase (boolean canLoadLevel, WebServiceType type, 
				boolean isJson, String baseURL, Map paramList, String authKey, String authValue) 
		{
			this(canLoadLevel, 
					new WebServiceValue(type, isJson , baseURL , paramList, authKey, authValue));
		}
	
		/// <summary>
		/// Initializes a new instance of the <see cref="laoHu.WebService.WebServiceBase`1"/> class.
		/// </summary>
		/// <param name="canLoadLevel">continue request when load level</param>
		/// <param name="wsValue">Ws value.</param>
		public WebServiceBase (boolean canLoadLevel, WebServiceValue wsValue)
		{
			/*
			GameObject g = new GameObject ();
			g.name = g.GetInstanceID ().ToString ();
			tools = g.AddComponent<WebServiceTools> ();
			if (canLoadLevel) {
				GameObject.DontDestroyOnLoad (g);
			}
			*/
			tools = new WebServiceTools();
			wsValue._webCode = code;
			tools._wsValue = wsValue;
			
			WebServiceSuccessJson c1 = new WebServiceSuccessJson(this, "OnSuccess");
			tools._SuccessCallBack = c1;//OnSuccess;
			
			WebServiceFailedJson c2 = new WebServiceFailedJson(this, "OnFailed");
			tools._FailedCallBack = c2;//OnFailed;

			MobaMessageFunc s = new MobaMessageFunc(tools, "StartService");
			startServiceDelegte = s;//tools.StartService;
		}

		public void AddWebCallback (int paramNum, WebRequestCallback<Object> onCallback1) {
			AddWebCallback(paramNum, onCallback1, null);
		}
		/// <summary>
		/// Adds the web callback.
		/// </summary>
		/// <param name="paramNum">Parameter number.</param>
		/// <param name="onCallback">On callback.</param>
		/// <param name="onCallbackFinal">On callback final.</param>
		public void AddWebCallback (int paramNum, WebRequestCallback<Object> onCallback1, 
				WebRequestCallback<Object> onCallback2)
		{
			//添加Callback
			if (requestCallBacks != null) {
				requestCallBacks.clear ();
				if (paramNum > 1) {
					for (int i=0; i< paramNum-1; i++) {
						if (onCallback1 != null)
							requestCallBacks.add (onCallback1);
					}
					if (onCallback2 != null) {
						requestCallBacks.add(onCallback2);
					}
				} else if (paramNum > 0) {
					if (onCallback1 != null)
						requestCallBacks.add (onCallback1);
				}
			}
		}
	
		private WebServiceTools tools = null;
	
		/// <summary>
		/// delegate
		/// </summary>
		protected MobaMessageFunc startServiceDelegte = null;
		/// <summary>
		/// real request
		/// </summary>
		public /*virtual System.Action*/void StartServiceDelegte() {
			//return startServiceDelegte;
			
			//Class<?>[] classes = { WebReturnCode.class };
			try {
				Helper.invokeMethod(startServiceDelegte.handler, startServiceDelegte.callback, null);
			} catch (Exception e) {
				Helper.logcat(LogTag.Web, e.toString());
			}
		}

		/// <summary>
		/// Raises the success event.
		/// </summary>
		/// <param name="jString">json string response</param>
		public /*virtual*/ void OnSuccess (String jString)
		{
			JSONObject response;
			try {
				response = new JSONObject(jString);
				if (requestCallBacks != null) {
					for (int i=0; i<requestCallBacks.size(); i++) {
						WebRequestCallback<Object> c = requestCallBacks.get(i);
						
						//	REBOL note, paramter template for callback
						Class<?>[] classes = { WebMessageCode.class, Object.class, WebReturnCode.class };
						try {
							Helper.invokeMethod(c.handler, c.callback, classes, code, response, WebReturnCode.OK);
						} catch (Exception e) {
						}
					}
	//				requestCallBack (code, null, WebReturnCode.OK);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/// <summary>
		/// Raises the failed event.
		/// </summary>
		/// <param name="error">Error.</param>
		protected /*virtual*/ void OnFailed (WebReturnCode error)
		{
			if (requestCallBacks != null) {
				for (int i=0; i<requestCallBacks.size(); i++) {
					//requestCallBacks [i] (code, null, error);
					WebRequestCallback<Object> c = requestCallBacks.get(i);
					
					//	REBOL note, paramter template for callback
					Class<?>[] classes = { WebMessageCode.class, Object.class, WebReturnCode.class };
					try {
						Helper.invokeMethod(c.handler, c.callback, classes, code, null, error);
					} catch (Exception e) {
						Helper.logcat(LogTag.Web, e.toString());
					}
				}
//				requestCallBack (code, default(T), error);
			}
		}
	
		/// <summary>
		/// The success call back.
		/// </summary>
		protected List<WebRequestCallback<Object>> requestCallBacks = 
				new ArrayList<WebRequestCallback<Object>> ();
	
		/// <summary>
		/// The code.
		/// </summary>
		protected /*virtual*/ WebMessageCode code;
}