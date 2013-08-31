package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 

import org.apache.http.NameValuePair;
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
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> coursesList;
 
    // url to get all curses list
    private static String url_all_courses = "http://lsosa.web44.net/getAllCourses.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COURSES = "courses";
    private static final String TAG_SUMMARY = "summary";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
 
    // Courses JSONArray
    JSONArray courses = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_courses);
 
        // Hashmap for ListView
        coursesList = new ArrayList<HashMap<String, String>>();
 
        // Loading courses in Background Thread
        new LoadAllCourses().execute();
 
        // Get listview
        ListView lv = getListView();
 
        // on seleting single course        
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
 
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        FlashcardActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);
 
                // starting new activity and expecting some response back
                startActivity(in);
            }
        });
 
    } 

 
    /**
     * Background Async Task to Load all courses by making HTTP Request
     * */
    class LoadAllCourses extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllCoursesActivity.this);
            pDialog.setMessage("Loading Courses. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All courses from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_courses, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Courses: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // Courses found
                    // Getting Array of Courses
                    courses = json.getJSONArray(TAG_COURSES);
 
                    // looping through All courses
                    for (int i = 0; i < courses.length(); i++) {
                        JSONObject c = courses.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String summary = c.getString(TAG_SUMMARY);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_SUMMARY, summary);
 
                        // adding HashList to ArrayList
                        coursesList.add(map);
                    }
                } else {
                    // no courses found
                	Toast.makeText(getApplicationContext(), "Not courses found!", Toast.LENGTH_LONG).show();
                    
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all courses
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllCoursesActivity.this, coursesList,
                            R.layout.list_item, new String[] { TAG_NAME,
                                    TAG_SUMMARY},
                            new int[] { R.id.course, R.id.summary });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}
