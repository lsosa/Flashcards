package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Database{
    
    private String users_id;   
    
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    
    ArrayList<HashMap<String, String>> coursesList;
    ArrayList<HashMap<String, String>> lecturesList;
    ArrayList<HashMap<String, String>> notesList;
    
    ArrayList<ArrayList<HashMap<String, String>>> userData;
    
    // url to get all curses list
    private static String url_all_courses = "http://lsosa.web44.net/getAllCourses.php";
    private static String url_all_lectures = "http://lsosa.web44.net/getAllLectures.php";
    private static String url_all_notes = "http://lsosa.web44.net/getAllNotes.php";
    
    // JSON Node names for the courses table
    private static final String TAG_COURSE_ID = "id";    
    private static final String TAG_COURSE_CODE = "course_code";
    private static final String TAG_FRIENDLY_NAME = "friendly_name";
    private static final String TAG_INSTRUCTOR = "instructor";
    private static final String TAG_COURSE_CREATED = "created";
    private static final String TAG_COURSE_SUCCESS = "success";
    
    // JSON Node names for the lectures table
    private static final String TAG_LECTURE_ID = "id";    
    private static final String TAG_LECTURE_CREATED = "created";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_LAST_MODIFIED = "last_modified";
    private static final String TAG_NAME = "name";
    private static final String TAG_LECTURE_SUCCESS = "success";
    
    // JSON Node names for the notes table
    private static final String TAG_NOTE_ID = "id";
    private static final String TAG_TERM = "term";
    private static final String TAG_DEFINITION = "definition";
    private static final String TAG_ORDER = "order";
    private static final String TAG_NOTE_SUCCESS = "success";  
    
 
    // Courses JSONArray
    private JSONArray courses = null;
    private JSONArray lectures = null;
    private JSONArray notes = null;    
    
    
	public Database(String users_id, String API) {	
		
		this.users_id = users_id;
		
		coursesList = new ArrayList<HashMap<String, String>>();
		lecturesList = new ArrayList<HashMap<String, String>>();
		notesList = new ArrayList<HashMap<String, String>>();
		
		userData = new ArrayList<ArrayList<HashMap<String,String>>>();		
		
	}
	
	
	/**
     * Background Async Task to Load all courses by making HTTP Request
     * */
    public ArrayList<ArrayList<HashMap<String, String>>> LoadAllUserData(){       
        
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("users_id", users_id));

		// getting JSON string from URL
		JSONObject jsonCourses = jParser.makeHttpRequest(url_all_courses, "GET", params);
		JSONObject jsonLectures = jParser.makeHttpRequest(url_all_lectures, "GET", params);
		JSONObject jsonNotes = jParser.makeHttpRequest(url_all_notes, "GET", params);

		coursesList = getAllCourses(jsonCourses);
		lecturesList = getAllLectures(jsonLectures);
		notesList = getAllNotes(jsonNotes);
		
		userData.add(coursesList);
		userData.add(lecturesList);
		userData.add(notesList);
		
		return userData;
    }
    
    protected ArrayList<HashMap<String, String>> getAllCourses(JSONObject jsonCourses){
    	
        try {
            // Checking for SUCCESS TAG
            int success = jsonCourses.getInt(TAG_COURSE_SUCCESS);

            if (success == 1) {
                // Courses found
                // Getting Array of Courses
                courses = jsonCourses.getJSONArray("courses");

                // looping through All courses
                for (int i = 0; i < courses.length(); i++) {
                    JSONObject c = courses.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_COURSE_ID);
                    String course_code = c.getString(TAG_COURSE_CODE);
                    String friendly_name = c.getString(TAG_FRIENDLY_NAME);
                    String instructor = c.getString(TAG_INSTRUCTOR);
                    String created = c.getString(TAG_LECTURE_CREATED);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_COURSE_ID, id);
                    map.put(TAG_COURSE_CODE, course_code);
                    map.put(TAG_FRIENDLY_NAME, friendly_name);
                    map.put(TAG_INSTRUCTOR, instructor);
                    map.put(TAG_COURSE_CREATED, created);

                    // adding HashList to ArrayList
                    coursesList.add(map);                       
                }
            } else {
                // no courses found            	
                
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return coursesList;
    }
    
    protected ArrayList<HashMap<String, String>> getAllLectures(JSONObject jsonLectures){
    	
    	try {
            // Checking for SUCCESS TAG
            int success = jsonLectures.getInt(TAG_LECTURE_SUCCESS);

            if (success == 1) {
                // Lectures found
                // Getting Array of Lectures
                lectures = jsonLectures.getJSONArray("lectures");

                // looping through All lectures
                for (int i = 0; i < lectures.length(); i++) {
                    JSONObject c = lectures.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_LECTURE_ID);
                    String course_id = c.getString("courses_id");
                    String name = c.getString(TAG_NAME);
                    String description = c.getString(TAG_DESCRIPTION);                        
                    String created = c.getString(TAG_COURSE_CREATED);
                    String last_modify = c.getString(TAG_LAST_MODIFIED);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_LECTURE_ID, id);
                    map.put("courses_id", course_id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_DESCRIPTION, description);
                    map.put(TAG_LECTURE_CREATED, created);
                    map.put(TAG_LAST_MODIFIED, last_modify);                        

                    // adding HashList to ArrayList
                    lecturesList.add(map);                       
                }
            } else {
                // no lectures found
            	
                
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return lecturesList;   	
    }
    
    protected ArrayList<HashMap<String, String>> getAllNotes(JSONObject jsonNotes){
    	
    	try {
            // Checking for SUCCESS TAG
            int success = jsonNotes.getInt(TAG_NOTE_SUCCESS);

            if (success == 1) {
                // Notes found
                // Getting Array of Notes
                notes = jsonNotes.getJSONArray("notes");

                // looping through All notes
                for (int i = 0; i < notes.length(); i++) {
                    JSONObject c = notes.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_NOTE_ID);
                    String lecture_id = c.getString("lecture_id");
                    String term = c.getString(TAG_TERM);
                    String definition = c.getString(TAG_DEFINITION);                        
                    String order = c.getString(TAG_ORDER);                        

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_NOTE_ID, id);
                    map.put("lecture_id", lecture_id);
                    map.put(TAG_TERM, term);
                    map.put(TAG_DEFINITION, definition);
                    map.put(TAG_ORDER, order);                                                

                    // adding HashList to ArrayList
                    notesList.add(map);                       
                }
            } else {
                // no notes found
            	
                
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return notesList;
    	
    }
}
