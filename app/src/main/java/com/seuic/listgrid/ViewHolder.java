package com.seuic.listgrid;

import java.util.ArrayList;
import java.util.List;

import android.widget.CheckBox;
import android.widget.TextView;

public class ViewHolder {
    public CheckBox mCheckBox;
	private List<TextView> mTVList;
	private int id;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public ViewHolder(){
		mTVList = new ArrayList<TextView>();
	}
	
	public void setText(String text,int position){
		mTVList.get(position).setText(text);
	}
	
	public void addView(TextView tv){
		mTVList.add(tv);
	}
	
	public List<TextView> getTextList(){
		return mTVList;
	}
}
