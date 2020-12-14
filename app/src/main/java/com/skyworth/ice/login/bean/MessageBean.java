package com.skyworth.ice.login.bean;

public class MessageBean {
    public static final int TYPE_SENT = 0;
    public static final int TYPE_RECEIVED = 1;
    private int messageType;
    private String messageContent;

    public MessageBean(String messageContent, int messageType){
        this.messageContent = messageContent;
        this.messageType = messageType;
    }

    public String getMessageContent(){
        return messageContent;
    }

    public int getMessageType(){
        return messageType;
    }
}
