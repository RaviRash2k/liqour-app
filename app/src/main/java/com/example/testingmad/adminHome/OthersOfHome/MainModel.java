package com.example.testingmad.adminHome.OthersOfHome;

public class MainModel {

    String itemName, itemPrice, itmImage;

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItmImage(String itmImage) {
        this.itmImage = itmImage;
    }

    public String getName() {
        return itemName;
    }

    public String getPrice() {
        return itemPrice;
    }

    public String getImage() {
        return itmImage;
    }
}
