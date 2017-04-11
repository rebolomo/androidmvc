package com.android.mvc.webservice;

import java.util.Map;

import com.android.mvc.controller.Singleton;
import com.android.mvc.message.OperationResponse;
import com.android.mvc.message.WebMessageCode;
import com.android.mvc.message.WebMessageManager;
import com.android.mvc.util.Helper;

public class NetworkHelper extends Singleton<NetworkHelper>
{
    public static NetworkHelper Instance() {
        return Singleton.getSingleton(NetworkHelper.class);
    }

    /////////////////////////////////////////////////////
    //  User
    public boolean Register(String username, String password)
    {
    	boolean isSend = false;
        try
        {
            RegisterRequest request = new RegisterRequest(username, password);
            WebRequestCallback c = new WebRequestCallback(this, "onWebRequestCallbackFinal");
            request.AddWebCallback(1, c);
            request.StartServiceDelegte();
        }
        catch (Exception EX)
        {
            isSend = false;
            Helper.logError("WebService", "GuestLogin Error : " + EX.getMessage());
        }
        return isSend;
    }
    
    public boolean Login(String username, String password)
    {
        boolean isSend = false;
        try
        {
            LoginRequest request = new LoginRequest(username, password);
            WebRequestCallback c = new WebRequestCallback(this, "onWebRequestCallbackFinal");
            request.AddWebCallback(1, c);
            request.StartServiceDelegte();
        }
        catch (Exception EX)
        {
            isSend = false;
            Helper.logError("WebService", "GuestLogin Error : " + EX.getMessage());
        }
        return isSend;
    }
    
    /// <summary>
    /// Ons the web reques callback.
    /// </summary>
    /// <param name="code">Code.</param>
    /// <param name="param">Parameter.</param>
    /// <param name="ret">Ret.</param>
    private void onWebRequestCallback(WebMessageCode code, Object param, WebReturnCode ret)
    {
        OperationResponse response = new OperationResponse();
        response.OperationCode = (short)Helper.enum2int(code);
        response.ReturnCode = (short)Helper.enum2int(ret);
        response.Parameters = param;
        WebMessageManager.SendMsg(code, response);
    }

    /// <summary>
    /// Ons the web reques callback.
    /// </summary>
    /// <param name="code">Code.</param>
    /// <param name="param">Parameter.</param>
    /// <param name="ret">Ret.</param>
    private void onWebRequestCallbackFinal(WebMessageCode code, Object param, WebReturnCode ret)
    {
    	OperationResponse response = new OperationResponse();
        response.OperationCode = (short)Helper.enum2int(code);
        response.ReturnCode = (short)Helper.enum2int(ret);
        response.Parameters = param;
        WebMessageManager.SendMsg(code, response, true);
    }
}
