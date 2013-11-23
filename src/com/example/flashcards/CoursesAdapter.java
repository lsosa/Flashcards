package com.example.flashcards;

import java.util.ArrayList;
import java.util.HashMap;

import com.togonotes.flashcards.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CoursesAdapter extends ArrayAdapter<HashMap<String, String>>{

	private ArrayList<HashMap<String, String>> courses;
	private Activity activity;

	public CoursesAdapter(Activity a, int textViewResourceId, ArrayList<HashMap<String, String>> courses){
		
		super(a, textViewResourceId, courses);
		this.courses = courses;
		this.activity = a;
	}
	
	public static class ViewHolder{
		public TextView courseIDTV;
        public TextView titleTV;
        public TextView descriptionTV;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
        ViewHolder holder;
        
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.courseIDTV = (TextView) v.findViewById(R.id.elementID);
            holder.titleTV = (TextView) v.findViewById(R.id.elementTitle);
            holder.descriptionTV = (TextView) v.findViewById(R.id.elementSubTitle);
            v.setTag(holder);
        }else{
        	
        	holder = (ViewHolder)v.getTag();
        }
        
        //final Custom custom = entries.get(position);
        String courseIDTV = courses.get(position).get("id");
        String titile = courses.get(position).get("friendly_name");
        String description = courses.get(position).get("course_code");
        
        holder.courseIDTV.setText(courseIDTV);
        holder.titleTV.setText(titile);
        holder.descriptionTV.setText(description);        
        
        return v;
	}
}
