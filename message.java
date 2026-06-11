/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.part1chatapp;

import java.io.BufferedReader;
import java.io.FileReader;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author tshep
 */
public class message {
    public class Message {

    // -------------------------------------------------------
    // FIELDS - stores info for one single message
    // -------------------------------------------------------
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

    // -------------------------------------------------------
    // PART 3 - STATIC ARRAYS
    // Static means shared across all Message objects and alive
    // for the whole session.
    //
    // sentMessages, recipientList, and sentMessageIDs
    // are all only populated when the user picks SEND.
    // This keeps them the same size so indexes always match.
    //
    // messageHashes and messageIDs store info for EVERY message
    // processed regardless of send/store/discard.
    // -------------------------------------------------------
    private static List<String> sentMessages        = new ArrayList<>(); // text of every SENT message
    private static List<String> disregardedMessages = new ArrayList<>(); // text of every DISCARDED message
    private static List<String> storedMessages      = new ArrayList<>(); // text loaded back from JSON file
    private static List<String> messageHashes       = new ArrayList<>(); // hash for every message processed
    private static List<String> messageIDs          = new ArrayList<>(); // ID for every message processed
    private static List<String> recipientList       = new ArrayList<>(); // recipient for every SENT message
    private static List<String> sentMessageIDs      = new ArrayList<>(); // ID for every SENT message only

    // -------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------

    /**
     * Creates a new Message and automatically generates an ID and hash.
     * The constructor does NOT touch any arrays - only sentMessage() does.
     *
     * @param recipient   the cell number this message is sent to
     * @param messageText the content of the message
     */
    public Message(String recipient, String messageText) {
        this.recipient   = recipient;
        this.messageText = messageText;
        this.messageID   = generateMessageID();
        this.messageHash = generateHash();
    }

    // -------------------------------------------------------
    // PART 1 - VALIDATION METHODS
    // -------------------------------------------------------

    /**
     * Checks that the message is not longer than 250 characters.
     *
     * @return true if the message length is valid
     */
    public boolean checkMessageLength() {
        return messageText.length() <= 250;
    }

    /**
     * Checks that the recipient number starts with + and is max 11 characters.
     *
     * @return true if the recipient number is correctly formatted
     */
    public boolean checkRecipientCell() {
        return recipient.startsWith("+") && recipient.length() <= 11;
    }

    /**
     * Generates a random 10-digit number to use as the message ID.
     *
     * @return the generated ID as a String
     */
    private String generateMessageID() {
        Random rand = new Random();
        long id = (long) (rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    /**
     * Builds a hash from the message ID and text.
     * Format: first 2 chars of ID : word count : first 2 chars of last word (uppercase).
     *
     * @return the hash string
     */
    public String generateHash() {
        String[] words    = messageText.trim().split("\\s+");
        int      count    = words.length;
        String   lastWord = words[words.length - 1];

        String hash = messageID.substring(0, 2)
                    + ":"
                    + count
                    + ":"
                    + lastWord.substring(0, Math.min(2, lastWord.length()));

        return hash.toUpperCase();
    }

    // -------------------------------------------------------
    // PART 2 - SEND / STORE / DISCARD
    // -------------------------------------------------------

    /**
     * Processes the user's choice for this message.
     *
     * Send    - adds text to sentMessages, recipient to recipientList,
     *           ID to sentMessageIDs, hash and ID to their global arrays
     * Store   - writes to JSON file, hash and ID saved to global arrays
     * Discard - adds text to disregardedMessages, hash and ID saved
     *
     * sentMessages, recipientList, and sentMessageIDs are ONLY populated
     * on Send so they always stay the same size and indexes always match.
     *
     * @param choice the user's choice: "send", "store", or "discard"
     * @return a result message shown to the user
     */
    public String sentMessage(String choice) {

        // Always save the hash and ID no matter what the user picks
        messageHashes.add(this.messageHash);
        messageIDs.add(this.messageID);

        switch (choice.toLowerCase()) {

            case "send":
                // These three always go in together so indexes always match
                sentMessages.add(this.messageText);
                recipientList.add(this.recipient);
                sentMessageIDs.add(this.messageID);
                return "Message successfully sent.";

            case "store":
                // Write to JSON file - sentMessages is NOT updated here
                writeToJSON();
                return "Message successfully stored.";

            case "discard":
                // Only goes into disregarded list
                disregardedMessages.add(this.messageText);
                return "Press 0 to delete message.";

            default:
                return "Invalid choice. Please enter Send, Store, or Discard.";
        }
    }

    /**
     * Writes this message to messages.json, one JSON object per line.
     * Appends to the file so previous messages are not overwritten.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     */
    private void writeToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("messageID",   this.messageID);
        obj.put("recipient",   this.recipient);
        obj.put("messageText", this.messageText);
        obj.put("messageHash", this.messageHash);

        // true = append mode
        try (FileWriter writer = new FileWriter("messages.json", true)) {
            writer.write(obj.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // PART 3 - LOAD JSON FILE INTO ARRAY
    // -------------------------------------------------------

    /**
     * Reads messages.json and loads each message text into storedMessages.
     * Called once at startup right after login.
     * If the file does not exist yet the app continues without crashing.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     */
    public static void loadStoredMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            // Each line in the file is one JSON object
            while ((line = reader.readLine()) != null) {
                JSONObject obj  = new JSONObject(line);
                String     text = obj.getString("messageText");
                storedMessages.add(text);
            }
            System.out.println("Stored messages loaded: " + storedMessages.size());
        } catch (IOException e) {
            // No file yet - that is fine, just continue
            System.out.println("No stored messages file found. Starting fresh.");
        }
    }

    // -------------------------------------------------------
    // PART 3 - SEARCH AND FEATURE METHODS
    // -------------------------------------------------------

    /**
     * Loops through storedMessages and returns the one with the most characters.
     *
     * @return the longest message string, or a message if none found
     */
    public String displayLongestMessage() {
        String longest = "";

        for (String msg : storedMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }

        if (longest.isEmpty()) {
            return "No stored messages found.";
        }
        return "Longest message: " + longest;
    }

    /**
     * Searches sentMessageIDs for the given ID and returns the matching message.
     * sentMessageIDs and sentMessages are always the same size so the index is safe.
     *
     * @param id the message ID to search for
     * @return the matching message text, or an error message
     */
    public String searchByMessageID(String id) {
        // Search only through IDs of sent messages
        // sentMessageIDs and sentMessages are always the same length
        for (int i = 0; i < sentMessageIDs.size(); i++) {
            if (sentMessageIDs.get(i).equals(id)) {
                return sentMessages.get(i); // same index - guaranteed to match
            }
        }
        return "Message not found.";
    }

    /**
     * Searches for all messages sent to the given recipient number.
     * recipientList and sentMessages are always the same size so the index is safe.
     *
     * @param recipient the cell number to search for
     * @return all matching message texts joined together, or an error message
     */
    public String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();

        // recipientList and sentMessages are always the same length
        // because both are only populated during "send"
        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i).equals(recipient)) {
                results.append(sentMessages.get(i)).append("\n");
            }
        }

