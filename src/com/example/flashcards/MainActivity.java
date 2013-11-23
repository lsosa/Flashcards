package com.example.flashcards;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import com.togonotes.flashcards.R;


public class MainActivity extends Activity {
	
	TextView tvTitle;
	Button bLogin, bRegister;
	
	String registerURL = "http://togonotes.com/account/login";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bLogin = (Button) findViewById(R.id.bLogin);
		bRegister = (Button) findViewById(R.id.bRegister);
		
		bLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		bRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(registerURL));
				startActivity(i);
			}
		});
	}
}
