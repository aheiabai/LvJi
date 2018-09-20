package com.example.aheiabai.myapplication;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aheiabai on 14/01/2016.
 */
public class LedClock extends AppWidgetProvider {
    private Timer timer=new Timer();
    private AppWidgetManager appWidgetManager;
    private Context context;
    private int[] digits=new int[]{
            R.drawable.su01,
            R.drawable.su02,
            R.drawable.su03,
            R.drawable.su04,
            R.drawable.su05,
            R.drawable.su06,
            R.drawable.su07,
            R.drawable.su08,
            R.drawable.su09,
            R.drawable.su10,
    };
    private int[] digitViews=new int[]{
            R.id.img01,
            R.id.img02,
            R.id.img04,
            R.id.img05,
            R.id.img07,
            R.id.img08,
    };

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.appWidgetManager=appWidgetManager;
        this.context=context;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x123);
            }
        },0,1000);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x123){
                RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.main);
                SimpleDateFormat df=new SimpleDateFormat("HHmmss");
                String timeStr=df.format(new Date());
                for (int i=0;i<timeStr.length();i++){
                    int num=timeStr.charAt(i)-48;
                    views.setImageViewResource(digitViews[i],digits[num]);
                }
                ComponentName componentName=new ComponentName(context,LedClock.class);
                appWidgetManager.updateAppWidget(componentName,views);
            }
            super.handleMessage(msg);

        }
    };
}
