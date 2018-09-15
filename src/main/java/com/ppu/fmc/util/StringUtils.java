package com.ppu.fmc.util;

public class StringUtils {
	public static boolean isEmpty(String val) {
		if (val == null)
			return true;
		
		return val.trim().length() < 1;
	}

	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

	public static String joinString(String separator, String[] arrays) {
		if (arrays == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(arrays[0]);
		
		for (int i = 1; i < arrays.length; i++) {
			sb.append(",").append(arrays[i]);
		}
		return sb.toString();
//		return StringUtils.arrayToDelimitedString(arrays, separator);
	}

	public static String objectsToString(Object[] obj, String separator) {
		StringBuilder tmp = new StringBuilder();
		 for (int i=0; i < obj.length-1; i++)
		    tmp.append(obj[i] + separator);
		 
		 tmp.append(obj[obj.length-1]);
		 
		 return tmp.toString();

	}

	public static String fixIPAddress(String ipAddress) {
		// repair ipv6, ex: "::ffff:209.191.163.210"
        if (ipAddress.indexOf(":") > -1){
            
            String[] ss = ipAddress.split(":", -1);
            
            ipAddress = ss[ss.length -1];
            
        }
        
        return ipAddress;
	}

}
