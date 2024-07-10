package com.example.testingmad.adminHome.OthersOfHome;

public class MainModel {

    String itemName, itemPrice, itmImage, itemQty, seller, itemCode;

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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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
