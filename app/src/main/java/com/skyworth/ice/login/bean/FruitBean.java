package com.skyworth.ice.login.bean;

public class FruitBean {

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT = 1;


    private int type;
    private String name;
    private int imageId;
    public FruitBean(String name, int imageId, int type){
        this.name = name;
        this.imageId = imageId;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
