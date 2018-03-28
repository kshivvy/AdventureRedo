package com.adventure;

import com.adventure.Adventure;
import com.adventure.ErrorConstants;
import com.adventure.Room;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class AdventureTest {

    Adventure testGame;
    Room[] rooms;

    @Before
    public void setUp() {
        testGame = new Adventure("https://courses.engr.illinois.edu/cs126/adventure/siebel.json");
        rooms = testGame.getLayout().getRooms();
    }

    /**
     * trimCommandFromInput(String userInput, String command) test suite.
     */

    //Test "go" command
    @Test
    public void testTrimInputOfCommandGo() {
        assertEquals("East", testGame.trimCommandFromInput("go East", "go "));
    }

    //Test single word command "quit", covers "exit" and "list" cases
    @Test
    public void testTrimInputOfSingleWordCommand() {
        assertEquals("quit", Adventure.trimCommandFromInput("quit", "quit"));
    }

    //Test for input with no commands
    @Test
    public void testTrimInputOfCommandWithNoCommandInInput() {
        assertEquals("no commands in this input", Adventure.trimCommandFromInput
                ("no commands in this input", "go "));
    }


    //Test for null command
    @Test
    public void testTrimInputOfNullCommand() {
        try {
            String expected = Adventure.trimCommandFromInput("random input", null);
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    //Test for null user input
    @Test
    public void testTrimInputOfCommandOnNullInput  () {
        try {
            String expected = Adventure.trimCommandFromInput(null, "random command");
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    /**
     * Test suite for getCommandFromInput(String userInput)
     */

    //Test for null user input
    @Test
    public void testParseInputForCommandNullInput() {
        try {
            String expected = Adventure.getCommandFromInput(null);
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    //Test for "quit" command
    @Test
    public void testParseInputForCommandQuit() {
        assertEquals("quit",Adventure.getCommandFromInput("quit"));
    }

    //Test for "exit" command
    @Test
    public void testParseInputForCommandExit() {
        assertEquals("exit",Adventure.getCommandFromInput("exit"));
    }

    //Test for "go" command
    @Test
    public void testParseInputForCommandGo() {
        assertEquals("go ",Adventure.getCommandFromInput("go Northwest"));
    }

    //Test for "list" command
    @Test
    public void testParseInputForCommandList() {
        assertEquals("list",Adventure.getCommandFromInput("list"));
    }

    //Test for "take" command
    @Test
    public void testParseInputForCommandDrop() {
        assertEquals("drop ",Adventure.getCommandFromInput("drop coin"));
    }

    //Test for "list" command
    @Test
    public void testParseInputForCommandTake() {
        assertEquals("take ",Adventure.getCommandFromInput("take keys"));
    }

    //Test for no commands
    @Test
    public void testParseInputForNoCommandInInput() {
        assertEquals("no commands in this user input",Adventure.getCommandFromInput("no commands in this user input"));

    }

    /**
     * Test suite for goADirection(String desiredDirection, com.adventure.Room currentRoom)
     */

    //Test for valid direction
    @Test
    public void testGoADirectionValidDirection() {
        //com.adventure.Room with only valid direction east
        Room testRoom = rooms[0];
        Room expected = rooms[1];

        assertEquals(expected, Adventure.goADirection("East", testRoom));
    }

    //Test for invalid direction
    @Test
    public void testGoADirectionInvalidDirection() {
        //com.adventure.Room with only valid direction east
        Room testRoom = rooms[0];

        assertEquals(testRoom, Adventure.goADirection("Invalid com.adventure.Direction", testRoom));
    }

    //Test for null com.adventure.Room
    @Test
    public void testGoADirectionNullRoom() {
        try {
            Room expected = Adventure.goADirection("East", null);
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    //Test for null direction
    @Test
    public void testGoADirectionNullDirection() {
        Room testRoom = rooms[0];

        try {
            Room expected = Adventure.goADirection(null, testRoom);
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    /**
     * Test suite for isValidItemToTake(String desiredItem, com.adventure.Room currentRoom)
     */

    //Test for valid item in room.
    @Test
    public void testIsValidItemToTakeItemInRoom() {
        Room testRoom = rooms[0];

        assertEquals(true, Adventure.isValidItemToTake("coin", rooms[0]));
    }

    //Test that valid item was added to inventory.
    @Test
    public void testIsValidItemToTakeItemAddedToInventory() {
        Room testRoom = rooms[0];
        Adventure.isValidItemToTake("coin", rooms[0]);

        assertEquals(true, Adventure.getInventory().contains("coin"));
    }

    //Test that valid item was removed from room.
    @Test
    public void testIsValidItemToTakeItemRemovedFromRoom() {
        Adventure.isValidItemToTake("coin", rooms[0]);
        Room testRoom = rooms[0];

        assertEquals(false, testRoom.getItems().contains("coin"));
    }

    //Test for inavlid item not in room.
    @Test
    public void testIsValidItemToTakeItemInNotRoom() {

        assertEquals(false, Adventure.isValidItemToTake("keys", rooms[0]));
    }

    //Test that inavlid item was not added to inventory..
    @Test
    public void testIsValidItemToTakeItemNotAddedToInventory() {
        Adventure.isValidItemToTake("keys", rooms[0]);

        assertEquals(false, Adventure.getInventory().contains("keys"));
    }

    //Test that room's items did not change.
    @Test
    public void testIsValidItemToTakeItemNotRemovedFromRoom() {
        Room testRoom = rooms[0];
        ArrayList<String> expected = testRoom.getItems();
        Adventure.isValidItemToTake("keys", testRoom);

        assertEquals(expected, testRoom.getItems());
    }

    //Test for null room.
    @Test
    public void testIsValidItemToTakeNullRoom() {
        try {
            boolean expected = Adventure.isValidItemToTake("coin", null);
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    //Test for null item.
    @Test
    public void testIsValidItemToTakeNullItem() {
        Room testRoom = rooms[0];

        try {
            boolean expected = Adventure.isValidItemToTake(null, testRoom);
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    /**
     * Test suite for isValidItemToDrop(String droppedItem, com.adventure.Room currentRoom)
     */

    //Test for null dropped item
    @Test
    public void testIsValidItemToDropNullItem() {
        Room testRoom = rooms[0];

        try {
            boolean expected = Adventure.isValidItemToDrop(null, testRoom);
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    //Test for null room.
    @Test
    public void testIsValidItemToDropNullRoom() {
        try {
            boolean expected = Adventure.isValidItemToDrop("coin", null);
            fail("Exception not thrown");
        } catch(IllegalArgumentException ex) {
            String error = ex.getMessage();
            assertEquals(ErrorConstants.NULL_INPUT, error);
        }
    }

    //Test for valid item to drop
    @Test
    public void testIsValidItemToDropValidItem() {
        Room testRoom = rooms[0];
        Adventure.isValidItemToTake("coin", testRoom);

        assertEquals(true, Adventure.isValidItemToDrop("coin", testRoom));
    }

    //Test that valid item was removed from inventory
    @Test
    public void testIsValidItemToDropItemRemovedFromInventory() {
        Room testRoom = rooms[0];
        Adventure.isValidItemToDrop("coin", testRoom);

        assertEquals(false, Adventure.getInventory().contains("coin"));
    }

    //Test that valid item was dropped to room
    @Test
    public void testIsValidItemToDropItemAddedToRoom() {
        Adventure.isValidItemToDrop("coin", rooms[0]);
        Room testRoom = rooms[0];

        assertEquals(true, testRoom.getItems().contains("coin"));
    }

    //Test for invalid item to drop
    @Test
    public void testIsValidItemToDropInvalidItem() {
        Room testRoom = rooms[0];

        assertEquals(false, Adventure.isValidItemToDrop("keys", testRoom));
    }

    //Test that invalid item was not dropped to room
    @Test
    public void testIsValidItemToDropItemNotAddedToRoom() {
        Adventure.isValidItemToDrop("keys", rooms[0]);
        Room testRoom = rooms[0];

        assertEquals(false, testRoom.getItems().contains("keys"));
    }

    /**
     *Test suite for isInventoryEmpty();
     */

    //Test for empty inventory
    @Test
    public void testIsInventoryEmpty() {
        assertEquals(false, Adventure.isInventoryEmpty());
    }
}