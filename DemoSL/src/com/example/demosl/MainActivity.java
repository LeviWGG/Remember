package com.example.demosl;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.example.demosl.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends Activity {
	Context mcontext;
//    float audioMaxVolumn;
//    float volumnCurrent;
//    float volumnRatio;
	SoundPool sp = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
	int id = sp.load("/system/media/audio/ui/stickykeydown.ogg", 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.activity_main);
        
        initSound();

    }
    
    public void onTest(){
    	String s="abc.def.hig";
    	String[] list =s.split("\\.") ;
    	boolean b = Arrays.asList(list).contains("df");
//    	for (int i = 0 ; i <list.length ; i++ ) {
//    		Log.d("test",list [i]);
//    	}
    	Log.d("test","boolean = "+b);
    }
    
    public void initSound() {
    	
//    	AudioManager am = (AudioManager) this 
//                .getSystemService(Context.AUDIO_SERVICE);
//        audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
//        volumnRatio = volumnCurrent / audioMaxVolumn;
    	
//    	sounddata = new HashMap<Integer, Integer>();
//        sounddata.put(1, id);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override 
            public void onLoadComplete(SoundPool sound,int sampleId,int status){
                Toast.makeText(MainActivity.this, 
                        "ÒôÐ§¼ÓÔØÍê³É£¡",
                        Toast.LENGTH_SHORT).show();
          }
      });
    }
    
    
    public void httpsLoad() throws IOException{
    	//URL myURL = new URL("https://www.baidu.com");
    	URL myURL = new URL("https://192.168.0.252/petoco/"); 
        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象 
    	
    	HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
    	
        HttpsURLConnection httpsConn = (HttpsURLConnection) myURL 
                .openConnection(); 
        // 取得该连接的输入流，以读取响应内容 
        InputStreamReader insr = new InputStreamReader(httpsConn 
                .getInputStream()); 
        // 读取服务器的响应内容并显示 
        int respInt = insr.read(); 
        while (respInt != -1) { 
            System.out.print((char) respInt); 
            respInt = insr.read();
            }
        
//        InputStream in = httpsConn.getInputStream();
        //copyInputStreamToOutputStream(in, System.out);
    }
    
    
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
    
    
    public static void  Dhttps(String local) {
    	try {
    		  CertificateFactory cf = CertificateFactory.getInstance("X.509");
    		  FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory().getPath()+"/meig.crt");
    		  // uwca.crt 打包在 asset 中，该证书可以从https://itconnect.uw.edu/security/securing-computer/install/safari-os-x/下载
    		  InputStream caInput = new BufferedInputStream(fis);
    		  final Certificate ca;
    		  try {
    		      ca = cf.generateCertificate(caInput);
    		      Log.i("Longer", "ca=" + ((X509Certificate) ca).getSubjectDN());
    		      Log.i("Longer", "key=" + ((X509Certificate) ca).getPublicKey());
    		  } finally {
    		      caInput.close();
    		  }
    		  // Create an SSLContext that uses our TrustManager
    		  SSLContext context = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
    		  context.init(null, new TrustManager[]{
    		          new X509TrustManager() {
    		              @Override
    		              public void checkClientTrusted(X509Certificate[] chain,
    		                      String authType)
    		                      throws CertificateException {

    		              }

    		              @Override
    		              public void checkServerTrusted(X509Certificate[] chain,
    		                      String authType)
    		                      throws CertificateException {
    		                  for (X509Certificate cert : chain) {

    		                      // Make sure that it hasn't expired.
    		                      cert.checkValidity();

    		                      // Verify the certificate's public key chain.
    		                      try {
    		                          cert.verify(((X509Certificate) ca).getPublicKey());
    		                      } catch (Exception e) {
    		                          e.printStackTrace();
    		                      }
    		                  }
    		              }

    		              @Override
    		              public X509Certificate[] getAcceptedIssuers() {
    		                  return new X509Certificate[0];
    		              }
    		          }
    		  }, null);

    		 URL myURL = new URL("https://192.168.0.252/petoco/update_data_json");
  			//HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
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
    		} catch (Exception e) {
    		  e.printStackTrace();
    		}

    }
    
    
    public void DDhttps(String local) {
    	try {
    		  CertificateFactory cf = CertificateFactory.getInstance("X.509");
    		  // uwca.crt 打包在 asset 中，该证书可以从https://itconnect.uw.edu/security/securing-computer/install/safari-os-x/下载
    		  InputStream caInput = new BufferedInputStream(getAssets().open("uwca.crt"));
    		  final Certificate ca;
    		  try {
    		      ca = cf.generateCertificate(caInput);
    		      Log.i("Longer", "ca=" + ((X509Certificate) ca).getSubjectDN());
    		      Log.i("Longer", "key=" + ((X509Certificate) ca).getPublicKey());
    		  } finally {
    		      caInput.close();
    		  }
    		  // Create an SSLContext that uses our TrustManager
    		  SSLContext context = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
    		  context.init(null, new TrustManager[]{
    		          new X509TrustManager() {
    		              @Override
    		              public void checkClientTrusted(X509Certificate[] chain,
    		                      String authType)
    		                      throws CertificateException {

    		              }

    		              @Override
    		              public void checkServerTrusted(X509Certificate[] chain,
    		                      String authType)
    		                      throws CertificateException {
    		                  for (X509Certificate cert : chain) {

    		                      // Make sure that it hasn't expired.
    		                      cert.checkValidity();

    		                      // Verify the certificate's public key chain.
    		                      try {
    		                          cert.verify(((X509Certificate) ca).getPublicKey());
    		                      } catch (Exception e) {
    		                          e.printStackTrace();
    		                      }
    		                  }
    		              }

    		              @Override
    		              public X509Certificate[] getAcceptedIssuers() {
    		                  return new X509Certificate[0];
    		              }
    		          }
    		  }, null);

    		  URL url = new URL("https://certs.cac.washington.edu/CAtest/");
    		  HttpsURLConnection urlConnection =
    		          (HttpsURLConnection)url.openConnection();
    		  urlConnection.setSSLSocketFactory(context.getSocketFactory());
    		  InputStream in = urlConnection.getInputStream();
    		} catch (Exception e) {
    		  e.printStackTrace();
    		}
    }
    
    public void KShttps(String local) {
    	try {
    		  CertificateFactory cf = CertificateFactory.getInstance("X.509");
    		  // uwca.crt 打包在 asset 中，该证书可以从https://itconnect.uw.edu/security/securing-computer/install/safari-os-x/下载
    		  InputStream caInput = new BufferedInputStream(getAssets().open("server.crt"));
    		  Certificate ca;
    		  try {
    		      ca = cf.generateCertificate(caInput);
    		      Log.i("Longer", "ca=" + ((X509Certificate) ca).getSubjectDN());
    		      Log.i("Longer", "key=" + ((X509Certificate) ca).getPublicKey());
    		  } finally {
    		      caInput.close();
    		  }

    		  // Create a KeyStore containing our trusted CAs
    		  String keyStoreType = KeyStore.getDefaultType();
    		  KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    		  keyStore.load(null, null);
    		  keyStore.setCertificateEntry("ca", ca);

    		  // Create a TrustManager that trusts the CAs in our KeyStore
    		  String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    		  TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    		  tmf.init(keyStore);

    		  // Create an SSLContext that uses our TrustManager
    		  SSLContext context = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
    		  context.init(null, tmf.getTrustManagers(), null);

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
    		} catch (Exception e) {
    		  e.printStackTrace();
    		}
    }
    
    
    public void KKhttps() {
    	try {
    		  CertificateFactory cf = CertificateFactory.getInstance("X.509");
    		  // uwca.crt 打包在 asset 中，该证书可以从https://itconnect.uw.edu/security/securing-computer/install/safari-os-x/下载
    		  InputStream caInput = new BufferedInputStream(getAssets().open("meig.crt"));
    		  Certificate ca;
    		  try {
    		      ca = cf.generateCertificate(caInput);
    		      Log.i("Longer", "ca=" + ((X509Certificate) ca).getSubjectDN());
    		      Log.i("Longer", "key=" + ((X509Certificate) ca).getPublicKey());
    		  } finally {
    		      caInput.close();
    		  }

    		  // Create a KeyStore containing our trusted CAs
    		  String keyStoreType = KeyStore.getDefaultType();
    		  KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    		  keyStore.load(null, null);
    		  keyStore.setCertificateEntry("ca", ca);

    		  // Create a TrustManager that trusts the CAs in our KeyStore
    		  String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    		  TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    		  tmf.init(keyStore);

    		  // Create an SSLContext that uses our TrustManager
    		  SSLContext context = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
    		  context.init(null, tmf.getTrustManagers(), null);

    		  URL url = new URL("https://certs.cac.washington.edu/CAtest/");
    		  HttpsURLConnection urlConnection =
    		          (HttpsURLConnection)url.openConnection();
    		  urlConnection.setSSLSocketFactory(context.getSocketFactory());
    		  InputStream in = urlConnection.getInputStream();
    		} catch (Exception e) {
    		  e.printStackTrace();
    		}
    }
    
    
    public static class MyHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
    
