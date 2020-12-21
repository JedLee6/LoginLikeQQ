package com.skyworth.ice.login.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.adapter.UserBeanAdapter;
import com.skyworth.ice.login.bean.UserBean;
import com.skyworth.ice.login.service.ForegroundService;
import com.skyworth.ice.login.service.InterractiveService;
import com.skyworth.ice.login.service.MyIntentService;
import com.skyworth.ice.login.service.UninteractiveService;
import com.skyworth.ice.login.util.BaseActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int UPDATE_TEXT = 1;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            InterractiveService.DownloadBinder downloadBinder = (InterractiveService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("TAG", "onServiceDisconnected executed");
        }
    };

    //View.MeasureSpec measureSpec;
    private EditText mAccountView;
    private EditText mPasswordView;
    private ImageView mClearAccountView;
    private ImageView mClearPasswordView;
    private CheckBox mEyeView;
    private CheckBox mDropDownView;
    private Button mLoginView;
    private Button mUserProfileButton;
    private Button mDialogButton;
    private Button mchangeTermsButton;
    private Button mStopServiceButton;
    private Button mStartForegroundServiceButton;
    private Button mStartIntentServiceButton;
    private Button mDownloadAsyncTaskButton;
    private Button mOpenDatabaseActivityButton;
    private TextView mForgetPsdView;
    private TextView mRegisterView;
    private LinearLayout mTermsLayout;
    private TextView mTermsView;
    private RelativeLayout mPasswordLayout;

    //显示账号下拉列表的窗口
    PopupWindow window;

    //这一组List是当用户点击账号下拉列表选择账号时，要隐藏的一组View
    private List<View> mDropDownInvisibleViews;

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    static class MyHandler extends Handler {
        //注意下面的LoginActivity类是MyHandler类所在的外部类，即所在的Activity
        WeakReference<LoginActivity> loginActivityWeakReference;

        MyHandler(LoginActivity loginActivity) {
            loginActivityWeakReference = new WeakReference<>(loginActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity loginActivity = loginActivityWeakReference.get();
            switch (msg.what) {
                case UPDATE_TEXT:
                    loginActivity.mTermsView.setText("Terms was changed");
                    break;
                default:
                    break;
            }
        }
    }

    //实例化一个MyHandler对象
    private Handler handler = new MyHandler(this);


    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "received local broadcast", Toast.LENGTH_SHORT).show();
        }
    }

    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(LoginActivity.this, "network changes", Toast.LENGTH_SHORT).show();

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("local_broadcast");
        LocalReceiver localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        findViewId();
        //把要隐藏的一组View加入到List中,这一组List是当用户点击账号下拉列表选择账号时，
        initDropDownGroup();

        //设置密码输入框中的输入提示和输入的密码文本各个字母之间的间距
        mPasswordView.setLetterSpacing(0.2f);

        //设置点击删除账号按钮点击事件
        mClearAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件即为将账号输入框中的文本清空
                mAccountView.setText("");
            }
        });
        //设置点击删除密码按钮点击事件
        mClearPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件即为将密码输入框中的文本清空
                mPasswordView.setText("");
            }
        });

        //密码眼睛CheckBox，可以设置是否隐藏或者显示密码
        mEyeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //账号输入框设置聚焦事件
        mAccountView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //当账号栏正在输入状态时，密码栏的清除按钮和眼睛按钮都隐藏，同理当密码栏正在输入状态时，反之
                if(hasFocus){
                    mClearPasswordView.setVisibility(View.INVISIBLE);
                    mEyeView.setVisibility(View.INVISIBLE);
                }else {
                    mClearPasswordView.setVisibility(View.VISIBLE);
                    mEyeView.setVisibility(View.VISIBLE);
                }
