package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class LecturesActivity extends ListActivity{
	
	private String course_id;
	private String course_code;
	
	ArrayList<HashMap<String, String>> selectedCourseLectures = new ArrayList<HashMap<String, String>>();
	
	ArrayList<HashMap<String, String>> coursesList;
    ArrayList<HashMap<String, String>> lecturesList;
    ArrayList<HashMap<String, String>> notesList;
 
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_courses);
        
        Intent i = getIntent();
        
        coursesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("courses");
        lecturesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("lectures");
        notesList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("notes");
        
        course_id = i.getStringExtra("id");
        course_code = i.getStringExtra("course_code");
        
        //Displays the lectures corresponding to the selected course.
        displayLectures();
        
        setTitle(course_code + " Lectures");       
 
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
                in.putExtra("courses", coursesList);
    			in.putExtra("lectures", lecturesList);
    			in.putExtra("notes", notesList);
 
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
    }
}

