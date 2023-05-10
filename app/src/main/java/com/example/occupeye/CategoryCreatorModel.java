package com.example.occupeye;

public class CategoryCreatorModel {
    String roomName;
    int image;
    String colour;

    String roomType;

    public CategoryCreatorModel(String roomName, int image, String colour, String roomType) {
        this.roomName = roomName;
        this.image = image;
        this.colour = colour;
        this.roomType = roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getColour() {
        return colour;
    }

    public int getImage() {
        return image;
    }

    public String getRoomType(){
        return roomType;
    }
}
