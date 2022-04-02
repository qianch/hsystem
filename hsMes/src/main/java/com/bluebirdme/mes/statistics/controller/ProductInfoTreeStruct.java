package com.bluebirdme.mes.statistics.controller;

import java.util.List;

public class ProductInfoTreeStruct {
	private String id;
	private String text;
	private String state;
	private List<ProductInfoTreeStruct> children;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<ProductInfoTreeStruct> getChildren() {
		return children;
	}
	public void setChildren(List<ProductInfoTreeStruct> children) {
		this.children = children;
	}
	
}
