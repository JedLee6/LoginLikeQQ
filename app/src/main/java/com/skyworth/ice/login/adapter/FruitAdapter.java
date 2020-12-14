package com.skyworth.ice.login.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.bean.FruitBean;

import java.util.List;

public class FruitAdapter extends ArrayAdapter<FruitBean> {

    //LisView Item的布局Id
    private int resourceId;


    public FruitAdapter(@NonNull Context context, int resource, @NonNull List<FruitBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    //内部类ViewHolder, 于对控件 实例进行缓存
    class ViewHolder{
        ImageView fruitImageView;
        TextView fruitTextView;
    }

    /**
     * getView的调用时机：当一个的View要显示到屏幕上时，便会调用getView来绘制它
     * 例如初次加载一个ListView，这个ListView在整个屏幕只能显示5个ListView Item，那么就会依次调用5次getView()
     * 当用户上下滑动ListView，新出现的ListView Item都要调用getView()来绘制它
     * @param position 要加载的ListView Item在整个ListView中的位置，起始值为0
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        //getItem()用于获得当前新出现的ListView Item所绑定的FruitBean数据
        FruitBean fruitBean = getItem(position);
        Log.d("TAG", "List View Item "+position);
        if(convertView == null){
            //这个view就是inflate出来的ListView Item的View对象
            //LayoutInflater用来找res/layout/下的xml布局文件，对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.fruitImageView = view.findViewById(R.id.fruit_image);
            viewHolder.fruitTextView = view.findViewById(R.id.fruit_name);
            /* Sets the tag associated with this view. A tag can be used to mark a view in its hierarchy and does not have to be unique within the
             * hierarchy. Tags can also be used to store data within a view without resorting to another data structure.
             */
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        //因为viewHolder中持有了View中ImageView和TextView的引用，因此在这里对引用对象修改就是修改ImageView和TextView本身
        viewHolder.fruitImageView.setImageResource(fruitBean.getImageId());
        viewHolder.fruitTextView.setText(fruitBean.getName());

        return view;
        //return super.getView(position, convertView, parent);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }
}