//                Log.i(TAG,"onFocusChange()::" + hasFocus);
            }
        });
        //密码输入框设置聚焦事件
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //当密码栏为正在输入状态时，账号栏的清除按钮隐藏
                if(hasFocus){
                    mClearAccountView.setVisibility(View.INVISIBLE);
                }else{
                    mClearAccountView.setVisibility(View.VISIBLE);
                }
            }
        });

        //账号下拉列表CheckBox，可以设置是否隐藏或者显示密码
        mDropDownView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //下拉按钮点击的时候，密码栏、忘记密码、新用户注册、同意服务条款先全部隐藏
                    setDropDownVisible(View.INVISIBLE);
                    //下拉箭头变为上拉箭头
                    //弹出一个popupWindow
                    showDropDownWindow();
                }else {
                    setDropDownVisible(View.VISIBLE);
                    //关闭popupWindow
                    window.dismiss();
                }
            }
        });
        /*
        mDropDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */

        //点击忘记密码的点击事件
        mForgetPsdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFindPsdDialog();
            }
        });

        //点击服务条款的点击事件
        mTermsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入服务条款界面
            }
        });

        mLoginView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                localBroadcastManager.sendBroadcast(new Intent("local_broadcast"));

                SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("book", MODE_PRIVATE).edit();
                sharedPreferencesEditor.putBoolean("domestic", false);
                sharedPreferencesEditor.putFloat("width", 17.6F);
                sharedPreferencesEditor.putLong("height", 24);
                Set<String> stringSet=new HashSet<>();
                stringSet.add("novel");
                stringSet.add("romance");
                sharedPreferencesEditor.putStringSet("tag",stringSet);
                sharedPreferencesEditor.putString("name", "Gone with the wind");
                sharedPreferencesEditor.putInt("thickness", 5);
                sharedPreferencesEditor.apply();

                sharedPreferencesEditor = getSharedPreferences("user", MODE_PRIVATE).edit();
                sharedPreferencesEditor.putBoolean("male", true);
                sharedPreferencesEditor.putFloat("height", 170.6F);
                sharedPreferencesEditor.putLong("weight", 60);
                stringSet=new HashSet<>();
                stringSet.add("hello");
                stringSet.add("world");
                sharedPreferencesEditor.putStringSet("alias",stringSet);
                sharedPreferencesEditor.putString("name", "Tom");
                sharedPreferencesEditor.putInt("age", 28);
                sharedPreferencesEditor.apply();
                //sharedPreferencesEditor.commit();

                sendOrderedBroadcast(new Intent("my_broadcast").setPackage("com.skyworth.ice.login"), null);
                //sendOrderedBroadcast(new Intent("my_broadcast").setPackage("com.example.activityandfragmenttest"), null);
                startActivity(new Intent(LoginActivity.this, ChatActivity.class));
            }
        });

        mUserProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            }
        });

        mDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DialogActivity.class));
            }
        });

        mchangeTermsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开RecyclerViewActivity
                startActivity(new Intent(LoginActivity.this, FoldableRecyclerViewActivity.class));

                //开启服务的运行
                //startService(new Intent(LoginActivity.this, UninteractiveService.class));
                //绑定service
                bindService(new Intent(LoginActivity.this, InterractiveService.class), serviceConnection, BIND_AUTO_CREATE);

                //Toast.makeText(LoginActivity.this, "button clicked", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        mStopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消绑定服务
                unbindService(serviceConnection);
                //停止服务的运行
                //Toast.makeText(LoginActivity.this, "you stopped the service", Toast.LENGTH_SHORT).show();
                //stopService(new Intent(LoginActivity.this, UninteractiveService.class));
            }
        });

        mStartForegroundServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(LoginActivity.this, ForegroundService.class));
            }
        });

        mStartIntentServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打印主线程的id
                Log.d("TAG", "Thread id id " + Thread.currentThread().getId());
                startService(new Intent(LoginActivity.this, MyIntentService.class));
            }
        });

        mDownloadAsyncTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AsyncTaskActivity.class));
            }
        });

        mOpenDatabaseActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DatabaseActivity.class));
            }
        });

        String accountEditTextDataString = loadDataFromFile();
        if (!TextUtils.isEmpty(accountEditTextDataString)) {
            mAccountView.setText(accountEditTextDataString);
            mAccountView.setSelection(accountEditTextDataString.length());
            Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        int age = sharedPreferences.getInt("age", 0);
        Toast.makeText(this, "name is "+name+" age is "+age, Toast.LENGTH_SHORT).show();
    }

    private String loadDataFromFile() {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileInputStream = openFileInput("data");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    private void findViewId() {
        mAccountView = findViewById(R.id.et_input_account);
        mPasswordView = findViewById(R.id.et_input_password);
        mClearAccountView = findViewById(R.id.iv_clear_account);
        mClearPasswordView = findViewById(R.id.iv_clear_password);
        mEyeView = findViewById(R.id.cb_login_open_eye);
        mDropDownView = findViewById(R.id.cb_login_drop_down);
        mLoginView = findViewById(R.id.btn_login);
        mUserProfileButton = findViewById(R.id.btn_user_profile);
        mDialogButton = findViewById(R.id.btn_dialog);
        mchangeTermsButton = findViewById(R.id.btn_change_term);
        mStopServiceButton = findViewById(R.id.btn_stop_service);
        mStartForegroundServiceButton = findViewById(R.id.btn_start_foreground_service);
        mStartIntentServiceButton = findViewById(R.id.btn_start_intent_service);
        mDownloadAsyncTaskButton = findViewById(R.id.btn_download_async_task);
        mOpenDatabaseActivityButton = findViewById(R.id.btn_open_database_activity);
        mForgetPsdView = findViewById(R.id.tv_forget_password);
        mRegisterView = findViewById(R.id.tv_register_account);
        mTermsLayout = findViewById(R.id.ll_terms_of_service_layout);
        mTermsView = findViewById(R.id.tv_terms_of_service);
        mPasswordLayout = findViewById(R.id.rl_password_layout);
    }

    private void initDropDownGroup() {
        mDropDownInvisibleViews = new ArrayList<>();
        mDropDownInvisibleViews.add(mPasswordView);
        mDropDownInvisibleViews.add(mForgetPsdView);
        mDropDownInvisibleViews.add(mRegisterView);
        mDropDownInvisibleViews.add(mPasswordLayout);
        mDropDownInvisibleViews.add(mLoginView);
        mDropDownInvisibleViews.add(mUserProfileButton);
        mDropDownInvisibleViews.add(mDialogButton);
        mDropDownInvisibleViews.add(mchangeTermsButton);
        mDropDownInvisibleViews.add(mStopServiceButton);
        mDropDownInvisibleViews.add(mStartForegroundServiceButton);
        mDropDownInvisibleViews.add(mStartIntentServiceButton);
        mDropDownInvisibleViews.add(mDownloadAsyncTaskButton);
        mDropDownInvisibleViews.add(mOpenDatabaseActivityButton);
        mDropDownInvisibleViews.add(mTermsLayout);
    }

    private void setDropDownVisible(int visible) {
        for (View view:mDropDownInvisibleViews){
            view.setVisibility(visible);
        }
    }

    //当用户点击账号下拉列表CheckBox时，便是要展现的一个窗口
    private void showDropDownWindow() {
        if(null == window){
            window = new PopupWindow(this);
        }
        //final PopupWindow window = new PopupWindow(this);
        //下拉菜单里显示上次登录的账号，在这里先模拟获取有记录的用户列表
        List<UserBean> userBeanList = new ArrayList<>();
        userBeanList.add(new UserBean("12345678","123456789"));
        userBeanList.add(new UserBean("22669988","22669988"));
        userBeanList.add(new UserBean("77552244","12341234"));

        //配置ListView的适配器
        final UserBeanAdapter adapter = new UserBeanAdapter(this);
        adapter.replaceData(userBeanList);
        //初始化ListView
        ListView userListView = (ListView) View.inflate(this,
                R.layout.window_account_drop_down,null);
        userListView.setAdapter(adapter);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow
                window.dismiss();
                //当下拉列表条目被点击时，显示刚才被隐藏视图,下拉箭头变上拉箭头
                //相当于mDropDownView取消选中
                mDropDownView.setChecked(false);
                //账号栏和密码栏文本更新
                UserBean checkedUser = adapter.getItem(position);
                mAccountView.setText(checkedUser.getAccount());
                mPasswordView.setText(checkedUser.getPassword());

            }
        });
        //添加一个看不见的FooterView，这样ListView就会自己在倒数第一个（FooterView）上边显示Divider，
        //进而在UI上实现最后一行也显示分割线的效果了
        userListView.addFooterView(new TextView(this));

        //配置popupWindow并显示
        window.setContentView(userListView);
        window.setAnimationStyle(0);
        window.setBackgroundDrawable(null);
        window.setWidth(mPasswordLayout.getWidth());
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setOutsideTouchable(false);
        window.showAsDropDown(mAccountView);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //当点击popupWindow之外区域导致window关闭时，显示刚才被隐藏视图，下拉箭头变上拉箭头
                //相当于mDropDownView取消选中
                mDropDownView.setChecked(false);
            }
        });

    }

    private void showFindPsdDialog() {
        //有空了做下
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDropDownInvisibleViews.clear();
        //我自己写的时候是不判空直接unregister
        unregisterReceiver(networkChangeReceiver);
        localBroadcastManager.unregisterReceiver(localReceiver);

        String accountEditTextDataString = mAccountView.getText().toString();
        saveDataToFile(accountEditTextDataString);
    }

    private void saveDataToFile(String stringData) {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = openFileOutput("data", Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(stringData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
