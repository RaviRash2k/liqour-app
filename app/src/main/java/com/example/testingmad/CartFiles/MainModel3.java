package com.example.testingmad.CartFiles;

public class MainModel3 {

    String itemName, itemPrice, itmImage, itemQty, cartCode, itemCode ;

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

    public void setCartCode(String cartCode) {
        this.cartCode = cartCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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
    public String getCartCode() {
        return cartCode;
    }

    public String getItemCode() {
        return itemCode;
    }
}
