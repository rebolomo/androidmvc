package com.android.mvc.webservice;
/// <summary>
/// REBOL todo, change to use config file
/// </summary>
public class WebAddress
{
    private static String game_address = null;

    public static String GameAddress()
    {
        if (game_address == null)
        {
            SetAddress();
        }
        return game_address;
    }
    
    public static boolean SetAddress() {
    	return WebAddress.SetAddress(null, null);
    }

	public static boolean SetAddress(String host, String port)
	{
	    ServerType type = ServerType.HomePc;
//	    ServerType type = ServerType.HomePc;
//	    ServerType type = ServerType.HomePc;
//	    ServerType type = ServerType.HomePc;
	    switch (type)
	    {    
	        case HomePc:
            {
                game_address = "http://10.0.2.2/api/v1.0/";//"http://192.168.1.100/api/v1.0/";
            }
            return true;
	        case Notebook:
            {
                game_address = "http://192.168.1.102/api/v1.0/";
            }
            return true;
	        case Office:
            {
                game_address = "http://192.168.0.105/api/v1.0/";
            }
            return true;
	        case Online:
            {
                game_address = "http://121.40.172.117/api/v1.0/";
            }
            return true;
	        case Redsun:
            {
                game_address = "http://172.114.110.108/api/v1.0/";
            }
            return true;
	        default:
	            break;
	    }
	    return false;
	}

	/// <summary>
	/// Clears the address.
	/// </summary>
    public static void ClearAddress()
    {
        game_address = null;
    }
}