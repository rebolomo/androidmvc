package com.android.mvc.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.android.mvc.message.MobaMessageFunc;
import com.android.mvc.util.Helper;
import com.android.mvc.util.LogTag;

//	Register all models layer.
public class ModelManager
{
	Map<EModelType, IModel> mDicModel;

	ModelManager ()
	{
		Init ();
	}

	static ModelManager mInstance;

	public static ModelManager Instance() {
		if (null == mInstance) {
			mInstance = new ModelManager ();
		}
		return mInstance;
	}

	public void Init ()
	{
		if (mDicModel == null) {
			mDicModel = new HashMap<EModelType, IModel> ();
			//object types = (EModelType[])Enum.GetValues (typeof(EModelType));
			EModelType types[] = EModelType.values();
			//ClientLogger.Info(" ModelManager.Init  " + types.Length);
			Helper.logcat(LogTag.Model, " ModelManager.Init  " + types.length);
			//foreach (object it in types) {
			for(int i = 0; i < types.length; i++) {
				EModelType it = types[i];
				int itInt = Helper.enum2int(it);
				if (itInt <= Helper.enum2int(EModelType.Model_null) || itInt >= Helper.enum2int(EModelType.Model_max))
					continue;
				AddModel (it);
			}
		}
	} 
	void AddModel (IModel model)
	{
		if (null != model) {
			mDicModel.put (model.ModelType(), model);
			model.RegisterMsgHandler ();
		}
	}

	void AddModel (EModelType e)
	{
		if (!mDicModel.containsKey(e)) {
			IModel m = CreateModel (e);
			AddModel (m);
		}
	}

	void RemoveModel (EModelType e)
	{
		if (mDicModel.containsKey(e)) {
			mDicModel.get(e).UnRegisterMsgHandler ();
			mDicModel.remove(e);
		}
	}

	IModel CreateModel (EModelType e)
	{
		IModel ret = null;
		//	REBOL note, replection class from type in java
		//Type t = Type.GetType ("XYClient.Core.Model." + e.ToString ());
		Class<?> t;
		try {
			String cName = e.toString();
			cName = "com.android.mvc.model." + cName;
			t = Class.forName(cName);
			if (null != t) {
				//object obj = System.Activator.CreateInstance (t);
				Object obj = t.newInstance();
				if(null == obj) {
					//ClientLogger.Error(" ====> CreateModel Error : " + e);
					Helper.logcat(LogTag.Model,"  ====> CreateModel Error : " + e.toString());
				}
				ret = (IModel)obj;
			}else{
				Helper.logError("Model", " ====> CreateModel Error: " + e.toString());
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return ret;
	}

	//public Object GetData<T> (EModelType e)
	public <T> Object GetData(EModelType e)
	{
		//T ret = default(T);
		T ret = null;
		if (mDicModel.containsKey(e)) {
//				ClientLogger.Info(" ====> GetData  : " + typeof(T));
			ret = (T)mDicModel.get(e).Data();
		}
		return ret;
	}

	public boolean ValidData (EModelType e)
	{
		return mDicModel.containsKey(e) /*&& mDicModel [e].Valid*/;
	}

	public void SetDataInvalid (EModelType e)
	{
		if (mDicModel.containsKey(e)) {
			mDicModel.get(e).ValidTo( false );
		}
	}

	public void AddListener (EModelType e, MobaMessageFunc msgFunc)
	{
		if (mDicModel.containsKey(e)) {
			mDicModel.get(e).AddModelListner (msgFunc);
		}
	}

	public void RemoveListener (EModelType e, MobaMessageFunc msgFunc)
	{
		if (mDicModel.containsKey(e)) {
			mDicModel.get(e).RemoveModelListner (msgFunc);
		}
	}

	public void InvokeModelMem (EModelType e, String methodName, Object[] param)
	{
		if (Helper.isNullOrBlank(methodName) || !mDicModel.containsKey(e)) {
			return;
		}
		Class t = mDicModel.get(e).getClass();
		if (null == t)
			return;
		Method methodInfo;
		try {
			methodInfo = t.getDeclaredMethod(methodName);
			if (null == methodInfo)
				return;
			methodInfo.invoke (mDicModel.get(e), param);
		} catch (NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
