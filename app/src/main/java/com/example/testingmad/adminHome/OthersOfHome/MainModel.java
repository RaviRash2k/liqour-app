package com.example.testingmad.adminHome.OthersOfHome;

public class MainModel {

    String name, position, email, image;

    public MainModel(String name, String position, String email, String image) {
        this.name = name;
        this.position = position;
        this.email = email;
        this.image = image;
    }

    public MainModel() {
    }


    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
