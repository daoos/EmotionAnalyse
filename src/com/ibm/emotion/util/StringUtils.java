package com.ibm.emotion.util;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * String Utils
 */
public class StringUtils {
	
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	
	private final static Pattern phonePattern = Pattern.compile("[1][3578]\\d{9}");
	
	private final static Pattern numberPattern = Pattern.compile("[0-9]");
	
	/**
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式(例如：1.yyyy年MM月dd日 HH:mm:ss 2.yyyy-MM-dd HH:mm:ss
	 *            3.yyyy-MM-dd 4.yyyy-MM-dd'T'HH:mm:ss.SSSZ 5.yyMMddkkmmss)
	 * @return 返回格式化后的日期字符串
	 */
	public static String dateToString(Date date, String pattern){
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	public static String formatDate(String inputDate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formatter.parse(inputDate);
			return formatter.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return inputDate;
	}
	
	public static Date stringToDate(String inputDate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formatter.parse(inputDate);
		}catch (Exception e){
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date stringToDate(String inputDate, String formatPattern){
		SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
		Date date = null;
		try {
			date = formatter.parse(inputDate);
		}catch (Exception e){
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 判断是不是一个合法的手机号码
	 * @param phoneNum
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNum){
		if(phoneNum == null || phoneNum.trim().length() == 0)
			return false;
		return phonePattern.matcher(phoneNum).matches();
	}
	
    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * 判断输入是否为IP地址
     * @param addr
     * @return
     */
	public static boolean isIP(String addr) {
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		//判断IP格式和范围
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(addr);
		boolean ipAddress = mat.find();
		return ipAddress;
	}
    
    /**
     * is null or its length is 0 or it is made by space
     * 
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
	/** 判断一个字符串在另一个字符串里的个数*/
    public static int countStr(String str1, String str2) {   
        int counter=0;  
        if (str1.indexOf(str2) == -1) {    
            return 0;  
        }   
            while(str1.indexOf(str2)!=-1){  
                counter++;  
                str1=str1.substring(str1.indexOf(str2)+str2.length());  
            }  
            return counter;    
    }
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     * 
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0 || str.equals("null"));
    }

    /**
     * compare two string
     * 
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
    	return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     * get length of CharSequence
     * 
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     * 
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     * 
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
    }

    /**
     * capitalize first letter
     * 
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * 
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     * 
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * 
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     * 
     * @param href
     * @return <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

/**
     * process special char in html
     * 
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     * 
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * 
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * 
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
    
    /** 小时 */
	private static final int hour = 1000 * 60 * 60;
//	private static final int hour = 1000 * 60;
	/**
	 * 支持 yyyy-MM-dd HH精确到小时
	 * @param start 开始时间 if：2012-2-10 11
	 * @param end 结束时间  if:2012-02-11 13:12:21
	 * @return 小时数
	 */
	public static int compareHourTime(String start,String end){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		try {
			Date d1 = df.parse(end);
			Date d2 = df.parse(start);
			long diff = d1.getTime() - d2.getTime();
			int hours = (int)(diff / hour);
			return hours;
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 根据当前时间 获取时间差 支持 yyyy-MM-dd HH精确到小时
	 * @param start 开始时间 if：2012-2-10 11
	 * @return 小时数
	 */
	public static int compareHourTime(String start){
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		try {
			Date d2 = sdf.parse(start);
			long diff = System.currentTimeMillis() - d2.getTime();
			int hours = (int)(diff / hour);
			return hours;
		} catch (Exception e) {
		}
		return 0;
	}
	
	public static List<String> removeDuplicateWithOrder(List<String> list) {
		HashSet<String> hashSet = new HashSet<String>();
		List<String> newlist = new ArrayList<String>();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String element = (String) iterator.next();
			if (hashSet.add(element)) {
				newlist.add(element);
			}
		}
		list.clear();
		list.addAll(newlist);
		return list;
	}
	
	/**
	 * 格式化double,保留一位小数
	 * @param price
	 * @return 格式化后的字符串
	 */
	public static String formatDoubleOne(Double doubleValue){
		DecimalFormat df = new DecimalFormat("0.0");
	    return df.format(doubleValue);
	}
	
	/**
	 * 格式化double类型,保留2位小数
	 * @param price
	 * @return 格式化后的字符串
	 */
	public static String formatDouble(Double doubleValue){
		DecimalFormat df = new DecimalFormat("0.00");
	    return df.format(doubleValue);
	}
	
	/**
	 * 格式化价格
	 * @param price
	 * @return
	 */
	public static double doubleFormat(Double doubleValue){
		DecimalFormat df = new DecimalFormat("0.00");
	    return Double.valueOf(df.format(doubleValue));
	}
	
	/**
	 * 订单日期格式化
	 * @param orderTime订单生成日期
	 */
	public static String orderTimeFormat(String orderTime){
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date orderDate = formatter.parse(orderTime);
			Date curDate = new Date(System.currentTimeMillis()); // 获取当前时间
			long current = curDate.getTime()/1000; // 当前时间的秒数
			long orderSeconds = orderDate.getTime()/1000; // 订单时间的秒数
			long diff = current - orderSeconds;// 两时间相差秒数
			if (diff < 10) { // 10秒内（不包含10秒）：刚刚
				return "刚刚";
			}
			if (diff >= 10 && diff < 1*60) { // 1分钟内（不包含1分钟）：XX秒前
				return diff + "秒前";
			}
			if (diff >= 1*60 && diff < 1*60*60) { // 1小时内（不包含1小时）：XX分钟前
				return diff/60 + "分钟前";
			}
			if (diff >= 1*60*60 && diff < 3*60*60) { // 3小时（不包含3小时）：X小时前
				return diff/(1*60*60) + "小时前";
			}
			long zero = (curDate.getTime() / (1000 * 3600 * 24)
					* (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset())/1000;// 今天零点零分零秒的秒数
			SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
			if (diff >= 3*60*60 && orderSeconds > zero) { // 超过3小时且在今天0点之后：今天  XX:XX
				return "今天" + hourFormat.format(orderDate);
			}
			long yesterdayZero = zero - 24*60*60;//昨天的这一时间的秒数
			if (orderSeconds <= zero && orderSeconds > yesterdayZero) {// 今天0点之前：昨天
				return "昨天";
			}
			SimpleDateFormat monthFormat = new SimpleDateFormat("MM-dd");
			if (orderSeconds <= yesterdayZero) { // 昨天0点之前：XX-XX
				return monthFormat.format(orderDate);
			}
			SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
			return yearFormat.format(orderTime);
		} catch (Exception e) {
			return orderTime;
		}
	}
	
	/**
	 * 格式化电话号码
	 * @param phone
	 * @return
	 */
	public static String formatPhone(String phone){
		if (phone.startsWith("+86")) {
			phone = phone.substring(3);
		}
		if (!StringUtils.isEmpty(phone)) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < phone.length(); i++) {
				if (numberPattern.matcher(String.valueOf(phone.charAt(i))).matches()) {
					sb.append(phone.charAt(i));
				}
			}
			return sb.toString();
		}
		return phone;
	}
	
	/**
	 * 根据日期取得星期几  
	 * @param date
	 * @return
	 */
    public static String getWeek(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");  
        String week = sdf.format(date);  
        return week;  
    }
    
    public static InputStream getStringStream(String sInputString) {
		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
				return tInputStringStream;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
    
    /**
     * 中文进行Unicode编码
     * @param cn
     * @return
     */
    public static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
          returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }
}