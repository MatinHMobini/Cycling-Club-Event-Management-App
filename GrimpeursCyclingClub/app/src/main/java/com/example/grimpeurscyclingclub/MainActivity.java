package com.example.grimpeurscyclingclub;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /*
     Any top level activity redirects here when closed
    */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("Here");
//        Account account = (Account) data.getSerializableExtra("account");

        if (data == null) { //Redirects user to login screen when they back out of a logged in instance
            launchEvent(LoginScreen.class);

        } else {


          String nextScreen = data.getStringExtra("nextActivity");
//        System.out.println(account.getEmail());
//        System.out.println(account.getPassword());
//        System.out.println(account.getUsername());
//        System.out.println(nextScreen);
        switch (nextScreen) {
            case "UIScreen":
                Account account = (Account) data.getSerializableExtra("account");
                if (account instanceof AdminAccount) {
                    //System.out.println("BEFORE ADMIN");
                    launchEvent(account, AdminActivity.class);
                } else if (account instanceof CyclingClubAccount) {
                    launchEvent(account, ClubActivity.class);
                } else if (account instanceof ParticipantAccount) {
                    launchEvent(account, ParticipantActivity.class);
                } else {
                    System.out.println("ACCOUNT TYPE NOT FOUND");
                }
                break;
            case "SignUpScreen":
                launchEvent(SignUpScreen.class);
                break;
            case "LogInScreen":
                launchEvent(LoginScreen.class);
                break;
            default:
                // :(
                break;
            }
        }

    }

    public void launchEvent(Class c) {
        // Immediately Start SignUpScreen
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 10);
    }
    public void launchEvent(Account account, Class c) {
        // Immediately Start SignUpScreen
        Intent intent = new Intent(this, c);
        intent.putExtra("account", account);
        startActivityForResult(intent, 10);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchEvent(LoginScreen.class);
        setContentView(R.layout.activity_main);

    }
}