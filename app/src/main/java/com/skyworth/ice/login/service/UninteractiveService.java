package com.skyworth.ice.login.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class UninteractiveService extends Service {

    private Thread mThread;

    public UninteractiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "UninteractiveService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "UninteractiveService onStartCommand");

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    //Toast.makeText(UninteractiveService.this, "执行耗时操作", Toast.LENGTH_SHORT).show();
                    System.out.println("执行耗时操作");
                }
            }
        });
        mThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("TAG", "UninteractiveService onDestroy");
        super.onDestroy();
    }
}