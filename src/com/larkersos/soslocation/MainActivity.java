package com.larkersos.soslocation;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.larkersos.soslocation.utils.NetWorkUtil;
import com.larkersos.soslocation.utils.UserLocationFound;

public class MainActivity extends Activity implements SensorListener {
	final String tag = "sosEyes";
	
	// ������ϢSensor
	SensorManager sm = null;

	// ��γ��
	// ��������
    UserLocationFound locationFound;
	private double latitude = 0.0;
	private double longitude = 0.0;
	TextView sosLongitude = null;
	TextView sosLatitude = null;
	// ��ǰIP
	TextView sosIp = null;
	// �绰����
	TelephonyManager mTelephonyMgr;
	TextView sosPhoneNum = null;

	TextView View1 = null;
	TextView View2 = null;
	TextView View3 = null;
	TextView View4 = null;
	TextView View5 = null;
	TextView View6 = null;
	TextView View7 = null;
	TextView View8 = null;
	TextView View9 = null;
	TextView View10 = null;
	TextView View11 = null;
	TextView View12 = null;
	
	// ˢ��
	Button btnRefresh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);

		setContentView(R.layout.activity_main);

		// ������ϢSensor
		View1 = (TextView) findViewById(R.id.edt1);
		View2 = (TextView) findViewById(R.id.edt2);
		View3 = (TextView) findViewById(R.id.edt3);
		View4 = (TextView) findViewById(R.id.edt4);
		View5 = (TextView) findViewById(R.id.edt5);
		View6 = (TextView) findViewById(R.id.edt6);
		View7 = (TextView) findViewById(R.id.edt7);
		View8 = (TextView) findViewById(R.id.edt8);
		View9 = (TextView) findViewById(R.id.edt9);
		View10 = (TextView) findViewById(R.id.edt10);
		View11 = (TextView) findViewById(R.id.edt11);
		View12 = (TextView) findViewById(R.id.edt12);


		// LBS
		sosLongitude = (TextView) findViewById(R.id.sosLongitude);
		sosLatitude = (TextView) findViewById(R.id.sosLatitude);
		
		sosIp = (TextView) findViewById(R.id.sosIp);
		
		// Phone
		sosPhoneNum = (TextView) findViewById(R.id.sosPhoneNum);
		
		//��ȡ���ݣ�5S�ȴ���λ
		refreshViewData(1000*5);
		// ˢ�°�ť
		btnRefresh = (Button)findViewById(R.id.refresh);
		btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// ˢ������ 60*10S�ȴ���λ
				refreshViewData(1000*60*10);
			}
		});

	}
	
	
	/**
	 * ��ȡ�����绰
	 * 
	 * @return
	 */
	private void refreshViewData(int  maxWait) {
		// ��ȡ��γ��
		getLocation(maxWait);
		sosLongitude.setText(String.valueOf(latitude));
		sosLatitude.setText(String.valueOf(longitude));
		
		// ��ȡ�����绰
		sosPhoneNum.setText(getPhoneNumber());
		
		// IP
		sosIp.setText(NetWorkUtil.getLocalIp(this));
		
	}
	
	/**
	 * ��ȡ�����绰
	 * 
	 * @return
	 */
	private String getPhoneNumber() {
		// �����绰����
		if(mTelephonyMgr == null){
			mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		}
		
		// ����
		String phoneType = "";
//		switch(mTelephonyMgr.getPhoneType()){
//			case TelephonyManager.PHONE_TYPE_CDMA:  phoneType = " (CDMA)";
//			case TelephonyManager.PHONE_TYPE_GSM:  phoneType = " (GMS)";
//			case TelephonyManager.PHONE_TYPE_SIP:  phoneType = " (SIP)";
//		}
		// ����
		switch(mTelephonyMgr.getNetworkType()){
			case TelephonyManager.NETWORK_TYPE_GPRS:  phoneType = " (GPRS)";
			case TelephonyManager.NETWORK_TYPE_EDGE:  phoneType = " (EDGE)";
			case TelephonyManager.NETWORK_TYPE_UMTS:  phoneType = " (UMTS)";
			case TelephonyManager.NETWORK_TYPE_HSDPA:  phoneType = " (HSDPA)";
			case TelephonyManager.NETWORK_TYPE_HSUPA:  phoneType = " (HSUPA)";
			case TelephonyManager.NETWORK_TYPE_CDMA:  phoneType = " (CDMA)";
			case TelephonyManager.NETWORK_TYPE_EVDO_0:  phoneType = " (EVDO)";
		}
		
		String networkOperatorName = mTelephonyMgr.getNetworkOperatorName();
		Log.d("getSimOperatorName:", mTelephonyMgr.getSimOperatorName());
		
		return mTelephonyMgr.getLine1Number() ==null ? "" : mTelephonyMgr.getLine1Number()  + " " +networkOperatorName + phoneType;
	}

	/**
	 * ��ȡ��ǰλ��
	 * 
	 * @return
	 */
	private void getLocation(int maxWait) {
		if(locationFound == null){
			locationFound = new UserLocationFound(this,maxWait);
		}
		// ��ȡ����
		if (locationFound != null) {
			locationFound.setLatitudeAndLongitude();
			latitude = locationFound.getLatitude(); 
			longitude = locationFound.getLongitude(); 
		}
	}
	
	public void onSensorChanged(int sensor, float[] values) {
		synchronized (this) {
			String str = "X��" + values[0] + "��Y��" + values[1] + "��Z��"
					+ values[2];
			switch (sensor) {
			case Sensor.TYPE_ACCELEROMETER:
				View1.setText("���ٶȣ�" + str);
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				View2.setText("�ų���" + str);
				break;
			case Sensor.TYPE_ORIENTATION:
				View3.setText("��λ��" + str);
				break;
			case Sensor.TYPE_GYROSCOPE:
				View4.setText("�����ǣ�" + str);
				break;
			case Sensor.TYPE_LIGHT:
				View5.setText("���ߣ�" + str);
				break;
			case Sensor.TYPE_PRESSURE:
				View6.setText("ѹ����" + str);
				break;
			case Sensor.TYPE_TEMPERATURE:
				View7.setText("�¶ȣ�" + str);
				break;
			case Sensor.TYPE_PROXIMITY:
				View8.setText("���룺" + str);
				break;
			case Sensor.TYPE_GRAVITY:
				View9.setText("������" + str);
				break;
			case Sensor.TYPE_LINEAR_ACCELERATION:
				View10.setText("���Լ��ٶȣ�" + str);
				break;
			case Sensor.TYPE_ROTATION_VECTOR:
				View11.setText("��תʸ����" + str);
				break;
			default:
				View12.setText("NORMAL��" + str);
				break;
			}
		}
	}

	public void onAccuracyChanged(int sensor, int accuracy) {
		Log.d(tag, "onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sm.registerListener(this,
				Sensor.TYPE_ACCELEROMETER | Sensor.TYPE_MAGNETIC_FIELD
						| Sensor.TYPE_ORIENTATION | Sensor.TYPE_GYROSCOPE
						| Sensor.TYPE_LIGHT | Sensor.TYPE_PRESSURE
						| Sensor.TYPE_TEMPERATURE | Sensor.TYPE_PROXIMITY
						| Sensor.TYPE_GRAVITY | Sensor.TYPE_LINEAR_ACCELERATION
						| Sensor.TYPE_ROTATION_VECTOR,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		sm.unregisterListener(this);
		super.onStop();
	}

}
