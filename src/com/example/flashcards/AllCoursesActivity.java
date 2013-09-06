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
 
public class AllCoursesActivity extends ListActivity {   
    
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
                Intent in = new Intent(getApplicationContext(),
                        LecturesActivity.class);
                
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
}
