package com.seuic.listgrid;

import android.view.View;

public class GridColumn {

	private String name;
	private int width;
	private float weight = 1;
	private int textColor = ListGridStyle.ItemStyle.TEXT_COLOR;
	private int visible = View.VISIBLE;
	
	public GridColumn(String name, int width){
		this.name = name;
		this.width = width;
	}
	
	public GridColumn(String name, float weight){
		this.name = name;
		this.weight = weight;
	}
	
	public String getName(){
		return name;
	}
	
	public int getWidth(){
		return width;
	}
	
	public float getWeight(){
		return weight;
	}
	
	public void setTextColor(int color){
		textColor = color;
	}
	
	public int getTextColor(){
		return textColor;
	}
	
	public void setVisible(int visible){
		this.visible = visible;
	}
	
	public int getVisible(){
		return visible;
	}
}
