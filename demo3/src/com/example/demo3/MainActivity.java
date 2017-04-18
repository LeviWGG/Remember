package com.example.demo3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.example.demo3.R.string;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	public Button mButton1;
	public Button mButton2;
	CommMethod	commMethod =null;
	public EditText mset;
	public EditText meee;
	public static final String LOGTAG = "Log";
	
	public void exchange(){
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, PointActivity.class);
		MainActivity.this.startActivity(intent);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//onBackPressed();
		//ȫ��
//		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,      
//                WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_main);
		
//		new Handler().postDelayed(new Runnable(){    
//	    	  public void run() {    
//	    		  setContentView(R.layout.activity_main);
////	    		  onBackPressed();
//	         }    
//	       }, 1000); 
		
		if(Settings.System.getInt(MainActivity.this.getContentResolver(), "ui", 0)==1){}
		
		mset = (EditText)findViewById(R.id.efm);
		meee = (EditText)findViewById(R.id.eee);
		commMethod = new CommMethod(MainActivity.this);
		final AgentApplication application = (AgentApplication) getApplication();
		application.addActivity(MainActivity.this);
		 
		mButton1 = (Button)findViewById(R.id.button1);
		mButton2 = (Button)findViewById(R.id.button2);
		mButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent i = new Intent();
//				i.setClass(MainActivity.this, ModifyPhoneInfoActivity.class);
//				MainActivity.this.startActivity(i);
				//���������ַŴ�1000������ʾ�ڰ�ť��
//				LayoutInflater inflater = getLayoutInflater();
//				   final View layout = inflater.inflate(R.layout.fmset,
//						   (ViewGroup) findViewById(R.id.dialog));
//
//				   new AlertDialog.Builder(MainActivity.this).setView(layout)
//				     .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							// TODO Auto-generated method stub
//							EditText msset;
//							msset = (EditText) layout.findViewById(R.id.efm);
//							
//							String a = (msset.getText().toString());
//							Log.e(LOGTAG,"a = "+a);
//							int set = (int) (Double.parseDouble(a)*1000);
//							mButton1.setText(set+"");
//						}
//					})
//				     .setNegativeButton("ȡ��", null).show();
				//�ػ���
//				Intent newIntent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
//				newIntent.putExtra("android.intent.extra.KEY_CONFIRM", false);
//		        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		        startActivity(newIntent);
//				Settings.System.putString(getContentResolver(),
//		                Settings.System.NEXT_ALARM_FORMATTED,
//		                "���� ����2:06");
//				Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
//		        alarmChanged.putExtra("alarmSet", true);
//		        getApplicationContext().sendBroadcast(alarmChanged);
				
				new Thread(){ 
				     public void run(){
				    	 while (true) {
				    		 Settings.System.putInt(MainActivity.this.getContentResolver(), "needRestart",1);
				    		 SystemClock.sleep(5000);
				    	 }
				    	 
				     }
				}.start();
			}
		});
		
		mButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(MainActivity.this, GpsGoogle.class);
//				MainActivity.this.startActivity(intent);
				
//				change();
				
				if(Settings.System.getString(MainActivity.this.getContentResolver(), "appName")==null){
					Settings.System.putString(MainActivity.this.getContentResolver(), "appName","com.example.demo3");
				}else {
					Settings.System.putString(MainActivity.this.getContentResolver(), "appName",null);
				}
				
//				new Thread(){ 
//				     public void run(){ 
//				    	 while (true) {
//				    		 Settings.System.putInt(MainActivity.this.getContentResolver(), "needRestart",0);
//				    		 Settings.System.putString(MainActivity.this.getContentResolver(), "appName","com.example.hellow");
//				    		 SystemClock.sleep(10000);
//				    		 if(Settings.System.getInt(MainActivity.this.getContentResolver(), "needRestart",0)==1){
//				    			try{
//				    				if(Settings.System.getString(MainActivity.this.getContentResolver(), "appName") != null){
//				    					Intent intent = MainActivity.this.getPackageManager().getLaunchIntentForPackage(
//				    							Settings.System.getString(MainActivity.this.getContentResolver(), "appName"));
//					 					startActivity(intent);
//				    				}
//				 				}catch(Exception e){
//				 					e.printStackTrace();
//				 				}
//				    		 }
//				    		 
//				    	 } 
//				     } 
//				}.start();
				
