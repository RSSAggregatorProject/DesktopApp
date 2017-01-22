package com.rssaggregator.desktop.model;

import com.google.gson.annotations.Expose;

/**
 * Object to connect the user to the API server. It contains the email and
 * password user.
 * 
 * @author Irina
 *
 */
public class Credentials {

	@Expose
	private String email;
	@Expose
	private String password;

	public Credentials() {
	}

	public Credentials(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
