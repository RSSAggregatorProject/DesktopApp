package com.rssaggregator.desktop.model;

import java.util.ArrayList;

public class TmpCategory {

	private int id;
	private String name;
	private ArrayList<TmpChannel> channels;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<TmpChannel> getChannels() {
		return channels;
	}

	public void setChannels(ArrayList<TmpChannel> channels) {
		this.channels = channels;
	}
}
