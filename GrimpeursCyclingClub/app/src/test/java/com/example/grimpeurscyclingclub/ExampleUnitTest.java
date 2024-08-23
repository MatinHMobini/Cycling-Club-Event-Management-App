package com.example.grimpeurscyclingclub;

import org.junit.Rule;
import org.junit.runner.RunWith;

//@RunWith(MockitoJUnitRunner.class)
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.Assert.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.errorprone.annotations.DoNotMock;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Context mMockContext;

    /*
        MORE POSSIBLE TESTS:
        1. Confirm account getter/setters work: build Account with Account(), set username, email, password with setters, compare getters with strings input to setters
        2. SignUpScreen's switchAccountOnClick works: instance of SignUpScreen; compare before and after of SignUpScreen.accountType value
            - I do not know if it is possible to get the XML view working. It is probably not work trying.
        3. Confirm restrictions on a method work: e.g: CyclingClubAccount.setSocialMediaLink(String) requires the first character of the parameter is "@"
            - BE VERY CAREFUL WHEN CHANGING IN-USE METHODS AS TO NOT BREAK THE APP

     */

    /* UNUSED TEST
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }*/

    @Test
    public void adminLogin_isCorrect() throws NoSuchAlgorithmException {
        //Get object we're testing
        LoginScreen objectUnderTest = new LoginScreen();

        //Hash password
        // Hashes password because plain text passwords are a bad idea
        // Makes instance of SHA 256 Algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Convert string to array of bytes, use the hash, and then save it
        byte[] hash = digest.digest("admin".getBytes(StandardCharsets.UTF_8));
        // Convert array of bytes back to base 64 (unicode) string
        String hashed = Base64.getEncoder().encodeToString(hash);

        String email = "quinn.slavish@gmail.com";
        String role = "Administrator";

        //Test method
        Account account = objectUnderTest.searchFirebaseForAccount("admin", hashed, email, role);
        //Evaluate test
        assertEquals("admin",account.getUsername());
    }

    @Test
    public void adminHasCorrectPass() throws NoSuchAlgorithmException {
        //Get object we're testing
        LoginScreen objectUnderTest = new LoginScreen();

        //Hash password
        // Hashes password because plain text passwords are a bad idea
        // Makes instance of SHA 256 Algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Convert string to array of bytes, use the hash, and then save it
        byte[] hash = digest.digest("admin".getBytes(StandardCharsets.UTF_8));
        // Convert array of bytes back to base 64 (unicode) string
        String hashed = Base64.getEncoder().encodeToString(hash);

        String email = "quinn.slavish@gmail.com";
        String role = "Administrator";

        Account account = objectUnderTest.searchFirebaseForAccount("admin", hashed, email, role);

        //Evaluate test (the password which is found in the firebase for the admin)
        assertEquals("jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=", account.getPassword());
    }

    @Test
    public void adminHasValidEmail() throws NoSuchAlgorithmException {
        //Get object we're testing
        LoginScreen objectUnderTest = new LoginScreen();

        //Hash password
        // Hashes password because plain text passwords are a bad idea
        // Makes instance of SHA 256 Algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Convert string to array of bytes, use the hash, and then save it
        byte[] hash = digest.digest("admin".getBytes(StandardCharsets.UTF_8));
        // Convert array of bytes back to base 64 (unicode) string
        String hashed = Base64.getEncoder().encodeToString(hash);

        String email = "quinn.slavish@gmail.com";
        String role = "Administrator";

        Account account = objectUnderTest.searchFirebaseForAccount("admin", hashed, email, role);

        // Make an email regex
        Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher emailMatcher = emailPattern.matcher(account.getEmail());

        Boolean validatedClientSide = true;

        // If the email does not match the email regex
        if (!emailMatcher.find()) {
            validatedClientSide = false;
        }

        //Evaluate test (the email is valid according to regex)
        assertEquals(Boolean.TRUE, validatedClientSide);
    }

    @Test
    public void participantLogin_isCorrect() throws NoSuchAlgorithmException {
        //Get object we're testing
        LoginScreen objectUnderTest = new LoginScreen();

        //Hash password
        // Hashes password because plain text passwords are a bad idea
        // Makes instance of SHA 256 Algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Convert string to array of bytes, use the hash, and then save it
        byte[] hash = digest.digest("cyclingIsLife!".getBytes(StandardCharsets.UTF_8));
        // Convert array of bytes back to base 64 (unicode) string
        String hashed = Base64.getEncoder().encodeToString(hash);

        String email = "caddict@gmail.com";
        String role = "Participant";

        //Test method
        Account account = objectUnderTest.searchFirebaseForAccount("cyclingaddict", hashed, email, role);
        //Evaluate test
        assertEquals("cyclingaddict",account.getUsername());
    }

    @Test
    public void clubLogin_isCorrect() throws NoSuchAlgorithmException {
        //Get object we're testing
        LoginScreen objectUnderTest = new LoginScreen();

        //Hash password
        // Hashes password because plain text passwords are a bad idea
        // Makes instance of SHA 256 Algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Convert string to array of bytes, use the hash, and then save it
        byte[] hash = digest.digest("GCCRocks!".getBytes(StandardCharsets.UTF_8));
        // Convert array of bytes back to base 64 (unicode) string
        String hashed = Base64.getEncoder().encodeToString(hash);

        String email = "gcc@gmail.com";
        String role = "Club";

        //Test method
        Account account = objectUnderTest.searchFirebaseForAccount("gccadmin", hashed, email, role);
        //Evaluate test
        assertEquals("gccadmin",account.getUsername());
    }

    @Test
    public void aLogin_isIncorrect() throws NoSuchAlgorithmException {
        //Get object we're testing
        LoginScreen objectUnderTest = new LoginScreen();

        //Hash password
        // Hashes password because plain text passwords are a bad idea
        // Makes instance of SHA 256 Algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Convert string to array of bytes, use the hash, and then save it
        byte[] hash = digest.digest("admin".getBytes(StandardCharsets.UTF_8));
        // Convert array of bytes back to base 64 (unicode) string
        String hashed = Base64.getEncoder().encodeToString(hash);

        String email = "quinn.slavish@gmail.com";
        String role = "No role found";

        //Test method
        Account account = objectUnderTest.searchFirebaseForAccount("admin", hashed, email, role);
        //Evaluate test
        assertEquals(null, account);
    }

    @Test
    public void commentConstructorAndAccessors_areFunctional() {

        String reviewer = "Reviewer";
        String clubAccount = "ClubAccount";
        String comment = "Comment about the ClubAccount made by the Reviewer";
        String date = Date.getTodaysDate();
        int rating = 5;

        Comment newComment = new Comment(reviewer, clubAccount, comment, date,rating);

        assertEquals(reviewer, newComment.getReviewer());
        assertEquals(clubAccount, newComment.getClubAccount());
        assertEquals(comment, newComment.getComment());
        assertEquals(date, newComment.getDate());
        assertEquals(rating, newComment.getRating());
    }


    @Test
    public void commentRatingSpinner_isBuilt() {

        //Get object we're testing
        ParticipantViewOfCyclingClub objectUnderTest = new ParticipantViewOfCyclingClub();
        //Build rating list
        List<String> ratingList = objectUnderTest.buildRatingSpinnerList();
        //Test that the list has all the elements
        assertEquals(5, ratingList.size());
    }

    @Test
    public void listOfKeysOfRegisteredEvents_Builds() throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Convert string to array of bytes, use the hash, and then save it
        byte[] hash = digest.digest("GCCRocks!".getBytes(StandardCharsets.UTF_8));
        // Convert array of bytes back to base 64 (unicode) string
        String hashed = Base64.getEncoder().encodeToString(hash);

        String email = "gcc@gmail.com";

        //Get object we're testing
        ParticipantAccount objectUnderTest = new ParticipantAccount("cyclingaddict",hashed,email);

        for (int i = 0; i < 5; i++) {
            //Add eventIds to ParticipantAccount's registeredEvents list
            objectUnderTest.getRegisteredEvents().add("EventId" + i);
            //Evaluate the size of the list to ensure events are being added
            assertEquals(i+1, objectUnderTest.getRegisteredEvents().size());
        }
    }

    @Test
    public void listOfCyclingClubsComments_Builds() throws NoSuchAlgorithmException {

        CyclingClubAccount cA = new CyclingClubAccount();
        ParticipantViewOfCyclingClub pV = new ParticipantViewOfCyclingClub();

        for (int i = 0; i < 5; i++) {
            String reviewer = "Reviewer" + i;
            String clubAccount = "ClubAccount";
            String comment = "Comment #" + i + " about the ClubAccount made by the Reviewer";
            String date = Date.getTodaysDate();
            int rating = i+1;

            Comment newComment = new Comment(reviewer, clubAccount, comment, date, rating);
            cA.getReviews().add(newComment);
            //Assert that all 5 Comments are being added to the CyclingClubAccount's reviews
            assertEquals(i+1, cA.getReviews().get(i).getRating());
        }
    }
}