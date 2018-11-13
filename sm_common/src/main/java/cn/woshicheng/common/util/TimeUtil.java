package cn.woshicheng.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;


/**
 * <p>Title: 日期相关处理函数</p>
 *日期和时间模式
 *日期和时间格式由日期和时间模式 字符串指定。在日期和时间模式字符串中，未加引号的字母 'A' 到 'Z' 和 'a' 到 'z'
 *被解释为模式字母，用来表示日期或时间字符串元素。文本可以使用单引号 (') 引起来，以免进行解释。"''"
 *表示单引号。所有其他字符均不解释；只是在格式化时将它们简单复制到输出字符串，或者在分析时与输入字符串进行匹配。
 *
 *y-年,M-月,d-日,H-时,m-分，s-秒,S-毫秒
 * <p>Description: </p>
 *
 * <p>Copyright: 2011</p>
 *
 * <p>Company: gotop</p>
 *
 * @author phc
 *
 * @date 2011-3-4
 *
 * @version 1.0
 **/
public class TimeUtil {

	/**
	 * 根据格式化类型将指定日期时间的格式化成相应的字符串形式的(y-年,M-月,d-日,H-时,m-分，s-秒,S-毫秒)日期时间;
	 * 
	 * @param date
	 *            Date 指定的日期
	 * 
	 * @param format
	 *            String 格式字符串
	 * 
	 * @return String
	 */
	public static String getCntDtStr(Date date, String format) {
		SimpleDateFormat dtFmt = new SimpleDateFormat(format);
		String nowDtStr = dtFmt.format(date);
		return nowDtStr;
	}
	
	
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	
	
	/**
	 * 将字符串转日期;
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate(String str) throws ParseException{
		SimpleDateFormat dtFmt = new SimpleDateFormat("yyyyMMddHHmmss");
		if (str == null) {
			System.err.println("传入参数不能为空");
			return null;
		}

		str = fillDate(str);
		if (str == null) {
			System.err.println("传入日期格式有误");
			return null;
		}

		if (!isDate(str.substring(0, 8)) || !isTime(str.substring(8))) {
			System.err.println("传入日期格式有误");
			return null;
		}
		Date date = dtFmt.parse(str);
		return date;
	}

	// ***************************************************************************************************************
	// 取得当前时间函数
	// ***************************************************************************************************************

	/**
	 * 取得系统当前的日期时间，返回时间格式是yyyyMMddHHmmssSSS的17位字符串
	 * 
	 * @return String
	 */
	public static String currentStr() {
		return getCntDtStr(new Date(), "yyyyMMddHHmmssSSS");
	}
	
	/**
	 * 取得系统当前的日期时间，返回时间格式是yyyyMMddHHmmssSSS的17位字符串
	 * 
	 * @return String
	 */
	public static String currentStr2() {
		return getCntDtStr(new Date(), "yyyyMMddHHmmss");
	}

	public static String currentDateStr(Date date1,Date date2) {
		String day1 = getCntDtStr(date1, "yyyy年MM月dd日");
		String day2 = getCntDtStr(date2, "yyyy年MM月dd日");
		String Time1 = getCntDtStr(date1, "HH:mm");
		String Time2 = getCntDtStr(date2, "HH:mm");
		if(day1.equals(day2)){
			return day1 + " " + Time1 + "~" + Time2;
		}
		return  day1 + " " + Time1 + "~" + day2 + " " + Time2;
	}
	
	/**
	 * 返回当前秒数，小数部分为毫秒
	 * 
	 * @return double
	 */
	public static double currentDouble() {
		Long ms = new Date().getTime();
		Double sec = ms.doubleValue() / 1000;
		return sec;
	}

	/**
	 * 取得系统当前的时间，返回时间格式是HHmmss的6位字符串
	 * 
	 * @return
	 */
	public static String now() {
		return getCntDtStr(new Date(), "HHmmss");
	}

	/**
	 * 取得系统当前的日期，返回时间格式是yyyyMMdd的6位字符串
	 * 
	 * @return
	 */
	public static String today() {
		return getCntDtStr(new Date(), "yyyyMMdd");
	}

	/**
	 * 返回整数，表示当前的秒和毫秒ssSSS
	 * 
	 * @return
	 */
	public static long secondMillitm() {
		return Long.parseLong(getCntDtStr(new Date(), "ssSSS"));
	}

