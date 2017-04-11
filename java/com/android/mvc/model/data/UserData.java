package com.android.mvc.model.data;

import android.widget.TextView;
 
//	User data
public class UserData extends BaseData {

	//int id;
    public String email;
    public String username;
    public int role_id;
    //String password_hash = db.Column(db.String(128))
    public boolean confirmed;
    public String name;
    
    //String location = db.Column(db.String(64))
    //about_me = db.Column(db.Text())
    public int created_time;
    public int last_seen;

    public int default_shop_id; // the default shop this user selected

    //  REBOL add, from BaseData
    @Override
    public void FillListItem(TextView title, TextView subTitle, TextView desc) {

    }
}
