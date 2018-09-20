package com.example.aheiabai.livewallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * Created by aheiabai on 12/01/2016.
 */
public class LiveWallpaper extends WallpaperService {
    private Bitmap heart;

    @Override
    public Engine onCreateEngine() {
        heart= BitmapFactory.decodeResource(getResources(),R.drawable.heart);
        return new MyEngine();
    }

    class MyEngine extends Engine {
        private boolean mVisible;
        private float mTouchX=-1;
        private float mTouchY=-1;
        private int count=1;
        private int originX=100,originY=100;
        private Paint myPaint=new Paint();
        Handler mHandle=new Handler();
        private final Runnable drawTarget=new Runnable() {
            @Override
            public void run() {
                drawFrame();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            myPaint.setARGB(76,0,0,255);
            myPaint.setAntiAlias(true);
            myPaint.setStyle(Paint.Style.FILL);
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandle.removeCallbacks(drawTarget);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible=visible;
            if (visible){
                drawFrame();
            }
            else{
                mHandle.removeCallbacks(drawTarget);
            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            drawFrame();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction()==MotionEvent.ACTION_MOVE){
                mTouchX=event.getX();
                mTouchY=event.getY();
            }
            else{
                mTouchX=-1;
                mTouchY=-1;
            }
            super.onTouchEvent(event);
        }
        private void drawFrame(){
            final SurfaceHolder holder=getSurfaceHolder();
            Canvas c=null;
            try{
                c=holder.lockCanvas();
                if (c!=null){
                    c.drawColor(0xffffffff);
                    drawTouchPoint(c);
                    myPaint.setAlpha(76);
                    c.translate(originX,originY);
                    for (int i=0;i<count;i++){
                        c.translate(80,0);
                        c.scale(0.95f, 0.95f);
                        c.rotate(20f);
                        c.drawRect(0,0,150,75,myPaint);
                    }
                }
            }
            finally {
                if (c!=null)holder.unlockCanvasAndPost(c);
            }
            mHandle.removeCallbacks(drawTarget);
            if (mVisible){
                count++;
                if (count>=50){
                    Random rand=new Random();
                    count=1;
                    originX+=(rand.nextInt(60)-30);
                    originY+=(rand.nextInt(60)-30);
                    try{
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                mHandle.postDelayed(drawTarget,100);
            }
        }

        private void drawTouchPoint(Canvas c){
            if (mTouchX>=0&&mTouchY>=0){
                myPaint.setAlpha(255);
                c.drawBitmap(heart,mTouchX,mTouchY,myPaint);
            }
        }
    }
}
