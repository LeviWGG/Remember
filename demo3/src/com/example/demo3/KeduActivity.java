package com.example.demo3;

import android.app.Activity;  
import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Matrix;  
import android.graphics.Paint;  
import android.graphics.Rect;  
import android.os.Bundle;  
import android.view.KeyEvent;  
import android.view.MotionEvent;  
import android.view.SurfaceHolder;  
import android.view.SurfaceView;  
import android.view.View;  
import android.widget.ImageView;  
import android.widget.LinearLayout;

public class KeduActivity extends Activity {
	
	private ImageView kedu_tiao;  
    private LinearLayout kedu_linear;  
    private LinearLayout staff_linear;  
      
    private KeduView kedu;  
    private StaffView staff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kedu);
		
		kedu_linear = (LinearLayout)findViewById(R.id.kedu_linear);  
        kedu = new KeduView(this, 0f, 0.1f);  
        kedu_linear.addView(kedu);  
        staff_linear = (LinearLayout)findViewById(R.id.staff_linear);  
        staff = new StaffView(this, 7.5f, 0.5f, "cm");  
        staff_linear.addView(staff);  
          
        kedu_tiao = (ImageView)findViewById(R.id.kedu_tiao);  
        kedu_tiao.setOnTouchListener(keduListener);
	}
	
	private ImageView.OnTouchListener keduListener = new ImageView.OnTouchListener(){  
        private float initx = 0;  
        
        @Override  
        public boolean onTouch(View v, MotionEvent event) {  
            switch(event.getAction()){  
                case MotionEvent.ACTION_DOWN:  
                    initx = event.getX();  
                    break;  
                case MotionEvent.ACTION_MOVE:  
                    float lastx = event.getX();  
                    if(lastx > initx + 5){  
                        kedu.draw(1);  
                        initx = lastx;  
                    }else if(lastx < initx -5){  
                        kedu.draw(-1);  
                        initx = lastx;  
                    }  
                    break;  
            }  
            return true;  
        }  
    };  
      
    class KeduView extends SurfaceView implements SurfaceHolder.Callback, Runnable{  
          
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
  
        public KeduView(Context context, float init_min, float interval) {  
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
        private static final int INIT_POINTER_LEFT = 20;  
        //�α������ұ�ʱX���λ��  
        private static final int INIT_POINTER_RIGHT = 370;  
        //�α궥��Y���λ��  
        private static final int INIT_POINTER_TOP = 36;  
        //���¿̶���������ߵ�X��λ��  
        private static final int INIT_NUM_X = 18;  
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
                    if(result <= 0){  
                        result  = init_min;  
                        POINTER_X = INIT_POINTER_LEFT;  
                    }else{  
                        if(POINTER_X < INIT_POINTER_LEFT){  
                            POINTER_X = INIT_POINTER_LEFT;  
                            result = init_min;  
                            //init_min -= KEDU_COUNT * interval;  
                        }  
                    }  
                    break;  
                case 1:  
                    POINTER_X += MOVE;  
                     
                    if(result < 3.5){
                    	result += interval;
                    	
                    }
                    if(POINTER_X > INIT_POINTER_RIGHT){  
                        POINTER_X = INIT_POINTER_RIGHT;  
                        //init_min += KEDU_COUNT * interval;  
                        //result = init_min;  
                    }  
                    break;  
            }  
            canvas.drawBitmap(pointer, POINTER_X, INIT_POINTER_TOP, paint);  
              
            for(int i=0; i<10; i++){  
                if(i == 0){  
                    NUM_X = INIT_NUM_X;  
                }  
                canvas.drawText(Float.toString(i * 5f * interval + init_min), NUM_X, NUM_Y, paint);  
                NUM_X += 50;  
            }  
              
            paint.setColor(Color.BLACK);  
            paint.setTextSize(RESULT_SIZE);  
            canvas.drawText(Float.toString(result), RESULT_X, RESULT_Y, paint);  
            //�����������ύ���õ�ͼ��    
            mSurfaceHolder.unlockCanvasAndPost(canvas);    
        }  
    }  
  
    class StaffView extends SurfaceView implements SurfaceHolder.Callback, Runnable{  
  
        private SurfaceHolder mSurfaceHolder = null;    
        private Canvas canvas;  
        private Paint paint;  
        //��������  
        private Bitmap background;  
        //�̶�  
        private Bitmap staff;  
        //�̶��α�  
        private Bitmap pointer;  
        //��ʼֵ  
        private float initValue;  
        //�̶ȵ�λ��Сֵ  
        private float interval;  
        //��λ  
        private String unit;  
        //�Ƿ��ʼ  
        private boolean isInit = true;  
          
        public StaffView(Context context, float initValue, float interval, String unit) {  
            super(context);  
            mSurfaceHolder = this.getHolder();    
            mSurfaceHolder.addCallback(this);    
              
            paint = new Paint();  
              
            this.setFocusable(true);  
            background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);  
            staff = BitmapFactory.decodeResource(getResources(), R.drawable.staff);  
            pointer = BitmapFactory.decodeResource(getResources(), R.drawable.kedu_pointer);  
    
            this.initValue = initValue;  
            this.interval = interval;  
            this.unit = unit;  
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
          
        private int move = 10;  //ÿ���ƶ��ľ���  
        private int initBx = 77;    //ͼƬ��X����  
        private int by = 0; //ͼƬ��Y����  
        private int bw = 258;   //ͼƬ���  
        private int bh = 31;    //ͼƬ�߶�  
        private int sx = 18;    //����X����  
        private int sy = 36;    //����Y����  
        private int jiange = 51; //��̶�֮�����  
        private int num_left = 33;  //��������ֵ���ߵľ���  
        private int RESULT_X = 36;  //�����X��λ��  
        private int RESULT_Y = 25;  //�����Y��λ��  
        private int RESULT_SIZE = 24;   //����������С  
          
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
              
            paint.setAntiAlias(true);    
            paint.setColor(Color.GRAY);  
              
            canvas.drawBitmap(background, new Matrix(), paint);  
              
            if(isInit){  
                result = initValue;  
            }else{  
                switch(direction){  
                    case 1:  
                        result = Arithmetic.add(result, interval);  
                        break;  
                    case -1:  
                        result = Arithmetic.sub(result, interval);  
                        if(result < 0){  
                            result = 0;  
                        }  
                        break;  
                }  
            }  
            initStaff();  
  
            canvas.drawBitmap(pointer, 143, 36, paint);  
  
            Paint reslutPaint = new Paint();  
            reslutPaint.setColor(Color.BLACK);  
            reslutPaint.setTextSize(RESULT_SIZE);  
            canvas.drawText(Float.toString(result) + " " + unit, RESULT_X, RESULT_Y, reslutPaint);  
            //�����������ύ���õ�ͼ��    
            mSurfaceHolder.unlockCanvasAndPost(canvas);    
        }  
          
        private void initStaff(){  
            int bx = initBx;  
            int num_x = num_left;  
            int mod = 0;  
            int midd = 2;  
            if(result != 0){  
                mod = (int)(Arithmetic.div(result, interval, 1) % 5);  
                bx += mod * move;  
            }  
            if(mod >= 3){  
                midd = 1;  
                num_x += (5 - mod) * move;  
            }else{  
                num_x -= mod * move;  
            }  
            float text = 0;  
            for(int i=0; i<5; i++){  
                if(i < midd){  
                    text = result - mod * interval - (midd - i) * 5 * interval;  
                }else if(i == midd){  
                    text = result - mod * interval;  
                }else{  
                    text += 5 * interval;  
                }  
                text = Arithmetic.round(text, 1);  
                if(text >= 0){  
                    canvas.drawText(Float.toString(text), num_x, 85, paint);  
                }  
                num_x += jiange;  
            }  
  
            //Ҫ���Ƶ�ͼƬ������������  
            Rect src = new Rect();  
            src.left = bx;  
            src.top = by;  
            src.right = bx + bw;  
            src.bottom = bh;  
              
            //Ҫ���ƵĻ���������������  
            Rect dst = new Rect();  
            dst.left = sx;  
            dst.top = sy;  
            dst.right = sx + bw;  
            dst.bottom = sy + bh;  
            canvas.drawBitmap(staff, src, dst, paint);  
        }  
          
        private float initx = 0;  
        @Override  
        public boolean onTouchEvent(MotionEvent event) {  
            switch(event.getAction()){  
                case MotionEvent.ACTION_DOWN:  
                    initx = event.getX();  
                    break;  
                case MotionEvent.ACTION_MOVE:  
                    float lastx = event.getX();  
                    if(lastx > initx + 5){  
                        isInit = false;  
                        draw(-1);  
                        initx = lastx;  
                    }else if(lastx < initx -5){  
                        isInit = false;  
                        draw(1);  
                        initx = lastx;  
                    }  
                    break;  
            }  
                return true;  
        }  
          
        public float getResult(){  
            return result;  
        }  
    }  
      
      
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){  
            staff.isInit = false;  
            staff.draw(-1);  
            return true;  
        }if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){  
            staff.isInit = false;  
            staff.draw(1);  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  

	
	
}
