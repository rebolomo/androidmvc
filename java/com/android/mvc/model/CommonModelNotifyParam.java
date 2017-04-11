package com.android.mvc.model;

public class CommonModelNotifyParam implements IModelNotifyParam
{
    int errorCode;
    Object data;
    String debugMessage;
    EModelType modelType;
    int typeID;
    int msgID;
    /*internal*/CommonModelNotifyParam(IModel model)
    {
        errorCode = model.LastError();
        data = model.Data();
        modelType = model.ModelType();
        debugMessage = model.DebugMessage();
        typeID = model.LastMsgType();
        msgID = model.LastMsgID();
    }
    @Override
    public int ErrorCode()  { return errorCode; } 
    public String DebugMessage() { return debugMessage; } 
    @Override
    public Object Data()  { return data; } 

    /*	C#
    public T GetData<T>() where T : class
    {
        return data as T;
    }
    */

    //	Limit the T to class type.
    public <T extends Class> T GetDataGeneric()
    {
        return (T)data;
    }
    
    public int TypeID() { return typeID; }
    public int MsgID() { return msgID; }

    @Override
    public EModelType ModelType() { return modelType; }
}
