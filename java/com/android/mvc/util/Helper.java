package com.android.mvc.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.mvc.message.ClientMsg;
import com.android.mvc.webservice.WebServiceType;
import com.androidquery.AQuery;
import com.androidquery.auth.BasicHandle;
import com.androidquery.callback.AjaxCallback;

public class Helper extends Application implements Constants{
	/*
	public static<T>T Instance(Class<T> type) {
    	//Class<K> type = null;
        return Singleton.getSingleton(type);
    }*/
	
	/**
     * Returns a java.util.Map containing all of the entries in this object.
     * If an entry in the object is a JSONArray or JSONObject it will also
     * be converted.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a java.util.Map containing the entries of this object
     */
    public static Map<String, Object> toMap(JSONObject json) {
    	Map<String, Object> results = new HashMap<String, Object>();
		try {
	    	
	    	Iterator keys=json.keys();  
	    	while(keys.hasNext()){  
	    		String key=(String) keys.next();  
	            Object value;

				value = json.get(key);
				if (value == null || JSONObject.NULL.equals(value)) {
	                value = null;
	            } else if (value instanceof JSONObject) {
	            	JSONObject obj = (JSONObject) value;
	                value = toMap(obj);
	            } else if (value instanceof JSONArray) {
	            	JSONArray obj = (JSONArray) value;
	                value = toList(obj);
	            } 
	            results.put(key, value);
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return results;
    }
    
    /**
     * Returns a java.util.List containing all of the elements in this array.
     * If an element in the array is a JSONArray or JSONObject it will also
     * be converted.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a java.util.List containing the elements of this array
     */
    public static List<Object> toList(JSONArray jsonArray) {
        List<Object> results = new ArrayList<Object>(jsonArray.length());
        try {
        for (int i = 0; i < jsonArray.length(); i++) {
        	Object element = jsonArray.get(i);
			
            if (element == null || JSONObject.NULL.equals(element)) {
                results.add(null);
            } else if (element instanceof JSONArray) {
            	JSONArray array = (JSONArray) element;
                results.add(toList(array));
            } else if (element instanceof JSONObject) {
            	JSONObject obj = (JSONObject) element;
                results.add(toMap(obj));
            } else {
                results.add(element);
            }
        }
			
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return results;
    }
    
    @SuppressLint("NewApi")
	public boolean isValidEmail(String email) {

    	if (TextUtils.isEmpty(email)) {

    		return false;

    	} else {

    		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    	}
    }
    
    public boolean isValidLogin(String login) {

        String regExpn = "^([a-zA-Z]{4,24})?([a-zA-Z][a-zA-Z0-9_]{4,24})$";
        CharSequence inputStr = login;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }

    public boolean isValidSearchQuery(String query) {

        String regExpn = "^([a-zA-Z]{1,24})?([a-zA-Z][a-zA-Z0-9_]{1,24})$";
        CharSequence inputStr = query;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }
    
    public boolean isValidPassword(String password) {

        String regExpn = "^[a-z0-9_]{6,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }
    
    public static String getCurrentTime() {
		long time = System.currentTimeMillis();
		String dateFormatted = Helper.timestamp2string(time);
		return dateFormatted;
    }
    
    public static long getCurrentTimestamp() {
		long time = System.currentTimeMillis();
		return time;
    }
    
    public static String timestamp2string(long dateTime) {
    	Timestamp t = new Timestamp(dateTime);
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        String dateFormatted = df.format(t);
		return dateFormatted;
    }
 
    public static void logError(String TAG, String msg) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        boolean logged = false;
        boolean foundMe = false;
        for(int i=0; i<stacktrace.length; i++) {
            StackTraceElement e = stacktrace[i];
            String methodName = e.getMethodName();
            if (foundMe) {
                if (!methodName.startsWith("access$")) {
                    Log.e(TAG, msg + "---" + String.format(Locale.US, "%s.%s", e.getClassName(), methodName));
                    logged = true;
                    break;
                }
            } else {
                if (methodName.equals("logcat")) {
                    foundMe = true;
                }
            }
        }
        if (!logged)
            Log.e(TAG, msg + "---" + "unlogged call");
    }
    
    public static void logcat(String TAG, String msg) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        boolean logged = false;
        boolean foundMe = false;
        for(int i=0; i<stacktrace.length; i++) {
            StackTraceElement e = stacktrace[i];
            String methodName = e.getMethodName();
            if (foundMe) {
                if (!methodName.startsWith("access$")) {
                    Log.i(TAG, msg + "---" + String.format(Locale.US, "%s.%s", e.getClassName(), methodName));
                    logged = true;
                    break;
                }
            } else {
                if (methodName.equals("logcat")) {
                    foundMe = true;
                }
            }
        }
        if (!logged)
            Log.e(TAG, msg + "---" + "unlogged call");
    }
    
    public static void apiRequest(AQuery aq, Context context, BasicHandle handle, String url, 
    		WebServiceType method, Map<String, Object> params, Object object, String callback) {
		Helper.logcat(LogTag.Web, url);
		
    	String contentHeader = "application/json";
    	AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
 		
        /*
    	//	REBOL todo, add progress bar in all activities, avoid creation in realtime.
    	ProgressDialog dialog = new ProgressDialog(context);
    	dialog.setIndeterminate(true);
    	dialog.setCancelable(false);
    	dialog.setInverseBackgroundForced(false);
    	dialog.setCanceledOnTouchOutside(true);
    	dialog.setTitle("Request...");
    	*/
    	StringEntity entity = null;
    	if(params != null) {
    		JSONObject input = new JSONObject(params);
    		entity = new StringEntity(input.toString(), "UTF-8");
    	}
		
    	if(method == WebServiceType.PUT) {
    		cb.weakHandler(object, callback).timeout(TIME_OUT);
    		aq/*.progress(dialog)*/.auth(handle).put(url, contentHeader, entity, JSONObject.class, cb);
    	} else if(method == WebServiceType.DELETE) {
    		aq/*.progress(dialog)*/.auth(handle).delete(url, JSONObject.class, object, callback);
    	} else if(method == WebServiceType.POST) {
    		/*cb.url(url).type(JSONObject.class).method(AQuery.METHOD_POST).header("Content-Type", contentHeader);
    		
    		if(entity != null) {
    			cb.param(AQuery.POST_ENTITY, entity);
    		}
    		*/
            Map<String, Object> params_post = new HashMap<String, Object>();
            params_post.put(AQuery.POST_ENTITY, entity);

    		cb.weakHandler(object, callback).header("Content-Type", contentHeader).timeout(TIME_OUT);
    		aq.auth(handle).ajax(url, params_post, JSONObject.class, cb);
    	} else if(method == WebServiceType.GET) {
            //  REBOL note, we put params in url in get mode.
            String query = urlEncodeUTF8(params);
            
            url += "?" + query;

    		cb.url(url).type(JSONObject.class).method(AQuery.METHOD_GET).header("Content-Type", contentHeader);
    
    		cb.weakHandler(object, callback).timeout(TIME_OUT);
    		aq/*.progress(dialog)*/.auth(handle).ajax(cb);
    	}
    }
    
    //	REBOL note, enums must have constructor with number parameter
    public static <T> T int2enum(int input, Class<T> enumType){
    	try {
			Method m = enumType.getMethod("values");
			T[] values = (T[]) m.invoke(null); 
			for(int i = 0; i < values.length; i++) {
				T v = values[i];
				Field f = enumType.getDeclaredField("value");
				f.setAccessible(true);
				int d = f.getInt(v);
				if(d == input) {
					return v;
				}
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public static <T> int enum2int(Object obj) {
    	try {
            Class<?> enumType = obj.getClass();
			Field f = enumType.getDeclaredField("value");
			f.setAccessible(true);
			int value = f.getInt(obj);
			return value;
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return -10000;
    }
    
    public static Map<String, Object> CreateHash(Object ...args) {
    	//	REBOL todo
    	Map<String, Object> hash = new HashMap<String, Object>();
		for (int i = 0; i < args.length; i += 2) {
			hash.put (args [i].toString(), args [i + 1]);
		}
	
		return hash;
    }
    
    public static Map<String, String> CreateHash(Map params) {
		return params;
    }
    
    public static boolean isNullOrBlank(String param) { 
	    return param == null || param.trim().length() == 0;
	}

	// REBOL note, learn from androidquery internal code.
	public static Object invokeMethod(Object handler, String callback,
	/*boolean fallback, */Class<?>[] cls, /*Class<?>[] cls2, */Object... params) throws Exception {

		if (handler == null || callback == null)
			return null;

		Method method = null;

		if (cls == null)
			cls = new Class[0];
		//method = handler.getClass().getDeclaredMethod(callback, cls);
		method = ReflectionUtils.getDeclaredMethod(handler, callback, cls);
		method.setAccessible(true);
		return method.invoke(handler, params);
	}

    public static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    
    public static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                urlEncodeUTF8(entry.getKey().toString()),
                urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();       
    }

    
    public static int uiMessageTypeBegin = Helper.enum2int(ClientMsg.UI_Msg_Begin);
    public static int uiMessageTypeEnd = Helper.enum2int(ClientMsg.UI_Msg_End);
    
    public static int modelTypeBegin = Helper.enum2int(ClientMsg.ModelChanged_Begin);
    public static int modelTypeEnd = Helper.enum2int(ClientMsg.ModelChanged_End);
}
