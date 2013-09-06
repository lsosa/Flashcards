package com.example.flashcards;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	EditText userEmail, userPassword;
	Button bLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		userEmail = (EditText) findViewById(R.id.userEmail);
		userPassword = (EditText) findViewById(R.id.userPassword);
		
		bLogin = (Button) findViewById(R.id.bSubmitLogin);
		
		bLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String users_id = "1";
				
				Intent i = new Intent(getApplicationContext(), Database.class);
				i.putExtra("users_id", users_id);
				startActivity(i);
				finish();
			}
		});
	}
	
	
}
