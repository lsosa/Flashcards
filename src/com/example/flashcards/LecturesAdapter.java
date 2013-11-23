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

public class LecturesAdapter extends ArrayAdapter<HashMap<String, String>>{
	
	private ArrayList<HashMap<String, String>> lectures;
	private Activity activity;

	
	public LecturesAdapter(Activity a, int textViewResourceId, ArrayList<HashMap<String, String>> lectures){
		
		super(a, textViewResourceId, lectures);
		this.lectures = lectures;
		this.activity = a;
	}
	
	public static class ViewHolder{
		public TextView lectureIDTV;
        public TextView nameTV;
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
            holder.lectureIDTV = (TextView) v.findViewById(R.id.elementID);
            holder.nameTV = (TextView) v.findViewById(R.id.elementTitle);
            holder.descriptionTV = (TextView) v.findViewById(R.id.elementSubTitle);
            v.setTag(holder);
        }else{
        	
        	holder = (ViewHolder)v.getTag();
        }       
        
        String lectureIDTV = lectures.get(position).get("id");
        String name = lectures.get(position).get("name");
        String description = lectures.get(position).get("description");
        
        holder.lectureIDTV.setText(lectureIDTV);
        holder.nameTV.setText(name);
        
        if(!description.equals("null")){
        	
        	holder.descriptionTV.setText(description);
        }else{
        	
        	holder.descriptionTV.setText("");
        }
               
        
        return v;
	}
	
	
}
