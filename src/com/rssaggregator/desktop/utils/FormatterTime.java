package com.rssaggregator.desktop.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class with a public static function "formattedAsTimeAgo" which change
 * the date format, like the style "Time Ago".
 */
public class FormatterTime {

	private static final double SECOND = 1;
	private static final double MINUTE = SECOND * 60;
	private static final double HOUR = MINUTE * 60;
	private static final double DAY = HOUR * 24;
	private static final double WEEK = DAY * 7;
	private static final double MONTH = DAY * 31;
	private static final double YEAR = DAY * 365.25;

	/**
	 * Change the simple date format to a "Time Ago" style. The style change
	 * according to the current date. Example: < 1 hour => "x minutes ago"
	 *
	 * @param dateItem
	 *            {@link Date} of the item formatted.
	 *
	 * @return The new formatted String date.
	 */
	public static String formattedAsTimeAgo(Date dateItem) {
		if (dateItem == null) {
			return "Unknown date";
		}
		Date currentDate = new Date();
		return selectCaseTimeAgo(currentDate, dateItem);
	}

	/**
	 * Return the new date format according to the current date and the date of
	 * the item.
	 *
	 * @param currentDate
	 *            {@link Date} and time of the current date.
	 * @param dateItem
	 *            {@link Date} of the item.
	 *
	 * @return The new {@link String} date format
	 */
	private static String selectCaseTimeAgo(Date currentDate, Date dateItem) {
		long diffSecondes = (currentDate.getTime() - dateItem.getTime()) / 1000;

		// Impossible case but for future case
		if (diffSecondes < 0) {
			return "In the future";
		}

		// < 1 minute => "Just now"
		if (diffSecondes < MINUTE) {
			return "Just now";
		}

		if (diffSecondes < HOUR) {
			return formatMinutesAgo(diffSecondes);
		}

		if (isSameDay(dateItem, currentDate)) {
			return formatAsToday(diffSecondes);
		}

		if (isYesterday(dateItem, currentDate)) {
			return formatAsYesterday(dateItem);
		}

		if (isLastWeek(diffSecondes)) {
			return formatAsLastWeek(dateItem);
		}

		if (isLastMonth(diffSecondes)) {
			return formatAsLastMonth(dateItem);
		}

		if (isLastYear(diffSecondes)) {
			return formatAsLastYear(dateItem, currentDate);
		}
		return formatAsOther(dateItem);
	}

	/**
	 * Return the new formatted date like "x minute(s) ago".
	 *
	 * @param diffSecondes
	 *            Number of secondes between the current date and the date of
	 *            the item.
	 *
	 * @return The new formatted {@link String} date.
	 */
	private static String formatMinutesAgo(long diffSecondes) {
		int diffMinutes = (int) (diffSecondes / MINUTE);

		return (diffMinutes == 1) ? "1 minute ago" : diffMinutes + " minutes ago";
	}

	/**
	 * Return the new formatted date like "x hour(s) ago".
	 *
	 * @param diffSecondes
	 *            Number of secondes between the current date and the date of
	 *            the item.
	 *
	 * @return The new formatted {@link String} date.
	 */
	private static String formatAsToday(long diffSecondes) {
		int diffHours = (int) (diffSecondes / HOUR);

		return (diffHours == 1) ? "1 hour ago" : diffHours + " hours ago";

	}

	/**
	 * Return the new formatted date like "Yesterday at h:mm AM/PM".
	 *
	 * @param dateItem
	 *            {@link Date} of the item.
	 *
	 * @return The new formatted {@link String} date.
	 */
	private static String formatAsYesterday(Date dateItem) {
		SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

		return "Yesterday at " + format.format(dateItem);
	}

	/**
	 * Return the new formatted date like "WeekDay at h:mm AM/PM".
	 *
	 * @param dateItem
	 *            {@link Date} of the item.
	 *
	 * @return The new formatted {@link String} date.
	 */
	private static String formatAsLastWeek(Date dateItem) {
		SimpleDateFormat formatDate = new SimpleDateFormat("EEEE", Locale.ENGLISH);
		SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

		return formatDate.format(dateItem) + " at " + formatTime.format(dateItem);
	}

