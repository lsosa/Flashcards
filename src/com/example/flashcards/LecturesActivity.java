package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



public class LecturesActivity extends ListActivity{

	private String course_id;
	private String course_code;	
	
	private ProgressDialog pDialog;
	
	ArrayList<HashMap<String, String>> selectedCourseLectures = new ArrayList<HashMap<String, String>>();
	
	ArrayList<ArrayList<HashMap<String, String>>> userData;	
    ArrayList<HashMap<String, String>> lecturesList;
    
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_courses);
        
        userData = new ArrayList<ArrayList<HashMap<String,String>>>();
        
        Intent i = getIntent();
        
        userData = Database.getInstance().getUserData();
        
        lecturesList = userData.get(1);       
        
        course_id = i.getStringExtra("id");
        course_code = i.getStringExtra("course_code");        
        
        setTitle(course_code + " Lectures");
        
        //Displays the lectures corresponding to the selected course.
        displayLectures();              
 
        // Get listview
        ListView lv = getListView();
 
        // on seleting single lecture        
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String lecture_id = ((TextView) view.findViewById(R.id.elementID)).getText().toString();
                String course_name = ((TextView) view.findViewById(R.id.elementTitle)).getText().toString();
 
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        FlashcardActivity.class);                
                
                in.putExtra("id", lecture_id);
                in.putExtra("name", course_name);               
 
                // starting new activity 
                startActivity(in);
            }
        });
 
    }
    
    private void displayLectures(){   	
    	
    	for(int i = 0; i < lecturesList.size(); i++){
    		
    		if(lecturesList.get(i).get("courses_id").equals(course_id))
    		{
    			selectedCourseLectures.add(lecturesList.get(i));
    		}
    	}
    	
    	if(selectedCourseLectures.size() != 0){
    		
			runOnUiThread(new Runnable() {
	
				@Override
				public void run() {
					ListAdapter adapter = new SimpleAdapter(
							LecturesActivity.this, selectedCourseLectures,
							R.layout.list_item, new String[] { "id", "name", "description" }, 
							new int[] {R.id.elementID, R.id.elementTitle, R.id.elementSubTitle });
	
					// updating listview
					setListAdapter(adapter);
				}
			});
    	}else{
    		
    		setTitle("No" + " Lectures");
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
            pDialog = new ProgressDialog(LecturesActivity.this);
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
            
    		lecturesList = userData.get(1);   		
    		selectedCourseLectures.clear();   		
    		displayLectures();                      
        }
	}
}

