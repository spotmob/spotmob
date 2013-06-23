package com.sellaspot;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sellaspot.datamodel.RESTData;
import com.sellaspot.db.DBSession;

public class RegisterActivity extends Activity {
	
	RegisterActivity registerActivity;
	String city;
	String state;
	String country;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		setContentView(R.layout.register);
		setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		registerActivity = this;
		
		ActionBar actionbar = getActionBar();
        
        Drawable d=getResources().getDrawable(R.drawable.banner);
        //actionbar.setBackgroundDrawable(d);
        actionbar.setTitle("SpotMob");
        actionbar.setIcon(d);

	    
        final Button button2 = (Button) findViewById(R.id.registerButton);
        button2.setOnClickListener(new View.OnClickListener() {
            
        	public void onClick(View v) {
            
        		TextView messageTextView = (TextView)findViewById(R.id.messageTextView);
        		
               	if(validateFields() && validatePasswordFields()) {
            	
	            	TextView passwordTextView = (TextView)registerActivity.findViewById(R.id.password);
	            	TextView emailTextView = (TextView)registerActivity.findViewById(R.id.email);
	            	TextView phoneTextView = (TextView)registerActivity.findViewById(R.id.phone);
	
	            	//Call API to register
	            	String userIdAndsessionId = RESTData.registerUser(
	            			emailTextView.getText().toString(),
	            			passwordTextView.getText().toString(),
	            			phoneTextView.getText().toString());
	
	            	if(userIdAndsessionId != null) {
	            		//If success, Insert username and session Id in db
	            		String[] data = userIdAndsessionId.split(",");
	            		new DBSession(registerActivity).insertIntoDB(data[0], data[1], emailTextView.getText().toString(), emailTextView.getText().toString());
	            	
	            		//Go to Sell a Spot main tabbed screen
	            		Intent intent = new Intent(registerActivity, MainActivity.class);
	            		registerActivity.startActivity(intent);
	            		finish();
	            	} else {
	            		//If register fails go to login page
	            		Intent intent2 = new Intent(registerActivity, LoginActivity.class);
	            		registerActivity.startActivity(intent2);
	            		finish();
	            	}
               	}
               	else {
               		
        			messageTextView.setText("Check the information entered");
        			
               	}
        	}
        });
        
	}

	public boolean validateFields() {
		
    	TextView emailTextView = (TextView)findViewById(R.id.email);
    	TextView passwordTextView = (TextView)findViewById(R.id.password);
    	TextView confirmPasswordTextView = (TextView)findViewById(R.id.confirmPassword);
    	TextView phoneTextView = (TextView)findViewById(R.id.phone);
    	
    	return (!(emailTextView.getText().toString().equalsIgnoreCase("") || 
    			passwordTextView.getText().toString().equalsIgnoreCase("") || 
    			confirmPasswordTextView.getText().toString().equalsIgnoreCase("") ||
    			phoneTextView.getText().toString().equalsIgnoreCase(""))
    			);
	}
	
	
	public boolean validatePasswordFields() {
		
    	TextView passwordTextView = (TextView)findViewById(R.id.password);
    	TextView confirmPasswordTextView = (TextView)findViewById(R.id.confirmPassword);
    	
    	return (passwordTextView.getText().toString().equals(confirmPasswordTextView.getText().toString()));
	}
	
}
