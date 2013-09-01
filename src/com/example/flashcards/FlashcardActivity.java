package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class FlashcardActivity extends Activity implements OnTouchListener{
	
	TextView keyword;
	TextView definition;	
	
	private String pid;
	private String courseName;
	
	// Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    
    ArrayList<HashMap<String, String>> flashcards;
    
    private GestureDetectorCompat gestureDetector;    
    
    // url to get all notes list
    private static String url_all_notes = "http://lsosa.web44.net/getAllCourses.php";
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NOTES = "notes";
    private static final String TAG_DEFINITION = "definition";
    private static final String TAG_PID = "pid";
    private static final String TAG_COURSE_NAME = "name";
    private static final String TAG_KEYWORD = "keyword";

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		// getting course details from intent
        Intent i = getIntent();
 
        // getting course id (pid) from intent
        pid = i.getStringExtra(TAG_PID);
        courseName = i.getStringExtra(TAG_COURSE_NAME);
        
		setTitle(courseName);
		setContentView(R.layout.flashcard);
		
		gestureDetector = new GestureDetectorCompat(this, new GestureListener());
		
		keyword = (TextView) findViewById(R.id.keyword);
		definition = (TextView) findViewById(R.id.definition);		
		
	}
	
	private class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
	
	public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    	
    	keyword.setText("YEA! Swipe");
    	definition.setText("Learn how to swipe");
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return false;
	}

}
