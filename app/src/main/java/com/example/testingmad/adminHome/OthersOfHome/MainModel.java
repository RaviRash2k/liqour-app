package com.example.testingmad.adminHome.OthersOfHome;

public class MainModel {

    String itemName, itemPrice, itmImage, itemQty;

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItmImage(String itmImage) {
        this.itmImage = itmImage;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemQty() {
        return itemQty;
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