	/**
	 * Return the new formatted date like "Month Day at h:mm AM/PM".
	 *
	 * @param dateItem
	 *            {@link Date} of the item.
	 *
	 * @return The new formatted {@link String} date.
	 */
	private static String formatAsLastMonth(Date dateItem) {
		SimpleDateFormat formatMonth = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
		SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

		return formatMonth.format(dateItem) + " at " + formatTime.format(dateItem);
	}

	/**
	 * Return the new formatted date like "Month Day".
	 *
	 * @param dateItem
	 *            {@link Date} of the item.
	 *
	 * @return The new formatted {@link String} date.
	 */
	private static String formatAsLastYear(Date dateItem, Date currentDate) {
		Calendar calitem = Calendar.getInstance();
		calitem.setTime(dateItem);
		Calendar calCurrent = Calendar.getInstance();
		calCurrent.setTime(currentDate);
		SimpleDateFormat format;

		if (calitem.get(Calendar.YEAR) == calCurrent.get(Calendar.YEAR)) {
			format = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
		} else {
			format = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
		}
		return format.format(dateItem);
	}

	/**
	 * Return the new formatted date like "Month Day, Year"
	 *
	 * @param dateItem
	 *            {@link Date} of the item.
	 *
	 * @return The new formatted {@link String} date.
	 */
	private static String formatAsOther(Date dateItem) {
		SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		return format.format(dateItem);
	}

	/**
	 * Compare the current date and the date of the item and check if these two
	 * dates have the same day.
	 *
	 * @param dateToCompare
	 *            {@link Date} to compare with the current date.
	 * @param currentDate
	 *            Current {@link Date}.
	 *
	 * @return True if it's the same day.
	 */
	private static boolean isSameDay(Date dateToCompare, Date currentDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
		String dateToCompareString = format.format(dateToCompare);
		String currentDateString = format.format(currentDate);

		return dateToCompareString.equals(currentDateString);
	}

	/**
	 * Compare the current date and the date of the item and check if there is
	 * one day between the two dates.
	 *
	 * @param dateItem
	 *            {@link Date} of the item.
	 * @param currentDate
	 *            Current {@link Date}.
	 *
	 * @return True if it's there is one day between the two dates.
	 */
	private static boolean isYesterday(Date dateItem, Date currentDate) {
		Calendar tmpCal = Calendar.getInstance();
		tmpCal.setTime(dateItem);
		tmpCal.add(Calendar.DAY_OF_MONTH, 1);
		dateItem = tmpCal.getTime();
		return isSameDay(dateItem, currentDate);
	}

	/**
	 * Check if the difference (in secondes) between the current date and the
	 * date of the item is less than a week (604800 secondes).
	 *
	 * @param diffSecondes
	 *            Number of secondes between the current date and the date of
	 *            the item.
	 *
	 * @return True if it's less than a week.
	 */
	private static boolean isLastWeek(long diffSecondes) {
		return diffSecondes < WEEK;
	}

	/**
	 * Check if the difference (in secondes) between the current date and the
	 * date of the item is less than a month (2678400 secondes).
	 *
	 * @param diffSecondes
	 *            Number of secondes between the current date and the date of
	 *            the item.
	 *
	 * @return True if it's less than a month.
	 */
	private static boolean isLastMonth(long diffSecondes) {
		return diffSecondes < MONTH;
	}

	/**
	 * Check if the difference (in secondes) between the current date and the
	 * date of the item is less than a year (31557600 secondes).
	 *
	 * @param diffSecondes
	 *            Number of secondes between the current date and the date of
	 *            the item.
	 *
	 * @return True if it's less than a year.
	 */
	private static boolean isLastYear(long diffSecondes) {
		return diffSecondes < YEAR;
	}

}
