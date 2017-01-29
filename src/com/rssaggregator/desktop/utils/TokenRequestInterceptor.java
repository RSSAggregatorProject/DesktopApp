package com.rssaggregator.desktop.utils;

import java.io.IOException;

import com.rssaggregator.desktop.model.AccessToken;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor for API requests. Sets the Authorization and the Content-Type
 * header.
 * 
 * @author Irina
 *
 */
public class TokenRequestInterceptor implements Interceptor {
	AccessToken accessToken;

	/**
	 * Constructor
	 * 
	 * @param accessToken
	 *            Access token
	 */
	public TokenRequestInterceptor(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Default constructor.
	 */
	public TokenRequestInterceptor() {
	}

	/**
	 * Intercept method. Inserts headers to each API request. Authorization and
	 * Content-Type are added to all requests.
	 */
	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {
		Request original = chain.request();
		Request request;

		if (accessToken != null) {
			if (accessToken.getApiToken() != null) {
				request = original.newBuilder().header("Content-Type", "application/json")
						.header("Authorization", accessToken.getApiToken()).method(original.method(), original.body())
						.build();
			} else {
				request = original.newBuilder().header("Content-Type", "application/json")
						.method(original.method(), original.body()).build();
			}
		} else {
			request = original.newBuilder().header("Content-Type", "application/json")
					.method(original.method(), original.body()).build();
		}
		return chain.proceed(request);
	}

	/**
	 * Resets the Access Token
	 */
	public void resetAccessToken() {
		accessToken = null;
	}

	/**
	 * Gets the Access Token.
	 * 
	 * @return Access Token instance.
	 */
	public AccessToken getAccessToken() {
		return accessToken;
	}

	/**
	 * Sets the Access Token
	 * 
	 * @param accessToken
	 *            Access Token instance.
	 */
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
}
