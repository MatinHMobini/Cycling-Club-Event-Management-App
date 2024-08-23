package com.example.grimpeurscyclingclub;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.content.Intent;

import android.os.Bundle;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // Get textbox passed through intent--this can eventually be replaced by passing the account itself
        Intent intent = getIntent();
        Account account = (Account) intent.getSerializableExtra("account");

        // TODO:
        //  Optionally, these could be combined into one text box and the string could be concatenated as below.
        //  e.g: roleText.setText("Welcome, " + intent.getStringExtra("username") + ", your role is " + intent.getStringExtra("role"));
        TextView userName = findViewById(R.id.userNameLayout);
        userName.setText("Welcome, " + account.getUsername());

        TextView role = findViewById(R.id.role);
        String roleText = "Cycling Club";
        if (account instanceof ParticipantAccount) {
            roleText = "Participant";
        }
        role.setText("your role is " + roleText);

    }
}