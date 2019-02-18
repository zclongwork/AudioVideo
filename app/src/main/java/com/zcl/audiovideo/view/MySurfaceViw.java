package com.zcl.audiovideo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Description SurfaceViw Demo
 * Author ZhangChenglong
 * Date 19/2/18 10:13
 */
public class MySurfaceViw extends SurfaceView implements SurfaceHolder.Callback {
    
    private final SurfaceHolder mHolder;
    private Paint paint;
    private Thread thread;
    private boolean isDraw = false;
    private int radius = 10;
    
    public MySurfaceViw(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        createPaint();
    }
    
    private void createPaint() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
    }
    
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isDraw) {
                    Canvas canvas = mHolder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawCircle(300, 500, radius, paint);
                        radius += 10;
                        if (radius > 70) {
                            radius += 3;
                            SystemClock.sleep(50);
                
                        }
                    }
                    if (isDraw) {
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        },"my_surface_thread");
        isDraw = true;
        thread.start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDraw = false;
        mHolder.removeCallback(this);
    }
    
    
    
}
