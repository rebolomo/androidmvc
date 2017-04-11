package com.android.mvc.message;
public class OperationResponse
{
    public String DebugMessage;
    public short OperationCode;
    public Object Parameters;
    public short ReturnCode;

    public /*override*/ String ToString()
    {
        return String.format("OperationResponse {0}: ReturnCode: {1}.", this.OperationCode, this.ReturnCode);
    }

    public String ToStringFull()
    {
        //return string.Format("OperationResponse {0}: ReturnCode: {1} ({3}). Parameters: {2}", new object[] { this.OperationCode, this.ReturnCode, SupportClass.DictionaryToString(this.Parameters), this.DebugMessage });
        return "";
    }
}
