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
            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent,0); //此为设置完成后返回到获取界面

    }
	
	private void getLocation()
	{
	// 获取位置管理服务
	LocationManager locationManager;
	String serviceName = LOCATION_SERVICE;
	locationManager = (LocationManager) this.getSystemService(serviceName);
	// 查找到服务信息
	Criteria criteria = new Criteria();
	criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
	criteria.setAltitudeRequired(false);
	criteria.setBearingRequired(false);
	criteria.setCostAllowed(true);
	criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

	String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
	Log.e("gg", provider);
	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); // 通过GPS获取位置
	updateToNewLocation(location);
	// 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
//	locationManager.requestLocationUpdates(provider, 100 * 1000, 500,
//	locationListener);
	}
	
	private void updateToNewLocation(Location location) {

        TextView tv1;
        tv1 = (TextView) this.findViewById(R.id.gps_display);
        if (location != null) {
            double  latitude = location.getLatitude();
            double longitude= location.getLongitude();
            tv1.setText("维度：" +  latitude+ "\n经度" + longitude);
        } else {
            tv1.setText("无法获取地理信息");
        }

    }

}
