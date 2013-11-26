package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.TypedValue;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.togonotes.flashcards.R;

public class FlashcardActivity extends Activity{
	
	TextView keyword;
	TextView definition;
	Button checkbox;
	
	Animation animFadeIn, animFadeOut;
	
	LinearLayout linearLayout;
	
	private int flashcardPosition = 0;	
	
	private ProgressDialog pDialog;
	
	private String lectureID;
	private String courseName;		 
    
    ArrayList<HashMap<String, String>> selectedNotes = new ArrayList<HashMap<String,String>>();
    
    ArrayList<ArrayList<HashMap<String, String>>> userData;    
    ArrayList<HashMap<String, String>> coursesList;
    ArrayList<HashMap<String, String>> lecturesList;
    ArrayList<HashMap<String, String>> notesList;
    
    private GestureDetectorCompat gestureDetector;   

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		userData = new ArrayList<ArrayList<HashMap<String,String>>>();
		
		// getting lecture details from intent
		Intent i = getIntent();
		
		userData = Database.getInstance().getUserData();
		
		if(userData.size() > 2){
			
			notesList = userData.get(2);
		}		        
        
        lectureID = i.getStringExtra("id");
        courseName = i.getStringExtra("name");       
        
		setTitle(courseName + " Flashcards");
		setContentView(R.layout.flashcard);	
		
		gestureDetector = new GestureDetectorCompat(this, new GestureListener());
		
		animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
		animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);			
		
		linearLayout = (LinearLayout) findViewById(R.id.flashcardParentView);
		keyword = (TextView) findViewById(R.id.keyword);
		definition = (TextView) findViewById(R.id.definition);
		checkbox = (Button) findViewById(R.id.checkBox1);
		
		definition.setVisibility(View.INVISIBLE);
		
		getNotesFromSelectedLecture();
		setFirstFlashCard();
		
		setOnTouchForLinearLayout();
		
		checkbox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			

				selectedNotes.remove(flashcardPosition);
				
				if(selectedNotes.size() > 0){

					flashcardPosition++;
			    	definition.setText("");    	   	
			    	
			    	if(flashcardPosition < selectedNotes.size()){
			    		
			    		keyword.setText(selectedNotes.get(flashcardPosition).get("term"));
			    		keyword.startAnimation(animFadeIn);	    	
			    	}else if(selectedNotes.size() != 0){
			    		
			    		flashcardPosition = 0;    		
			    		keyword.setText(selectedNotes.get(flashcardPosition).get("term"));
			    		keyword.startAnimation(animFadeIn);	    	
			    	}else{
			    		
			    		flashcardPosition = 0;
			    	}

					// Toast.makeText(getApplicationContext(), "Ok is checked!",
					// Toast.LENGTH_LONG).show();					
				}else{
					
					if(definition.getText().equals("")){
						
						keyword.setText("You know everything. Go back to reset.");
					}else{							
						
						definition.setText("You know everything. Go back to reset.");
						definition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
					}					
					
					checkbox.setVisibility(View.INVISIBLE);
				}
			} 
		});		
			
		
	}
	
	private void setOnTouchForLinearLayout(){
		
		linearLayout.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {				
				
				gestureDetector.onTouchEvent(event);				
				return true;
			}
		});
	}
	
	private void getNotesFromSelectedLecture(){		
		
		for (int i = 0; i < notesList.size(); i++) {

			if (notesList.get(i).get("lecture_id").equals(lectureID)) {
				selectedNotes.add(notesList.get(i));
			}
		}		
	}

	private void setFirstFlashCard(){
		
		//Check if we have at least one note for the lecture.
		if(selectedNotes.size() != 0){
			
			keyword.setText(selectedNotes.get(0).get("term"));
	    	//definition.setText(selectedNotes.get(0).get("definition"));
		}else{
			
			checkbox.setVisibility(4);
			keyword.setText("No Notes :(");
			definition.setText("Go and create some notes so that you can study!");
		}
		
//		runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//								
//			}
//		});		
		
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
		definition.setText("");		
		
		if(flashcardPosition < 0 && selectedNotes.size() != 0){
			
			flashcardPosition = selectedNotes.size() - 1;
			
			keyword.setText(selectedNotes.get(flashcardPosition).get("term"));
			keyword.startAnimation(animFadeIn);	    	
		}else if(selectedNotes.size() != 0){		
			
			keyword.setText(selectedNotes.get(flashcardPosition).get("term"));
			keyword.startAnimation(animFadeIn);	    	
		}else {
			
			flashcardPosition = 0;
		}
    }

    public void onSwipeLeft() {
    	
    	flashcardPosition++;
    	definition.setText("");    	   	
    	
    	if(flashcardPosition < selectedNotes.size()){
    		
    		keyword.setText(selectedNotes.get(flashcardPosition).get("term"));
    		keyword.startAnimation(animFadeIn);	    	
    	}else if(selectedNotes.size() != 0){
    		
    		flashcardPosition = 0;    		
    		keyword.setText(selectedNotes.get(flashcardPosition).get("term"));
    		keyword.startAnimation(animFadeIn);	    	
    	}else{
    		
    		flashcardPosition = 0;
    	}   	
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    	
    	
    	if(selectedNotes.size() != 0){
    		
    		if(definition.getText().equals("")){    			
    			
    			keyword.setText("");
    			definition.setText(selectedNotes.get(flashcardPosition).get("definition"));
    			keyword.startAnimation(animFadeOut);    			
    			definition.startAnimation(animFadeIn);   			
    			
    		}else if(keyword.getText().equals("")){
    			
    			keyword.setText(selectedNotes.get(flashcardPosition).get("term"));
    			definition.setText("");
    			definition.startAnimation(animFadeOut);
    			keyword.startAnimation(animFadeIn); 			
    			
    		}
    	}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
    	switch(item.getItemId()){
    		
    		case R.id.action_reload:   			
				
				new refreshUserData().execute();
				
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);    		
    	}		
	}
	
	private class refreshUserData extends AsyncTask<String, String, String>{
		
		/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FlashcardActivity.this);
            pDialog.setMessage("Refreshing User Data. Please wait...");
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
            
            if(userData.size() > 2){
    			
    			notesList = userData.get(2);
    		}
    		
    		selectedNotes.clear();
    		definition.setText("");
    		keyword.setText("");
    		
    		getNotesFromSelectedLecture();
    		setFirstFlashCard();
    			
    		//setCoursesListAdapter();
            	
            /*Intent i = new Intent(getApplicationContext(), AllCoursesActivity.class);
    		i.putExtra("api_key", api_key);
    		i.putExtra("courses", userData.get(0));
    		i.putExtra("lectures", userData.get(1));
    		i.putExtra("notes", userData.get(2));
    		startActivity(i);
    		finish(); */  			
                       
        }
	}

}
