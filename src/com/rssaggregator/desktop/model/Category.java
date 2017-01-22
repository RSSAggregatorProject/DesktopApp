package com.rssaggregator.desktop.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

	@SerializedName("id_cat")
	@Expose
	private Integer categoryId;

	@Expose
	private String name;

	@Expose
	private Integer unread;

	@SerializedName("feeds")
	@Expose
	private List<Channel> channels;

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

	public Integer getUnread() {
		return unread;
	}

	public void setUnread(Integer unread) {
		this.unread = unread;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
}
