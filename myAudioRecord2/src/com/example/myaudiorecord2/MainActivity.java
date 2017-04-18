package com.example.myaudiorecord2;  
 
import android.app.Activity;  
import android.content.Context;
import android.media.AudioFormat;  
import android.media.AudioManager;  
import android.media.AudioRecord;  
import android.media.AudioTrack;  
import android.media.MediaRecorder;  
import android.os.Bundle;  
import android.util.Log;
import android.view.View;  
import android.widget.Button;  
import android.widget.Toast;  

public class MainActivity extends Activity {  
   /** Called when the activity is first created. */  
    Button btnRecord, btnPlay, btnStop, btnExit;  
    boolean isRecording = false;//是否录放的标记   
    static final int frequency = 44100;  
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;  
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;  
    int recBufSize,playBufSize;  
    AudioRecord audioRecord;  
    AudioTrack audioTrack;  
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  

        recBufSize = AudioRecord.getMinBufferSize(frequency,  
                channelConfiguration, audioEncoding);  

        playBufSize=AudioTrack.getMinBufferSize(frequency,  
                channelConfiguration, audioEncoding);  
        // -----------------------------------------   
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,  
                channelConfiguration, audioEncoding, recBufSize);  
  
        audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, frequency,  
                channelConfiguration, audioEncoding,  
                playBufSize, AudioTrack.MODE_STREAM);  
        
        Log.d("audiorecond", "jiangning MediaRecorder.AudioSource.MIC = " + MediaRecorder.AudioSource.MIC + 
        		"frequency = " + frequency + 
        		"channelConfiguration = " +channelConfiguration +
        		"audioEncoding = " + audioEncoding +
        		"recBufSize = " + recBufSize);
        //------------------------------------------   
        btnRecord = (Button) this.findViewById(R.id.btnRecord);  
        btnRecord.setOnClickListener(new ClickEvent());  
        btnPlay = (Button) this.findViewById(R.id.btnPlay);  
        btnPlay.setOnClickListener(new ClickEvent());  
        btnStop = (Button) this.findViewById(R.id.btnStop);  
        btnStop.setOnClickListener(new ClickEvent());  
        btnExit = (Button) this.findViewById(R.id.btnExit);  
        btnExit.setOnClickListener(new ClickEvent());  
    }  
 
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        android.os.Process.killProcess(android.os.Process.myPid());  
    }  
  
    class ClickEvent implements View.OnClickListener {  
  
        @Override  
        public void onClick(View v) {  
            if (v == btnRecord) {  
                isRecording = true;  
                new RecordThread().start();// 开一条线程边录边放   
            } else if (v == btnPlay) {  
                isRecording = true; 
                new PlayThread().start();
            }
            else if (v == btnStop) {  
                isRecording = false;  
            } else if (v == btnExit) {  
                isRecording = false;  
                MainActivity.this.finish();  
            }  
        }  
    }  
  
    class RecordThread extends Thread {  
        public void run() {  
            try {  
                byte[] buffer = new byte[recBufSize];  
                audioRecord.startRecording();//开始录制   
                audioTrack.play();//开始播放   
                  
               while (isRecording) {  
                   //从MIC保存数据到缓冲区   
                    int bufferReadResult = audioRecord.read(buffer, 0,  
                          recBufSize);              	   
                   byte[] tmpBuf = new byte[bufferReadResult];  
                   System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);  
                   //Log.d("audioRecond", "jiangning buffer = " + buffer);
                    //写入数据即播放   
                   audioTrack.write(tmpBuf, 0, tmpBuf.length);  
                }  
               audioTrack.stop();  
               audioRecord.stop();  
          } catch (Throwable t) {  
               Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show(); 
            }  
        }  
    };  
    
    class PlayThread extends Thread {  
        public void run() {  
            try {  
                byte[] buffer = new byte[recBufSize];  
                audioTrack.play();//开始播放   
                  
               while (isRecording) {  
                   //从MIC保存数据到缓冲区   
                    int bufferReadResult = audioRecord.read(buffer, 0,  
                          recBufSize);  

                    byte[] tmpBuf = new byte[bufferReadResult];  
                   System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);  
                    //写入数据即播放   
                   audioTrack.write(tmpBuf, 0, tmpBuf.length);  
                }  
               audioTrack.stop();  
               audioRecord.stop();  
          } catch (Throwable t) {  
               Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show(); 
            }  
        }  
    }; 
}  
