package com.android.mvc.webservice;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.mvc.app.App;
import com.android.mvc.message.UIMessageManager;
import com.android.mvc.message.UIMessageType;
import com.android.mvc.message.WebMessageCode;
import com.android.mvc.util.Helper;
import com.android.mvc.util.LogTag;
import com.androidquery.AQuery;
import com.androidquery.auth.BasicHandle;
import com.androidquery.callback.AjaxStatus;

public class WebServiceTools// : MonoBehaviour
{
	//#region static part

	/// <summary>
	/// http header
	/// </summary>
	private static Map webServiceHeader = null;

	/// <summary>
	/// 
	/// </summary>
	/// <param name="hash">Hash.</param>
	public static void InitHeader (Map header)
	{
		if (WebServiceTools.webServiceHeader == null) {
			WebServiceTools.webServiceHeader = header;
		} else {
			Helper.logcat(LogTag.Web, "Header is not null, maybe run is function in other place!");
		}
	}
    
	WebServiceTools ()
	{
		Map<String, String> params = new HashMap<String, String>();
        params.put("APP-KEY", "456");
        params.put("APP-TOKEN", "999");
        params.put("USER-TOKEN", "fcf50a8e-1967-4d9f-bbb4-67041e807dda");
		InitHeader (Helper.CreateHash (params));
	}
	
	public static String UserToken() {

		if (WebServiceTools.webServiceHeader.containsKey("USER-TOKEN")
			&& WebServiceTools.webServiceHeader.get("USER-TOKEN") != null
			//&& !string.IsNullOrEmpty (WebServiceTools.webServiceHeader ["USER-TOKEN"].ToString ())) {
			&& !Helper.isNullOrBlank(WebServiceTools.webServiceHeader.get("USER-TOKEN").toString ())) {
			return WebServiceTools.webServiceHeader.get("USER-TOKEN").toString();
		} 
		else {
			Helper.logError("WebService", "Get some error hare!");
		}
		return null;
	}

	private static void ChangeUserToken (String token)
	{
		if (WebServiceTools.webServiceHeader != null 
				&& WebServiceTools.webServiceHeader.containsKey ("USER-TOKEN") 
				&& !WebServiceTools.webServiceHeader.get("USER-TOKEN").toString().equals (token)) {
			WebServiceTools.webServiceHeader.put( "USER-TOKEN", token );
			Helper.logcat (LogTag.Web, WebServiceTools.webServiceHeader.get("USER-TOKEN").toString());
		} else {
			Helper.logcat (LogTag.Web, "Header not contains Token KEY, run Init function first!");
		}
	}

	//#endregion

	/// <summary>
	/// timeout for request
	/// </summary>
	private float customTimeOut = 2000;

	/// <summary>
	/// callbacks
	/// </summary>
	public WebServiceSuccessJson _SuccessCallBack = null;
	public WebServiceFailedJson _FailedCallBack = null;
	public WebServiceValue _wsValue = null;

	public void StartService ()
	{
		if (_wsValue == null || _SuccessCallBack == null || _FailedCallBack == null) {
			Helper.logcat(LogTag.Web, "Service value is null ,cant start service!");
			return;
		}
		
		//	REBOL note, check the network
		if(!App.getInstance().isConnected()) {
			UIMessageManager.SendMsg(UIMessageType.UI_Popup_NetworkError);
			return;
		}
		//	REBOL todo, token auth
		
		/*
		//	REBOL note, add token for server check
		string token_key = "token_" + accountid;
		string token = PlayerPrefs.GetString (token_key, "0");
		if (!_wsValue._paramList.Contains ("token")) {
			_wsValue._paramList.Add ("token", token);
		}

		if (!_wsValue._paramList.Contains ("token_roleid")) {
			_wsValue._paramList.Add ("token_roleid", roleid);
		}
		*/

		//if (_wsValue._type == WebServiceType.POST) {
			if (_wsValue._isJson) {
				
				
				PostWebServiceWithJson (_wsValue);
						/*_wsValue._webCode, _wsValue._baseURL, _wsValue._paramList, 
						_wsValue._authKey, _wsValue._authValue);*/// MiniJSON.Json.Serialize (_wsValue._paramList));
			} else {
				//PostWebServiceWithoutJson (ValueParse.UrlParse (_wsValue._baseURL, _wsValue._paramList));
			}
		//} else {
		//	//GetWebService (ValueParse.UrlParse (_wsValue._baseURL, _wsValue._paramList));
		//}
	}

	/// <summary>
	/// post with json
	/// </summary>
	private void PostWebServiceWithJson (WebServiceValue wsValue)
			/*WebMessageCode code, String serviceUrl, Map params, String authKey, 
			String authValue)*/
	{
		//	REBOL todo
		//StartCoroutine (YieldService (code, serviceUrl, jstring));
		/*
		YieldService (wsValue._webCode, wsValue._baseURL, wsValue._paramList
				, wsValue._authKey, wsValue._authValue, wsValue._type); */
		YieldServiceAsync(wsValue._webCode, wsValue._baseURL, wsValue._paramList
				, wsValue._authKey, wsValue._authValue, wsValue._type); 
	}

