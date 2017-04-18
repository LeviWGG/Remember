package com.example.demosl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import android.util.Log;


public class Test {
	private static boolean download_https(String local) {
		File f = new File(local);
		
		Log.d("test","local : "+local);
		if( f.exists()) {
			f.delete();
		}

		try {
			//SSLContext sc = SSLContext.getInstance("TLS");
			//sc.init(null, new TrustManager[] { new MyTrustManager() }, new SecureRandom());
			//HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			//URL myURL = new URL("https://192.168.0.252/petoco/update_data_json");
			URL myURL = new URL("https://192.168.0.252/petoco/update_data_json");
			HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
			HttpsURLConnection conn = (HttpsURLConnection) myURL.openConnection();
			conn.setConnectTimeout(30*1000);
			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream fin = conn.getInputStream();
			FileOutputStream fout = new FileOutputStream(local, true);
			byte[] buffer = new byte[1024];
			int c = 0;
			while ((c = fin.read(buffer)) != -1) {
				fout.write(buffer, 0, c);
			}
			fin.close();
			fout.close();
		}catch ( Exception e ) {
			Log.e("test", "test"+".download_http.failed! e="+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static class MyHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}
