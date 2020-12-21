package com.skyworth.ice.login.ui;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.database.MyDatabaseHelper;
import com.skyworth.ice.login.util.BaseActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

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
                //输出内部存储的完整路径
                Log.d("TAG", getApplicationInfo().dataDir+"/shared_prefs");
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

                //遍历shared_prefs文件夹下所有的xml文件
                Map<String, String> map = getFilesDatas(getApplicationInfo().dataDir + "/shared_prefs");
                for (String key :
                        map.keySet()) {
                    //String value = map.get(key);
                    //Toast.makeText(DatabaseActivity.this, "文件名：" + key + " 内容：", Toast.LENGTH_SHORT).show();
                    //Log.d("TAG", "文件名：" + key + " 内容：" + value);
                    //key.substring(0,key.length()-4)是为了去点SP文件名后面的.xml子字符串
                    SharedPreferences sharedPreferences = getSharedPreferences(key.substring(0,key.length()-4), MODE_PRIVATE);
                    traversalMap(sharedPreferences.getAll());
                    //parseXMLWithSAX(value);
                }


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

    private void traversalMap(Map<String, ?> map) {
        Toast.makeText(this, "traversal Map:"+map.toString(), Toast.LENGTH_SHORT).show();
        for (Map.Entry<String, ?> mapEntry:
                map.entrySet()){
            String key = mapEntry.getKey();
            String value = mapEntry.getValue().toString();
            Toast.makeText(this, "key:" + key + " value:" + value, Toast.LENGTH_SHORT).show();
            Log.d("TAG", "key:" + key + " value:" + value);
        }
    }

    private void parseXMLWithSAX(String xmlData) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            ContentHandler contentHandler = new ContentHandler();
            xmlReader.setContentHandler(contentHandler);
            //开始执行解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ContentHandler extends DefaultHandler {

        private String nodeName;
        private StringBuilder name;
        private StringBuilder age;

        @Override
        public void startDocument() throws SAXException {
            name = new StringBuilder();
            age = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            //记录当前节点名
            nodeName = localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            //根据当前的节点名判断将内容添加到哪一个StringBuilder对象
            Log.d("TAG", "nodeName:" + nodeName);
            if ("string".equals(nodeName)) {
                name.append(ch, start, length);
            } else if ("int".equals(nodeName)) {
                age.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("map".equals(localName)) {
                Log.d("ContentHandler", "name is" + name.toString().trim());
                Log.d("ContentHandler", "age is" + age.toString().trim());
                //最后将StringBuilder清空掉
                name.setLength(0);
                age.setLength(0);
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }
    }

    /**
     * 获取某文件夹下的文件名和文件内容,存入map集合中
     * @param filePath 需要获取的文件的 路径
     * @return 返回存储文件名和文件内容的map集合
     */
    public static Map<String, String> getFilesDatas(String filePath){
        Map<String, String> files = new HashMap<>();
        File file = new File(filePath); //需要获取的文件的路径
        String[] fileNameLists = file.list(); //存储文件名的String数组
        File[] filePathLists = file.listFiles(); //存储文件路径的String数组
        for(int i=0;i<filePathLists.length;i++){
            if(filePathLists[i].isFile()){
                try {//读取指定文件路径下的文件内容
                    String fileDatas = readFile(filePathLists[i]);
                    //把文件名作为key,文件内容为value 存储在map中
                    files.put(fileNameLists[i], fileDatas);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return files;
    }

    /**
     * 读取指定目录下的文件
     * @param path 文件的路径
     * @return 文件内容
     * @throws IOException
     */
    public static String readFile(File path) throws IOException {
        //创建一个输入流对象
        InputStream is = new FileInputStream(path);
        //定义一个缓冲区
        byte[] bytes = new byte[1024];// 1kb
        //通过输入流使用read方法读取数据
        int len = is.read(bytes);
        //System.out.println("字节数:"+len);
        String str = null;
        while(len!=-1){
            //把数据转换为字符串
            str = new String(bytes, 0, len);
            //System.out.println(str);
            //继续进行读取
            len = is.read(bytes);
        }
        //释放资源
        is.close();
        return str;
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