package com.delcam.utils;

public class FormatUtil {
	
	public static String phone(String src) {
		if(src == null) {
			return "";
		}
		if(src.length() == 8) {
			return src.replaceFirst("(^[0-9]{4})([0-9]{4})$", "$1-$2");
		}else {
			return src.replaceFirst("(^02|[0-9]{3})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
		}
	}
	
	public static String Fax(String src) {
		if(src == null) {
			return "";
		}
		if(src.length() == 8) {
			return src.replaceFirst("(^[0-9]{4})([0-9]{4})$", "$1-$2");
		}else {
			return src.replaceFirst("(^02|[0-9]{3})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
		}
	}
	
	public static String BizNum(String src) {
		if(src == null) {
			return "";
		}
			return src.replaceFirst("([0-9]{3})([0-9]{2})([0-9]{5})$", "$1-$2-$3");
	}
}
