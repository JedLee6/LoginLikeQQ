package com.skyworth.ice.login.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.bean.FruitBean;

import java.util.List;

public class RecyclerViewFruitAdapter extends RecyclerView.Adapter<RecyclerViewFruitAdapter.ViewHolder>{

    private List<FruitBean> mFruitList;

    //把要在RecyclerView中要展示的数据传递到RecyclerViewFruitAdapter中的mFruitList存储起来
    public RecyclerViewFruitAdapter(List<FruitBean> fruitBeanList){
        mFruitList = fruitBeanList;
    }

    //自定义的RecyclerViewFruitAdapter一般要有一个内部类ViewHolder，
    //而ViewHolder一般要跟RecyclerView的Item的View实例想相绑定，
    //因为每个ViewHolder就是存储每一个RecyclerView Item里面中各个View的属性
    static class ViewHolder extends RecyclerView.ViewHolder {
        View recyclerViewItem;
        ImageView fruitImageView;
        TextView fruitTextView;

        public ViewHolder(View view){
            super(view);
            recyclerViewItem = view;
            fruitImageView = view.findViewById(R.id.fruit_image);
            fruitTextView = view.findViewById(R.id.fruit_name);
        }
    }

    /* @NotNull：注解用在指明一个参数，字段或者方法的返回值不可以为null，是标准化的；
     * @NonNull ：变量到一个被@NonNull修饰（标记）的参数的方法中时，开发工具IDE会警告程序可能会有崩溃的风险。这个是一个静态的分析方法，运行时不报任何警告；
     **/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Log.d("RecyclerView", "onCreateViewHolder Item "+ viewGroup.toString()+" "+i);

        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.fruit_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.fruitTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                FruitBean fruitBean = mFruitList.get(position);
                Toast.makeText(v.getContext(), "you clicked textView " + fruitBean.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.fruitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                FruitBean fruitBean = mFruitList.get(position);
                Toast.makeText(v.getContext(), "you clicked imageView " + fruitBean.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.recyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                FruitBean fruitBean = mFruitList.get(position);
                Toast.makeText(v.getContext(), "you cliked view " + fruitBean.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    //于对RecyclerView子项的数据进行赋值，会在每个子项被滚动到屏幕内时候执行，这里我们通过参数i得到当前项的
    //FruitBean实例，然后再将数据设置到ViewHolder的ImageView和TextView中
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Log.d("RecyclerView", "onBindHolder Item "+ " "+i);

        FruitBean fruitBean = mFruitList.get(i);
        viewHolder.fruitImageView.setImageResource(fruitBean.getImageId());
        viewHolder.fruitTextView.setText(fruitBean.getName());
    }

    //用于告诉RecyclerView一共有多少子项，直接返回数据源的长度就可以了，
    //因为RecyclerView在setAdapter()后会隐式调用getItemCount()
    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

}