	/**
	 * 取得当前系统日期的星期数。该星期数是从星期天开始的0-6的数值
	 * 
	 * @return
	 */
	public static long currentWeek() {
		Calendar now = new GregorianCalendar();
		Integer cntWeek = now.get(Calendar.DAY_OF_WEEK) - 1;
		return cntWeek.longValue();
	}

	/**
	 * 取得当前系统日期的月份数。该月份是从1月开始的1~12的数值
	 * 
	 * @return
	 */
	public static long currentMonth() {
		Calendar now = new GregorianCalendar();
		Integer cntMonth = now.get(Calendar.MONTH) + 1;
		return cntMonth.longValue();
	}

	/**
	 * 取得当前系统日期的日数。该日数是在所在月份中的从1号开始的1~31的数值
	 * 
	 * @return
	 */
	public static long currentDay() {
		Calendar now = new GregorianCalendar();
		Integer cntMonth = now.get(Calendar.DATE);
		return cntMonth.longValue();
	}

	/**
	 * 取得当前系统日期毫秒数，以字符串形式返回
	 * 
	 * @return
	 */
	public static String currentMillitm() {
		return getCntDtStr(new Date(), "SSS");
	}

	/**
	 * 返回指定年月的第一个星期天的日子数，即几号
	 * 
	 * @param year
	 *            指定年份。如果指定月份数小于0，则返回-1
	 * @param month
	 *            指定月份。如果指定月份数小于1或者大于12，则返回-1
	 * @return
	 */
	public static int getFirstWeek(int year, int month) {
		if (year < 0 || month < 1 || month > 12)
			return -1;
		Calendar cal = new GregorianCalendar(year, month - 1, 1);
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return (week == 0 ? 1 : (8 - week));
	}

	/**
	 * 返回指定年月的最后一个星期天的日子数，即几号
	 * 
	 * @param year
	 *            指定年份。如果指定月份数小于0，则返回-1
	 * @param month
	 *            指定月份。如果指定月份数小于1或者大于12，则返回-1
	 * @return
	 */
	public static int getLastWeek(int year, int month) {
		if (year < 0 || month < 1 || month > 12)
			return -1;
		Calendar cal = new GregorianCalendar(year, month, 0);
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int date = cal.get(Calendar.DATE);
		return date - week;
	}

