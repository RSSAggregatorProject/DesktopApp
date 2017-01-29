package com.rssaggregator.desktop.utils;

import java.io.IOException;

import com.rssaggregator.desktop.model.AccessToken;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenRequestInterceptor implements Interceptor {
	AccessToken accessToken;

	public TokenRequestInterceptor(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public TokenRequestInterceptor() {
	}

	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {
		Request original = chain.request();
		Request request;

		if (accessToken != null) {
			if (accessToken.getApiToken() != null) {
				System.out.println("INTERCEPT: Access Token");
				request = original.newBuilder().header("Content-Type", "application/json")
						.header("Authorization", accessToken.getApiToken()).method(original.method(), original.body())
						.build();
			} else {
				System.out.println("INTERCEPT: No Token");
				request = original.newBuilder().header("Content-Type", "application/json")
						.method(original.method(), original.body()).build();
			}
		} else {
			System.out.println("INTERCEPT: No Token");
			request = original.newBuilder().header("Content-Type", "application/json")
					.method(original.method(), original.body()).build();
		}
		return chain.proceed(request);
	}

	public void resetAccessToken() {
		accessToken = null;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
}
