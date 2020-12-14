package com.skyworth.ice.login.util;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ActivityController {
    //这样静态引用会导致内存泄漏
    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void finishAllActivity() {
        for (Activity activity :
                activityList) {
            //一定要加这个判断吗，不加上可以吗，是不是为了防止内存不足部分activity被杀死
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
