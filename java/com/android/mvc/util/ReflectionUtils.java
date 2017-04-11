package com.android.mvc.util;

import java.lang.reflect.Field;  
  
import java.lang.reflect.InvocationTargetException;  
import java.lang.reflect.Method;  
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
  
/** 
 * reflection helper
 * @author syh 
 * 
 */  
  
public class ReflectionUtils {  
  
    /** 
     * loop in the class hierachy to find DeclaredMethod 
     * @param object : object in child class
     * @param methodName : method name in parent class
     * @param parameterTypes : parameter types in parent class
     * @return method in parent class
     */  
      
    public static Method getDeclaredMethod(Object object, String methodName, Class<?> ... parameterTypes){  
        Method method = null ;  
          
        for(Class<?> clazz = object.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                method = clazz.getDeclaredMethod(methodName, parameterTypes) ;  
                return method ;  
            } catch (Exception e) {   
                //  have to do nothing here, don't throw exeption here.
                //  if print exeption or throw it, will not execute clazz = clazz.getSuperclass(), 
                //  thus can't access to parent class.
            }  
        }  
          
        return null;  
    }  
      
    /** 
     * call method directly, ignore access decorator(private, protected, default)
     * @param object : 子类对象 
     * @param object : object in child class
     * @param methodName : method name in parent class
     * @param parameterTypes : parameter types in parent class
     * @return method result in parent class
     */  
      
    public static Object invokeMethod(Object object, String methodName, Class<?> [] parameterTypes,  
            Object [] parameters) {  
        //  get method 
        Method method = getDeclaredMethod(object, methodName, parameterTypes) ;  
          
        // ignore access decorator
        method.setAccessible(true) ;  
          
            try {  
                if(null != method) {  
                      
                    // call the method with parameters
                    return method.invoke(object, parameters) ;  
                }  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            } catch (InvocationTargetException e) {  
                e.printStackTrace();  
            }  
          
        return null;  
    }  
  
    /** 
     * loop in the class hierachy to find DeclaredMethod 
     * @param object : object in child class
     * @param fieldName : field name in parent class
     * @param parameterTypes : parameter types in parent class
     * @return field in parent class
     */  
    public static Field getDeclaredField(Object object, String fieldName){  
        Field field = null ;  
          
        Class<?> clazz = object.getClass() ;  
          
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                field = clazz.getDeclaredField(fieldName) ;  
                return field ;  
            } catch (Exception e) {  
                //  have to do nothing here, don't throw exeption here.
                //  if print exeption or throw it, will not execute clazz = clazz.getSuperclass(), 
                //  thus can't access to parent class.
            }   
        }  
      
        return null;  
    }     
      
    public static Field[] getDeclaredFields(Object object){  
        List<Field> fields = new ArrayList<Field>() ;  
          
        Class<?> clazz = object.getClass() ;  
          
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                Field[] pis = clazz.getDeclaredFields() ; 
                for(int i = 0; i < pis.length; i++) {
                	fields.add(pis[i]);
                }        
            } catch (Exception e) {  
                //  have to do nothing here, don't throw exeption here.
                //  if print exeption or throw it, will not execute clazz = clazz.getSuperclass(), 
                //  thus can't access to parent class. 
            }   
        }  
        Field[] array = new Field[fields.size()];  
        array = fields.toArray(array); 

        return array;
    }   
    /** 
     * set field directly, ignore decorator
     * @param object : object in child class
     * @param fieldName : field name in parent class
     * @param value : value to set 
     */  
      
    public static void setFieldValue(Object object, String fieldName, Object value){  
        //  get field  
        Field field = getDeclaredField(object, fieldName) ;  

        field.setAccessible(true) ;  
          
        try {  
             field.set(object, value) ;  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        }  
          
    }  
      
    /** 
     * get field directly, ignore decorator
     * @param object : object in child class
     * @param fieldName : field name in parent class
     * @param value : value of field 
     */  
    public static Object getFieldValue(Object object, String fieldName){   
        Field field = getDeclaredField(object, fieldName) ; 
          
        field.setAccessible(true) ;  
          
        try {  
            return field.get(object) ;  
              
        } catch(Exception e) {  
            e.printStackTrace() ;  
        }  
          
        return null;  
    }  
}  