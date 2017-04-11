package com.android.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.android.mvc.model.data.BaseData;
import com.android.mvc.model.data.UserData;

//  Helper to get data from model layer.
public class ModelTools 
{
    ////////////////////////////////////////////////////////////////////////////////////
    //  Shop
    private static Map<Integer, BaseData> Get_dataMap(EModelType type) {
        Map<Integer, BaseData> ret = (Map<Integer, BaseData>)Get_Data(type);
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //  User
    public static UserData Get_userData_X() {
        UserData ret = (UserData)Get_Data(EModelType.Model_userData);
        return ret;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    static Object Get_Data(EModelType type)
    {
        Object ret = null;
        ModelManager mmng = ModelManager.Instance();
        if (mmng != null && mmng.ValidData(type))//EModelType.Model_orderListData))
        {
            ret = /*(OrderData) */mmng.GetData/*<OrderData>*/(type);//EModelType.Model_orderListData);
        }
        return ret;
    }
}