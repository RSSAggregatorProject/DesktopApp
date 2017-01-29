package com.rssaggregator.desktop.utils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Class to deserialize a date format with GSON.
 *
 * https://kylewbanks.com/blog/String-Date-Parsing-with-GSON-UTC-Time-Zone
 */
public class DateDeserializer implements JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String date = element.getAsString();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}