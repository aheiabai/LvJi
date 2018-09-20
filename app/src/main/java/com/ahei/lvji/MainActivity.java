package com.ahei.lvji;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView tvShow;

    File photoCacheDir =null;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread(new Runnable() {
            @Override
            public void run() {
//                Glide.get(MainActivity.this).clearDiskCache();
                photoCacheDir=Glide.getPhotoCacheDir(MainActivity.this);


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        long size=calculateSize(photoCacheDir);
                        tvShow = (TextView) findViewById(R.id.tvShow);
                        tvShow.setText(size+"");
                    }
                });
            }
        }).start();



    }

    private static long calculateSize(File dir) {
        if (dir == null) return 0;
        if (!dir.isDirectory()) return dir.length();
        long result = 0;
        File[] children = dir.listFiles();
        if (children != null)
            for (File child : children)
                result += calculateSize(child);
        return result;
    }
}
