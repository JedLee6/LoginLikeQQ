package com.skyworth.ice.login.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.util.BaseActivity;

import java.lang.ref.WeakReference;

public class AsyncTaskActivity extends BaseActivity {

    Button loadProgressBarButton;
    Button cancelProgressBarButton;
    TextView progressStatusTextView;
    ProgressBar progressBar;

    DownloadAsyncTask downloadAsyncTask;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Handler mMainHandler = new Handler();

    private static final int MSG_UPDATE_INFO = 0x100;
    private boolean isUpdate = true;

    private static class DownloadAsyncTask extends AsyncTask<String, Integer, String> {

        WeakReference<AsyncTaskActivity> asyncTaskActivityWeakReference;

        public DownloadAsyncTask(AsyncTaskActivity asyncTaskActivity) {
            asyncTaskActivityWeakReference = new WeakReference<>(asyncTaskActivity);
        }

        private AsyncTaskActivity getAsyncTaskActivity(){
            return asyncTaskActivityWeakReference.get();
        }

        @Override
        protected void onPreExecute() {
            getAsyncTaskActivity().progressStatusTextView.setText(R.string.downloading);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                int count = 0;
                int length = 1;
                while (count < 99) {
                    if (this.isCancelled()) {
                        return null;
                    }
                    count += length;
                    publishProgress(count);
                    //模拟耗时任务
                    Thread.sleep(50);
                    Log.d("TAG", "Download " + count + " %");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            getAsyncTaskActivity().progressBar.setProgress(progresses[0]);
            getAsyncTaskActivity().progressStatusTextView.setText("loading..." + progresses[0] + "%");
        }

        @Override
        protected void onPostExecute(String s) {
            getAsyncTaskActivity().progressStatusTextView.setText(R.string.download_finished);
        }

        /*
        @Override
        protected void onCancelled() {
            getAsyncTaskActivity().progressStatusTextView.setText(R.string.cancelled);
            getAsyncTaskActivity().progressBar.setProgress(0);
        }
         */
    }

    private void initHandlerThread() {
        mHandlerThread = new HandlerThread("thread_name");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                checkForUpdate();
                if (isUpdate) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_INFO);
                }
            }
        };
    }

    //模拟从服务器解析数据
    private void checkForUpdate(){
        try{
            //模拟耗时
            Thread.sleep(1200);

            //更新UI的操作只能在UI主线程执行
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    String result = "Updating, the current stock situation is:<font color='red'>%d</font>";
                    result = String.format(result, (int) (Math.random() * 5000 + 1000));
                    progressStatusTextView.setText(Html.fromHtml(result));
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        initView();

        initHandlerThread();

        downloadAsyncTask = new DownloadAsyncTask(this);
        loadProgressBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //防止一个asyncTask被执行多次
                if(downloadAsyncTask.getStatus()== AsyncTask.Status.PENDING){
                    downloadAsyncTask.execute();
                } else if (downloadAsyncTask.getStatus() == AsyncTask.Status.FINISHED) {
                    Log.d("TAG", "downloadAsyncTask Finished");
                    downloadAsyncTask = new DownloadAsyncTask(AsyncTaskActivity.this);
                    downloadAsyncTask.execute();
                }
            }
        });

        cancelProgressBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downloadAsyncTask!=null&& downloadAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
                    downloadAsyncTask.cancel(true);
                }
            }
        });
    }

    private void initView(){
        loadProgressBarButton = findViewById(R.id.button_load_progress);
        cancelProgressBarButton = findViewById(R.id.button_cancel);
        progressStatusTextView = findViewById(R.id.text_view_progress_status);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    protected void onResume() {
        isUpdate = true;
        super.onResume();
        mHandler.sendEmptyMessage(MSG_UPDATE_INFO);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isUpdate = false;
        mHandler.removeMessages(MSG_UPDATE_INFO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
    }

}