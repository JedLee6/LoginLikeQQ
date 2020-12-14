package com.skyworth.ice.login.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.adapter.FruitAdapter;
import com.skyworth.ice.login.adapter.MyFruitAdapter;
import com.skyworth.ice.login.bean.FruitBean;
import com.skyworth.ice.login.util.ActivityController;
import com.skyworth.ice.login.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {

    private String[] data = { "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple","Strawberry","Cherry","Mango",
            "Apple", "Banana", "Orange", "Watermelon", "Pear", "Grape",
            "Pineapple", "Strawberry", "Cherry", "Mango" };

    private List<FruitBean> fruitList = new ArrayList<FruitBean>();

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ChatActivity.this, android.R.layout.simple_list_item_1, data);

        //mListView = (ListView) findViewById(R.id.lv_chat);
        //mListView.setAdapter(adapter);

        initFruits();
        FruitAdapter fruitAdapter = new FruitAdapter(ChatActivity.this,
                                    R.layout.fruit_item, fruitList);
        MyFruitAdapter myFruitAdapter = new MyFruitAdapter(this, fruitList);
        mListView = findViewById(R.id.lv_chat);
        mListView.setAdapter(myFruitAdapter);

        //设置ListView的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position就是用户点击的ListView Item在整个ListView中的位置
                FruitBean fruitBean = fruitList.get(position);
                Toast.makeText(ChatActivity.this, "You Clicked "+fruitBean.getName(), Toast.LENGTH_SHORT).show();

                //发送强制下线广播
                sendBroadcast(new Intent("force_offline"));
                //sendBroadcast(new Intent("my_broadcast2").setPackage("com.skyworth.ice.login"), null);

                //ActivityController.finishAllActivity();
            }
        });
    }

    private void initFruits(){
        for (int i = 0; i < 2; i++) {
            FruitBean apple = new FruitBean("Apple", R.drawable.ic_launcher_background, 0);
            fruitList.add(apple);
            FruitBean banana = new FruitBean("Banana", R.drawable.ic_launcher_background, 1);
            fruitList.add(banana);
            FruitBean orange = new FruitBean("Orange", R.drawable.ic_launcher_background, 1);
            fruitList.add(orange);
            FruitBean watermelon = new FruitBean ("Watermelon", R. drawable.ic_launcher_background, 0);
            fruitList.add(watermelon);
            FruitBean pear = new FruitBean("Pear", R.drawable.ic_launcher_background, 1);
            fruitList.add(pear);
            FruitBean grape = new FruitBean("Grape", R.drawable.ic_launcher_background, 1);
            fruitList.add(grape);
            FruitBean pineapple = new FruitBean("Pineapple", R.drawable.ic_launcher_background, 1);
            fruitList.add(pineapple);
            FruitBean strawberry = new FruitBean("Strawberry", R.drawable.ic_launcher_background, 1);
            fruitList.add(strawberry);
            FruitBean cherry = new FruitBean("Cherry", R.drawable.ic_launcher_background, 0);
            fruitList.add(cherry);
            FruitBean mango = new FruitBean("Mango", R.drawable.ic_launcher_background, 1);
            fruitList.add(mango);
        }
    }

}