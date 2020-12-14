package com.skyworth.ice.login.ui;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.LinearLayout;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.adapter.FruitAdapter;
import com.skyworth.ice.login.adapter.RecyclerViewFruitAdapter;
import com.skyworth.ice.login.bean.FruitBean;
import com.skyworth.ice.login.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends BaseActivity {

    private List<FruitBean> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initFruits();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_user_profile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setLayoutManager(staggeredGridLayoutManager);
        RecyclerViewFruitAdapter fruitAdapter = new RecyclerViewFruitAdapter(fruitList);
        recyclerView.setAdapter(fruitAdapter);
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