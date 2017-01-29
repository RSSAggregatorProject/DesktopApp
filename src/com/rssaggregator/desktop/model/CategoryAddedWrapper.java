package com.rssaggregator.desktop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Category created when user adds new category.
 * 
 * @author Irina
 *
 */
public class CategoryAddedWrapper {

	@Expose
	private String status;
	@SerializedName("id_cat")
	@Expose
	private Integer categoryId;
	@Expose
	private String name;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
