/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MessageTest - all unit tests for the Message class.
 * Part 2 tests: message length, recipient format, hash, ID, sentMessage actions
 * Part 3 tests: arrays populated, longest message, search by ID,
 *               search by recipient, delete by hash, full report
 *
 * @author tshep
 * @version Part 3
 */
public class MessageTest {

    // -------------------------------------------------------
    // SETUP - runs before every single test
    // Clears all arrays so each test starts completely fresh
    // -------------------------------------------------------

    @Before
    public void setUp() {
        Message.clearAllArrays();
    }

    // -------------------------------------------------------
    // PART 2 TESTS
    // -------------------------------------------------------

    /**
     * A short message under 250 characters should pass the length check.
     */
    @Test
    public void testMessageLength_valid() {
        Message msg = new Message("+27831234567", "Hello there!");
        assertTrue("Short message should be valid", msg.checkMessageLength());
    }

    /**
     * A message of exactly 251 characters should fail the length check.
     */
    @Test
    public void testMessageLength_tooLong() {
        String longText = "A".repeat(251);
        Message msg = new Message("+27831234567", longText);
        assertFalse("Message over 250 chars should be invalid", msg.checkMessageLength());
    }

    /**
     * A number that starts with + and is 11 characters should pass.
     */
    @Test
    public void testRecipientCell_valid() {
        Message msg = new Message("+27831234567", "Hello");
        assertTrue("Valid international number should pass", msg.checkRecipientCell());
    }

    /**
     * A number without the + international code should fail.
     */
    @Test
    public void testRecipientCell_missingPlus() {
        Message msg = new Message("0831234567", "Hello");
        assertFalse("Number without + should fail", msg.checkRecipientCell());
    }

    /**
     * The generated hash should not be null or empty.
     */
    @Test
    public void testHash_notEmpty() {
        Message msg = new Message("+27831234567", "Hello World");
        assertNotNull("Hash should not be null", msg.getMessageHash());
        assertFalse("Hash should not be empty", msg.getMessageHash().isEmpty());
    }

    /**
     * The generated message ID should not be null or empty.
     */
    @Test
    public void testMessageID_notEmpty() {
        Message msg = new Message("+27831234567", "Hello World");
        assertNotNull("Message ID should not be null", msg.getMessageID());
        assertFalse("Message ID should not be empty", msg.getMessageID().isEmpty());
    }

    /**
     * Choosing Send should return the correct success message.
     */
    @Test
    public void testSentMessage_sendReturnsCorrectMessage() {
        Message msg = new Message("+27831234567", "Hello World");
        String result = msg.sentMessage("send");
        assertEquals("Message successfully sent.", result);
    }

    /**
     * Choosing Discard should return the correct response.
     */
    @Test
    public void testSentMessage_discardReturnsCorrectMessage() {
        Message msg = new Message("+27831234567", "Hello World");
        String result = msg.sentMessage("discard");
        assertEquals("Press 0 to delete message.", result);
    }

    /**
     * Choosing Store should return the correct success message.
     */
    @Test
    public void testSentMessage_storeReturnsCorrectMessage() {
        Message msg = new Message("+27831234567", "Hello World");
        String result = msg.sentMessage("store");
        assertEquals("Message successfully stored.", result);
    }

    // -------------------------------------------------------
    // PART 3 TESTS - six new tests using exact POE test data
    // -------------------------------------------------------

    /**
     * TEST 1 - Sent messages array is correctly populated.
     *
     * POE message 1: "Did you get the cake?"  to +27834557896  - flagged Send
     * POE message 4: "It is dinner time!"     to 0838884567   - flagged Send
     *
     * Both texts must appear in the sentMessages array.
     */
    @Test
    public void testSentMessagesArray_correctlyPopulated() {

        Message msg1 = new Message("+27834557896", "Did you get the cake?");
        msg1.sentMessage("send");

        Message msg4 = new Message("0838884567", "It is dinner time!");
        msg4.sentMessage("send");

        assertTrue("sentMessages should contain message 1",
                Message.getSentMessages().contains("Did you get the cake?"));

        assertTrue("sentMessages should contain message 4",
                Message.getSentMessages().contains("It is dinner time!"));
    }

