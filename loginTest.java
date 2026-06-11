/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */


import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void testCheckUserNameValid() {
        Login login = new Login();
        assertTrue(login.checkUserName("ab_1"));
    }

    @Test
    public void testCheckUserNameInvalid() {
        Login login = new Login();
        assertFalse(login.checkUserName("abcde"));
        assertFalse(login.checkUserName("abc_123"));
    }

    @Test
    public void testCheckPasswordComplexityValid() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Passw0rd!"));
    }

    @Test
    public void testCheckPasswordComplexityInvalid() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
        assertFalse(login.checkPasswordComplexity("Password1"));
    }

    @Test
    public void testCheckCellPhoneNumberValid() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27831234567"));
    }

    @Test
    public void testCheckCellPhoneNumberInvalid() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("0831234567"));
        assertFalse(login.checkCellPhoneNumber("+2783123456789"));
    }

    @Test
    public void testRegisterUserSuccess() {
        Login login = new Login();
        String result = login.registerUser("ab_1", "Passw0rd!", "+27831234567");
        assertEquals("User registered successfully.", result);
    }

    @Test
    public void testRegisterUserFailure() {
        Login login = new Login();
        String result = login.registerUser("abcdef", "Passw0rd!", "+27831234567");
        assertTrue(result.contains("Username is not correctly formatted"));
    }

    @Test
    public void testLoginUserSuccess() {
        Login login = new Login();
        login.registerUser("ab_1", "Passw0rd!", "+27831234567");
        assertTrue(login.loginUser("ab_1", "Passw0rd!"));
    }

    @Test
    public void testLoginUserFailure() {
        Login login = new Login();
        login.registerUser("ab_1", "Passw0rd!", "+27831234567");
        assertFalse(login.loginUser("wrong", "Passw0rd!"));
    }

    @Test
    public void testReturnLoginStatus() {
        Login login = new Login();
        login.registerUser("ab_1", "Passw0rd!", "+27831234567");
        boolean success = login.loginUser("ab_1", "Passw0rd!");
        String message = login.returnLoginStatus(success);
        assertTrue(message.contains("Welcome ab_1"));
    }
}