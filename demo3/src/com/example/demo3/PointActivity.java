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
        //��������  
        private Bitmap background;  
        //�̶��α�  
        private Bitmap pointer;  
  
        //�̶ܿ���  
        private static final int KEDU_COUNT = 25;  
        //�̶���Сֵ  
        private float init_min;  
        //ÿ���̶ȵĵ�λֵ  
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
        //ÿ��X���ƶ�������  
        private static final int MOVE = 10;  
        //�α��������ʱX���λ��  
        private static final int INIT_POINTER_LEFT = 60;  
        //�α������ұ�ʱX���λ��  
        private static final int INIT_POINTER_RIGHT = 560;  
        //�α궥��Y���λ��  
        private static final int INIT_POINTER_TOP = 0;  
        //���¿̶���������ߵ�X��λ��  
        private static final int INIT_NUM_X = 60;  
        //�����X��λ��  
        private static final int RESULT_X = 36;  
        //�����Y��λ��  
        private static final int RESULT_Y = 25;  
        //����������С  
        private static final int RESULT_SIZE = 24;  
        //�α�X���λ��  
        private int POINTER_X = INIT_POINTER_LEFT;  
        //���¿̶�����X��λ��  
        private int NUM_X = INIT_NUM_X;  
        //���¿̶����ֵ�Y��λ��  
        private int NUM_Y = 85;  
        //���  
        private float result = 0;  
  
        /** 
         * @param direction ����-1����1���ң�0���� 
         */  
        public void draw(int direction){  
            //��ȡ����  
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
            //�����������ύ���õ�ͼ��    
            mSurfaceHolder.unlockCanvasAndPost(canvas);    
        }  
		
		
	}

}
