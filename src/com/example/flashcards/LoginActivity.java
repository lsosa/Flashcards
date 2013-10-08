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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

public class LoginActivity extends Activity{
	
	private EditText userEmail, userPassword;
	private Button bLogin;
	
	private ProgressDialog pDialog;
	
	ArrayList<ArrayList<HashMap<String, String>>> userData;
	
	private Context loginActivity = this;
	private Boolean isUserAuthenticated;	
	private String users_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		userData = new ArrayList<ArrayList<HashMap<String,String>>>();
		
		userEmail = (EditText) findViewById(R.id.userEmail);
		userPassword = (EditText) findViewById(R.id.userPassword);
		
		bLogin = (Button) findViewById(R.id.bSubmitLogin);
		
		bLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				users_id = "1";
				
				isUserAuthenticated = logInUser();
				
				if(isUserAuthenticated){
					
					new getAllUserData().execute();					
				}else{
					
					new AlertDialog.Builder(loginActivity).setTitle("Login Error").setMessage("Incorrect username or password").setNeutralButton("OK", null).show();
				}				
			}
		});
	}
	
	private Boolean logInUser(){
		
		return true;
		//return false;
	}
	
	private class getAllUserData extends AsyncTask<String, String, String>{
		
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

			Database d = new Database(users_id, "");
			userData = d.LoadAllUserData();
			
			return null;
		}
		
		/**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all data
            pDialog.dismiss();
            
            //Check if we have the courses, lectures, and notes for the user.
            if(userData.size() == 3){
            	
            	Intent i = new Intent(getApplicationContext(), AllCoursesActivity.class);
    			i.putExtra("users_id", users_id);
    			i.putExtra("courses", userData.get(0));
    			i.putExtra("lectures", userData.get(1));
    			i.putExtra("notes", userData.get(2));
    			startActivity(i);
    			finish();
            }           
        }
	}
	
	
}
