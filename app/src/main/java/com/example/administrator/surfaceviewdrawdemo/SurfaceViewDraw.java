package com.example.administrator.surfaceviewdrawdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * surfaceView实现画正弦曲线
 * Created by Administrator on 2016/1/21.
 */
public class SurfaceViewDraw extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    private SurfaceHolder holder;//控制surfaceView
    private Canvas canvas;  //画布 通过holder来锁定画布解锁画布
    private boolean isDrawing; //绘画的标志位
    private int x=0;
    private int y=0;
    private Path path;
    private Paint paint;

    public SurfaceViewDraw(Context context) {
        this(context, null);
    }

    public SurfaceViewDraw(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewDraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //得到surfaceViewHolder
        holder=getHolder();
        //给holder添加监听
        holder.addCallback(this);
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

    }

    /**
     * 在surfaceview创建时开启子线程绘画
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing=true;
        path.moveTo(0, 400);
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
       while (isDrawing){
          draw();
           x += 1;
           y= (int) (100*Math.sin(x*2* Math.PI/180)+400);
           path.lineTo(x,y);

           if (x>=getWidth()){
               break;
           }
       }
    }

    private void draw() {
        try {

            //得到画布
            canvas=holder.lockCanvas();
            //开始绘画
            canvas.drawColor(Color.WHITE);

            canvas.drawPath(path,paint);

        }catch (Exception e){

        }
        //将画布放到finally里面确保每次都能够将内容提交
        finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }


    }

}