	/**
	 * 将字符串类型的日期时间转换成为日期类型，然后返回该日期的距1970年1月1日以来的秒数
	 * 
	 * <p>
	 * 该方法首先验证转入参数timeStr是否为标准的日期时间格式的字符串，格式应该为yyyyMMddHHmmss，
	 * 如果timeStr的格式有误或者日期时间有误（如20100302093461中秒数为61），该方法将返回-1
	 * </p>
	 * 
	 * @param timeStr
	 *            字符串类型的日期时间，格式应该为yyyyMMddHHmmss。如果位数不足，函数将自动补齐
	 * @return long 长整型的秒数
	 */
	public static long toTime(String timeStr) {

		if (timeStr == null) {
			System.err.println("传入参数不能为空");
			return -1;
		}

		timeStr = fillDate(timeStr);
		if (timeStr == null) {
			System.err.println("传入日期格式有误");
			return -1;
		}

		if (!isDate(timeStr.substring(0, 8)) || !isTime(timeStr.substring(8))) {
			System.err.println("传入日期格式有误");
			return -1;
		}
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, Integer.parseInt(timeStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(timeStr.substring(4, 6)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(timeStr.substring(6, 8)));
		cal.set(Calendar.HOUR_OF_DAY, Integer
				.parseInt(timeStr.substring(8, 10)));
		cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(10, 12)));
		cal.set(Calendar.SECOND, Integer.parseInt(timeStr.substring(12)));
		return cal.getTimeInMillis() / 1000;
	}

	// ***************************************************************************************************************
	// 时间判断函数
	// ***************************************************************************************************************

	/**
	 * 判断传入参数是否为日期
	 * 
	 * @param time
	 *            字符串类型的日期，格式应该为:yyyyMMdd
	 * 
	 * @return true-传入参数是日期<br/>
	 *         false-传入参数不是日期
	 */
	public static boolean isDate(String date) {
		boolean ret = true;
		if (date == null || date.length() != 8) {
			ret = false;
		}
		try {
			int year = new Integer(date.substring(0, 4)).intValue();
			int month = new Integer(date.substring(4, 6)).intValue();
			int day = new Integer(date.substring(6)).intValue();
			Calendar cal = Calendar.getInstance();
			cal.setLenient(false); // 允许严格检查日期格式
			cal.set(year, month - 1, day);
			cal.getTime();// 该方法调用就会抛出异常
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

	/**
	 * 判断传入参数是否为时间
	 * 
	 * @param time
	 *            字符串类型的时间，格式应该为:HHmmss
	 * 
	 * @return true-传入参数是时间<br/>
	 *         false-传入参数不是时间
	 */
	public static boolean isTime(String time) {
		boolean ret = true;
		if (time == null || time.length() != 6) {
			ret = false;
		}
		try {
			int h = new Integer(time.substring(0, 2)).intValue();
			int m = new Integer(time.substring(2, 4)).intValue();
			int s = new Integer(time.substring(4)).intValue();
			Calendar cal = Calendar.getInstance();
			cal.setLenient(false); // 允许严格检查日期格式
			cal.set(Calendar.HOUR_OF_DAY, h);
			cal.set(Calendar.MINUTE, m);
			cal.set(Calendar.SECOND, s);
			cal.getTime();// 该方法调用就会抛出异常
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

	/**
	 * 判断传入参数是否为日期时间
	 * 
	 * @param time
	 *            字符串类型的日期时间，格式应该为:yyyyMMddHHmmss
	 * 
	 * @return true-传入参数是日期时间<br/>
	 *         false-传入参数不是日期时间
	 */
	public static boolean isDateTime(String dateTime) {
		boolean ret = false;
		if (dateTime == null || dateTime.length() != 14) {
			ret = false;
		} else if (isDate(dateTime.substring(0, 8))
				&& isTime(dateTime.substring(8)))
			ret = true;
		return ret;
	}

	private static Pattern numberPattern = Pattern.compile("\\d++");

	/**
	 * 输入年份是否为润年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
        return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
	}

	// ***************************************************************************************************************
	// 其它操作函数
	// ***************************************************************************************************************

	/**
	 * 根据日期类型（年月周日时分秒）按一定的数量来修改指定的日期时间。
	 * 
	 * <p>
	 * 例如，修改20100301110202这个日期，修改日期类型为“日(type为TINO_DAY)”，修改数量为“-1”，即前一天，
	 * 则系统会返回20100228110202
	 * </p>
	 * <p>
	 * 输入的开始日期时间字符串,输入格式YYYYMMDDHHMMSS,要求输入时不得少于要求计算的时间单位，如计算年，则输入要保证4位年份
	 * 
	 * @param time
	 *            字符串类型的日期，格式应该为yyyyMMddHHmmss，如果位数不足将函数将自动补足位数， 前提是time应该全为数字，
	 *            而且time的位数应该在4、6、8、10、12、14、17之中，即“年”、“年月”、“年月日”、“年月日时”、
	 *            “年月日时分”、 “年月日时分秒”和“年月日时分秒毫秒”来进行处理。否则，函数将返回空。同时要求输入时不得少
	 *            于要求计算的时间单位，如计算年，则输入要保证4位年份
	 * @param type
	 *            待修改的日期类型：year = 年</div> <div>
	 *            quarter = 季度</div> <div>
	 *            month = 月</div> <div>
	 *            week = 周</div> <div>
	 *            day = 日</div> <div>
	 *            hour = 小时</div> <div>
	 *            minute = 分钟</div> <div>
	 *            second = 秒</div>
	 * @param interval
	 *            修改量
	 * 
	 * @return String 字符串类型的时间，格式应该为:yyyyMMddHHmmss
	 */
	public static String add(String time,String type, int interval) {
		long typeL = -1;
		if(type.equals("year")){
			typeL = 1;
		}else if(type.equals("quarter")){
			typeL = 2;
		}else if(type.equals("month")){
			typeL = 3;
		}else if(type.equals("week")){
			typeL = 5;
		}else if(type.equals("day")){
			typeL = 6;
		}else if(type.equals("hour")){
			typeL = 7;
		}else if(type.equals("minute")){
			typeL = 8;
		}else if(type.equals("second")){
			typeL = 9;
		}
		if (time == null || time.trim().equals("")) {
			return null;
		}
		String fullTime = "";

		if ((fullTime = fillDate(time)) == null) {
			return null;
		}

		if (!isDateTime(fullTime)) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(toTime(fullTime) * 1000);
		switch ((int) typeL) {
		case 1:
			if (time.length() < 4)
				return null;
			cal.add(Calendar.YEAR, interval);
			break;
		case 2:
			if (time.length() < 6)
				return null;
			cal.add(Calendar.MONTH, interval * 3);
			break;
		case 3:
			if (time.length() < 6)
				return null;
			cal.add(Calendar.MONTH, interval);
			break;
		case 5:
			if (time.length() < 8)
				return null;
			cal.add(Calendar.WEEK_OF_YEAR, interval);
			break;
		case 6:
			if (time.length() < 8)
				return null;
			cal.add(Calendar.DATE, interval);
			break;
		case 7:
			if (time.length() < 10)
				return null;
			cal.add(Calendar.HOUR_OF_DAY, interval);
			break;
		case 8:
			if (time.length() < 12)
				return null;
			cal.add(Calendar.MINUTE, interval);
		case 9:
			if (time.length() < 14)
				return null;
			cal.add(Calendar.SECOND, interval);
			break;
		default:
			return null;
		}

		return getCntDtStr(cal.getTime(), "yyyyMMddHHmmss");
	}
	/**
	 * 根据日期类型（年月周日时分秒）按一定的数量来修改指定的日期时间。
	 *
	 * <p>
	 * 例如，修改20100301110202这个日期，修改日期类型为“日(type为TINO_DAY)”，修改数量为“-1”，即前一天，
	 * 则系统会返回20100228110202
	 * </p>
	 * <p>
	 * 输入的开始日期时间字符串,输入格式YYYYMMDDHHMMSS,要求输入时不得少于要求计算的时间单位，如计算年，则输入要保证4位年份
	 *
	 * @param time
	 *            字符串类型的日期，格式应该为yyyyMMddHHmmss，如果位数不足将函数将自动补足位数， 前提是time应该全为数字，
	 *            而且time的位数应该在4、6、8、10、12、14、17之中，即“年”、“年月”、“年月日”、“年月日时”、
	 *            “年月日时分”、 “年月日时分秒”和“年月日时分秒毫秒”来进行处理。否则，函数将返回空。同时要求输入时不得少
	 *            于要求计算的时间单位，如计算年，则输入要保证4位年份
	 * @param type
	 *            待修改的日期类型：year = 年</div> <div>
	 *            quarter = 季度</div> <div>
	 *            month = 月</div> <div>
	 *            week = 周</div> <div>
	 *            day = 日</div> <div>
	 *            hour = 小时</div> <div>
	 *            minute = 分钟</div> <div>
	 *            second = 秒</div>
	 * @param interval
	 *            修改量
	 *
	 * @return String 字符串类型的时间，格式应该为:yyyyMMddHHmmss
	 */
	public static Date add4Date(String time,String type, int interval) {
		long typeL = -1;
		if(type.equals("year")){
			typeL = 1;
		}else if(type.equals("quarter")){
			typeL = 2;
		}else if(type.equals("month")){
			typeL = 3;
		}else if(type.equals("week")){
			typeL = 5;
		}else if(type.equals("day")){
			typeL = 6;
		}else if(type.equals("hour")){
			typeL = 7;
		}else if(type.equals("minute")){
			typeL = 8;
		}else if(type.equals("second")){
			typeL = 9;
		}
		if (time == null || time.trim().equals("")) {
			return null;
		}
		String fullTime = "";

		if ((fullTime = fillDate(time)) == null) {
			return null;
		}

		if (!isDateTime(fullTime)) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(toTime(fullTime) * 1000);
		switch ((int) typeL) {
			case 1:
				if (time.length() < 4)
					return null;
				cal.add(Calendar.YEAR, interval);
				break;
			case 2:
				if (time.length() < 6)
					return null;
				cal.add(Calendar.MONTH, interval * 3);
				break;
			case 3:
				if (time.length() < 6)
					return null;
				cal.add(Calendar.MONTH, interval);
				break;
			case 5:
				if (time.length() < 8)
					return null;
				cal.add(Calendar.WEEK_OF_YEAR, interval);
				break;
			case 6:
				if (time.length() < 8)
					return null;
				cal.add(Calendar.DATE, interval);
				break;
			case 7:
				if (time.length() < 10)
					return null;
				cal.add(Calendar.HOUR_OF_DAY, interval);
				break;
			case 8:
				if (time.length() < 12)
					return null;
				cal.add(Calendar.MINUTE, interval);
			case 9:
				if (time.length() < 14)
					return null;
				cal.add(Calendar.SECOND, interval);
				break;
			default:
				return null;
		}

		return cal.getTime();
	}
	/**
	 * 根据日期类型（年月周日时分秒）按一定的数量来修改指定的日期时间。
	 *
	 * <p>
	 * 例如，修改20100301110202这个日期，修改日期类型为“日(type为TINO_DAY)”，修改数量为“-1”，即前一天，
	 * 则系统会返回20100228110202
	 * </p>
	 * <p>
	 * 输入的开始日期时间字符串,输入格式YYYYMMDDHHMMSS,要求输入时不得少于要求计算的时间单位，如计算年，则输入要保证4位年份
	 *
	 * @param time
	 *            字符串类型的日期，格式应该为yyyyMMddHHmmss，如果位数不足将函数将自动补足位数， 前提是time应该全为数字，
	 *            而且time的位数应该在4、6、8、10、12、14、17之中，即“年”、“年月”、“年月日”、“年月日时”、
	 *            “年月日时分”、 “年月日时分秒”和“年月日时分秒毫秒”来进行处理。否则，函数将返回空。同时要求输入时不得少
	 *            于要求计算的时间单位，如计算年，则输入要保证4位年份
	 * @param type
	 *            待修改的日期类型：year = 年</div> <div>
	 *            quarter = 季度</div> <div>
	 *            month = 月</div> <div>
	 *            week = 周</div> <div>
	 *            day = 日</div> <div>
	 *            hour = 小时</div> <div>
	 *            minute = 分钟</div> <div>
	 *            second = 秒</div>
	 * @param interval
	 *            修改量
	 *
	 * @return String 字符串类型的时间，格式应该为:yyyyMMddHHmmss
	 */
	public static Date add(Date time,String type, int interval) {
		long typeL = -1;
		if(type.equals("year")){
			typeL = 1;
		}else if(type.equals("quarter")){
			typeL = 2;
		}else if(type.equals("month")){
			typeL = 3;
		}else if(type.equals("week")){
			typeL = 5;
		}else if(type.equals("day")){
			typeL = 6;
		}else if(type.equals("hour")){
			typeL = 7;
		}else if(type.equals("minute")){
			typeL = 8;
		}else if(type.equals("second")){
			typeL = 9;
		}
		if (time == null ) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time.getTime());
		switch ((int) typeL) {
		case 1:
			cal.add(Calendar.YEAR, interval);
			break;
		case 2:
			cal.add(Calendar.MONTH, interval * 3);
			break;
		case 3:
			cal.add(Calendar.MONTH, interval);
			break;
		case 5:
			cal.add(Calendar.WEEK_OF_YEAR, interval);
			break;
		case 6:
			cal.add(Calendar.DATE, interval);
			break;
		case 7:
			cal.add(Calendar.HOUR_OF_DAY, interval);
			break;
		case 8:
			cal.add(Calendar.MINUTE, interval);
			break;
		case 9:
			cal.add(Calendar.SECOND, interval);
			break;
		default:
			return null;
		}

		return cal.getTime();
	}
	/**
	 * 计算两个日期时间之间的差值。如果日期格式出错或出现其它异常则返回-1
	 * 
	 * @param start
	 *            字符串类型的日期，格式应该为yyyyMMddHHmmss，如果位数不足将函数将自动补足位数， 前提是time应该全为数字，
	 *            而且time的位数应该在4、6、8、10、12、14、17之中，即“年”、“年月”、“年月日”、“年月日时”、
	 *            “年月日时分”、 “年月日时分秒”和“年月日时分秒毫秒”来进行处理。否则，函数将返回空。同时要求输入时不得少
	 *            于要求计算的时间单位，如计算年，则输入要保证4位年份
	 * @param end
	 *            同start
	 * @param type
	 *            待修改的日期类型：year = 年</div> <div>
	 *            month = 月</div> <div>
	 *            day = 日</div> <div>
	 *            hour = 小时</div> <div>
	 *            minute = 分钟</div> <div>
	 *            second = 秒</div>
	 * 
	 * @return long 返回两个日期时间之间指定类型的差值
	 */
	public static long different(String start, String end, String type) {
		long typeL = -1;
		if(type.equals("year")){
			typeL = 1;
		}else if(type.equals("month")){
			typeL = 3;
		}else if(type.equals("day")){
			typeL = 6;
		}else if(type.equals("hour")){
			typeL = 7;
		}else if(type.equals("minute")){
			typeL = 8;
		}else if(type.equals("second")){
			typeL = 9;
		}
		if (start == null || start.trim().equals("") || end == null
				|| end.trim().equals("")) {
			return -1;
		}
		String fullTime1 = null, fullTime2 = null;
		if (!numberPattern.matcher(start).matches()
				|| !numberPattern.matcher(end).matches()) {
			return -1;
		}

		if ((fullTime1 = fillDate(start)) == null
				|| (fullTime2 = fillDate(end)) == null) {
			return -1;
		}

		if (!isDateTime(fullTime1) || !isDateTime(fullTime2)) {
			return -1;
		}
		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		cal1.setTimeInMillis(toTime(fullTime1) * 1000);
		cal2.setTimeInMillis(toTime(fullTime2) * 1000);
		long ret = -1;
		switch ((int) typeL) {
		case 1:
			if (start.length() < 4 || end.length() < 4)
				return -1;
			ret = Math.abs(cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR));
			break;
		case 3:
			if (start.length() < 6 || end.length() < 6)
				return -1;
			if (cal1.getTimeInMillis() - cal2.getTimeInMillis() > 0) {
				ret = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
						+ cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
			} else {
				ret = (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12
						+ (cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH));
			}
			break;
		case 6:
			if (start.length() < 8 || end.length() < 8)
				return -1;
			ret = Math.abs((cal1.getTimeInMillis() - cal2.getTimeInMillis()))
					/ (1000 * 60 * 60 * 24);
			break;
		case 7:
			if (start.length() < 10 || end.length() < 10)
				return -1;
			ret = Math.abs((cal1.getTimeInMillis() - cal2.getTimeInMillis()))
					/ (1000 * 60 * 60);
			break;
		case 8:
			if (start.length() < 12 || end.length() < 12)
				return -1;
			ret = Math.abs((cal1.getTimeInMillis() - cal2.getTimeInMillis()))
					/ (1000 * 60);
			break;
		case 9:
			if (start.length() < 14 || end.length() < 14)
				return -1;
			ret = Math
					.abs((cal1.getTimeInMillis() - cal2.getTimeInMillis()) / 1000);
			break;
		default:
			return -1;
		}
		return ret;
	}
	/**
	 * 补全字符串类型的日期时间，补全后的返回值格式为14位的"yyyyMMddHHmmss"日期时间的字符串
	 * 
	 * <p>
	 * 传入参数的长度为：4，6，8，10，12，14和17将被分别认为是“年”，“年月”，“年月日”，“年月日时”，
	 * “年月日时分”，“年月日时分秒”和“年月日时分秒毫秒”来进行处理。如果传入参数位数不符合处理长度 或者是包含了其它非數字字符將函數將返回null
	 * </p>
	 * 
	 * @param dateTime
	 *            待补全的字符串日期时间
	 * 
	 * @return String 字符串类型的时间，格式应该为:yyyyMMddHHmmss
	 */
	private static String fillDate(String dateTime) {
		if (dateTime == null || dateTime.trim().equals("")) {
			return null;
		}
		dateTime = dateTime.trim();
		if (!numberPattern.matcher(dateTime).matches()) {
			return null;
		}
		String ret = dateTime;
		switch (dateTime.length()) {
		case 4:
			ret += "0101000000";
			break;
		case 6:
			ret += "01000000";
			break;
		case 8:
			ret += "000000";
			break;
		case 10:
			ret += "0000";
			break;
		case 12:
			ret += "00";
			break;
		case 14:
			break;
		case 17:
			ret = ret.substring(0, 14);
			break;
		default:
			ret = null;
			break;
		}
		return ret;
	}
	
	
	/****
	 * 秒转化为xx:xx:xx
	 * @param time
	 * @return
	 */
    public static String secToTime(int time) {  
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
  
    private static String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }
	
	public static void main(String[] args) throws ParseException {
		Date date = new Date();
		Date date2 = add(date, "day", 3);
		System.out.println(getCntDtStr(date2, "yyyyMMdd"));
	}
}
