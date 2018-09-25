package com.ppu.fmc.util;

import java.util.List;
import java.util.StringTokenizer;

import com.ppu.fmc.exception.IpLocationNotFoundException;
import com.ppu.fmc.firepower.model.IpLocation;

public class CSVUtils {
	public static String getLocation(List<?> list, String ipAddress) throws IpLocationNotFoundException {
		if (StringUtils.isEmpty(ipAddress) || list == null || list.size() < 1)
			return "";

		// diambil
		StringTokenizer token = new StringTokenizer(ipAddress, ".");

		if (token.countTokens() != 4) {
			throw new IllegalArgumentException("IP address must be in the format 'xxx.xxx.xxx.xxx'");
		}

		int dots = 0;
		String byte1 = "";
		String byte2 = "";
		String byte3 = "";
		while (token.hasMoreTokens()) {
			++dots;

			if (dots == 1) {
				byte1 = token.nextToken();
			} else if (dots == 2) {
				byte2 = token.nextToken();
			} else if (dots == 3) {
				byte3 = token.nextToken();
			} else
				break;
		}

		String client3 = byte1 + "." + byte2 + "." + byte3;

		for (int i = 0; i < list.size(); i++) {
			IpLocation _obj = (IpLocation) list.get(i);

			StringTokenizer _token = new StringTokenizer(_obj.getIp(), ".");

			// segmen class A handler
			if (_token.countTokens() == 3) {
				if (_obj.getIp().equals(client3)) {
					return _obj.getLabel();
				}
			} else if (_token.countTokens() == 4) {
				if (_obj.getIp().equals(ipAddress)) {
					return _obj.getLabel();
				}
			}

		}

		throw new IpLocationNotFoundException(ipAddress);
	}
}