//    private static class MyTrustManager implements X509TrustManager {
//
//		@Override
//		public void checkClientTrusted(X509Certificate[] chain, String authType)
//				throws CertificateException {
//
//		}
//
//		@Override
//		public void checkServerTrusted(X509Certificate[] chain, String authType)
//				throws CertificateException {
//			for (X509Certificate cert : chain) {
//
//                // Make sure that it hasn't expired.
//                cert.checkValidity();
//
//                // Verify the certificate's public key chain.
//                try {
//                    cert.verify(((X509Certificate) ca).getPublicKey());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//		}
//
//		@Override
//		public X509Certificate[] getAcceptedIssuers() {
//			return new X509Certificate[0];
//		}
//	}
    
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	Log.d("test",""+keyCode);
    	//onTest();
//    	try {
//			httpsLoad();
//			Log.d("test","https");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                	//httpsLoad();
                	//Dhttps(Environment.getExternalStorageDirectory().getPath()+"/update_data_json");
//                	KShttps(Environment.getExternalStorageDirectory().getPath()+"/update_data_json");
                	//KKhttps();
                	System.out.println("run22222 : ");
                	download_https(Environment.getExternalStorageDirectory().getPath()+"/update_data_json");
                	Log.d("test","https");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    	
//    	AudioManager am = (AudioManager) this
//                .getSystemService(Context.AUDIO_SERVICE);
//    	float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
//    	float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
//    	float volumnRatio = volumnCurrent / audioMaxVolumn;
//    	
//    	sp.play(id, volumnRatio, volumnRatio, 1, 0, 1);
    	
    	
//    	final MediaPlayer mediaPlayer = new MediaPlayer(); 
//    	if (mediaPlayer.isPlaying()) {  
//    	   mediaPlayer.reset();
//    	}
//    	try {
//			mediaPlayer.setDataSource("/system/media/audio/ui/KeypressStandard.ogg");
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
//			mediaPlayer.prepare();
//			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//				@Override
//                public void onCompletion(MediaPlayer mp) {
//                	mediaPlayer.release();
//                }
//            });
//			mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {// ´íÎó´¦ÀíÊÂ¼þ  
//		         @Override 
//		         public boolean onError(MediaPlayer player, int arg1, int arg2) {  
//		        	 mediaPlayer.release();
//		        	 Log.e("test","error");
//		        	 return false;  
//		         }  
//			});
//			mediaPlayer.start();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	
    	
    	
//    	if(keyCode==0){
//    		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);  
//    		  
//    		mHomeIntent.addCategory(Intent.CATEGORY_HOME);  
//    		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  
//    		                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);  
//    		startActivity(mHomeIntent);
//    	}
    	return false;
    }
}
