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
 
public class AllCoursesActivity extends ListActivity {

	ArrayList<ArrayList<HashMap<String, String>>> userData;
	ArrayList<HashMap<String, String>> coursesList;
    ArrayList<HashMap<String, String>> lecturesList;
    ArrayList<HashMap<String, String>> notesList;
    
    private String users_id;
    
    private ProgressDialog pDialog;
 
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_courses);
        
        userData = new ArrayList<ArrayList<HashMap<String,String>>>();
        
        Intent i = getIntent();
        
        coursesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("courses");
        lecturesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("lectures");
        notesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("notes");
        
        users_id = i.getStringExtra("users_id");
        
        setCoursesListAdapter();              
 
        // Get listview
        ListView lv = getListView();
 
        // on seleting single course        
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String course_id = ((TextView) view.findViewById(R.id.elementID)).getText().toString();
                String course = ((TextView) view.findViewById(R.id.elementTitle)).getText().toString();
 
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), LecturesActivity.class);
                
                in.putExtra("users_id", users_id);
                in.putExtra("id", course_id);
                in.putExtra("course_code", course);
                in.putExtra("courses", coursesList);
    			in.putExtra("lectures", lecturesList);
    			in.putExtra("notes", notesList);
 
                // starting new activity 
                startActivity(in);
            }
        });
 
    }
    
    private void setCoursesListAdapter(){
    	
    	runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ListAdapter adapter = new SimpleAdapter(
		                AllCoursesActivity.this, coursesList,
		                R.layout.list_item, new String[] { "id", "course_code", "friendly_name"},
		                new int[] { R.id.elementID, R.id.elementTitle, R.id.elementSubTitle });
				
				// updating listview
		        setListAdapter(adapter);
			}
		});
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
            pDialog = new ProgressDialog(AllCoursesActivity.this);
            pDialog.setMessage("Refreshing User Data. Please wait...");
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
    			
    			coursesList = userData.get(0);
    			lecturesList = userData.get(1);
    			notesList = userData.get(2);
    			
    			setCoursesListAdapter();
            }           
        }
	}
}
