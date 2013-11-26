package com.larkersos.soslocation.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class Util {
	/**
	 * 
	 * 获取当前网络IP（wifi）
	 * 
	 * @return
	 */
	public String getWifiAddress(WifiManager wifiManager) {
		String ipStr = null; 
		// 判断wifi是否开启
		if(wifiManager!= null && wifiManager.isWifiEnabled()){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			// ip解析转换
			ipStr = intToIp(ipAddress);
		}

		return ipStr;
	}

	/**
	 * ip解析转换
	 * @param i
	 * @return
	 */
	private String intToIp(int i) {
		return (i & 0xFF) + "." +

		((i >> 8) & 0xFF) + "." +

		((i >> 16) & 0xFF) + "." +

		(i >> 24 & 0xFF);

	}

	/**
	 * 
	 * 获取当前网络IP（GPRS）
	 * 
	 * @return
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();

				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}

		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}

		return null;

	}
}
