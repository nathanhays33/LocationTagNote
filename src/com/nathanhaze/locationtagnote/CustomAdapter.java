package com.nathanhaze.locationtagnote;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


 
public class CustomAdapter extends ArrayAdapter<Note>{
    private ArrayList<Note> entries;
    private Activity activity;
 
    public CustomAdapter(Activity a, int textViewResourceId, ArrayList<Note> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }
 
    public static class ViewHolder{
        public TextView item1;
        public TextView item2;
        public TextView item3;
        public TextView item4;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_item, null);
            holder = new ViewHolder();
           
            holder.item1 = (TextView) v.findViewById(R.id.date);
            holder.item2 = (TextView) v.findViewById(R.id.message);
            
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final Note custom = entries.get(position);
        if (custom != null) {
    		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Mathlete-Bulky.otf");
    		holder.item1.setTypeface(tf);
    		holder.item2.setTypeface(tf);
            
        	holder.item1.setText(custom._date);
        	if(custom._message.length() >20){
        		holder.item2.setText(custom._message.substring(0, 17) + "..."); 
        	}
        	else{
                holder.item2.setText(custom._message); 
        	}
        	/*
            holder.item2.setText(Float.toString(custom._maxspeed));
            holder.item3.setText(Float.toString(custom._distance));
            holder.item4.setText(Integer.toString(custom._altitude)); 
            */     
        }
        return v;
    }
 
}