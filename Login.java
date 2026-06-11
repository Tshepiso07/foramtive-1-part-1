/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.part1chatapp;

/**
 *
 * @author tshep
  */
 /**
 * Login Class
 *
 * This class stores user details and provides all the validation,
 * registration, and login logic 
 *
 * Responsibilities:
 * - Store username, password, and phone number
 * - Validate username format
 * - Validate password complexity
 * - Validate South African phone number format
 * - Register a user with proper validation
 * - Allow login with stored credentials
 * - Return appropriate feedback messages
 */
public class Login {

    // Fields to store user details once registered
    private String username;
    private String password;
    private String phoneNumber;

    /**
     * Step 5: Validate the username.
     * Rules:
     * - Must contain an underscore
     * - Must be no more than 5 characters long
     *
     * @param username The username entered by the user
     * @return true if valid, false otherwise
     */
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Step 6: Validate the password complexity.
     * Rules:
     * - At least 8 characters long
     * - Contains at least 1 capital letter
     * - Contains at least 1 number
     * - Contains at least 1 special character (!, @, %, #, etc.)
     *
     * @param password The password entered by the user
     * @return true if valid, false otherwise
     */
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // Loop through each character in the password
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Step 7: Validate the South African phone number.
     * Rules:
     * - Must start with +27 (international code for South Africa)
     * - Must be no more than 12 characters long
     *
     * @param phone The phone number entered by the user
     * @return true if valid, false otherwise
     */
    public boolean checkCellPhoneNumber(String phone) {
        return phone.startsWith("+27") && phone.length() <= 12;
    }

    /**
     * Step 8: Register the user.
     * - Validates username, password, and phone number
     * - Stores the details if all validations pass
     * - Returns specific feedback messages
     *
     * @param username The username entered
     * @param password The password entered
     * @param phoneNumber The phone number entered
     * @return A message indicating success or the reason for failure
     */
    public String registerUser(String username, String password, String phoneNumber) {

        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // Store the validated details
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;

        return "User registered successfully.";
    }

    /**
     * Step 9: Login feature.
     * Compares entered credentials with stored ones.
     *
     * @param username The username entered
     * @param password The password entered
     * @return true if login is successful, false otherwise
     */
    public boolean loginUser(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Step 9: Return login status message.
     * Provides feedback depending on whether login succeeded.
     *
     * @param success Result of login attempt
     * @return A welcome message if successful, or an error message if not
     */
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + username + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
