package com.skyworth.ice.login.ui;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skyworth.ice.login.R;
import com.skyworth.ice.login.adapter.MessageAdapter;
import com.skyworth.ice.login.bean.MessageBean;
import com.skyworth.ice.login.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class DialogActivity extends BaseActivity {

    private List<MessageBean> messageBeanList = new ArrayList<>();
    private EditText dialogEditText;
    private Button sendDialogButton;
    private Button replyDialogButton;
    private RecyclerView dialogRecycleView;
    private MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        dialogEditText = findViewById(R.id.edit_text_dialog);
        sendDialogButton = findViewById(R.id.button_send_dialog);
        replyDialogButton = findViewById(R.id.button_reply_dialog);
        dialogRecycleView = findViewById(R.id.recycler_view_dialog);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        dialogRecycleView.setLayoutManager(linearLayoutManager);
        final MessageAdapter messageAdapter = new MessageAdapter(messageBeanList);
        dialogRecycleView.setAdapter(messageAdapter);

        sendDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = dialogEditText.getText().toString();
                if(!"".equals(messageContent)){
                    MessageBean messageBean = new MessageBean(messageContent, MessageBean.TYPE_SENT);
                    messageBeanList.add(messageBean);
                    //当有新消息时，刷新RecyclerView，以定位到最后一行
                    messageAdapter.notifyItemInserted(messageBeanList.size() - 1);
                    //messageAdapter.notifyItemChanged(messageBeanList.size()-1);
                    //将RecyclerView定位到最后一行
                    dialogRecycleView.scrollToPosition(messageBeanList.size() - 1);
                    //清空EditText中的内容
                    dialogEditText.setText("");
                }
            }
        });

        replyDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = dialogEditText.getText().toString();
                if(!"".equals(messageContent)){
                    MessageBean messageBean = new MessageBean(messageContent, MessageBean.TYPE_RECEIVED);
                    messageBeanList.add(messageBean);
                    //当有新消息时，刷新RecyclerView，以定位到最后一行
                    messageAdapter.notifyItemInserted(messageBeanList.size() - 1);
                    //将RecyclerView定位到最后一行
                    dialogRecycleView.scrollToPosition(messageBeanList.size() - 1);
                    //清空EditText中的内容
                    dialogEditText.setText("");
                }
            }
        });

    }
}