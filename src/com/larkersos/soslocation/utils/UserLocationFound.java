package com.larkersos.soslocation.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * LBS
 * @author cns
 *
 */
public class UserLocationFound {
	private int latitude = 0;
	private int longitude = 0;
	private Context context;

	private LocationManager locationManager;
	private String provider;
	private Location location;
	public long waitMaxMillis = 0;

	public UserLocationFound(Context context,long waitMax) {
		this.context = context;
		this.waitMaxMillis = waitMax;
		setLatitudeAndLongitude();
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLongitude() {
		return longitude;
	}

	/**
	 * ��γ������
	 */
	public void setLatitudeAndLongitude() {
		// TODO Auto-generated method stub
		// ��ȡ LocationManager ����
		locationManager = (LocationManager) (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// locationManager.setTestProviderEnabled("gps", true);
		// ��ȡ Location Provider
		getProvider();
		if(provider !=null){
			// ���δ����λ��Դ���� GPS ���ý���
			openGPS();
			// ��ȡλ��
			location = locationManager.getLastKnownLocation(provider);
			// ��ʾλ����Ϣ�����ֱ�ǩ
			updateWithNewLocation(location);
			// ע������� locationListener ���� 2 �� 3 ���������Կ��ƽ��� gps ��Ϣ��Ƶ���Խ�ʡ�������� 2 ������Ϊ���룬
			// ��ʾ���� listener �����ڣ��� 3 ������Ϊ�� , ��ʾλ���ƶ�ָ�������͵��� listener
		}

	}

	// Gps ��Ϣ������
	private final LocationListener locationListener = new LocationListener() {

		// λ�÷����ı�����
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		// provider ���û��رպ����
		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		// provider ���û����������
		public void onProviderEnabled(String provider) {

		}

		// provider ״̬�仯ʱ����
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void updateWithNewLocation(Location location2) {
		// ��ǰλ��
		if (location == null) {
			// �����̶߳�λ
//			new Thread(){
//				public void run(){
					long waitMillis= 0 ;
					long begin = System.currentTimeMillis();
					while (location == null && waitMillis <= waitMaxMillis) {
						locationManager.requestLocationUpdates(provider, 2000, (float) 0.1,
								locationListener);
						waitMillis = System.currentTimeMillis() - begin;
					}
//				}
//			}.start();
		}

		// ��ȡ����
		if (location != null) {
			latitude = ((int) (location.getLatitude() * 100000));
			longitude = (int) (location.getLongitude() * 100000);
			// changeFormat(latitude,longitude);
		} else {
			latitude = 0;
			longitude = 0;
		}
	}

	private void openGPS() {
		// TODO Auto-generated method stub

		if (locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
				|| locationManager
						.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
			Toast.makeText(context, " λ��Դ�����ã� ", Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(context, " λ��Դδ���ã�", Toast.LENGTH_SHORT).show();
	}

	private void getProvider() {
		// TODO Auto-generated method stub
		// ����λ�ò�ѯ����
		Criteria criteria = new Criteria();
		// ��ѯ���ȣ���
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// �Ƿ��ѯ��������
		criteria.setAltitudeRequired(false);
		// �Ƿ��ѯ��λ�� : ��
		criteria.setBearingRequired(false);
		// �Ƿ������ѣ���
		criteria.setCostAllowed(false);
		// ����Ҫ�󣺵�
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// ��������ʵķ��������� provider ���� 2 ������Ϊ true ˵�� , ���ֻ��һ�� provider ����Ч�� , �򷵻ص�ǰ
		// provider
		provider = locationManager.getBestProvider(criteria, true);
	}
}
