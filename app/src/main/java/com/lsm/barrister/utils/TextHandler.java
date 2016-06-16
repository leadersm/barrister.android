package com.lsm.barrister.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;


public class TextHandler {

	public static SpannableString getKeywordsSpannable(String text, String keywords) {
		
		
		SpannableString ss = new SpannableString(text);

		try {
			
			int index0 = text.indexOf(keywords);
			
			int index1 = index0 + keywords.length();
			
			ss.setSpan(new ForegroundColorSpan(0xff3d94de), index0, index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		} catch (Exception e) {
		}
		return ss;
	}
	
	public static SpannableString getReadKeywordsSpannable(String text, String keywords,boolean markread) {

		SpannableString ss = new SpannableString(text);
		
		try {
		
			int index0 = text.indexOf(keywords);
			
			int index1 = index0 + keywords.length();
			
			if(index0>0){
				
				if(!markread){
					ss.setSpan(new ForegroundColorSpan(0xff000000), 0, index0, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				}
			}
			
			ss.setSpan(new ForegroundColorSpan(0xff3d94de), index0, index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			
			if(!markread){
				ss.setSpan(new ForegroundColorSpan(0xff000000), index1, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			
			return ss;
			
		} catch (Exception e) {
		}
		
		if(!markread){
			ss.setSpan(new ForegroundColorSpan(0xff000000), 0, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		return ss;
	}

	public static String string2unicode(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8);
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF);
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}

	public static String unicode2string(String str) {
		str = (str == null ? "" : str);
		if (str.indexOf("\\u") == -1)
			return str;

		StringBuffer sb = new StringBuffer(1000);

		for (int i = 0; i <= str.length() - 6;) {
			String strTemp = str.substring(i, i + 6);
			String value = strTemp.substring(2);
			int c = 0;
			for (int j = 0; j < value.length(); j++) {
				char tempChar = value.charAt(j);
				int t = 0;
				switch (tempChar) {
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();
	}

	public static String chinaBigUnit(double num) {

		if (Math.abs(num) <= wan) {
			return String.valueOf(num);
		} else if (Math.abs(num) > wan && Math.abs(num) < baiwan) {
			return String.format("%.2f万", num / wan);
		} else if (Math.abs(num) > baiwan && Math.abs(num) < qianwan) {
			return String.format("%.2f百万", num / baiwan);
		} else if (Math.abs(num) > qianwan && Math.abs(num) < yi) {
			return String.format("%.2f千万", num / qianwan);
		} else {
			return String.format("%.2f亿", num / yi);
		}
	}

	public static final float wan = 10000;
	public static final float baiwan = 1000000;
	public static final float qianwan = 10000000;
	public static final float yi = 100000000;

	public static String chinaBigUnit(String strNum) {
		try {
			if (strNum.endsWith(".00")) {
				strNum = strNum.replace(".00", "");
			}
			double num = Double.parseDouble(strNum);
			return chinaBigUnit(num);
		} catch (Exception e) {

		}
		return strNum;
	}

	public static String wan(String strNum) {

		try {

			double num = Double.parseDouble(strNum);

			if (Math.abs(num) <= wan) {
				return String.valueOf(num);
			} else {
				return String.format("%.2f万", num / wan);
			}
		} catch (NumberFormatException e) {

		}
		return strNum;
	}

	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 验证手机号码
	 * @return
	 */
	public static boolean isMobileNum(String mobileNum) {

		if (mobileNum.length() > 11) {
			return false;
		}

		boolean result = false;

		try {

			Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");

			Matcher m = p.matcher(mobileNum);

			result = m.matches();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return result;

	}
	
	/** 
     * 验证用户名，支持中英文（包括全角字符）、数字、下划线和减号 （全角及汉字算两位）,长度为4-20位,中文按二位计数 
     *  
     * @param userName 
     * @return 
     */  
    public static boolean validateNickname(String userName) {  
        String validateStr = "^[\\w\\-－＿[0-9]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$";  
        boolean valid = false;  
        valid = matcher(validateStr, userName);
        if (valid) {  
            int strLenth = getStrLength(userName);  
            if (strLenth < 4 || strLenth > 20) {  
                valid = false;  
            }  
        }  
        return valid;  
    }  
  
    /** 
     * 获取字符串的长度，对双字符（包括汉字）按两位计数 
     * @param value 
     * @return 
     */  
    public static int getStrLength(String value) {  
        int valueLength = 0;  
        String chinese = "[\u0391-\uFFE5]";  
        for (int i = 0; i < value.length(); i++) {  
            String temp = value.substring(i, i + 1);  
            if (temp.matches(chinese)) {  
                valueLength += 2;  
            } else {
                valueLength += 1;  
            }  
        }  
        return valueLength;  
    }  
  
    private static boolean matcher(String reg, String string) {  
        boolean tem = false;  
        Pattern pattern = Pattern.compile(reg);  
        Matcher matcher = pattern.matcher(string);  
        tem = matcher.matches();  
        return tem;  
    }

}
