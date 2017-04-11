package com.android.mvc.model;

import com.android.mvc.message.MobaMessageFunc;

interface IModel 
//internal interface IModel
{
    EModelType ModelType();
    boolean Valid();
    void ValidTo(boolean isValid);
    int LastMsgType();
    int LastMsgID();
    int LastError();
    String DebugMessage();
    Object Data();
    void RegisterMsgHandler();
    void UnRegisterMsgHandler();
    void AddModelListner(MobaMessageFunc msgFunc);
    void RemoveModelListner(MobaMessageFunc msgFunc);
}
