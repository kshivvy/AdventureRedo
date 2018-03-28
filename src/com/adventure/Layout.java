package com.adventure;

import java.util.Arrays;

public class Layout {
    private String startingRoom;
    private String endingRoom;
    private Room[] rooms;

    Layout(String start, String end, Room[] roomsArray) {
        this.startingRoom = start;
        this.endingRoom = end;
        this.rooms = roomsArray;
    }

    public String getStartingRoom() {
        return startingRoom;
    }

    public String getEndingRoom() {
        return endingRoom;
    }

    public Room[] getRooms() {
        return rooms;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if(obj == null || obj.getClass()!= this.getClass()) {
            return false;
        }

        Layout layout = (Layout) obj;

        //Checks that all the member variables of the com.adventure.Layout object are equal to this instance.
        if (layout.startingRoom.equals(this.startingRoom) && layout.endingRoom.equals(this.endingRoom)
                && Arrays.deepEquals(layout.rooms, this.rooms)) {
            return true;
        }
        return false;
    }
}
