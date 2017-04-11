package com.android.mvc.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.mvc.R;
import com.android.mvc.R.id;
import com.android.mvc.R.layout;
import com.android.mvc.common.ActivityBase;
import com.android.mvc.message.MobaMessageFunc;
import com.android.mvc.util.Helper;

public class RegisterActivity extends ActivityBase {

    Toolbar toolbar;

    EditText signupUsername, signupPassword, signupEmail;
    Button signupBtn;
    TextView termLabel, mRegularSignup;

    private String username, password, email, language;

    private MobaMessageFunc optionItemClickCallback;
    private MobaMessageFunc backPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        signupUsername = (EditText) findViewById(R.id.signupUsername);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        signupEmail = (EditText) findViewById(R.id.signupEmail);

        termLabel = (TextView) findViewById(R.id.signup_label_terms);

        signupBtn = (Button) findViewById(R.id.signupJoinHowBtn);

        super.OnCreateFinished();
    }
    
    public String getUsername() {
    	return signupUsername.getText().toString();
    }
    
    public String getPassword() {
    	return signupPassword.getText().toString();
    }
    
    public String getEmail() {
    	return signupEmail.getText().toString();
    }
    
    public void SetErrorForUsername(String error) {
    	signupUsername.setError(error);
    }
    
    public void SetErrorForPassword(String error) {
    	signupPassword.setError(error);
    }
    
    public void SetErrorForEmail(String error) {
    	signupEmail.setError(error);
    }

    @Override
    public void onBackPressed(){
        try {
            Helper.invokeMethod(backPressedCallback.handler, backPressedCallback.callback, null);
        } catch (Exception e) {
        }
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

    public void SetBackPressedCallback(MobaMessageFunc callback) {
        backPressedCallback = callback;
    }

    public void SetTermLabelCallback(View.OnClickListener c) {
        termLabel.setOnClickListener(c);
    }

    public void SetSignupBtnCallback(View.OnClickListener c) {
        signupBtn.setOnClickListener(c);
    }

    public void SetOptionsItemSelectedCallback(MobaMessageFunc callback) {
        optionItemClickCallback = callback;
    }


}
