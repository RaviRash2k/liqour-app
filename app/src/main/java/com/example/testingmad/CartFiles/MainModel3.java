package com.example.testingmad.CartFiles;

public class MainModel3 {

    String itemName, itemPrice, itmImage, itemQty;

    //Setters
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


    //Getters
    public String getName() {
        return itemName;
    }

    public String getPrice() {
        return itemPrice;
    }

    public String getImage() {
        return itmImage;
    }

    public String getItemQty() {
        return itemQty;
    }
}
