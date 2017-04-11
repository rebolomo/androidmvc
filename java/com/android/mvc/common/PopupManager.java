package com.android.mvc.common;

import java.util.Arrays;
import java.util.List;

import com.android.mvc.R;
import com.android.mvc.app.App;
import com.android.mvc.controller.Singleton;
import com.android.mvc.message.MobaMessage;
import com.android.mvc.message.UIMessageManager;
import com.android.mvc.message.UIMessageType;
import com.android.mvc.util.Helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

//	REBOL note, manage popups.
public class PopupManager extends Singleton<ScreenCtrlManager>
				implements DialogInterface.OnClickListener {
	private Context mContext;
	
	private ProgressDialog dialog;
	public void Create(Context context) {
		mContext = context;
		
		dialog = new ProgressDialog(mContext);
		
		RegisterMsgHandler();
	}
	
	public void Destroy() {
		UnRegisterMsgHandler();
	}

	List<UIMessageType> listCode;
	public void RegisterMsgHandler() {
		listCode = Arrays.asList( 
			//	Web request related
			UIMessageType.UI_Popup_WebRequest_Start,
			UIMessageType.UI_Popup_WebRequest_End,
			UIMessageType.UI_Popup_WebRequest_TimeOut,
			UIMessageType.UI_Popup_WebRequest_Error,
			UIMessageType.UI_Popup_WebRequest_Failed,

			//	Token error
			UIMessageType.UI_Popup_WebRequest_TokenError,

			UIMessageType.UI_Popup_NetworkError
		);

		for(int i = 0; i < listCode.size(); i++) {
			UIMessageType it = listCode.get(i);
			UIMessageManager.AddListener (it, this, "OnGetMsg");
		}
	}

	/// <summary>
	/// Cancel listener
	/// </summary>
	public void UnRegisterMsgHandler ()
	{
		for(int i = 0; i < listCode.size(); i++) {
			UIMessageType it = listCode.get(i);
			UIMessageManager.RemoveListener (it, this, "OnGetMsg");
		}
	}

	public void OnGetMsg (MobaMessage msg)
	{
		/*
		int msgID = 0;
		MobaMessageType type = UIMessageManager..ClientMsg_to_RawCode (
				msg.ID());//, msgID);
		msgID = WebMessageManager.retCodeOut;
		*/
		UIMessageType code = Helper.int2enum(msg.ID(), UIMessageType.class);
		if (listCode.contains(code)) {
			String methodName = "OnGetMsg_" + code.toString();
			//ClientLogger.Debug ("WebMessage", "-----------------" + methodName);
			//System.Reflection.MethodInfo methodinfo = 
			//this.GetType ().GetMethod (methodName, BindingFlags.NonPublic | BindingFlags.Instance);

			try {
				Class<?>[] classes = { MobaMessage.class };
				Helper.invokeMethod(this, methodName, classes, msg);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void OnGetMsg_UI_Popup_WebRequest_Start(MobaMessage msg) {
		//	REBOL note, start request, show progressdialog
		//ProgressDialog dialog = new ProgressDialog(context);
    	dialog.setIndeterminate(true);
    	dialog.setCancelable(false);
    	dialog.setInverseBackgroundForced(false);
    	dialog.setCanceledOnTouchOutside(true);
    	dialog.setTitle("Request...");
    	
    	dialog.show();
	}

	private void OnGetMsg_UI_Popup_WebRequest_End(MobaMessage msg) {
		//	REBOL note, hide progressdialog
		dialog.hide();
	}

	private void OnGetMsg_UI_Popup_WebRequest_Timeout(MobaMessage msg) {
		//	REBOL note, start dialog, retry the request
	}

	private void OnGetMsg_UI_Popup_WebRequest_Error(MobaMessage msg) {
		Toast.makeText(mContext, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
	}

	private void OnGetMsg_UI_Popup_WebRequest_Failed(MobaMessage msg) {
	}

	private void OnGetMsg_UI_Popup_WebRequest_TokenError(MobaMessage msg) {
	}

	private void OnGetMsg_UI_Popup_NetworkError(MobaMessage msg) {
		//	Network disconnected
		BaseScreenCtrl currentCtrl = Singleton.getSingleton(ScreenCtrlManager.class).currentCtrl();
		AlertDialog.Builder builder = new AlertDialog.Builder(currentCtrl.mScreen);
		// Add the buttons
		builder.setPositiveButton(R.string.popup_ok, this);
		builder.setNegativeButton(R.string.popup_cancel, this);
		builder.setTitle(R.string.popup_error_network);
		AlertDialog dialog = builder.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int id) {
		switch (id) {
		//	REBOL note, dialog layout
		case 0://R.id.btn_retry:
            if (!App.getInstance().isConnected()) {
                Toast.makeText(mContext, R.string.msg_network_error, Toast.LENGTH_SHORT).show();

            //} else if (!checkUsername() || !checkPassword()) {    //  REBOL todo, open

            } else {  	
                //signin();
            }
			break;			
		case 1://R.id.btn_network_setting:
            //	REBOL todo, go to network settings.
            break;
		default:
			break;
		}
    }
}
