package com.android.mvc.model;

import com.android.mvc.util.Helper;

public enum EModelType
{
    Model_null(Helper.modelTypeBegin) ,//= ClientMsg.ModelChanged_Begin,
 //    Model_orderListData(Helper.modelTypeBegin + 1), 
 //    Model_productListData(Helper.modelTypeBegin + 2),
	// Model_customerListData(Helper.modelTypeBegin + 3),
 //    Model_orderProductListData(Helper.modelTypeBegin + 5),
 //    Model_shopListData(Helper.modelTypeBegin + 6),
    Model_userData(Helper.modelTypeBegin + 7),		//	user data
    Model_max(Helper.modelTypeEnd);// = ClientMsg.ModelChanged_End
    
    private int value = 0;

    private EModelType(int value) {    //    must be private, otherwise cause compiler error
        this.value = value;
    }
}