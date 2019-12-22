package com.example.pillboxesapp.Database;

public class PillBoxEntity {
    private String boxID;
    private String userID;
    private String pillName;
    private int boxNum;
    private int pillStock;

    public PillBoxEntity(String boxID, String userID, int boxNum, String pillName) {
        this.boxID = boxID;
        this.userID = userID;
        this.boxNum = boxNum;
        this.pillName = pillName;
        pillStock = -1;
    }

    public PillBoxEntity(String boxID, String userID, String pillName, int boxNum, int pillStock) {
        this.boxID = boxID;
        this.userID = userID;
        this.boxNum = boxNum;
        this.pillName = pillName;
        this.pillStock = pillStock;
    }

    public String getID() {
        return boxID;
    }

    public String getUserID() {
        return userID;
    }

    public int getBoxNum() {
        return boxNum;
    }

    public String getPillName() {
        return pillName;
    }

    public int getStock() {
        return pillStock;
    }

    public void setBoxNum(int num) {
        boxNum = num;
    }

    public void setPillName(String name) {
        pillName = name;
    }

    public void setStock(int stock) {
        pillStock = stock;
    }
}
