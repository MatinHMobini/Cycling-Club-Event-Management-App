package com.example.grimpeurscyclingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//FIREBASE IMPLEMENTATION
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;


public class SignUpScreen extends AppCompatActivity {

    private String accountType = "Participant";
    private FirebaseDatabase database;
    private DatabaseReference refDatabase;
    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        //Get instance of database to read and write to
        database = FirebaseDatabase.getInstance();
        refDatabase = database.getReference("users");

    }

    /*Firebase account creation method
     * This method takes the user's information from the text fields and creates a new account
     * in Firebase with the information.
     *
     * This method does not login existing users.
     * This method does check for existing accounts with the same information.
     */
    public void buttonCreateAccountClick(View view) throws NoSuchAlgorithmException {



        //Edit texts to read from user text fields
        EditText usernameEdit = findViewById(R.id.editUsername);
        EditText emailEdit = findViewById(R.id.editEmail);
        EditText passwordEdit = (EditText) findViewById(R.id.editPassword);


        //Make strings from editTexts
        //Usernames are NOT case sensitive
        String username = usernameEdit.getText().toString().toLowerCase();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        // Make an email regex
        Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher emailMatcher = emailPattern.matcher(email);

        boolean validatedClientSide = true;

        // If the email does not match the email regex
        if (!emailMatcher.find()) {
            emailEdit.setText("");
            emailEdit.setHint("Email must be valid");
            emailEdit.setHintTextColor(Color.argb(255,255,0,0));

            validatedClientSide = false;
        }

        if(username.equals("")) {
            usernameEdit.setText("");
            usernameEdit.setHint("You must enter a username");
            usernameEdit.setHintTextColor(Color.argb(255,255,0,0));
            validatedClientSide = false;
        }

        if(password.equals("")) {
            passwordEdit.setText("");
            passwordEdit.setHint("You must enter a password");
            passwordEdit.setHintTextColor(Color.argb(255,255,0,0));
            validatedClientSide = false;
        }



        if (validatedClientSide) {
            // Hashes password because plain text passwords are a bad idea
            // Makes instance of SHA 256 Algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Convert string to array of bytes, use the hash, and then save it
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            // Convert array of bytes back to base 64 (unicode) string
            String hashed = Base64.getEncoder().encodeToString(hash);

            //Make DatabaseReferences from Strings
            DatabaseReference newUserRoleInfo = database.getReference("users/" + username + "/role");
            DatabaseReference newUserEmailInfo = database.getReference("users/" + username + "/email");
            DatabaseReference newUserPasswordInfo = database.getReference("users/" + username + "/password");

//          Add a way to check if username exists

            //Listener to check for existing usernames--we define the method inside addOnCompleteListener parameters
           refDatabase.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

               @Override
               public void onComplete(@NonNull Task<DataSnapshot> task) {

                   if (task.isSuccessful()) { //Successful task case--if the usernames were read

                       if (task.getResult().exists()) { //If the username is found
                           usernameEdit.setText("");
                           usernameEdit.setHint("Username is already in use ");
                           usernameEdit.setHintTextColor(Color.argb(255,255,0,0));
                           System.out.println(refDatabase.child("users").child(username).get().toString());
                       } else    { //Username is not found: user can create account with this username
                           // Make object
                           if(accountType.equals("Participant"))
                               account = new ParticipantAccount(username, hashed, email);
                           if(accountType.equals("Club"))
                               account = new CyclingClubAccount(username, hashed, email);
                           if(accountType.equals("Admin"))
                               account = new AdminAccount(username, hashed, email);

                           newUserRoleInfo.setValue(accountType);
                           newUserEmailInfo.setValue(email); //This does not verify emails
                           newUserPasswordInfo.setValue(hashed);

                           //Display welcome screen to user and pass account information to welcome screen
                           Intent intent = new Intent(getApplicationContext(), SignUpScreen.class);
                           intent.putExtra("account", account);
                           intent.putExtra("username", username);
                           intent.putExtra("role", accountType);
                           intent.putExtra("nextActivity", "UIScreen");

                           setResult(1, intent);

                           finish();
                       }

                   } else { //If the usernames were not read
                       Toast.makeText(SignUpScreen.this, "Failed to search by username.", Toast.LENGTH_SHORT);
                   }
               }
           });
        }
    }

    public void switchAccountOnClick(View view) {
        // Implement onClick when ready
        int pressID = view.getId();
        Button participant = findViewById(R.id.cycleParticipant);
        Button club = findViewById(R.id.cycleClub);

        if (pressID == R.id.cycleParticipant) {
            accountType = "Participant";
            club.setBackgroundColor(Color.argb(255,128,128,128));
            participant.setBackgroundColor(Color.argb(255,103,80,164));
        } else {
            accountType = "Club";
            participant.setBackgroundColor(Color.argb(255,128,128,128));
            club.setBackgroundColor(Color.argb(255,103,80,164));

        }

    }
}