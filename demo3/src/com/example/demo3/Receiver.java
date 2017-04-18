package com.example.demo3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
	private MainActivity mMain;
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
//		String name = intent.getExtras().getString("name");  
//        Log.i("Recevier1", "接收到:"+name);
//		Intent intent1 = new Intent(arg0, PointActivity.class);
//		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		arg0.startActivity(intent1);
//		Toast.makeText(arg0, android.os.Build.FORGE_PULLDOWNMENU_DISABLE, Toast.LENGTH_SHORT).show();
		Toast.makeText(arg0, "广播收到", Toast.LENGTH_SHORT).show();
	}

}
