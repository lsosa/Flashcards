package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FlashcardActivity extends Activity{
	
	TextView keyword;
	TextView definition;
	
	private String courseName;
	
	// Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    
    ArrayList<HashMap<String, String>> flashcards;
    
    // url to get all notes list
    private static String url_all_notes = "http://lsosa.web44.net/getAllCourses.php";
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NOTES = "notes";
    private static final String TAG_DEFINITION = "definition";
    private static final String TAG_PID = "pid";
    private static final String TAG_KEYWORD = "keyword";

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.flashcard);
		
		keyword = (TextView) findViewById(R.id.keyword);
		definition = (TextView) findViewById(R.id.definition);
		
	}

}
