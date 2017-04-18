package com.example.demo3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Gps extends Activity {
	
	private Button mGpsTest;
	private Button mGpsRun;
	private TextView mGpsDisplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		
		mGpsTest = (Button)findViewById(R.id.gps_test);
		mGpsTest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openGPSSettings();
			}
		});
		mGpsRun = (Button)findViewById(R.id.gps_run);
		mGpsRun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getLocation();
			}
		});
	}
	private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);
        if (alm
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPSģ������", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Toast.makeText(this, "�뿪��GPS��", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent,0); //��Ϊ������ɺ󷵻ص���ȡ����

    }
	
	private void getLocation()
	{
	// ��ȡλ�ù������
	LocationManager locationManager;
	String serviceName = LOCATION_SERVICE;
	locationManager = (LocationManager) this.getSystemService(serviceName);
	// ���ҵ�������Ϣ
	Criteria criteria = new Criteria();
	criteria.setAccuracy(Criteria.ACCURACY_FINE); // �߾���
	criteria.setAltitudeRequired(false);
	criteria.setBearingRequired(false);
	criteria.setCostAllowed(true);
	criteria.setPowerRequirement(Criteria.POWER_LOW); // �͹���

	String provider = locationManager.getBestProvider(criteria, true); // ��ȡGPS��Ϣ
	Log.e("gg", provider);
	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); // ͨ��GPS��ȡλ��
	updateToNewLocation(location);
	// ���ü��������Զ����µ���Сʱ��Ϊ���N��(1��Ϊ1*1000������д��ҪΪ�˷���)����Сλ�Ʊ仯����N��
//	locationManager.requestLocationUpdates(provider, 100 * 1000, 500,
//	locationListener);
	}
	
	private void updateToNewLocation(Location location) {

        TextView tv1;
        tv1 = (TextView) this.findViewById(R.id.gps_display);
        if (location != null) {
            double  latitude = location.getLatitude();
            double longitude= location.getLongitude();
            tv1.setText("ά�ȣ�" +  latitude+ "\n����" + longitude);
        } else {
            tv1.setText("�޷���ȡ������Ϣ");
        }

    }

}
