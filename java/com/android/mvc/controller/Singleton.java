package com.android.mvc.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/***
 * 
 * @author Linux yiye
 *
 * @param <T>
 */
public class Singleton<T> {

	/***
	 * use ConcurrentHashMap to store singletons
	 * Class as key ---singleton type
	 * Object as value---singleton itself
	 */
	private static final ConcurrentMap<Class, Object> map = new ConcurrentHashMap<>();
	
	/***
	 * 
	 * @param type  
	 * @return T is the singleton
	 */
	public static<T>T getSingleton(Class<T> type){
		/***
		 * get singleton from map
		 */
		Object ob = map.get(type);
		
			try {
				if(ob==null){
			/***
			 * use map in safe mode, set the lock
			 */
					synchronized (map) {
						/***
						 * create a instance 
						 */
					ob=  type.newInstance();
	
					map.put(type,ob);
					}
				}
				
			/***
			*  exception from type.newInstance()
		        *  
			*IllegalAccessException - if the class is not accesible, or the null constructor is not accessible
			*InstantiationException - if the class is interface, abstract, array class, primitive class, void, etc
			*/
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		/***
		 * instance to T
		 */
		return  (T) ob;
	}

	public static<T> void Remove(Class<T> type){
		map.remove(type);
	} 
}