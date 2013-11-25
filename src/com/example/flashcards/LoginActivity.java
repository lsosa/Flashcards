package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import com.togonotes.flashcards.R;

public class LoginActivity extends Activity{
	
	private EditText userName, userPassword;
	private Button bLogin;
	
	private ProgressDialog pDialog;
	
	ArrayList<ArrayList<HashMap<String, String>>> userData;
	
	private Context loginActivity = this;		
	private String api_key = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		userData = new ArrayList<ArrayList<HashMap<String,String>>>();
		
		userName = (EditText) findViewById(R.id.userName);
		userPassword = (EditText) findViewById(R.id.userPassword);
		
		bLogin = (Button) findViewById(R.id.bSubmitLogin);		
		
		bLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(userName.getText().toString().equals("")){
					
					userName.setHintTextColor(Color.RED);
					userName.setHint("Enter username");					
				}else if(userPassword.getText().toString().equals("")){
					
					userPassword.setHintTextColor(Color.RED);;
					userPassword.setHint("Enter password");
				}else{
					
					new AuthUser().execute();
				}							
			}
		});
	}
	
	private class AuthUser extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {			
			super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			api_key = Database.getInstance().LoginUser(userName.getText().toString(), userPassword.getText().toString());
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			
			pDialog.dismiss();
			
			if(!api_key.equals("")){
				
				new GetAllUserData().execute();
			}else{
				
				new AlertDialog.Builder(loginActivity).setTitle("Login Error").setMessage("Incorrect username or password").setNeutralButton("OK", null).show();
			}
		}	
		
	}
	
	private class GetAllUserData extends AsyncTask<String, String, String>{
		
		/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading User Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

		@Override
		protected String doInBackground(String... arg0) {

			userData = Database.getInstance().LoadAllUserData();
			
			return null;
		}
		
		/**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all data
            pDialog.dismiss();         
            	
            Intent i = new Intent(getApplicationContext(), AllCoursesActivity.class);   		
    		startActivity(i);
    		finish();                      
        }
	}
	
	
}
