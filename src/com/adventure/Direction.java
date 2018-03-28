package com.adventure;

public class Direction {

    private String directionName;
    private String room;

    public java.lang.String getDirectionName() {
        return directionName;
    }

    public String getRoom() {
        return room;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if(obj == null || obj.getClass()!= this.getClass()) {
            return false;
        }

        Direction direction = (Direction) obj;

        //Checks that all the member variables of the parameter com.adventure.Direction object are equal to this com.adventure.Direction.
        if (direction.directionName.equals(this.directionName) && direction.room.equals(this.room)) {
            return true;
        }
        return false;
    }
}
