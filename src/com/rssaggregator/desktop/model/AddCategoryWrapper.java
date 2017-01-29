package com.rssaggregator.desktop.model;

import com.google.gson.annotations.Expose;

/**
 * Body of the API request Add Category.
 * 
 * @author Irina
 *
 */
public class AddCategoryWrapper {

	@Expose
	private String name;

	public AddCategoryWrapper(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