//				application.onTerminate();
				
//				String a = (meee.getText().toString());
//				int set = Integer.parseInt(a);
//				mButton1.setText(set);
				//����ϵͳ����
//				Intent mIntent = new Intent();
//				ComponentName comp = new ComponentName("com.android.settings", 
//				"com.android.settings.Settings");
//				mIntent.setComponent(comp);
//				mIntent.setAction("android.intent.action.VIEW");
//				startActivity(mIntent);
				
				//������б�
//				try {
//					Intent intent= new Intent();        
//					intent.setAction("android.intent.action.VIEW");    
//					Uri content_url = Uri.parse("http://��ֹ����");   
//					intent.setData(content_url);  
//					startActivity(intent);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				
				
				//��ݰ�������apk
//				try{
//					Intent intent = MainActivity.this.getPackageManager().getLaunchIntentForPackage("com.caf.fmradio");
//					startActivity(intent);
//				}catch(Exception e){
//					Toast.makeText(getApplicationContext(), "û�а�װ", Toast.LENGTH_LONG).show();
//				}
				
				//onBackPressed();
				
//				Toast.makeText(getApplicationContext(), android.os.Build.FORGE_PULLDOWNMENU_DISABLE, Toast.LENGTH_LONG).show();
//				Toast.makeText(getApplicationContext(), android.os.Build.FORGE_PULLDOWNMENU_DISABLE, Toast.LENGTH_LONG).show();
				
//				Toast.makeText(getApplicationContext(), 
//						MainActivity.this.getString(R.string.hello_world)+"11" ,
//						Toast.LENGTH_LONG ).show();
				
//				int a;
//				Settings.System.putInt(MainActivity.this.getContentResolver(), "ui_horizontal",2);
//				
//				a = Settings.System.getInt(MainActivity.this.getContentResolver(), "ui_horizontal", 0);
////				if(Settings.System.getInt(MainActivity.this.getContentResolver(), "ui", 0)==1){}
//				Toast.makeText(getApplicationContext(), a+"", Toast.LENGTH_SHORT).show();
				
//				 Intent intent = new Intent();  
//		            intent.setAction("com.car.fmapp.vol.resume");  
////		            intent.putExtra("name", "xiazdong");  
//		            MainActivity.this.sendBroadcast(intent);  
//		            Toast.makeText(getApplicationContext(), "���͹㲥�ɹ�", Toast.LENGTH_SHORT).show();
			}
		});
	}

	
//	@Override
//	public void onAttachedToWindow() {
//		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//		super.onAttachedToWindow();
//	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_HOME) {
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	
	public static void change(){
		File file = new File("/system/build.prop");  
		File dest = new File("/persist/build.prop");  
		try { 
			BufferedWriter writer  = new BufferedWriter(new FileWriter(dest)); 
		    BufferedReader reader = new BufferedReader(new FileReader(file));  
		     
		    String line = reader.readLine();  
//		    while(line!=null){  
//		    	if(line.indexOf(ro + "=") != -1){
//		    		int w  =line.indexOf("=");
//		    		System.out.print(w);
//		    		line = line.substring(0, w+1) + value;
//		    		
//		    	}
//		    	writer.write(line + "\r\n"); 
//		        line = reader.readLine();  
//		    }
		    writer.write(line + "\r\n");
		    writer.flush();  
		    reader.close();  
		    writer.close();  
		} catch (FileNotFoundException e) {  
		    e.printStackTrace();  
		} catch (IOException e) {  
		    e.printStackTrace();  
		} 
		
	}
	
}
