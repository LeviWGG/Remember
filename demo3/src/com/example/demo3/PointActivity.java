package com.example.demo3;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PointActivity extends Activity {
	private LinearLayout pointer_linear;
	private PointerView pointer;
	
	public TextView shu;
	public ImageView tu; 
	
//	AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
	
	public Button mButton3;
	public Button mButton4;
	public Button mButton5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pointer);
		
		shu = (TextView)findViewById(R.id.shu);
		tu = (ImageView)findViewById(R.id.tu);
		AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
		int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
		if(currentVolume == 0){
			tu.setBackgroundResource(R.drawable.mute_state);
		}else if(currentVolume <= 5){
			tu.setBackgroundResource(R.drawable.min);
		}else if(currentVolume <= 10){
			tu.setBackgroundResource(R.drawable.medium);
		}else{
			tu.setBackgroundResource(R.drawable.max);
		}
		
		final AgentApplication application = (AgentApplication) getApplication();
		application.addActivity(PointActivity.this);
		
		mButton3 = (Button)findViewById(R.id.button3);
		mButton4 = (Button)findViewById(R.id.button4);
		mButton5 = (Button)findViewById(R.id.button5);
		
		mButton3.setOnClickListener(new button3Listener());
		mButton4.setOnClickListener(new button4Listener());
		mButton5.setOnClickListener(new button5Listener());
		
		pointer_linear = (LinearLayout)findViewById(R.id.pointer1);
		pointer = new PointerView(this, 0f, 0.1f);
		pointer_linear.addView(pointer);
	}
	class button3Listener implements OnClickListener{
		
		public void onClick(View v){
			pointer.draw(-1);
			AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
			audio.adjustStreamVolume(
		            AudioManager.STREAM_VOICE_CALL,
		            AudioManager.ADJUST_RAISE,
		            AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//			int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
//			if(currentVolume == 0){
//				tu.setBackgroundResource(R.drawable.mute_state);
//			}else if(currentVolume <= 5){
//				tu.setBackgroundResource(R.drawable.min);
//			}else if(currentVolume <= 10){
//				tu.setBackgroundResource(R.drawable.medium);
//			}else{
//				tu.setBackgroundResource(R.drawable.max);
//			}
		}
		
	}
	class button4Listener implements OnClickListener{
		
		public void onClick(View v){
			pointer.draw(1);
			AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
			audio.adjustStreamVolume(
		            AudioManager.STREAM_VOICE_CALL,
		            AudioManager.ADJUST_LOWER,
		            AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//			int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
//			if(currentVolume == 0){
//				tu.setBackgroundResource(R.drawable.mute_state);
//			}else if(currentVolume <= 5){
//				tu.setBackgroundResource(R.drawable.min);
//			}else if(currentVolume <= 10){
//				tu.setBackgroundResource(R.drawable.medium);
//			}else{
//				tu.setBackgroundResource(R.drawable.max);
//			}
		}
		
	}
	class button5Listener implements OnClickListener{
		
		public void onClick(View v){
			pointer.draw(2);
//			final AgentApplication application = (AgentApplication) getApplication();
//			application.onTerminate();
		}
		
	}
	
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//	    switch (keyCode) {
//	    case KeyEvent.KEYCODE_VOLUME_UP:
//	        audio.adjustStreamVolume(
//	            AudioManager.STREAM_MUSIC,
//	            AudioManager.ADJUST_RAISE,
//	            AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//	        return true;
//	    case KeyEvent.KEYCODE_VOLUME_DOWN:
//	        audio.adjustStreamVolume(
//	            AudioManager.STREAM_MUSIC,
//	            AudioManager.ADJUST_LOWER,
//	            AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//	        return true;
//	    default:
//	        break;
//	    }
//	    return super.onKeyDown(keyCode, event);
//	}
	
	class PointerView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
		private SurfaceHolder mSurfaceHolder = null;    
        private Canvas canvas;  
        //画布背景  
        private Bitmap background;  
        //刻度游标  
        private Bitmap pointer;  
  
        //总刻度数  
        private static final int KEDU_COUNT = 25;  
        //刻度最小值  
        private float init_min;  
        //每个刻度的单位值  
        private float interval;  
  
        public PointerView(Context context, float init_min, float interval) {  
            super(context);  
            mSurfaceHolder = this.getHolder();    
            mSurfaceHolder.addCallback(this);    
            this.setFocusable(true);  
            background = BitmapFactory.decodeResource(getResources(), R.drawable.staff);  
            pointer = BitmapFactory.decodeResource(getResources(), R.drawable.kedu_pointer);  
              
            this.init_min = init_min;  
            this.interval = interval;  
        }  
  
        @Override  
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {  
              
        }  
  
        @Override  
        public void surfaceCreated(SurfaceHolder holder) {  
            new Thread(this).start();  
        }  
  
        @Override  
        public void surfaceDestroyed(SurfaceHolder holder) {  
              
        }  
          
        @Override  
        public void run() {  
            draw(0);  
        }  
        //每次X轴移动的像素  
        private static final int MOVE = 10;  
        //游标在最左边时X轴的位置  
        private static final int INIT_POINTER_LEFT = 60;  
        //游标在最右边时X轴的位置  
        private static final int INIT_POINTER_RIGHT = 560;  
        //游标顶端Y轴的位置  
        private static final int INIT_POINTER_TOP = 0;  
        //底下刻度数字最左边的X轴位置  
        private static final int INIT_NUM_X = 60;  
        //结果的X轴位置  
        private static final int RESULT_X = 36;  
        //结果的Y轴位置  
        private static final int RESULT_Y = 25;  
        //结果的字体大小  
        private static final int RESULT_SIZE = 24;  
        //游标X轴的位置  
        private int POINTER_X = INIT_POINTER_LEFT;  
        //底下刻度数字X轴位置  
        private int NUM_X = INIT_NUM_X;  
        //底下刻度数字的Y轴位置  
        private int NUM_Y = 85;  
        //结果  
        private float result = 0;  
  
        /** 
         * @param direction 方向，-1向左，1向右，0不动 
         */  
        public void draw(int direction){  
            //获取画布  
            canvas = mSurfaceHolder.lockCanvas();  
            if (mSurfaceHolder == null || canvas == null) {    
                return;    
            }  
            canvas.drawColor(Color.WHITE);  
            Paint paint = new Paint();    
            paint.setAntiAlias(true);    
            paint.setColor(Color.GRAY);  
            canvas.drawBitmap(background, new Matrix(), paint);  
              
            switch(direction){  
                case -1:  
                    POINTER_X -= MOVE;  
                    result -= interval;  
//                    if(result < 0){  
//                        //result  = init_min;  
//                        POINTER_X = INIT_POINTER_RIGHT;  
//                    }else{  
                        if(POINTER_X < INIT_POINTER_LEFT){  
                            POINTER_X = INIT_POINTER_RIGHT;  
                            //result = init_min;  
                            //init_min -= KEDU_COUNT * interval;  
                        }  
//                    }  
                    break;  
                case 1:  
                    POINTER_X += MOVE;  
                     
                    if(result < 3.5){
                    	result += interval;
                    	
                    }
                    if(POINTER_X > INIT_POINTER_RIGHT){  
                        POINTER_X = INIT_POINTER_LEFT;  
                        //init_min += KEDU_COUNT * interval;  
                        //result = init_min;  
                    }  
                    break; 
                case 2:
                	POINTER_X = INIT_POINTER_LEFT + 9*MOVE;
            }  
            canvas.drawBitmap(pointer, POINTER_X, INIT_POINTER_TOP, paint);  
              
            for(int i=0; i<20; i++){  
                if(i == 0){  
                    NUM_X = INIT_NUM_X;  
                }  
                canvas.drawText(Float.toString(i * 5f * interval + init_min), NUM_X, NUM_Y, paint);  
                NUM_X += 50;  
            }  
              
            paint.setColor(Color.BLACK);  
            paint.setTextSize(RESULT_SIZE);  
            //canvas.drawText(Float.toString(result), RESULT_X, RESULT_Y, paint);  
            //解锁画布，提交画好的图像    
            mSurfaceHolder.unlockCanvasAndPost(canvas);    
        }  
		
		
	}

}
