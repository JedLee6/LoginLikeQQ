package com.skyworth.ice.login.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.bean.MessageBean;

import java.util.List;

//泛型的返回值便会指定onCreateViewHolder()的返回值类型、onBindViewHolder()中形参的类型
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageBean> messageBeanList;

    public MessageAdapter(List<MessageBean> messageBeanList){
        this.messageBeanList = messageBeanList;
    }

    /*
    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftLinearLayout;
        LinearLayout rightLinearLayout;
        TextView leftMessageTextView;
        TextView rightMessageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftLinearLayout = itemView.findViewById(R.id.linear_layout_left_message);
            rightLinearLayout = itemView.findViewById(R.id.linear_layout_right_message);
            leftMessageTextView = itemView.findViewById(R.id.text_view_left_message);
            rightMessageTextView = itemView.findViewById(R.id.text_view_right_message);
        }
    }
    */

    private static class MyMessageViewHolder extends RecyclerView.ViewHolder{
        TextView rightMessageTextView;

        public MyMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            rightMessageTextView = itemView.findViewById(R.id.text_view_right_message2);
        }
    }

    private static class FriendsMessageViewHolder extends RecyclerView.ViewHolder{

        TextView leftMessageTextView;

        public FriendsMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            leftMessageTextView = itemView.findViewById(R.id.text_view_left_message2);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Log.d("RecyclerView", "onCreateViewHolder Item "+ viewGroup.toString()+" "+i);

        if (i == MessageBean.TYPE_SENT) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_message_item, viewGroup, false);
            return new MyMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_message_item, viewGroup, false);
            return new FriendsMessageViewHolder(view);
        }

        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
        //return new ViewHolder(view);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        Log.d("RecyclerView", "onBindViewHolder Item "+ " "+i);

        if (viewHolder instanceof MyMessageViewHolder) {
            ((MyMessageViewHolder) viewHolder).rightMessageTextView.setText(messageBeanList.get(i).getMessageContent());
        } else {
            ((FriendsMessageViewHolder) viewHolder).leftMessageTextView.setText(messageBeanList.get(i).getMessageContent());
        }

        /*
        MessageBean messageBean = messageBeanList.get(i);
        if (MessageBean.TYPE_RECEIVED == messageBean.getMessageType()) {
            viewHolder.leftLinearLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLinearLayout.setVisibility(View.GONE);
            viewHolder.leftMessageTextView.setText(messageBean.getMessageContent());
        } else if (MessageBean.TYPE_SENT == messageBean.getMessageType()) {
            viewHolder.leftLinearLayout.setVisibility(View.GONE);
            viewHolder.rightLinearLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMessageTextView.setText(messageBean.getMessageContent());
        }
        */
    }

    @Override
    public int getItemCount() {
        return messageBeanList.size();
    }

    /*
     * @param position 数据源的下标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     * getItemViewType()的返回值会影响到onCreateViewHolder()形参i，这个i就代表viewType
     * 因此我们可以重写getItemViewType()使其返回对应的RecyclerView Item的类型
     */
    @Override
    public int getItemViewType(int position) {
        return messageBeanList.get(position).getMessageType();
    }

}
