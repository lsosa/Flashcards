package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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
	
	private int flashcardPosition = 0; 
	
	private String lectureID;
	private String courseName;
	
	// Creating JSON Parser object
    JSONParser jParser = new JSONParser(); 
    
    ArrayList<HashMap<String, String>> selectedNotes = new ArrayList<HashMap<String,String>>();
    
    ArrayList<HashMap<String, String>> coursesList;
    ArrayList<HashMap<String, String>> lecturesList;
    ArrayList<HashMap<String, String>> notesList;
    
    private GestureDetectorCompat gestureDetector;   

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		// getting lecture details from intent
		Intent i = getIntent();
        
        coursesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("courses");
        lecturesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("lectures");
        notesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("notes");
 
        // getting lecture id from intent
        lectureID = i.getStringExtra("id");
        courseName = i.getStringExtra("name");       
        
		setTitle(courseName + " Flashcards");
		setContentView(R.layout.flashcard);	
		
		gestureDetector = new GestureDetectorCompat(this, new GestureListener());
		
		keyword = (TextView) findViewById(R.id.keyword);
		definition = (TextView) findViewById(R.id.definition);
		
		getNotesFromSelectedLecture();
		setFirstFlashCard();
		
	}
	
	private void getNotesFromSelectedLecture(){
		
		for(int i = 0; i < notesList.size(); i++){
    		
    		if(notesList.get(i).get("lecture_id").equals(lectureID))
    		{
    			selectedNotes.add(lecturesList.get(i));
    		}
    	}
	}
	
	@Override
	protected void onPause() {		
		super.onPause();
		//finish();
	}

	private void setFirstFlashCard(){
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				//Check if we have at least one note for the lecture.
				if(selectedNotes.size() != 0){
					
					keyword.setText(notesList.get(0).get("term"));
			    	definition.setText(notesList.get(0).get("definition"));
				}else{
					
					keyword.setText("No Notes :(");
					definition.setText("Go and create some notes so that you can study!");
				}				
			}
		});		
		
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
		
		flashcardPosition--;
		
		if(flashcardPosition < 0 && selectedNotes.size() != 0){
			
			flashcardPosition = selectedNotes.size() - 1;
			
			keyword.setText(notesList.get(flashcardPosition).get("term"));
	    	definition.setText(notesList.get(flashcardPosition).get("definition"));
		}else if(selectedNotes.size() != 0){		
			
			keyword.setText(notesList.get(flashcardPosition).get("term"));
	    	definition.setText(notesList.get(flashcardPosition).get("definition"));
		}else {
			
			flashcardPosition = 0;
		}
    }

    public void onSwipeLeft() {
    	
    	flashcardPosition++;
    	
    	if(flashcardPosition < selectedNotes.size()){
    		
    		keyword.setText(notesList.get(flashcardPosition).get("term"));
	    	definition.setText(notesList.get(flashcardPosition).get("definition"));
    	}else if(selectedNotes.size() != 0){
    		
    		flashcardPosition = 0;
    		
    		keyword.setText(notesList.get(flashcardPosition).get("term"));
	    	definition.setText(notesList.get(flashcardPosition).get("definition"));
    	}else{
    		
    		flashcardPosition = 0;
    	}
    	
    	
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
