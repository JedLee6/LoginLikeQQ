package com.skyworth.ice.login.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class InterractiveService extends Service {
    public InterractiveService() {
    }

    private DownloadBinder downloadBinder = new DownloadBinder();

    public class DownloadBinder extends Binder{
        public void startDownload(){
            Log.d("TAG", "startDownload executed");
        }

        public void getProgress(){
            Log.d("TAG", "getProgress executed");
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return downloadBinder;
    }
}