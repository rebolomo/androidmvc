package com.android.mvc.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.android.mvc.model.data.BaseData;

public class ValueParse
{
	public static Object ParseJsonToList(Class myListElementType, Object baseValue)
    {
		JSONArray j = (JSONArray)(baseValue);
		
		List<?> jsonList = (List<?>)Helper.toList(j);              
		Object returnObj = null;

		returnObj = Array.newInstance(myListElementType, jsonList.size());

		for (int i = 0; i < j.length(); i++) {
			//returnObj.SetValue (ParseJsonToClassInternal(baseType.GetElementType (), jsonList [i]), i);
			Object obj = ParseJsonToClassInternal(myListElementType, jsonList.get(i));
			//returnObj[i] = (obj);
			Array.set(returnObj, i, obj);
		}
		
		return returnObj;
    }
    /// <summary>
    /// REBOL note, support 
    /// </summary>
    /// <param name="baseType"></param>
    /// <param name="serverData"></param>
    /// <returns></returns>
    public static Object ParseJsonToClass(Class baseType, Object serverData)
    {
    	//return null;
    	
        //  REBOL note, deal with base data
        //if (baseType.isAssignableFrom(BaseData.class))
    	if( serverData instanceof BaseData)
        {
            BaseData data = (BaseData) ParseJsonToClassInternal(baseType, serverData);
            if (data != null)   //  REBOL note, server will pass null to client, check it
            {
            	Map map = (Map)serverData;
                //data.changed_keys = (serverData as Dictionary<string, object>).Keys.ToList();
            	data.changed_keys = new ArrayList<String>(map.keySet());    
            }
            return data;
        }
        else
        {
            return ParseJsonToClassInternal(baseType, serverData);
        }  
    }
    
 
    /// <summary>
    /// Get int 32
    /// </summary>
    /// <returns><c>true</c>, idic has key in int type, <c>false</c> idic doesn't has key in int value.</returns>
    /// <param name="idic">map to check in.</param>
    /// <param name="Key">key to check.</param>
    /// <param name="intvalue">no use.</param>
    public static int outIntValue;
    public static boolean GetIntValue (Map idic, String Key, /*out*/ int intvalue)
	{
		intvalue = 0;
		if (idic.containsKey(Key)) {
			String s = idic.get(Key).toString ();
			intvalue =  Integer.valueOf(s);
			outIntValue = intvalue;
			return true;
		}
		return false;
	}
	/// <summary>
	/// get int 32
	/// </summary>
	/// <returns>int32 value.</returns>
	/// <param name="idic">map to check in.</param>
    /// <param name="Key">key to check.</param>
	public static int GetIntValue (Map idic, String Key)
	{
		String s = idic.get(Key).toString ();
		return Integer.valueOf(s);
	}
	
	//	refer to GetIntValue
	public static boolean outBoolValue;
	public static boolean GetBoolValue (Map idic, String Key, /*out*/ boolean boolvalue)
	{
		boolvalue = false;
		if (idic.containsKey (Key)) {
			String s = idic.get(Key).toString ();
			boolvalue = Boolean.valueOf(s);
			outBoolValue = boolvalue;
			return true;
		}
		return false;
	}
	
	//	refer to GetIntValue
	public static boolean GetBoolValue (Map idic, String Key)
	{
		String s = idic.get(Key).toString ();
		return Boolean.valueOf(s);
	}
	
	//	refer to GetIntValue
	public static String outStringValue;
	public static boolean GetStringValue (Map idic, String Key, /*out*/ String stringvalue)
	{
		stringvalue = "";//String.Empty;
		if (idic.containsKey (Key)) {
			stringvalue = idic.get(Key).toString();
			outStringValue = stringvalue;
			return true;
		}
		return false;
	}
	
	//	refer to GetIntValue
	public static String GetStringValue (Map idic, String Key)
	{
		return idic.get(Key).toString();
	}
	
	//	refer to GetIntValue
	public static long outLongValue;
	public static boolean GetInt64Value (Map idic, String Key, /*out*/ long longvalue)
	{
		longvalue = 0;
		if (idic.containsKey (Key)) {
			String s = idic.get(Key).toString();
			longvalue = Long.valueOf(s);//long.Parse (idic [Key].ToString ());
			outLongValue = longvalue;
			return true;
		}
		return false;
	}
	
