package com.delcam.utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class EtcUtils {
	
	//사용자 정의 기본 값
    public static String  GetCurrency( Long reqMoney ) {
    	try {
			NumberFormat formatter = NumberFormat.getNumberInstance();
			return  formatter.format(reqMoney);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "0" ; 
		}
    }
    
	//사용자 정의 기본 값
    public static String  GetZeroPadding( Long reqCode ) {
    	 return String.format("%09d", reqCode);
    }
    
    //사용자 정의 기본 값
    public static String  GetZeroPadding( int paddCnt , Long reqCode ) {
    	 return String.format("%0"+ paddCnt +"d", reqCode);
    }
    
    //사용자 정의 기본 값
    public static String  GetYYYYMM() {
    	 LocalDate date = LocalDate.now();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
    	 return  date.format(formatter);
    }
    
    public static String phonFormat(String src) {
        if (src == null) {
          return "";
        }
        if (src.length() == 8) {
          return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
          return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }
     
    public static String bizNoFormat(String src) {
        if (src == null) {
          return "";
        }
        if (src.length() == 10) {
          return src.replaceFirst("(^[0-9]{3})([0-9]{2})([0-9]{5})$", "$1-$2-$3");
        }else {
          return src ; 
        }
    }
    
    public static String priceConvertKorean(int num) {
    	String tmpNum = Integer.toString(num);
    	String[] han1 = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
    	String[] han2 = {"", "십", "백", "천", "", "십", "백", "천", "", "십", "백", "천", "", "십", "백", "천"};
    	String result = "";
    	for(int i = 0; i < tmpNum.length(); i++) {
    		String str = "";
    		int tmpSize = Integer.parseInt(String.valueOf(tmpNum.charAt(tmpNum.length()-(i+1))));
    		String han = han1[tmpSize];
    		if(han != "") str += han+han2[i];
    		if(i == 4) str += "만";
    		if(i == 8) str += "억";
    		if(i == 12) str += "조";
    		result = str + result;
    	}
    	if(num != 0) result = result + "원";
    	return result;
    }
         	
}
