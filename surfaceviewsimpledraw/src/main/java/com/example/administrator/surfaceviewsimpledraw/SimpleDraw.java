package com.example.administrator.surfaceviewsimpledraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 通过path来记录滑过的路径  canva.drawPath（）来画出轨迹
 * Created by Administrator on 2016/1/22.
 */
public class SimpleDraw extends SurfaceView implements SurfaceHolder.Callback ,Runnable{
    private SurfaceHolder holder;
    private Paint paint;
    private Path path;
    private  boolean isDrawing;
    private Canvas canvas;

    public SimpleDraw(Context context) {
        this(context,null);
    }

    public SimpleDraw(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleDraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        holder=getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(40);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing=false;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (isDrawing){
            draw();
        }
        long endTime = System.currentTimeMillis();
        if (100>(endTime-startTime)){
            try {
                Thread.sleep(100-(endTime-startTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawPath(path,paint);
        }catch (Exception e){

        }finally {
            if (canvas!=null){

                holder.unlockCanvasAndPost(canvas);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
}
