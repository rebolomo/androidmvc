package com.android.mvc.message;
import com.android.mvc.util.Helper;

public enum UIMessageType {
	
	UI_Null(Helper.uiMessageTypeBegin),  // 11001
	
	UI_Test(Helper.uiMessageTypeBegin + 1),
	// TopView
	UI_TopView_UpdateTitle(Helper.uiMessageTypeBegin + 2), // Update title in top view
	UI_TopView_BackButton(Helper.uiMessageTypeBegin + 3), // back button click
	UI_TopView_Hide(Helper.uiMessageTypeBegin + 4), // hide topview
	UI_TopView_Show(Helper.uiMessageTypeBegin + 5), // show topview

	//	Activity related
	//UI_Activity_Create(Helper.uiMessageTypeBegin + 10), // 
	UI_Activity_CreateFinished(Helper.uiMessageTypeBegin + 11), // 
	UI_Activity_ResumeFinished(Helper.uiMessageTypeBegin + 12), // 
	
	
	//	Popup related
	UI_Popup_WebRequest_Start(Helper.uiMessageTypeBegin + 20),
	UI_Popup_WebRequest_End(Helper.uiMessageTypeBegin + 21),
	UI_Popup_WebRequest_TimeOut(Helper.uiMessageTypeBegin + 22),
	UI_Popup_WebRequest_Error(Helper.uiMessageTypeBegin + 23),
	UI_Popup_WebRequest_Failed(Helper.uiMessageTypeBegin + 24),

	//	Token error
	UI_Popup_WebRequest_TokenError(Helper.uiMessageTypeBegin + 25),

	//	Network error
	UI_Popup_NetworkError(Helper.uiMessageTypeBegin + 26),
	
	//	Misc
	UI_OrderTotalPrice(Helper.uiMessageTypeBegin + 50),
	UI_RefreshOrderProductList(Helper.uiMessageTypeBegin + 51),
	UI_Max(Helper.uiMessageTypeEnd); // =ClientMsg.UI_Msg_End
	
    private int value = 0;

    private UIMessageType(int value) {    
        this.value = value;
    }
    
}
