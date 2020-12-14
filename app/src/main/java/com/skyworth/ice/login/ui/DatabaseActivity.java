package com.skyworth.ice.login.ui;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.database.MyDatabaseHelper;
import com.skyworth.ice.login.util.BaseActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DatabaseActivity extends BaseActivity {

    private MyDatabaseHelper myDatabaseHelper;
    private Button createDatabaseButton;
    private Button addDataButton;
    private Button updateDataButton;
    private Button deleteDataButton;
    private Button queryDataButton;
    private Button operateFileButton;
    private Button readContactsButton;
    private Button openWebViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        //构建一个SQLiteOpenHelper对象，用于指定数据库名和版本号
        myDatabaseHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

        createDatabaseButton = findViewById(R.id.button_create_database);
        createDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这样当第一次点击Create database按钮时，就会检测到当前程序中并没有
                //BookStore.db这个数据库，于是会创建该数据库并调用MyDatabaseHelper中
                //onCreate()方法，这样Book也就得到了创建，然后会弹出一个Toast提示创建成功
                myDatabaseHelper.getWritableDatabase();
            }
        });

        addDataButton = findViewById(R.id.button_add_data);
        addDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                //开始组装第一条数据
                contentValues.put("name", "The Da Vinci Code");
                contentValues.put("author", "Dan Brown");
                contentValues.put("pages", 454);
                contentValues.put("price", 16.96);
                sqLiteDatabase.insert("Book", null, contentValues);
                contentValues.clear();
                //开始组装第二条数据
                contentValues.put("name", "The Lost Symbol");
                contentValues.put("author", "Dan Brown");
                contentValues.put("pages", 510);
                contentValues.put("price", 19.95);
                sqLiteDatabase.insert("Book", null, contentValues);
            }
        });

        updateDataButton = findViewById(R.id.button_update_data);
        updateDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("price", 10.99);
                sqLiteDatabase.update("Book", contentValues, "name = ?", new String[]{"The Da Vinci Code"});
            }
        });

        deleteDataButton = findViewById(R.id.button_delete_data);
        deleteDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                sqLiteDatabase.delete("Book", "pages>?", new String[]{"500"});
            }
        });

        queryDataButton = findViewById(R.id.button_query_data);
        queryDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                //查询Book表中所有数据
                Cursor cursor = sqLiteDatabase.query("Book", null, null, null, null, null, null);
                if(cursor.moveToFirst()){
                    do {
                        //遍历Cursor对象，取出数据并打印
                        Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("TAG", "book id " + id);
                        Log.d("TAG", "book name " + name);
                        Log.d("TAG", "book author " + author);
                        Log.d("TAG", "book pages " + pages);
                        Log.d("TAG", "book price " + price);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

        operateFileButton = findViewById(R.id.button_operate_file);
        operateFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输出files目录
                Log.d("TAG", getFilesDir().getAbsolutePath());
                //输出cache目录
                Log.d("TAG", getCacheDir().getAbsolutePath());
                //输出shared_prefs目录,暂无此方法
                //输出数据库目录
                Log.d("TAG", getDatabasePath("BookStore.db").getAbsolutePath());
                //输出code_cache目录
                Log.d("TAG", getCodeCacheDir().getAbsolutePath());
                //输出lib目录(软链接),暂无此方法

                //输出外部存储的files文件夹目录
                Log.d("TAG", getExternalFilesDir(null).getAbsolutePath());
                Log.d("TAG", getExternalFilesDirs(null)[0].getAbsolutePath());
                //输出外部存储的cache文件夹目录
                Log.d("TAG", getExternalCacheDir().getAbsolutePath());
                Log.d("TAG", getExternalCacheDirs()[0].getAbsolutePath());

            }
        });

        readContactsButton = findViewById(R.id.button_read_contacts);
        readContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        openWebViewButton = findViewById(R.id.button_open_web_view);
        openWebViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatabaseActivity.this, WebViewActivity.class));
            }
        });
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
        }
    }

    private void readContacts(){
        Cursor cursor = null;
        try {
            //查询联系人数据
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                int count = 0;
                while (cursor.moveToNext() && count < 10) {
                    count++;
                    //获得联系人姓名
                    String contacterName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String contacterNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.d("TAG", contacterName + " " + contacterNumber);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (1 == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                Toast.makeText(this, "You denied permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        //一定要记得关闭数据库
        myDatabaseHelper.close();
        super.onDestroy();
    }
}