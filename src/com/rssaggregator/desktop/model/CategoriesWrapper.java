package com.rssaggregator.desktop.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class which represents the data fetched from the API request fetchData.
 *
 * @author Irina
 *
 */
public class CategoriesWrapper {

	@Expose
	private String status;
	@SerializedName("data")
	@Expose
	List<Category> categories;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