	//	refer to GetIntValue
	public static long GetInt64Value (Map idic, String Key)
	{
		String s = idic.get(Key).toString();
		return Long.valueOf(s);
	}
	/*
	/// <summary>
	/// map to url params
	/// </summary>
	/// <returns>url地址.</returns>
	/// <param name="baseUrl">基础url.</param>
	/// <param name="paramlist">键值对.</param>
	public static string UrlParse (string baseUrl, IDictionary paramlist)
	{
		string finalUrl = string.Empty;
		IList keyList = new ArrayList (paramlist.Keys);
		IList valueList = new ArrayList (paramlist.Values);
		
		if (keyList.Count == 0) {
			return baseUrl;
		}
		
		for (int i = 0; i < paramlist.Count; i++) {
			if (i == 0) {
				finalUrl = string.Format ("{0}?", baseUrl);
			} else {
				finalUrl = string.Format ("{0}&", finalUrl);
			}
			finalUrl = string.Format ("{0}{1}={2}", finalUrl, keyList [i].ToString (), valueList [i].ToString ());
		}
		return finalUrl;
	}
	*/
	//	REBOL note, type check
	private static boolean isList (Class type)
	{
		//if (type.IsGenericType && type.GetGenericTypeDefinition () == typeof(List<>)) {
		boolean result = List.class.isAssignableFrom(type);
		return result;
	}

	private static boolean isDict (Class type)
	{
		//boolean r = 
		//type.IsGenericType && type.GetGenericTypeDefinition () == typeof(Dictionary<,>);
		boolean r =
				type.isAssignableFrom(HashMap.class);
		//Type keyType = type.GetGenericArguments()[0];
		//Type valueType = type.GetGenericArguments()[1];
		//Debug.Log("Key type: " + keyType.ToString());
		//Debug.Log("Value type: " + valueType.ToString());
		return r;
	}
	
	/// <summary>
	/// Parses the json to class.
	/// </summary>
	/// <returns>The json to class.</returns>
	/// <param name="baseType">Base type.</param>
	/// <param name="baseValue">Base value.</param>
	private static Object ParseJsonToClassInternal(Class baseType, Object baseValue)
	{
		if (baseValue == null)
			return null;
		//#region parse array

		if (isList (baseType)) {
			JSONArray j = (JSONArray)(baseValue);

			
			List<?> jsonList = (List<?>)Helper.toList(j);       

			//List returnObj = (List)Activator.CreateInstance (baseType);
			List returnObj = new ArrayList();
			
			//	Get item type in list.
			Class myListElementType = jsonList.get(0).getClass();
			
			//returnObj = (ArrayList<?>) Array.newInstance(myListElementType, jsonList.size());
	
			//Class myListElementType = baseType.GetGenericArguments () [0];			
			//Class myListElementType = jsonList.get(0).getClass();
			
			for (int i = 0; i < jsonList.size(); i++) {
				Object obj = ParseJsonToClassInternal(myListElementType, jsonList.get(i));
				returnObj.add(obj);
			}
			
			return returnObj;
		} else if (baseType.isArray()) {
			//IList jsonList = (IList)baseValue;
			//Array returnObj = (Array)Activator.CreateInstance (baseType, jsonList.Count);
			JSONArray j = (JSONArray)(baseValue);

			List<?> jsonList = (List<?>)Helper.toList(j);              
			Object returnObj = null;
			//returnObj = (List)baseType.newInstance();
			Class myListElementType = baseType.getComponentType();
			
			returnObj = Array.newInstance(myListElementType, jsonList.size());
			//Class myListElementType = jsonList.get(0).getClass();
			
			for (int i = 0; i < j.length(); i++) {
				//returnObj.SetValue (ParseJsonToClassInternal(baseType.GetElementType (), jsonList [i]), i);
				Object obj = ParseJsonToClassInternal(myListElementType, jsonList.get(i));
				//returnObj[i] = (obj);
				Array.set(returnObj, i, obj);

			}
			
			return returnObj;
		}
		//#endregion
		//#region parse class
		//else if (baseType..Namespace.StartsWith ("XYClient")) {
		else if( isWrapClass(baseType) || String.class.isInstance(baseValue)){
			return baseValue;
		} else {
			Object returnObj = null;
			try {
				returnObj = baseType.newInstance();
				Field[] _pis = baseType.getFields();
				Map keyPairs = null;
				Class _baseType = baseValue.getClass();
				keyPairs = (Map)baseValue;
				for(int i = 0; i < _pis.length; i++) {
					String keyName = _pis[i].getName();
					try {
						if(keyPairs.containsKey(keyName)) {
							_pis[i].set(returnObj, keyPairs.get(keyName));
						}
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//Activator.CreateInstance (baseType);
						
			return returnObj;
		}
	}

	//	REBOL note, check if primitive type
	public static boolean isWrapClass(Class clz) { 
        try { 
           return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) { 
            return false; 
        } 
    } 
}