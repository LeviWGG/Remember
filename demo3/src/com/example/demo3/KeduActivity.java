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
        //每次X轴移动的像素  
        private static final int MOVE = 10;  
        //游标在最左边时X轴的位置  
        private static final int INIT_POINTER_LEFT = 20;  
        //游标在最右边时X轴的位置  
        private static final int INIT_POINTER_RIGHT = 370;  
        //游标顶端Y轴的位置  
        private static final int INIT_POINTER_TOP = 36;  
        //底下刻度数字最左边的X轴位置  
        private static final int INIT_NUM_X = 18;  
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
            //解锁画布，提交画好的图像    
            mSurfaceHolder.unlockCanvasAndPost(canvas);    
        }  
    }  
  
    class StaffView extends SurfaceView implements SurfaceHolder.Callback, Runnable{  
  
        private SurfaceHolder mSurfaceHolder = null;    
        private Canvas canvas;  
        private Paint paint;  
        //画布背景  
        private Bitmap background;  
        //刻度  
        private Bitmap staff;  
        //刻度游标  
        private Bitmap pointer;  
        //初始值  
        private float initValue;  
        //刻度单位最小值  
        private float interval;  
        //单位  
        private String unit;  
        //是否初始  
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
          
        private int move = 10;  //每次移动的距离  
        private int initBx = 77;    //图片上X坐标  
        private int by = 0; //图片上Y坐标  
        private int bw = 258;   //图片宽度  
        private int bh = 31;    //图片高度  
        private int sx = 18;    //画布X坐标  
        private int sy = 36;    //画布Y坐标  
        private int jiange = 51; //大刻度之间距离  
        private int num_left = 33;  //最左边数字到左边的距离  
        private int RESULT_X = 36;  //结果的X轴位置  
        private int RESULT_Y = 25;  //结果的Y轴位置  
        private int RESULT_SIZE = 24;   //结果的字体大小  
          
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
            //解锁画布，提交画好的图像    
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
  
            //要绘制的图片矩形区域设置  
            Rect src = new Rect();  
            src.left = bx;  
            src.top = by;  
            src.right = bx + bw;  
            src.bottom = bh;  
              
            //要绘制的画布矩形区域设置  
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
