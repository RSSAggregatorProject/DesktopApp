package unittests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.rssaggregator.desktop.utils.FormatterTime;

public class FormatterTimeTest {

	@Test
	public void firstSimpleTest() {
		Assert.assertTrue(true);
	}

	@Before
	public void setup() {
	}

	public Date setMockDate(int year, int month, int day, int hours, int minutes, int secondes) {
		Calendar tmpCal = Calendar.getInstance();
		tmpCal.set(year, month, day, hours, minutes, secondes);
		return tmpCal.getTime();
	}

	@Test
	public void formatTimeNullDateTest()
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Date nonNullDate = new Date();

		String result = FormatterTime.formattedAsTimeAgo(null);
		Assert.assertEquals(result, "Unknown date");
		result = FormatterTime.formattedAsTimeAgo(nonNullDate);
		Assert.assertNotEquals(result, ("Unknown date"));
	}
	
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();


	@Test
	public void formatJustNowTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
		method.setAccessible(true);

		Date currentDate = new Date();
		Date sameDate = new Date();
		String result = (String) method.invoke(method, currentDate, sameDate);
		Assert.assertEquals(result, "Just now");

		Date currentDate2 = setMockDate(2015, 4, 17, 9, 23, 23);
		Date thirtySecondesLater = setMockDate(2015, 4, 17, 9, 22, 53);
		String result2 = (String) method.invoke(method, currentDate2, thirtySecondesLater);
		Assert.assertEquals(result2, "Just now");

		Date currentDate3 = setMockDate(2015, 4, 17, 9, 23, 23);
		Date limitDate = setMockDate(2015, 4, 17, 9, 22, 24);
		String result3 = (String) method.invoke(method, currentDate3, limitDate);
		Assert.assertEquals(result3, "Just now");

		Date currentDate4 = setMockDate(2015, 4, 17, 9, 23, 23);
		Date afterDate = setMockDate(2015, 4, 17, 9, 22, 12);
		String result4 = (String) method.invoke(method, currentDate4, afterDate);
		Assert.assertEquals(result4, "1 minute ago");
	}

	@Test
	public void formatMinutesTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
		method.setAccessible(true);
		Date currentDate = setMockDate(2015, 4, 17, 9, 23, 23);
		Date simpleDate = setMockDate(2015, 4, 17, 9, 0, 15);
		String result = (String) method.invoke(method, currentDate, simpleDate);
		Assert.assertEquals(result, "23 minutes ago");

		Date currentDate2 = setMockDate(2015, 4, 17, 9, 23, 23);
		Date simpleDate2 = setMockDate(2015, 4, 17, 9, 0, 24);
		String result2 = (String) method.invoke(method, currentDate2, simpleDate2);
		Assert.assertEquals(result2, "22 minutes ago");

		Date currentDate3 = setMockDate(2015, 4, 17, 9, 23, 23);
		Date simpleDate3 = setMockDate(2015, 4, 17, 9, 0, 22);
		String result3 = (String) method.invoke(method, currentDate3, simpleDate3);
		Assert.assertEquals(result3, "23 minutes ago");

		Date currentDate4 = setMockDate(2015, 4, 17, 9, 23, 23);
		Date afterLimit = setMockDate(2015, 4, 17, 8, 23, 6);
		String result4 = (String) method.invoke(method, currentDate4, afterLimit);
		Assert.assertEquals(result4, "1 hour ago");
	}

	@Test
	public void formatHoursTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
		method.setAccessible(true);
		Date currentDate = setMockDate(2015, 4, 17, 9, 23, 23);
		Date simpleDate = setMockDate(2015, 4, 17, 5, 0, 34);
		String result = (String) method.invoke(method, currentDate, simpleDate);
		Assert.assertEquals(result, "4 hours ago");

		Date currentDate2 = setMockDate(2015, 4, 17, 9, 23, 23);
		Date yesterday = setMockDate(2015, 4, 16, 23, 23, 24);
		String result2 = (String) method.invoke(method, currentDate2, yesterday);
		Assert.assertEquals(result2, "Yesterday at 11:23 PM");

		Date currentDate3 = setMockDate(2015, 4, 17, 9, 23, 23);
		Date yesterdayLimit = setMockDate(2015, 4, 16, 0, 0, 0);
		String result3 = (String) method.invoke(method, currentDate3, yesterdayLimit);
		Assert.assertEquals(result3, "Yesterday at 12:00 AM");

		Date currentDate4 = setMockDate(2015, 4, 1, 9, 23, 23);
		Date yesterdayAndMonth = setMockDate(2015, 3, 30, 23, 23, 24);
		String result4 = (String) method.invoke(method, currentDate4, yesterdayAndMonth);
		Assert.assertEquals(result4, "Yesterday at 11:23 PM");
	}

	@Test
	public void formatSevenDaysTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
		method.setAccessible(true);
		Date currentDate = setMockDate(2015, 3, 17, 9, 23, 23);
		Date simpleDate = setMockDate(2015, 3, 14, 23, 59, 59);
		String result = (String) method.invoke(method, currentDate, simpleDate);
		Assert.assertEquals(result, "Tuesday at 11:59 PM");

		Date currentDate2 = setMockDate(2015, 3, 17, 9, 23, 23);
		Date simpleDate2 = setMockDate(2015, 3, 10, 9, 23, 24);
		String result2 = (String) method.invoke(method, currentDate2, simpleDate2);
		Assert.assertEquals(result2, "Friday at 9:23 AM");
	}

	@Test
	public void formatMonthTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
		method.setAccessible(true);
		Date currentDate = setMockDate(2015, 3, 17, 9, 23, 23);
		Date simpleDate = setMockDate(2015, 3, 2, 23, 23, 23);
		String result = (String) method.invoke(method, currentDate, simpleDate);
		Assert.assertEquals(result, "April 2 at 11:23 PM");

		Date currentDate2 = setMockDate(2015, 3, 17, 9, 23, 23);
		Date simpleDate2 = setMockDate(2015, 2, 28, 23, 23, 23);
		String result2 = (String) method.invoke(method, currentDate2, simpleDate2);
		Assert.assertEquals(result2, "March 28 at 11:23 PM");
	}

	@Test
	public void formatMoreTimeTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("selectCaseTimeAgo", params);
		method.setAccessible(true);
		Date currentDate = setMockDate(2015, 3, 17, 9, 23, 23);
		Date simpleDate = setMockDate(2015, 2, 2, 23, 23, 23);
		String result = (String) method.invoke(method, currentDate, simpleDate);
		Assert.assertEquals(result, "March 2");

		Date currentDate2 = setMockDate(2015, 3, 17, 9, 23, 23);
		Date simpleDate2 = setMockDate(2014, 2, 2, 23, 23, 23);
		String result2 = (String) method.invoke(method, currentDate2, simpleDate2);
		Assert.assertEquals(result2, "March 2, 2014");
	}

	@Test
	public void formatMinutesAgoTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = long.class;

		Method method = myTarget.getDeclaredMethod("formatMinutesAgo", params);
		method.setAccessible(true);
		long diffSecondes = 61;
		String result = (String) method.invoke(method, diffSecondes);
		Assert.assertEquals(result, "1 minute ago");

		long diffSecondes2 = 1380;
		String result2 = (String) method.invoke(method, diffSecondes2);
		Assert.assertEquals(result2, "23 minutes ago");
	}

	@Test
	public void formatAsTodayTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = long.class;

		Method method = myTarget.getDeclaredMethod("formatAsToday", params);
		method.setAccessible(true);
		long diffSecondes = 14400;
		String result = (String) method.invoke(method, diffSecondes);
		Assert.assertEquals(result, "4 hours ago");
	}

	@Test
	public void formatAsYesterdayTest()
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = Date.class;

		Method method = myTarget.getDeclaredMethod("formatAsYesterday", params);
		method.setAccessible(true);
		Date date = setMockDate(2015, 3, 23, 23, 23, 23);
		String result = (String) method.invoke(method, date);
		Assert.assertEquals(result, "Yesterday at 11:23 PM");
	}

	@Test
	public void formatAsLastWeekTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = Date.class;

		Method method = myTarget.getDeclaredMethod("formatAsLastWeek", params);
		method.setAccessible(true);
		Date date = setMockDate(2015, 3, 23, 23, 23, 23);
		String result = (String) method.invoke(method, date);
		Assert.assertEquals(result, "Thursday at 11:23 PM");
	}

	@Test
	public void formatAsLastMonthTest()
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = Date.class;

		Method method = myTarget.getDeclaredMethod("formatAsLastMonth", params);
		method.setAccessible(true);
		Date date = setMockDate(2015, 3, 23, 23, 23, 23);
		String result = (String) method.invoke(method, date);
		Assert.assertEquals(result, "April 23 at 11:23 PM");
	}

	@Test
	public void formatAsLastYearTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("formatAsLastYear", params);
		method.setAccessible(true);
		Date date = setMockDate(2015, 3, 23, 23, 23, 23);
		Date currentDate = setMockDate(2015, 5, 23, 23, 23, 23);
		String result = (String) method.invoke(method, date, currentDate);
		Assert.assertEquals(result, "April 23");
	}

	@Test
	public void formatAsOtherTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = Date.class;

		Method method = myTarget.getDeclaredMethod("formatAsOther", params);
		method.setAccessible(true);
		Date date = setMockDate(2015, 3, 23, 23, 23, 23);
		String result = (String) method.invoke(method, date);
		Assert.assertEquals(result, "April 23, 2015");
	}

	@Test
	public void isSameDayTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("isSameDay", params);
		method.setAccessible(true);
		Date date = setMockDate(2015, 3, 23, 23, 23, 23);
		Date date2 = setMockDate(2015, 3, 23, 23, 23, 23);
		boolean result = (boolean) method.invoke(method, date, date2);
		Assert.assertEquals(result, true);

		Date date3 = setMockDate(2015, 3, 23, 23, 23, 23);
		Date date4 = setMockDate(2015, 2, 23, 23, 23, 23);
		boolean result2 = (boolean) method.invoke(method, date3, date4);
		Assert.assertEquals(result2, false);
	}

	@Test
	public void isYesterdayTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[2];
		params[0] = Date.class;
		params[1] = Date.class;

		Method method = myTarget.getDeclaredMethod("isYesterday", params);
		method.setAccessible(true);
		Date date = setMockDate(2015, 3, 22, 23, 23, 23);
		Date date2 = setMockDate(2015, 3, 23, 23, 23, 23);
		boolean result = (boolean) method.invoke(method, date, date2);
		Assert.assertEquals(result, true);

		Date date3 = setMockDate(2015, 3, 23, 23, 23, 23);
		Date date4 = setMockDate(2015, 3, 21, 23, 23, 23);
		boolean result2 = (boolean) method.invoke(method, date3, date4);
		Assert.assertEquals(result2, false);
	}

	@Test
	public void isLastWeekTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = long.class;

		Method method = myTarget.getDeclaredMethod("isLastWeek", params);
		method.setAccessible(true);
		long diffSecondes = 518400;
		boolean result = (boolean) method.invoke(method, diffSecondes);
		Assert.assertEquals(result, true);

		diffSecondes = 691200;
		result = (boolean) method.invoke(method, diffSecondes);
		Assert.assertEquals(result, false);
	}

	@Test
	public void isLastMonthTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = long.class;

		Method method = myTarget.getDeclaredMethod("isLastMonth", params);
		method.setAccessible(true);
		long diffSecondes = 1296000;
		boolean result = (boolean) method.invoke(method, diffSecondes);
		Assert.assertEquals(result, true);

		diffSecondes = 3456000;
		result = (boolean) method.invoke(method, diffSecondes);
		Assert.assertEquals(result, false);
	}

	@Test
	public void isLastYearTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<FormatterTime> myTarget = FormatterTime.class;
		Class<?> params[] = new Class[1];
		params[0] = long.class;

		Method method = myTarget.getDeclaredMethod("isLastYear", params);
		method.setAccessible(true);
		long diffSecondes = 12960000;
		boolean result = (boolean) method.invoke(method, diffSecondes);
		Assert.assertEquals(result, true);

		diffSecondes = 62208000;
		result = (boolean) method.invoke(method, diffSecondes);
		Assert.assertEquals(result, false);
	}

}
