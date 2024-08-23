package com.example.grimpeurscyclingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {

    //Activity/instance variables
    private FirebaseDatabase database;
    private DatabaseReference refDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_screen);
        //Get instance of database to read and write to
        database = FirebaseDatabase.getInstance();
        refDatabase = database.getReference("users");
    }


    /**
     * This method calls the SignUpScreen activity.
     *
     * */
    public void buttonRedirectToSignUpScreenClick (View view) {



        Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
        intent.putExtra("nextActivity", "SignUpScreen");
        setResult(10, intent);
        finish();
    }


    /**
     * This method attempts to login the user and informs the user if they could log in or not.
     * If the login is successful, it CLOSES AND RETURNS ACCOUNT TYPE INFORMATION for the shell
     * to call the appropriate activities.
     *
     */

    public void buttonLoginAccountClick (View view) throws NoSuchAlgorithmException {

        //Edit texts to read from user text fields
        EditText usernameEdit = findViewById(R.id.editUsername);
        EditText passwordEdit = findViewById(R.id.editPassword);

        //Make strings from editTexts
        //Usernames are NOT case sensitive
        String username = usernameEdit.getText().toString().toLowerCase();
        String password = passwordEdit.getText().toString();

        //Catch empty fields
        if(username.equals("")) {
            usernameEdit.setText("");
            usernameEdit.setHint("You must enter a username");
            usernameEdit.setHintTextColor(Color.argb(255,255,0,0));
            return; //Finish method at this stage
        }

        //Catch empty fields
        if(password.equals("")) {
            passwordEdit.setText("");
            passwordEdit.setHint("You must enter a password");
            passwordEdit.setHintTextColor(Color.argb(255,255,0,0));
            return; //Finish method at this stage
        }

        // Hashes password because plain text passwords are a bad idea
        // Makes instance of SHA 256 Algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Convert string to array of bytes, use the hash, and then save it
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        // Convert array of bytes back to base 64 (unicode) string
        String hashed = Base64.getEncoder().encodeToString(hash);

        //Listener to check for existing usernames--we define the method inside addOnCompleteListener parameters
        refDatabase.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) { //Successful task case--if the usernames were read

                    if (task.getResult().exists()) { //If the username is found

                        //Get password of user
                        String passFound = task.getResult().child("password").getValue(String.class);

                        if (passFound.equals(hashed)) { //If the entered password matches the password in the database

                            String emailFound = task.getResult().child("email").getValue(String.class);
                            String roleFound = task.getResult().child("role").getValue(String.class);

                            Account account = searchFirebaseForAccount(username, passFound, emailFound, roleFound);
                            /*
                            //Account to return once logged in
                            Account account;

                            //Create account from data stored at username
                            if (roleFound.equals("Participant")) { //ParticipantAccount case
                                account = new ParticipantAccount(username, passFound, emailFound);
                            } else if (roleFound.equals("Club")) { //ClubAccount case
                                account = new CyclingClubAccount(username, passFound, emailFound);
                            } else { // AdminAccount case
                                account = new AdminAccount(username, passFound, emailFound);
                            }*/

                            Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                            intent.putExtra("account", account);
                            intent.putExtra("username", username);
                            intent.putExtra("role", roleFound);
                            intent.putExtra("nextActivity", "UIScreen");
                            setResult(1, intent);

                            finish();
                        } else { //Password does not match
                            passwordEdit.setText("");
                            usernameEdit.setText("");
                            usernameEdit.setHint("Username or password is incorrect");
                            passwordEdit.setHintTextColor(Color.argb(255,255,0,0));
                        }

                    } else { //Username is not found

                        usernameEdit.setText("");
                        usernameEdit.setHint("Account does not exist");
                        usernameEdit.setHintTextColor(Color.argb(255,255,0,0));
                    }

                } else { //If the usernames were not read
                    Toast.makeText(LoginScreen.this, "Failed to search by username.", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //Build the account to return
    public Account searchFirebaseForAccount(String username, String passFound, String emailFound, String roleFound) {

        //Account to return once logged in
        Account account = null;

        //Create account from data stored at username
        if (roleFound.equals("Participant")) { //ParticipantAccount case
            account = new ParticipantAccount(username, passFound, emailFound);
        } else if (roleFound.equals("Club")) { //ClubAccount case
            account = new CyclingClubAccount(username, passFound, emailFound);
        } else if (roleFound.equals("Administrator")){ // AdminAccount case
            account = new AdminAccount(username, passFound, emailFound);
        }

        return account;
    }

}

