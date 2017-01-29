package com.rssaggregator.desktop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Return of the API Request Add Feed.
 * 
 * @author Irina
 *
 */
public class FeedAddedWrapper {

	@Expose
	private String status;
	@SerializedName("id_feed")
	@Expose
	private Integer channelId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
}
