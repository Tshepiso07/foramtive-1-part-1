/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.part1chatapp;

/**
 *
 * @author tshep
 */
import java.util.Random;
import java.util.Scanner;

    public class QuickChat {
        static Scanner sc = new Scanner(System.in);
        static Random rand = new Random();

        // Arrays to store messages - max 100 messages for simplicity
        static String[] messageIDs = new String[100];
        static int[] messageNums = new int[100];
        static String[] recipients = new String[100];
        static String[] messages = new String[100];
        static String[] messageHashes = new String[100];
        static int messageCount = 0;

        public static void main(String[] args) {
            // Requirement 1: Only logged in users can send messages
            if (!login()) {
                System.out.println("Exiting program.");
                return;
            }

            // Requirement 2: Welcome message
            System.out.println("Welcome to QuickChat.");

            // Requirement 5: Set number of messages at start
            int maxMessages = getMessageLimit();

            // Requirement 4: Run until user quits
            boolean running = true;
            while (running) {
                int choice = displayMenu();

                switch (choice) {
                    case 1:
                        // Requirement 3a: Send Messages
                        if (messageCount < maxMessages) {
                            sendMessage(messageCount + 1);
                        } else {
                            System.out.println("You have reached the maximum number of messages.");
                        }
                        break;

                    case 2:
                        // Requirement 3b: Show recently sent messages - Coming Soon
                        System.out.println("Coming Soon.");
                        break;

                    case 3:
                        // Requirement 3c: Quit
                        System.out.println("Exiting QuickChat. Goodbye!");
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }

        // Requirement 1: Login method
        static boolean login() {
            String correctUsername = "admin";
            String correctPassword = "1234";

            System.out.println("--- Login ---");
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                System.out.println("Login successful!\n");
                return true;
            } else {
                System.out.println("Login failed. Incorrect username or password.");
                return false;
            }
        }

        // Requirement 5: Get how many messages user wants to send
        static int getMessageLimit() {
            while (true) {
                System.out.print("How many messages do you want to send in this session? ");
                try {
                    int limit = Integer.parseInt(sc.nextLine());
                    if (limit > 0 && limit <= 100) {
                        return limit;
                    } else {
                        System.out.println("Please enter a number between 1 and 100.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
        }

        // Requirement 3: Display numeric menu
        static int displayMenu() {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        // Requirement 6: Send message and store all required info
        static void sendMessage(int msgNumber) {
            System.out.println("\n--- Message " + msgNumber + " ---");

            // Recipient - must be max 10 characters and have international code
            String recipient;
            while (true) {
                System.out.print("Enter recipient cell number (e.g. +27821234567): ");
                recipient = sc.nextLine();
                if (recipient.length() <= 10 && recipient.startsWith("+")) {
                    break;
                } else {
                    System.out.println("Invalid number. Must be max 10 characters and start with '+'");
                }
            }

            // Message - max 250 characters
            String messageText;
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = sc.nextLine();
                if (messageText.length() <= 250) {
                    System.out.println("Message sent!");
                    break;
                } else {
                    System.out.println("Please enter a message of less than 250 characters.");
                }
            }

            // Generate Unique Message ID - 10 digit random number
            String messageID = generateMessageID();

            // Generate Message Hash
            String hash = generateMessageHash(messageID, msgNumber, messageText);

            // Store in arrays
            messageIDs[messageCount] = messageID;
            messageNums[messageCount] = msgNumber;
            recipients[messageCount] = recipient;
            messages[messageCount] = messageText;
            messageHashes[messageCount] = hash;
            messageCount++;

            // Display confirmation
            System.out.println("\n--- Message Details ---");
            System.out.println("Message ID: " + messageID);
            System.out.println("Message Number: " + msgNumber);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + messageText);
            System.out.println("Message Hash: " + hash);
        }

        // Generate 10-digit random Message ID
        static String generateMessageID() {
            String id = "";
            for (int i = 0; i < 10; i++) {
                id += rand.nextInt(10);
         }
            return id;
        }

        // Generate Message Hash: first 2 digits of ID : message number : FIRST LAST word in CAPS
        static String generateMessageHash(String id, int msgNum, String messageText) {
            String firstTwoDigits = id.substring(0, 2);

            String[] words = messageText.trim().split("\\s+");
            String firstWord = words[0].toUpperCase();
            String lastWord = words[words.length - 1].toUpperCase();

            return firstTwoDigits + ":" + msgNum + ":" + firstWord + lastWord;
        }
    }
   