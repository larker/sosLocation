package com.larkersos.soslocation.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetWorkUtil {

	/** û������ */
	public static final int NETWORKTYPE_INVALID = 0;
	/** wap���� */
	// public static final int NETWORKTYPE_WAP = 1;
	/** 2G���� */
	public static final int NETWORKTYPE_2G = 2;
	/** 3G��3G�������磬��ͳ��Ϊ�������� */
	public static final int NETWORKTYPE_3G = 3;
	/** wifi���� */
	public static final int NETWORKTYPE_WIFI = 4;
	
	/**
	 * 	��ȡ����
	 * @param context
	 * @return
	 */
	public static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}
	
	
	/**
	 * ��ȡ����״̬��wifi,wap,2g,3g.
	 * 
	 * @param context
	 *            ������
	 * @return int ����״̬ {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},
	 *         {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}
	 *         <p>
	 *         {@link #NETWORKTYPE_WIFI}
	 */

	public static int getNetWorkType(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		int mNetWorkType = 0;
		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();
			if (type.equalsIgnoreCase("WIFI")) {
				mNetWorkType = NETWORKTYPE_WIFI;
			} else if (type.equalsIgnoreCase("MOBILE")) {
				// String proxyHost = android.net.Proxy.getDefaultHost();
				// mNetWorkType = TextUtils.isEmpty(proxyHost) ?
				// (isFastMobileNetwork(context) ? NETWORKTYPE_3G :
				// NETWORKTYPE_2G) : NETWORKTYPE_WAP;
				mNetWorkType = isFastMobileNetwork(context) ? NETWORKTYPE_3G
						: NETWORKTYPE_2G;
			}
		} else {
			mNetWorkType = NETWORKTYPE_INVALID;
		}

		return mNetWorkType;
	}
	
	
	/**
	 * 
	 * ��ȡ��ǰ����IP
	 * 
	 * @return
	 */
	public static String getLocalIp(Context context) {
		String ipStr = null; 
		// ��ȡ��ǰ����״̬
		int mNetWorkType = getNetWorkType(context);
		if(mNetWorkType == NETWORKTYPE_WIFI){
			ipStr = getWifiAddress(context);
		}else{
			ipStr = getLocalIpAddress();
		}
		return ipStr;
	}
	
	/**
	 * 
	 * ��ȡ��ǰ����IP��wifi��
	 * 
	 * @return
	 */
	public static String getWifiAddress(Context context) {
		String ipStr = null; 
		// �ж�wifi�Ƿ���
	    //��ȡwifi����
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager!= null && wifiManager.isWifiEnabled()){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			// ip����ת��
			ipStr = intToIp(ipAddress);
		}

		return ipStr;
	}

	/**
	 * ip����ת��
	 * @param i
	 * @return
	 */
	private static String intToIp(int i) {
		return (i & 0xFF) + "." +

		((i >> 8) & 0xFF) + "." +

		((i >> 16) & 0xFF) + "." +

		(i >> 24 & 0xFF);

	}

	/**
	 * 
	 * ��ȡ��ǰ����IP��GPRS��
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
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