        if (results.length() == 0) {
            return "No messages found for recipient: " + recipient;
        }
        return results.toString().trim();
    }

    /**
     * Finds the entry matching the given hash and removes it from all arrays.
     * Removes the entry at the same index in sentMessages, recipientList,
     * sentMessageIDs, messageHashes, and messageIDs.
     *
     * @param hash the hash string to search for
     * @return a success message with the deleted text, or an error
     */
    public String deleteByHash(String hash) {
        // Search through hashes of sent messages only
        for (int i = 0; i < sentMessages.size(); i++) {
            // Find the hash that matches at the same index in sentMessages
            // sentMessages and messageHashes may differ in size so we
            // match by looking up the hash stored in sentMessageIDs position
            if (i < messageHashes.size() && messageHashes.get(i).equals(hash)) {

                // Save the text before removing
                String deletedText = sentMessages.get(i);

                // Remove from all three parallel sent arrays at the same index
                sentMessages.remove(i);
                recipientList.remove(i);
                sentMessageIDs.remove(i);
                messageHashes.remove(i);
                messageIDs.remove(i);

                return "Message: " + deletedText + " successfully deleted.";
            }
        }

        // Also check if hash exists in messageHashes beyond sentMessages size
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                messageHashes.remove(i);
                messageIDs.remove(i);
                return "Message successfully deleted.";
            }
        }

        return "Hash not found.";
    }

    // -------------------------------------------------------
    // PART 3 - MESSAGE REPORT
    // -------------------------------------------------------

    /**
     * Builds and returns a formatted report of all sent messages.
     * Shows Hash, Recipient, and Message text for every sent message.
     * sentMessages, recipientList, and sentMessageIDs are always the same size.
     *
     * @return the full report as a String
     */
    public static String printMessages() {
        StringBuilder report = new StringBuilder();
        report.append("=== Message Report ===\n");

        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("----------------------------\n");
            report.append("Hash:      ").append(sentMessageIDs.get(i)).append("\n");
            report.append("Recipient: ").append(recipientList.get(i)).append("\n");
            report.append("Message:   ").append(sentMessages.get(i)).append("\n");
        }

        report.append("============================\n");
        return report.toString();
    }

    // -------------------------------------------------------
    // GETTERS
    // -------------------------------------------------------

    /** @return this message's ID */
    public String getMessageID()   { return messageID; }

    /** @return this message's recipient number */
    public String getRecipient()   { return recipient; }

    /** @return this message's text */
    public String getMessageText() { return messageText; }

    /** @return this message's hash */
    public String getMessageHash() { return messageHash; }

    /** @return the full sentMessages list */
    public static List<String> getSentMessages()        { return sentMessages; }

    /** @return the full storedMessages list */
    public static List<String> getStoredMessages()      { return storedMessages; }

    /** @return the full disregardedMessages list */
    public static List<String> getDisregardedMessages() { return disregardedMessages; }

    /** @return the full messageHashes list */
    public static List<String> getMessageHashes()       { return messageHashes; }

    /** @return the full messageIDs list */
    public static List<String> getMessageIDs()          { return messageIDs; }

    /**
     * Clears all arrays completely.
     * Used by unit tests so each test starts with empty arrays.
     */
    public static void clearAllArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipientList.clear();
        sentMessageIDs.clear();
    }

    /**
     * Adds a message text directly into storedMessages.
     * Used by unit tests to set up test data without needing a real JSON file.
     *
     * @param text the message text to add
     */
    public static void addToStoredMessages(String text) {
        storedMessages.add(text);
    }
}

    
}
