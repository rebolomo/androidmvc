package com.android.mvc.model.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.mvc.util.ReflectionUtils;
import com.android.mvc.util.ValueParse;

import android.widget.TextView;
 
public class BaseData {
	public int id;

	// / <summary>
	//	Indicated which keys changed, only need replace value of these keys when clone.
	// / // REBOL note,
	//	Have to show the client which keys changed, cause of the response of server may contains
	//	only parts of the field of a basedata. Client will only overwrite these keys value 
	//	in deserialize process
	// / </summary>
	public List<String> changed_keys;
	// / <summary>
	// / The changed_data.
	// / </summary>
	private Map<String, Object> changed_data = new HashMap<String, Object>();
	// / <summary>
	// / The all_data.
	// / </summary>
	private Map<String, Object> all_data = new HashMap<String, Object>();
	// / <summary>
	// / The m lock.
	// / </summary>
	private Object mLock = new Object();

	// / <summary>
	// / clone part of the fields: changed_keys
	// / </summary>
	// / <param name="data">Data.</param>
	private void Clone(BaseData data) {
		// lock (mLock) {
		synchronized (mLock) {
			if (data != null) {

				//	get changed_keys
				Class<?> baseType = data.getClass();
				Field[] pis = baseType.getDeclaredFields();
				changed_data.clear();
				for (int i = 0; i < pis.length; i++) {
					Field pi = pis[i];
					if (data.changed_keys == null || data.changed_keys.contains(pi.getName())) {
						// if (pi.CanRead && pi.CanWrite) { // REBOL note,
						// ignore field not in server.
						try {
							changed_data.put(pi.getName(), pi.get(data));
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// }
					}
				}

				//	set fields
				pis = baseType.getDeclaredFields();
				// changed_data.clear();
				for (int i = 0; i < pis.length; i++) {
					Field pi = pis[i];
					if (changed_data.containsKey(pi.getName())) {
						// if (pi.CanWrite) {
						// REBOL TODO
						Object value = null;// ValueParse.ParseJsonToClass
											// (pi.getDeclaringClass(),
											// changed_data.get(pi.getName()));
						try {
							pi.set(this, value);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// ClientLogger.Info (" SetData Data : " + baseType + " " +
						// pi.Name + " " + value);
						// }
					}
				}
			}
		}
	}

	// / <summary>
	// / deep copy, any fields, not only changed_keys.
	// / </summary>
	// / <param name="data">Data.</param>
	public void Copy(BaseData data) {
		// lock (mLock) {
		if (data != null) {

			//Class<?> baseType = data.getClass();
			//Field[] pis = baseType.getDeclaredFields();
			Field[] pis = ReflectionUtils.getDeclaredFields(data);
			changed_data.clear();
			for (int i = 0; i < pis.length; i++) {
				Field pi = pis[i];
				try {
				//  REBOL note, only need public field.
                	int m = pi.getModifiers();
                	if(Modifier.isPublic(m)) 
					{
                		all_data.put(pi.getName(), pi.get(data));
                	}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//	set fields
			//pis = baseType.getDeclaredFields();
			// changed_data.clear();
			for (int i = 0; i < pis.length; i++) {
				Field pi = pis[i];
				if (all_data.containsKey(pi.getName())) {
					// if (pi.CanWrite) {
					// REBOL TODO
					Object value = ValueParse.ParseJsonToClass
										 (pi.getType(),
										 all_data.get(pi.getName()));
					try {
						pi.set(this, value);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// ClientLogger.Info (" SetData Data : " + baseType + " " +
					// pi.Name + " " + value);
					// }
				}
			}
		}
	}

	// / <summary>
	// / REBOL note, common used data assignment, pass data from server to client
	// / </summary>
	// / <param name="userdata"></param>
	public void SetData(BaseData userdata, boolean checkDataChange) {
		if (userdata != null) {
			BaseData basedata = this;
			// BaseData olddata = new BaseData();
			Class<?> t = this.getClass();
			// BaseData olddata = (BaseData)System.Activator.CreateInstance(t);
			BaseData oldData;
			try {
				oldData = (BaseData) t.newInstance();
				oldData.Copy(basedata);// copy olddata.
				basedata.Clone(userdata);// update current data.

				if (checkDataChange) {
					// check data changed, will trigger some events.
					CheckDataChanged(oldData, basedata);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// / <summary>
	// / REBOL note, check data changed, some datas will override to make its own CheckDataChanged
	// / </summary>
	// / <param name="oldData"></param>
	// / <param name="newData"></param>
	public/*virtual*/void CheckDataChanged(BaseData oldData, BaseData newData) {
	}

	private Object ParseJsonToClass( Field field, Object obj) {
		Object value = null;// ValueParse.ParseJsonToClass
		// (pi.getDeclaringClass(),
		// all_data.get(pi.getName()));
		Class<?> type = field.getDeclaringClass();
		try {
			if (type == String.class) {
				// get field value from obj
				value = (String) field.get(obj);
				return value;
			} else if(type == Integer.class) {
				value = (Integer) field.get(obj); 
				return value;
			}
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//	REBOL add, fill item value in a list activity
	public void FillListItem(TextView title, TextView subTitle, TextView desc) {
		//	datas override this FillListItem
	}
}