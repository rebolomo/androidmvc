package com.android.mvc.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.android.mvc.message.ClientMsg;
import com.android.mvc.message.MobaMessage;
import com.android.mvc.message.MobaMessageFunc;
import com.android.mvc.message.MobaMessageManager;
import com.android.mvc.message.OperationResponse;
import com.android.mvc.model.data.BaseData;
import com.android.mvc.util.Helper;
import com.android.mvc.util.LogTag;
import com.android.mvc.util.ReflectionUtils;
import com.android.mvc.webservice.WebReturnCode;
//	internal in C#,  remove public modifier in JAVA.
abstract class ModelBase<T> implements IModel
//internal abstract class ModelBase<T> :IModel
{
	//	REBOL add
	protected Class itemClass;
	EModelType modelType;
	boolean bValid;
	int lastError;
	String debugMessage;
	int msgType;
	int msgID;
	T data;

	protected ModelBase ()
	{
		bValid = true;
		lastError = Helper.enum2int(WebReturnCode.OK);
	}

	protected void Init (EModelType mt)
	{
		modelType = mt;
	}

	public EModelType ModelType() { return modelType; }

	public boolean Valid() { return bValid; } 

	public void ValidTo(boolean value) { bValid = value; }

	public Object Data() { return data; } 
	
	protected void DataTo(Object value) { data = (T)value; }

	public int LastMsgType() { return msgType; } 
	
	protected void LastMsgTypeTo(int value) { msgType = value; }

	public int LastMsgID() { return msgID; } 
	
	protected void LastMsgIDTo(int value) { msgID = value; }

	public int LastError() { return lastError; } 
	
	protected void LastErrorTo(int value) { lastError = value; }

	public String DebugMessage() { return debugMessage; } 
	
	protected void DebugMessageTo(String value) { debugMessage = value; }

    public void SetData(Object userdata) {
    	SetData(userdata, true);
    }
    
    public void SetData(Object userdata, boolean checkDataChange /*= true*/)
    {
        if (userdata != null)
        {
        	Class<?> baseType = userdata.getClass();
        	Class<?> parentType = baseType.getSuperclass();
            if (baseType.getSuperclass() == BaseData.class)
            {
                BaseData basedata = (BaseData) Data();
                if (basedata != null)
                {
                    basedata.SetData((BaseData) userdata, checkDataChange);
                }
                else
                {
                	DataTo( userdata);
                }
            }
            else
            {
            	DataTo(userdata); 
            }
        }
    }

    public abstract void RegisterMsgHandler ();

	public abstract void UnRegisterMsgHandler ();

	public /*virtual*/ void AddModelListner (MobaMessageFunc msgFunc)
	{
		if (null != msgFunc) {
			//	REBOL TODO, modeltype to clientmsg
			int intModelType = Helper.enum2int(modelType);
			MobaMessageManager.RegistMessage (/*Helper.int2enum(intModelType, ClientMsg.class)*/intModelType, msgFunc);
		}
	}

	public void RemoveModelListner (MobaMessageFunc msgFunc)
	{
		if (null != msgFunc) {
			int intModelType = Helper.enum2int(modelType);
			MobaMessageManager.UnRegistMessage (/*Helper.int2enum(intModelType, ClientMsg.class)*/intModelType, msgFunc);
		}
	}

	protected abstract void OnGetMsg (MobaMessage msg);

	//internal static 
	CommonModelNotifyParam GetNotifyData()
    {
        CommonModelNotifyParam ret = null;
        if (null != this)
        {
            ret = new CommonModelNotifyParam(this);
        }
        return ret;
    }
	

	//	delete single item
	protected void DeleteSingleItem(int itemId) {
		if (itemId > 0)
	    {
	        Map<Integer, Object> oldList = (Map<Integer, Object>)this.Data();//new HashMap<Integer, CustomerData>();
	        if(oldList == null) {
            	oldList = new HashMap<Integer, Object>();
            }
	        oldList.remove(itemId);
	        this.DataTo( oldList );
	        this.DebugMessageTo("delete single item for: " + modelType.toString());
	        Helper.logcat(LogTag.Model, this.DebugMessage());
	    }
	}
	
	//	insert one
	protected void PushSingleItem(Object singleData) {
		Object itemData = (Object)singleData;
		if (itemData != null)
        {
            Map<Integer, Object> oldList = (Map<Integer, Object>)this.Data();//new HashMap<Integer, CustomerData>();
            if(oldList == null) {
            	oldList = new HashMap<Integer, Object>();
            }

            //Field f;
			try {
				//f = itemData.getClass().getDeclaredField("id");
				//Integer id = f.getInt(itemData);
				Integer id = (Integer) ReflectionUtils.getFieldValue(itemData, "id");
                oldList.put(id, itemData);
                this.DataTo( oldList );
                this.DebugMessageTo("insert one for: " + modelType.toString());
                Helper.logcat(LogTag.Model, this.DebugMessage());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }	
		
	}
	
	//	replace parts
	protected void ReplaceParts(Object listData) 
	{
		List<Object> itemDataList = (List<Object>)listData;
		if (itemDataList != null)
        {
			Field f;
			try {
                Map<Integer, Object> oldList = (Map<Integer, Object>)this.Data();//new HashMap<Integer, Object>();
                if(oldList == null) {
                	oldList = new HashMap<Integer, Object>();
                }
                for (int i = 0; i < itemDataList.size(); i++)
                {
                	Object itemData = itemDataList.get(i);
                	Class c = itemData.getClass();
                	f = c.getField("id");
					Integer id = f.getInt(itemData);
                    oldList.put(id, itemData);
                }

                this.DataTo( oldList );
                this.DebugMessageTo("replace parts for: !" + modelType.toString());
                Helper.logcat(LogTag.Model, this.DebugMessage());
                
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	//	replace all
	protected void ReplaceAll(Object listData) 
	{
		List<Object> itemDataList = (List<Object>)listData;
		if (itemDataList != null)
        {
			Field f;
			try {
				Map<Integer, Object> oldList = new HashMap<Integer, Object>();
                for (int i = 0; i < itemDataList.size(); i++)
                {
                	Object itemData = itemDataList.get(i);
                	Class c = itemData.getClass();
                	f = c.getField("id");
					Integer id = f.getInt(itemData);
                    oldList.put(id, itemData);
                }

                this.DataTo( oldList );
                this.DebugMessageTo("replace all for: " + modelType.toString());
                Helper.logcat(LogTag.Model, this.DebugMessage());
                
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            
        }
	}
	
	//	replace single in non-map data layer, take Model_userData for example.
	protected void ReplaceAllNoMap(Object singleData) 
	{
		if (singleData != null)
        {
            this.DataTo( singleData );
            this.DebugMessageTo("get single item for: " + modelType.toString());
            Helper.logcat(LogTag.Model, this.DebugMessage());
        }
	}
}
