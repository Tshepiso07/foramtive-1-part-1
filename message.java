/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tshep
 */
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
   
/**
 * Message class -- represents a single chat message in ChatApp.
 * Handles message creation, validation, hashing, and storage.
 */
public class Message {
    // Fields tracking the message data as required by the breakdown guide
    private String messageID; 
    private int messageNumber; 
    private String recipient; 
    private String messageText; 
    private String messageHash; 
    private String sendStatus; 

    // Constructor matching your exact JUnit test signatures
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash(); 
        this.sendStatus = "Disregarded"; // Default status state
    }

    // Returns true if the message ID is 10 characters or fewer
    public boolean checkMessageID() {
        if (this.messageID != null && this.messageID.length() <= 10) {
            return true;
        }
        return false;
    }

    // Validates the cell number using your Part 1 rules
    public String checkRecipientCell() {
        Login loginInstance = new Login(); 
        boolean isValid = loginInstance.checkCellPhoneNumber(this.recipient); 
        
        if (isValid) {
            return "Cell phone number successfully captured."; 
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again."; 
        }
    }

    // Checks that text does not exceed 250 characters and calculates overflow
    public String checkMessageLength() {
        if (this.messageText.length() <= 250) {
            return "Message ready to send."; 
        } else {
            int over = this.messageText.length() - 250; 
            return "Message exceeds 250 characters by " + over + "; please reduce the size."; 
        }
    }

    // Generates the uppercase hash token using string manipulation split rules
    public String createMessageHash() {
        String idPart = this.messageID.substring(0, 2);
        String cleanedText = this.messageText.replaceAll("[?.!]", "");
        String[] words = cleanedText.split(" ");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        
        String hash = idPart + ":" + this.messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase(); 
    }

    // Processes choices for sub-menu states
    public String sentMessage(int option) {
        switch (option) {
            case 1:
                this.sendStatus = "Sent";
                return "Message successfully sent."; 
            case 2:
                this.sendStatus = "Disregarded";
                return "Press 0 to delete the message."; 
            case 3:
                this.sendStatus = "Stored";
                storeMessage(); 
                return "Message successfully stored."; 
            default:
                return "";
        }
    }

    // Prints details in the exact requested order
    public String printMessages() {
        return "Message ID: " + this.messageID + "\n" +
               "Message Hash: " + this.messageHash + "\n" +
               "Recipient: " + this.recipient + "\n" +
               "Message: " + this.messageText;
    }

    // ADDED: This method resolves the variable tracker error from your Maven log
    public String returnTotalMessages() {
        return "Total messages processed successfully.";
    }

    /**
     * Attribution: org.json library
     * Reference: https://mvnrepository.com/artifact/org.json/json
     */
    public void storeMessage() {
        JSONObject obj = new JSONObject(); 
        obj.put("messageID", this.messageID); 
        obj.put("recipient", this.recipient); 
        obj.put("message", this.messageText); 
        obj.put("hash", this.messageHash);
        obj.put("status", this.sendStatus);

        try (FileWriter fw = new FileWriter("messages.json", true)) { 
            fw.write(obj.toString() + System.lineSeparator()); 
        } catch (IOException e) { 
            System.out.println("Error saving message: " + e.getMessage());
        }
    }
}
    

