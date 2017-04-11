package com.android.mvc.view;

import com.android.mvc.R;
import com.android.mvc.common.ActivityBase;
import com.android.mvc.message.MobaMessageFunc;
import com.android.mvc.util.Helper;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends ActivityBase {
	////////////////////////////////////
	//	UI
    private Toolbar toolbar;
    private Button signinBtn;
    private EditText signinUsername, signinPassword;
    
    /////////////////////////////////////
    //	Callback delegate
    private MobaMessageFunc optionItemClickCallback;
    private MobaMessageFunc backPressedCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
    
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        signinUsername = (EditText) findViewById(R.id.signinUsername);
        signinPassword = (EditText) findViewById(R.id.signinPassword);

        signinBtn = (Button) findViewById(R.id.signinBtn);
        
        super.OnCreateFinished();
    }
    
    public void SetSigninBtnCallback(View.OnClickListener c) {
        signinBtn.setOnClickListener(c);
    }

    public void SetOptionsItemSelectedCallback(MobaMessageFunc callback) {
        optionItemClickCallback = callback;
    }
    
    public void SetBackPressedCallback(MobaMessageFunc callback) {
    	backPressedCallback = callback;
    }
    
    public String getUsername() {
    	return signinUsername.getText().toString();
    }
    
    public String getPassword() {
    	return signinPassword.getText().toString();
    }
    
    public void SetErrorForUsername(String error) {
    	signinUsername.setError(error);
    }
    
    public void SetErrorForPassword(String error) {
    	signinPassword.setError(error);
    }

    @Override
    public void onBackPressed(){
        try {
            Helper.invokeMethod(backPressedCallback.handler, backPressedCallback.callback, null);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);

        menu.add(11, R.id.menu_item1, 1, "Forget Password");
        return true;
    }

    //  REBOL note, move to ctrl
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Class<?>[] classes = { MenuItem.class };
        try {
            boolean result = (boolean) Helper.invokeMethod(optionItemClickCallback.handler, optionItemClickCallback.callback, classes, item);
            if(result) {
                return result;
            }
        } catch (Exception e) {
        }
        
        return super.onOptionsItemSelected(item);
    }
}