    /**
     * TEST 2 - displayLongestMessage returns the correct message.
     *
     * Load all 5 POE messages into storedMessages.
     * The longest is: "Where are you? You are late! I have asked you to be on time."
     */
    @Test
    public void testDisplayLongestMessage_returnsCorrectMessage() {

        Message.addToStoredMessages("Did you get the cake?");
        Message.addToStoredMessages("Where are you? You are late! I have asked you to be on time.");
        Message.addToStoredMessages("Hii, I am good thanks how are you?");
        Message.addToStoredMessages("It is dinner time!");
        Message.addToStoredMessages("Ok, I am leaving without you.");

        // Create a fresh Message - constructor does NOT touch arrays
        Message msg = new Message("+27831234567", "test");
        String result = msg.displayLongestMessage();

        assertTrue("Should return the longest message",
                result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    /**
     * TEST 3 - searchByMessageID returns the correct message.
     *
     * Send message 4: "It is dinner time!" to 0838884567.
     * Searching with msg4's actual generated ID must return that text.
     */
    @Test
    public void testSearchByMessageID_returnsCorrectMessage() {

        // Send message 4 - this adds it to sentMessages and sentMessageIDs
        Message msg4 = new Message("0838884567", "It is dinner time!");
        msg4.sentMessage("send");

        // Save the ID that was generated for msg4
        String idToSearch = msg4.getMessageID();

        // Use a fresh Message to call the search - constructor does NOT touch arrays
        Message searcher = new Message("+27831234567", "test");
        String result = searcher.searchByMessageID(idToSearch);

        assertEquals("Search by ID should return the correct message",
                "It is dinner time!", result);
    }

    /**
     * TEST 4 - searchByRecipient returns all matching messages.
     *
     * +27838884567 is the recipient of message 2 and message 5.
     * Both must appear in the search result.
     */
    @Test
    public void testSearchByRecipient_returnsAllMatchingMessages() {

        // Message 2
        Message msg2 = new Message("+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage("send");

        // Message 5
        Message msg5 = new Message("+27838884567", "Ok, I am leaving without you.");
        msg5.sentMessage("send");

        // Use a fresh Message to call the search
        Message searcher = new Message("+27831234567", "test");
        String result = searcher.searchByRecipient("+27838884567");

        assertTrue("Result should contain message 2",
                result.contains("Where are you? You are late! I have asked you to be on time."));

        assertTrue("Result should contain message 5",
                result.contains("Ok, I am leaving without you."));
    }

    /**
     * TEST 5 - deleteByHash removes the correct message.
     *
     * Send message 2, then delete it using its hash.
     * The return must match: "Message: [text] successfully deleted."
     */
    @Test
    public void testDeleteByHash_removesCorrectMessage() {

        // Send message 2
        Message msg2 = new Message("+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage("send");

        // Save the hash before deleting
        String hash = msg2.getMessageHash();

        // Use a fresh Message to call delete
        Message deleter = new Message("+27831234567", "test");
        String result = deleter.deleteByHash(hash);

        assertTrue("Result should contain the deleted message text",
                result.contains("Where are you? You are late! I have asked you to be on time."));

        assertTrue("Result should say successfully deleted",
                result.contains("successfully deleted"));
    }

    /**
     * TEST 6 - printMessages report contains all required fields.
     *
     * After sending a message, the report must contain:
     * the recipient number and the message text.
     */
    @Test
    public void testDisplayReport_containsRequiredFields() {

        Message msg1 = new Message("+27834557896", "Did you get the cake?");
        msg1.sentMessage("send");

        String report = Message.printMessages();

        assertTrue("Report should contain the recipient",
                report.contains("+27834557896"));

        assertTrue("Report should contain the message text",
                report.contains("Did you get the cake?"));
    }
}




    