	public void requestCallback(String url, JSONObject json, AjaxStatus status) {

		//#region check timeout
		long serviceStartTime = Helper.getCurrentTimestamp();//Time.time;
		
		//	REBOL temp
		int wwwCode = status.getCode();
		//if (Helper.isNullOrBlank(wwwCode)){ //www.error)) {
		if(wwwCode == 200) {
			// the response from server
			String unzip_json = json.toString();
			
			//	REBOL todo, get return code
			// parse json
			int json_len = unzip_json.length();
			Helper.logcat(LogTag.Web, "====> return : data length " + json_len);
			//Helper.logcat ("WebService", unzip_json.Substring (0, (json_len < 5000 ? json_len : 5000)));//检查传过来的json

			//	check errorcode
			JSONObject response;
			try {
				response = json;//new JSONObject (unzip_json);
				
				if (response.has("errorcode")) {
					int errorcode = response.getInt("errorcode");
					WebReturnCode error = Helper.int2enum(errorcode, WebReturnCode.class);
					if (error != WebReturnCode.OK) {
						Helper.logcat (LogTag.Web, "====> get custom error with : " + json_len);
						//_FailedCallBack (error);
						_FailedCallBackWrapper(error);
	
						if (WebReturnCode.TOKEN_ERROR == error) {
							//UIMessageManager.SendMsg (UIMessageType.UI_Popup_WebRequest_TokenError, this);
						} else {
							//	REBOL note, error process
							//String error_text = MsgUtil.GetError ((int)error);
							//UIMessageManager.SendMsg (UIMessageType.UI_Popup_WebRequest_Failed, error_text);
						}
					} else {
						//_SuccessCallBack (unzip_json);
						Class<?>[] classes = { String.class };
						try {
							Helper.invokeMethod(_SuccessCallBack.handler, _SuccessCallBack.callback, classes, unzip_json);
						} catch (Exception e) {
						}
						//string success_text = MsgUtil.GetSuccess (code);
						//UIMessageManager.SendMsg (UIMessageType.UI_Popup_WebRequest_End, success_text);
					}
				} else {
					//_SuccessCallBack (unzip_json);
					Class<?>[] classes = { String.class };
					try {
						Helper.invokeMethod(_SuccessCallBack.handler, _SuccessCallBack.callback, classes, unzip_json);
					} catch (Exception e) {
					}
					//string success_text = MsgUtil.GetSuccess (code);
					//UIMessageManager.SendMsg (UIMessageType.UI_Popup_WebRequest_End, success_text);
				}
				//release
				ReleaseSelf ();
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		//#endregion
		//#region other state
		else {
			Helper.logcat (LogTag.Web, "====> get error with : " + wwwCode + " " + url);// + " " + jstring);
			//#region call back
			//String[] tArray = www.error.Split (new char[]{' '});
			if (wwwCode == 401) {
				_FailedCallBackWrapper (WebReturnCode.ERROR_UNAUTHOR_OR_AUTHOR_TIMEOUT);
				UIMessageManager.SendMsg (UIMessageType.UI_Popup_WebRequest_TimeOut, this);
			} else if (wwwCode == 404) {
				_FailedCallBackWrapper (WebReturnCode.URL_ERROR);
				UIMessageManager.SendMsg (UIMessageType.UI_Popup_WebRequest_Error, this);
			} else {
				_FailedCallBackWrapper (WebReturnCode.SYSTEM_ERROR);
				UIMessageManager.SendMsg (UIMessageType.UI_Popup_WebRequest_Error, this);
			}
			//#endregion

		}
		//#endregion
    }
	
	private void YieldServiceAsync (WebMessageCode code, String serviceUrl, Map params, 
			String authKey, String authValue, WebServiceType type)
	{
		JSONObject obj = new JSONObject(params);
		String jstring = obj.toString();
		
		Helper.logcat(LogTag.Web, " ====> PostWebServiceWithJson : " + code);
		Helper.logcat (LogTag.Web, " ====> serviceUrl : " + serviceUrl + " request : " + jstring);
		Helper.logcat (LogTag.Web, "====> request : data length " + jstring.length());

		BasicHandle handle = new BasicHandle(authKey, authValue); 

		//int method = AQuery.METHOD_POST;
		AQuery aq = new AQuery(App.getInstance().getApplicationContext());
		Helper.apiRequest(aq, App.getInstance().getApplicationContext(), handle, serviceUrl, type, params, this, "requestCallback");
	}
	
	private void _FailedCallBackWrapper(WebReturnCode code) {
		Class<?>[] classes = { WebReturnCode.class };
		try {
			Helper.invokeMethod(_FailedCallBack.handler, _FailedCallBack.callback, classes, code);
		} catch (Exception e) {
		}
	}
	
	public void ReleaseSelf ()
	{
		//GameObject.Destroy (gameObject);
	}
}