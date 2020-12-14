package com.skyworth.ice.login.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.ui.LoginActivity;

public class ForegroundService extends Service {

    private static final String CHANNEL_ID = "my_channel_1";

    public ForegroundService() {
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = CHANNEL_ID;
            // 用户可以看到的通知渠道的名字.
            CharSequence name = getString(R.string.channel_name);
            // 用户可以看到的通知渠道的描述
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            //最后在notificationManager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "onCreate executed");

        createNotificationChannel();

        Intent intent = new Intent(this, LoginActivity.class);
        //利用通知传递数据
        intent.putExtra("news_url", "news_from_Notification"+(int)(Math.random()*10));
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //NotificationCompat是兼容版的Notification，必须要配置channel ID，否则在Android8以上不能正常显示通知内容
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentText("ForegroundService正在后台运行")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("前台服务")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); //设置点击通知后将要跳转到的目的程序

        //记得申请前台服务权限
        startForeground(1, builder.build());

        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        //notificationManager.notify(1, builder.build());

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}