package com.skyworth.ice.login.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.bean.FruitBean;

import java.util.ArrayList;
import java.util.List;

public class MyFruitAdapter extends BaseAdapter {

    private static final int TYPE_ITEM_COUNT = 2;

    private List<FruitBean> fruitBeanList;
    private Context context;

    public MyFruitAdapter(Context context, List<FruitBean> fruitBeanList) {
        this.context = context;
        this.fruitBeanList = fruitBeanList;
    }

    @Override
    public int getCount() {
        return fruitBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return fruitBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class TitleViewHolder{
        TextView titleTextView;
    }

    static class ContentViewHolder{
        TextView contentTextView;
        ImageView contentImageView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TitleViewHolder titleViewHolder;
        ContentViewHolder contentViewHolder;

        if (FruitBean.TYPE_TITLE == getItemViewType(position)) {
            titleViewHolder = new TitleViewHolder();
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.fruit_title_item, null);
                titleViewHolder.titleTextView = convertView.findViewById(R.id.fruit_title_name);
                convertView.setTag(titleViewHolder);
            } else {
                titleViewHolder = (TitleViewHolder) convertView.getTag();
                titleViewHolder.titleTextView.setText(fruitBeanList.get(position).getName());
            }

        } else if (FruitBean.TYPE_CONTENT == getItemViewType(position)) {
            contentViewHolder = new ContentViewHolder();
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.fruit_item, null);
                contentViewHolder.contentTextView = convertView.findViewById(R.id.fruit_name);
                contentViewHolder.contentImageView = convertView.findViewById(R.id.fruit_image);
                convertView.setTag(contentViewHolder);
            } else {
                contentViewHolder = (ContentViewHolder) convertView.getTag();
            }
            contentViewHolder.contentImageView.setImageResource(fruitBeanList.get(position).getImageId());
            contentViewHolder.contentTextView.setText(fruitBeanList.get(position).getName());
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return fruitBeanList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_ITEM_COUNT;
    }


}
