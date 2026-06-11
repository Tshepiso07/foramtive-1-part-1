/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.part1chatapp;

/**
 *
 * @author tshep
 */


    {    
import java.util.Scanner;

 public class MainApp {   
     public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Login loginSystem = new Login();

        // -------------------------------------------------------
        // PART 1 - REGISTRATION
        // -------------------------------------------------------
        System.out.println("=== Welcome to QuickChat ===");
        System.out.println("\n--- Register ---");

        System.out.print("Enter username (must contain _ and be max 5 characters): ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter cell number (e.g. +27831234567): ");
        String cellNumber = input.nextLine();

        String registerResult = loginSystem.registerUser(username, password, cellNumber);
        System.out.println(registerResult);

        // Stop if registration failed
        if (!registerResult.contains("successfully")) {
            System.out.println("Registration failed. Please restart and try again.");
            input.close();
            return;
        }

        // -------------------------------------------------------
        // PART 1 - LOGIN
        // -------------------------------------------------------
        System.out.println("\n--- Login ---");

        System.out.print("Username: ");
        String loginUser = input.nextLine();

        System.out.print("Password: ");
        String loginPass = input.nextLine();

        boolean loginResult = loginSystem.loginUser(loginUser, loginPass);
        System.out.println(loginResult);

        // Stop if login failed
       if (!loginResult) { 
    System.out.println("Login failed. Please restart and try again.");
    input.close();
    return;
        }

        // -------------------------------------------------------
        // PART 3 - Load stored messages from JSON right after login
        // Fills the storedMessages array before the menu appears
        // -------------------------------------------------------
        Message.loadStoredMessages();

        // -------------------------------------------------------
        // PART 2 + PART 3 - MAIN MENU LOOP
        // -------------------------------------------------------
        System.out.println("Welcome to ChatApp.");
        boolean running = true;

        while (running) {

            System.out.println("\n1) Send Messages");
            System.out.println("2) View Messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 1 and 4.");
                continue;
            }

            switch (choice) {

                case 1:
                    // Message Entry Loop Setup
                    System.out.println("How many messages would you like to send?");
                    int numMessages;
                    try {
                        numMessages = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Returning to menu.");
                        break;
                    }

                    int sentCount = 0;

                    for (int i = 0; i < numMessages; i++) {
                        // Calculate human-readable message sequence increment
                        int messageNumber = i + 1;
                        System.out.println("\n--- Message " + messageNumber + " of " + numMessages + " ---");

                        System.out.print("Enter Recipient Cell Number: ");
                        String recipient = input.nextLine();

                        System.out.print("Enter Message Text (Max 250 characters): ");
                        String messageText = input.nextLine();

                        // Construct Message object - ID and hash are generated automatically
                        Message tracker = new Message(recipient, messageText);

                        // Validate length and recipient format
                        System.out.println(tracker.checkMessageLength());
                        System.out.println(tracker.checkRecipientCell());

                        // Show the generated ID and hash
                        System.out.println("Message ID:   " + tracker.getMessageID());
                        System.out.println("Message Hash: " + tracker.getMessageHash());

                        // Present sub-menu options for message action
                        System.out.println("Select Action State:\n1. Send\n2. Disregard\n3. Store");
                        int subChoice;
                        try {
                            subChoice = Integer.parseInt(input.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid choice. Skipping message.");
                            continue;
                        }

                        // Map the number choice to the string sentMessage() expects
                        String action;
                        switch (subChoice) {
                            case 1:  action = "send";    break;
                            case 2:  action = "discard"; break;
                            case 3:  action = "store";   break;
                            default: action = "discard"; break;
                        }

                        // Process the message and show result
                        System.out.println(tracker.sentMessage(action));

                        if (action.equals("send")) {
                            sentCount++;
                        }

                        // Show message summary after each message
                        System.out.println("\n--- Message Summary ---");
                        System.out.println(Message.printMessages());
                        System.out.println("-----------------------\n");
                    }

                    // Report total messages sent
                    System.out.println("Total messages sent: " + sentCount + " out of " + numMessages + ".");
                    break;

                case 2:
                    // Show full report of all sent messages
                    System.out.println(Message.printMessages());
                    break;

                case 3:
                    running = false;
                    System.out.println("Exiting ChatApp. Goodbye!");
                    break;

                case 4:
                    // Open the stored messages sub-menu - Part 3
                    storedMessagesMenu(input);
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
                    break;
            }
        }

        input.close();
    }

    // -------------------------------------------------------
    // PART 3 - STORED MESSAGES SUB-MENU
    // -------------------------------------------------------

    /**
     * Displays the stored messages sub-menu and handles each option.
     * Each option calls the matching method in the Message class.
     *
     * @param input the shared Scanner reading user input
     */
    private static void storedMessagesMenu(Scanner input) {

        // Fresh Message instance to call instance methods
        // Constructor does NOT touch any arrays so this is safe
        Message helper = new Message("0000000000", "helper");

        int subChoice = 0;

        while (subChoice != 7) {

            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("1) Display all stored messages");
            System.out.println("2) Display longest message");
            System.out.println("3) Search by message ID");
            System.out.println("4) Search by recipient");
            System.out.println("5) Delete message by hash");
            System.out.println("6) Display full message report");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");

            try {
                subChoice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 1 and 7.");
                continue;
            }

            switch (subChoice) {

                case 1:
                    // Show every message loaded from the JSON file
                    java.util.List<String> stored = Message.getStoredMessages();
                    if (stored.isEmpty()) {
                        System.out.println("No stored messages found.");
                    } else {
                        System.out.println("\n--- All Stored Messages ---");
                        for (int i = 0; i < stored.size(); i++) {
                            System.out.println((i + 1) + ") " + stored.get(i));
                        }
                    }
                    break;

                case 2:
                    // Find and print the longest message in storedMessages
                    System.out.println(helper.displayLongestMessage());
                    break;

                case 3:
                    // User types an ID and we find the matching sent message
                    System.out.print("Enter message ID to search for: ");
                    String searchID = input.nextLine();
                    System.out.println(helper.searchByMessageID(searchID));
                    break;

                case 4:
                    // User types a recipient number and we find all their messages
                    System.out.print("Enter recipient number to search for: ");
                    String searchRecipient = input.nextLine();
                    System.out.println(helper.searchByRecipient(searchRecipient));
                    break;

                case 5:
                    // User types a hash and we delete that message from all arrays
                    System.out.print("Enter message hash to delete: ");
                    String hashToDelete = input.nextLine();
                    System.out.println(helper.deleteByHash(hashToDelete));
                    break;

                case 6:
                    // Print the full formatted report of all sent messages
                    System.out.println(Message.printMessages());
                    break;

                case 7:
                    // Return to the main menu
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1 to 7.");
            }
        }
    }
