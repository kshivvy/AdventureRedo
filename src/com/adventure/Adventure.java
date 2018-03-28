package com.adventure;

import jdk.nashorn.internal.parser.JSONParser;

import java.util.ArrayList;
import java.util.Scanner;

public class Adventure {

    private static Layout world = null;
    private static ArrayList<String> inventory = new ArrayList<>();
    private static String DEFAULT_URL = "https://courses.engr.illinois.edu/cs126/adventure/siebel.json";
    private static String DEFAULT_FILE_PATH = "Siebel.json";

    /**
     * Sets up com.adventure.Layout object for gameplay.
     * @param jsonPath - String url to construct the com.adventure.adventure and com.adventure.Layout classes with.
     */
    public Adventure(String jsonPath) {
        world = JsonParser.getJsonObjectFromUrl(jsonPath);
        if (world == null) {
            world = JsonParser.getJsonObjectFromFile(jsonPath);
        }
    }

    public Adventure() {
        world = JsonParser.getJsonObjectFromFile(DEFAULT_FILE_PATH);
    }

    /**
     * Sets up gameplay by generating URL and creating new com.adventure.adventure object.
     * @param args - Standard input for main function.
     */
    public static void main(String[] args) {
        if (args.length == 0 || (JsonParser.getJsonObjectFromUrl(args[0])
                == null && JsonParser.getJsonObjectFromFile(args[0]) == null)) {
            System.out.println("Command line argument was invalid. Loading default .json");
            Adventure game = new Adventure();
        } else {
            Adventure game = new Adventure(args[0]);
        }

        /**
        String url = getUrl();

        while (JsonParser.getJsonObjectFromUrl(url) == null) {
            url = getUrl();
        }

        Adventure game = new Adventure(url);

        */
        loadGame();
    }

    /**
     * Generates default or user specified url.
     * @return URL as a string
     */
    private static String getUrl() {

        System.out.println();
        System.out.println("Do you want to use the default url? [Y / N]");

        String inputUrl = readInput();

        if (inputUrl.equalsIgnoreCase("n")) {
            System.out.println("Please input a url.");
            return readInput();
        } else if ((inputUrl.equalsIgnoreCase("y"))){
            return DEFAULT_URL;
        } else {
            return getUrl();
        }
    }

    /**
     * Generates starting room and takes user input until the current room is the end room.
     */
    private static void loadGame() {

        Room startRoom = null;
        Room endRoom = null;
        Room currentRoom = null;

        //Populates end room and start room.
        for (Room room : world.getRooms()) {
            if (room.getName().equals(world.getStartingRoom())) {
                startRoom = room;
            }
            else if (room.getName().equals(world.getEndingRoom())) {
                endRoom = room;
            }
        }

        currentRoom = startRoom;

        //Exits for null start room.
        if (startRoom == null) {
            System.out.println("The layout JSON is not valid. No start room is specified.");
            System.exit(0);
        }

        //Exits for null end room.
        if (endRoom == null) {
            System.out.println("The layout JSON is not valid. No end room is specified.");
            System.exit(0);
        }

        //Takes user movement input to set room until they reach then end room.
        while (currentRoom != endRoom) {
            printRoom(currentRoom);
            String input = readInput();
            currentRoom = getNextRoom(input, currentRoom);
        }

        printRoom(currentRoom);
        System.out.println("You have reached your final destination.");
    }

    /**
     * Prints out description of current room to user.
     * @param currentRoom - com.adventure.Room to get attributes (name, description, etc.) from
     */
    private static void printRoom(Room currentRoom){

        //Ends game instead of throwing exception to user
        if (currentRoom == null) {
            System.exit(0);
        }

        System.out.println();
        System.out.println(currentRoom.getDescription());

        //Checks if game is starting or ending.
        if (currentRoom.getName().equals(world.getStartingRoom())) {
            System.out.println("Your journey begins here");
        }
        if (currentRoom.getName().equals(world.getEndingRoom())) {
            System.out.println("You have reached your final destination");
        }

        ArrayList<String> items = currentRoom.getItems();

        //Prints items availible in room
        if(items != null && items.size() != 0) {
            System.out.print("This room contains: ");
            for (int i = 0; i < items.size(); i++) {
                System.out.print(items.get(i));

                if (i < items.size() - 2) {
                    System.out.print(", ");
                }
                else if (i == items.size() - 2){
                    System.out.print(" and ");
                }
            }
        }
        else {
            System.out.print("This room contains nothing.");
        }

        System.out.println();
        Direction[] directions = currentRoom.getDirections();

        //Prints directions user can move in
        if (directions != null && directions.length != 0) {
            System.out.print("From here, you can go: ");
            for (int i = 0; i < directions.length; i++) {
                System.out.print(directions[i].getDirectionName());

                if (i < directions.length - 2) {
                    System.out.print(", ");
                }
                else if (i == directions.length - 2){
                    System.out.print(" or ");
                }
            }
        }

        System.out.println();
    }

    /**
     * Takes in user input.
     * Code derived from: https://stackoverflow.com/questions/30249324/how-to-get-java-to-wait-for-user-input
     * @return - User input as a String
     */
    private static String readInput() {
        String input = "";
        Scanner userInput = new Scanner(System.in);

        //Waits for user to type something.
        while(true) {
            input = userInput.nextLine();
            if(!input.isEmpty()) {
                return input;
            }
        }
    }

