package com.android.mvc.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.mvc.message.MobaMessage;
import com.android.mvc.message.OperationResponse;
import com.android.mvc.message.WebMessageCode;
import com.android.mvc.message.WebMessageManager;
import com.android.mvc.message.MobaMessageManager.MobaMessageType;
import com.android.mvc.model.data.UserData;
import com.android.mvc.util.Helper;
import com.android.mvc.util.LogTag;
import com.android.mvc.util.ValueParse;
import com.android.mvc.webservice.WebRequest;
import com.android.mvc.webservice.WebReturnCode;

class Model_userData extends ModelBase< Map<Integer, UserData> >
//internal class Model_resetData :ModelBase<ResetData>
{
	public Model_userData ()
	{
		super.Init (EModelType.Model_userData);
	}
	
	//#region 消息监听

	List<WebMessageCode> listCode;
	/// <summary>
	/// 监听哪些Web请求消息
	/// </summary>
	@Override
	public void RegisterMsgHandler ()
	{
		listCode = Arrays.asList( 
				WebMessageCode.METHOD_ACCOUNT_LOGIN,// + "api/" + API_VERSION + "/user/login";// + API_FILE_EXTENSION;
			    WebMessageCode.METHOD_ACCOUNT_SIGNUP,// + "api/" + API_VERSION + "/user/register";// + API_FILE_EXTENSION;
			    //WebMessageCode.METHOD_ACCOUNT_AUTHORIZE,// + "api/" + API_VERSION + "/method/account.authorize" + API_FILE_EXTENSION;
			    //WebMessageCode.METHOD_ACCOUNT_LOGINBYFACEBOOK,// + "api/" + API_VERSION + "/method/account.signInByFacebook" + API_FILE_EXTENSION;
			    WebMessageCode.METHOD_ACCOUNT_RECOVERY,// + "api/" + API_VERSION + "/method/account.recovery" + API_FILE_EXTENSION;
			    WebMessageCode.METHOD_ACCOUNT_SETPASSWORD,// + "api/" + API_VERSION + "/method/account.setPassword" + API_FILE_EXTENSION;
			    //WebMessageCode.METHOD_ACCOUNT_CONNECT_TO_FACEBOOK,// + "api/" + API_VERSION + "/method/account.connectToFacebook" + API_FILE_EXTENSION;
			    //WebMessageCode.METHOD_ACCOUNT_DISCONNECT_FROM_FACEBOOK,// + "api/" + API_VERSION + "/method/account.disconnectFromFacebook" + API_FILE_EXTENSION;
			    WebMessageCode.METHOD_ACCOUNT_LOGOUT,// + "api/" + API_VERSION + "/method/account.logOut" + API_FILE_EXTENSION;
				WebMessageCode.METHOD_SHOPS_NEW,	//	REBOL note, 这两个请求会更改user的default_shop_id
			    WebMessageCode.METHOD_SHOPS_EDIT
		);
		/*
		foreach (var it in listCode) {
			WebMessageManager.AddListener_model (it, OnGetMsg);
		}
		*/
		for(int i = 0; i < listCode.size(); i++) {
			WebMessageCode it = listCode.get(i);
			WebMessageManager.AddListener_model (it, this, "OnGetMsg");
		}
	}
	
	/// <summary>
	/// 注销监听
	/// </summary>
	@Override
	public void UnRegisterMsgHandler ()
	{
		/*
		foreach (var it in listCode) {
			WebMessageManager.RemoveListener_model (it, OnGetMsg);
		}*/
		for(int i = 0; i < listCode.size(); i++) {
			WebMessageCode it = listCode.get(i);
			WebMessageManager.RemoveListener_model (it, this, "OnGetMsg");
		}
	}
	
	/// <summary>
	/// 消息分发
	/// </summary>
	/// <param name="msg">Message.</param>
	@Override
	public void OnGetMsg (MobaMessage msg)
	{
		int msgID = 0;
		MobaMessageType type = WebMessageManager.ClientMsg_to_RawCode (
				/*Helper.int2enum(msg.ID(), ClientMsg.class)*/msg.ID() /*, out msgID*/);
		msgID = WebMessageManager.retCodeOut;
		
		WebMessageCode code = Helper.int2enum(msgID, WebMessageCode.class);
		if (listCode.contains(code)) {
			String methodName = "OnGetMsg_" + type.toString() + "_" + code.toString();
			//ClientLogger.Debug ("WebMessage", "-----------------" + methodName);
			//System.Reflection.MethodInfo methodinfo = 
			//this.GetType ().GetMethod (methodName, BindingFlags.NonPublic | BindingFlags.Instance);

			//	REBOL TODO, 通过反射把方法信息拼出来？
			try {
				Class<?>[] classes = { MobaMessage.class };
				Helper.invokeMethod(this, methodName, classes, msg);
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//#endregion
	
	//#region 回调函数
	
	//OnGetMsg_WebCode_METHOD_ACCOUNT_LOGIN
	private void OnGetMsg_WebCode_METHOD_ACCOUNT_LOGIN(MobaMessage msg)
	{
		this.LastErrorTo( Helper.enum2int(WebReturnCode.NORESULT));
		if (null != msg) {
			OperationResponse operationResponse = (OperationResponse)msg.Param() ;
			if (null != operationResponse) {
				this.LastErrorTo((int)operationResponse.ReturnCode);
				WebReturnCode code = Helper.int2enum(this.LastError(), WebReturnCode.class);
				switch (code) {
				case OK: 
					JSONObject jsonObj = (JSONObject)operationResponse.Parameters;
					//		REBOL note, save token
					try {
						WebRequest.token = jsonObj.getString("access_token");
						
						Helper.logcat(LogTag.Model, "token: " + WebRequest.token);
						
						//Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();
						Class c1 = UserData.class;
						//	REBOL note，对于model data的解析要先toMap
						UserData data1 = (UserData)ValueParse.ParseJsonToClass (c1, 
							Helper.toMap(jsonObj.getJSONObject("user")));
					
						super.ReplaceAllNoMap(data1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				    break;
				default:
					break;
				}
			}
		}
		boolean valid = (this.LastError() == Helper.enum2int(WebReturnCode.OK)) && (null != this.Data());
		this.ValidTo(valid); 
	}
	
	private void OnGetMsg_WebCode_METHOD_ACCOUNT_SIGNUP(MobaMessage msg)
	{
		this.LastErrorTo( Helper.enum2int(WebReturnCode.NORESULT));
		if (null != msg) {
			OperationResponse operationResponse = (OperationResponse)msg.Param() ;
			if (null != operationResponse) {
				this.LastErrorTo((int)operationResponse.ReturnCode);
				WebReturnCode code = Helper.int2enum(this.LastError(), WebReturnCode.class);
				switch (code) {
				case OK: 
					JSONObject jsonObj = (JSONObject)operationResponse.Parameters;
					
					//		REBOL note, 存token
					try {
						WebRequest.token = jsonObj.getString("access_token");
						
						Helper.logcat(LogTag.Model, "token: " + WebRequest.token);
						
						Class c1 = UserData.class;
						UserData data1 = (UserData)ValueParse.ParseJsonToClass (c1, 
							Helper.toMap(jsonObj.getJSONObject("user")));
					
						super.ReplaceAllNoMap(data1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				    break;
				default:
					break;
				}
			}
		}
		boolean valid = (this.LastError() == Helper.enum2int(WebReturnCode.OK)) && (null != this.Data());
		this.ValidTo(valid); 
	}
	
	private void OnGetMsg_WebCode_METHOD_ACCOUNT_RECOVERY(MobaMessage msg)
	{
		this.LastErrorTo( Helper.enum2int(WebReturnCode.NORESULT));
		if (null != msg) {
			OperationResponse operationResponse = (OperationResponse)msg.Param() ;
			if (null != operationResponse) {
				this.LastErrorTo((int)operationResponse.ReturnCode);
				WebReturnCode code = Helper.int2enum(this.LastError(), WebReturnCode.class);
				switch (code) {
				case OK: 
					JSONObject jsonObj = (JSONObject)operationResponse.Parameters;

					Class c1 = UserData.class;
					UserData data1;
					try {
						data1 = (UserData)ValueParse.ParseJsonToClass (c1, 
							Helper.toMap(jsonObj.getJSONObject("user")));
						
						super.ReplaceAllNoMap(data1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				    break;
				default:
					break;
				}
			}
		}
		boolean valid = (this.LastError() == Helper.enum2int(WebReturnCode.OK)) && (null != this.Data());
		this.ValidTo(valid); 
	}
	
	private void OnGetMsg_WebCode_METHOD_ACCOUNT_SETPASSWORD(MobaMessage msg)
	{
		this.LastErrorTo( Helper.enum2int(WebReturnCode.NORESULT));
		if (null != msg) {
			OperationResponse operationResponse = (OperationResponse)msg.Param() ;
			if (null != operationResponse) {
				this.LastErrorTo((int)operationResponse.ReturnCode);
				WebReturnCode code = Helper.int2enum(this.LastError(), WebReturnCode.class);
				switch (code) {
				case OK: 
					JSONObject jsonObj = (JSONObject)operationResponse.Parameters;

					Class c1 = UserData.class;
					UserData data1;
					try {
						data1 = (UserData)ValueParse.ParseJsonToClass (c1, 
							Helper.toMap(jsonObj.getJSONObject("user")));
						super.ReplaceAllNoMap(data1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				

				    break;
				default:
					break;
				}
			}
		}
		boolean valid = (this.LastError() == Helper.enum2int(WebReturnCode.OK)) && (null != this.Data());
		this.ValidTo(valid); 
	}
	
	private void OnGetMsg_WebCode_METHOD_ACCOUNT_LOGOUT(MobaMessage msg)
	{
		this.LastErrorTo( Helper.enum2int(WebReturnCode.NORESULT));
		if (null != msg) {
			OperationResponse operationResponse = (OperationResponse)msg.Param() ;
			if (null != operationResponse) {
				this.LastErrorTo((int)operationResponse.ReturnCode);
				WebReturnCode code = Helper.int2enum(this.LastError(), WebReturnCode.class);
				switch (code) {
				case OK: 
					JSONObject jsonObj = (JSONObject)operationResponse.Parameters;

					Class c1 = UserData.class;
					UserData data1;
					try {
						data1 = (UserData)ValueParse.ParseJsonToClass (c1, 
							Helper.toMap(jsonObj.getJSONObject("user")));
						super.ReplaceAllNoMap(data1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				

				    break;
				default:
					break;
				}
			}
		}
		boolean valid = (this.LastError() == Helper.enum2int(WebReturnCode.OK)) && (null != this.Data());
		this.ValidTo(valid); 
	}

	private void OnGetMsg_WebCode_METHOD_SHOPS_NEW(MobaMessage msg)
	{
		this.LastErrorTo( Helper.enum2int(WebReturnCode.NORESULT));
		if (null != msg) {
			OperationResponse operationResponse = (OperationResponse)msg.Param() ;
			if (null != operationResponse) {
				this.LastErrorTo((int)operationResponse.ReturnCode);
				WebReturnCode code = Helper.int2enum(this.LastError(), WebReturnCode.class);
				switch (code) {
				case OK: 
					JSONObject jsonObj = (JSONObject)operationResponse.Parameters;
					//		REBOL note, save token
					try {
						Class c1 = UserData.class;
						//	REBOL note，对于model data的解析要先toMap
						UserData data1 = (UserData)ValueParse.ParseJsonToClass (c1, 
							Helper.toMap(jsonObj.getJSONObject("user")));
					
						super.ReplaceAllNoMap(data1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				    break;
				default:
					break;
				}
			}
		}
		boolean valid = (this.LastError() == Helper.enum2int(WebReturnCode.OK)) && (null != this.Data());
		this.ValidTo(valid); 
	}

	private void OnGetMsg_WebCode_METHOD_SHOPS_EDIT(MobaMessage msg)
	{
		OnGetMsg_WebCode_METHOD_SHOPS_NEW(msg);
	}
}