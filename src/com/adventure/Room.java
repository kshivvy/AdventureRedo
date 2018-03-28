package com.adventure;

import java.util.ArrayList;
import java.util.Arrays;

public class Room {

    private String name;
    private String description;
    private Direction[] directions;
    private ArrayList<String> items;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void addItem(String item) {
        items.add(item);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if(obj == null || obj.getClass()!= this.getClass()) {
            return false;
        }

        Room room = (Room) obj;

        //Checks that all the member variables of parameter com.adventure.Room object are equal to this instance.
        if (room.getName().equals(this.getName()) && room.getDescription().equals(this.getDescription())
                && Arrays.deepEquals(room.getDirections(), this.getDirections())) {
            return true;
        }
        return false;
    }
}