    /**
     * Parses the user input for commands like "quit" and "go" and calls appropriate methods.
     * @param userInput - Commands inputted by user.
     * @param currentRoom - com.adventure.Room where user is in.
     * @return - The com.adventure.Room the player has arrived at.
     */
    public static Room getNextRoom(String userInput, Room currentRoom) {

        String command = getCommandFromInput(userInput);
        String originalInput = userInput;

        userInput = trimCommandFromInput(userInput, command);

        //Checks if user invoked called quit and exit commands
        if (command.equals("quit") || command.equals("exit")) {
            System.exit(0);
        }

        //Checks if user invoked movement command
        else if (command.equals("go ")) {
            return goADirection(userInput, currentRoom);

        }

        //Checks if user invoked list commmand
        else if(command.equals("list")) {
            if (isInventoryEmpty()) {
                System.out.println("No items carried.");
            }
        }

        //Checks if user invoked drop item command
        else if(command.equals("drop ")) {
            if (isValidItemToDrop(userInput, currentRoom)) {
                System.out.println("You dropped " + userInput + ".");
            } else {
                System.out.println("I can't drop " + userInput + ".");
            }
        }

        //Checks if user invoked take item command
        else if (command.equals("take ")){
            if (isValidItemToTake(userInput, currentRoom)) {
                System.out.println("You picked up: " + userInput);
            } else {
                System.out.println("I can't take " + userInput + ".");
            }
        }

        else {
            System.out.println("I don't understand: '" + originalInput + "'" );
        }

        return currentRoom;
    }

    /**
     * Returns recognized commands from user input.
     * @param userInput - user input
     * @param command - command to remove
     * @return - trimmed input
     */
    public static String trimCommandFromInput(String userInput, String command) {

        if (userInput == null || command == null) {
            throw new IllegalArgumentException(ErrorConstants.NULL_INPUT);
        }

        String output = userInput;

        if (userInput.equals(command)) {
            return output;
        }

        output = userInput.replace(command, "");
        output = output.trim();
        return output;
    }

    /**
     * Parses user input for command keywords and returns them.
     * @param userInput - user input
     * @return command keyword if found in input, else the original input
     */
    public static String getCommandFromInput(String userInput) {

        if (userInput == null) {
            throw new IllegalArgumentException(ErrorConstants.NULL_INPUT);
        }

        //Check's if user invoked quit or exitor
        if (userInput.trim().equals("quit") || userInput.trim().equals("exit")) {
            return userInput;
        }

        //Checks if user invoked movement command
        else if (userInput.indexOf("go ") == 0) {
            return "go ";
        }

        //Checks if user invoked list commmand
        else if(userInput.equalsIgnoreCase("list")) {
            return "list";
        }

        //Checks if user invoked drop item command
        else if(userInput.indexOf("drop ") == 0) {
            return "drop ";
        }

        //Checks if user invoked take item command
        else if (userInput.indexOf("take ") == 0){
            return "take ";
        }

        return userInput;
    }

    /**
     * Returns the com.adventure.Room when the user moves in a direction. If a direction is invalid,
     * returns the current com.adventure.Room.
     * @param desiredDirection - com.adventure.Direction to move inputted by user
     * @param currentRoom - com.adventure.Room where user is in.
     * @return - The room to which the user moved to.
     */
    public static Room goADirection(String desiredDirection, Room currentRoom) {

        if (desiredDirection == null || currentRoom == null) {
            throw new IllegalArgumentException(ErrorConstants.NULL_INPUT);
        }

        Direction[] availibleDirections = currentRoom.getDirections();

        //Checks if direction to move is valid, and returns the next com.adventure.Room if it is.
        for (Direction direction : availibleDirections) {
            if (desiredDirection.equalsIgnoreCase(direction.getDirectionName())) {
                String nextRoomString = direction.getRoom();
                for (Room room : world.getRooms()) {
                    if (nextRoomString.equals(room.getName())) {
                        return room;
                    }
                }
            }
        }

        System.out.println("I can't go " + desiredDirection + ".");
        return  currentRoom;
    }

    /**
     * Prints out items carried to the user.
     */
    public static boolean isInventoryEmpty() {
        System.out.println();
        if (inventory.isEmpty()) {
            return true;
        } else {
            System.out.println("Items carried: ");
            for (String item : inventory) {
                System.out.println(item);
            }
            return false;
        }
    }

    /**
     * Drops an item from the inventory in the com.adventure.Room.
     * @param droppedItem - item to be dropped from inventory
     * @param currentRoom - com.adventure.Room where user is in.
     */
    public static boolean isValidItemToDrop(String droppedItem, Room currentRoom) {

        if (droppedItem == null || currentRoom == null) {
            throw new IllegalArgumentException(ErrorConstants.NULL_INPUT);
        }

        if (inventory.contains(droppedItem)) {
            inventory.remove(droppedItem);
            currentRoom.addItem(droppedItem);
            return true;
        }

        return false;
    }

    /**
     * Places an item into the inventory and removes it from the com.adventure.Room.
     * @param desiredItem - item to take from com.adventure.Room.
     * @param currentRoom - com.adventure.Room where user is in.
     * @return - true if item is in room, else false if it is not
     */
    public static boolean isValidItemToTake(String desiredItem, Room currentRoom) {

        if (desiredItem == null || currentRoom == null) {
            throw new IllegalArgumentException(ErrorConstants.NULL_INPUT);
        }

        for (String item : currentRoom.getItems()) {
            if (desiredItem.equalsIgnoreCase(item)) {
                inventory.add(item);
                currentRoom.getItems().remove(item);
                return true;
            }
        }

        return false;
    }

    public Layout getLayout() {
        return world;
    }

    public static ArrayList<String> getInventory() {
        return inventory;
    }
}
