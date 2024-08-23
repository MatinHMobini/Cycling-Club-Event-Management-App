package com.example.grimpeurscyclingclub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import java.util.regex.Matcher;
import java.util.regex.*;

//FIREBASE IMPLEMENTATION


public class ClubInformationGatheringActivity extends AppCompatActivity {
// get inputs for main contact, number, social media link, link them to firebase, validate the fields. if your trying to change your info you might need to pull info from firebase and load it into the activity
    private FirebaseDatabase database;
    private DatabaseReference refDatabase;

    //Account passed by intent
    PublicUser account;

    //EditTexts from xml
    EditText nameEdit;
    EditText phoneEdit;
    EditText socialEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: replace with club information gathering activity
        setContentView(R.layout.profile_information_gathering);
        //Get instance of database to read and write to
        database = FirebaseDatabase.getInstance();
        refDatabase = database.getReference("users");

        //get Account to get information from
        Intent intent = getIntent();

        if (intent.getSerializableExtra("cyclingAccount") != null) { //If Activity was passed a CyclingClubAccount
            account = (CyclingClubAccount) intent.getSerializableExtra("cyclingAccount");
        }
        else if (intent.getSerializableExtra("participantAccount") != null) { //Else if Activity was passed a ParticipantAccount
            account = (ParticipantAccount) intent.getSerializableExtra("participantAccount");
        }
        else  { // Else something went wrong
            System.out.println("SOMETHING HAS GONE WRONG; TERMINATING PROGRAM FROM CLubInformationGatheringActivity.java");
            System.exit(0);
        }

        //Edit texts to read from user text fields
        nameEdit = findViewById(R.id.mainContactName);
        phoneEdit = findViewById(R.id.phoneNumber);
        socialEdit = (EditText) findViewById(R.id.socialMediaLink);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Fill text fields will existing information
        nameEdit.setText(account.getMainContactName());
        phoneEdit.setText(account.getPhoneNumber());
        socialEdit.setText(account.getSocialMediaLink());

    }

    public void buttonCreateAccountClick(View view) {

        //**DO get username from this account then use it in line 108 **
        String username = account.getUsername();



        //System.out.println("YYY");

        //Make strings from editTexts
        //names are NOT case sensitive
        String name = nameEdit.getText().toString();
        String phonenumber = phoneEdit.getText().toString();
        String social = socialEdit.getText().toString();

        boolean validatedClientSide = true;

        // Make an phone_number regex
        Pattern phonePattern = Pattern.compile("^\\d{10}$");
        Matcher phoneMatcher = phonePattern.matcher(phonenumber);

        // Make an socials regex
        Pattern socialPattern = Pattern.compile("^@[a-zA-Z0-9_][a-zA-Z0-9_.]*$");
        Matcher socialMatcher = socialPattern.matcher(social);

        if(phonenumber.equals("")) {
            phoneEdit.setText("");
            phoneEdit.setHint("enter a phone number");
            phoneEdit.setHintTextColor(Color.argb(255,255,0,0));
            validatedClientSide = false;
        }

        if(!phoneMatcher.find()) {
            phoneEdit.setText("");
            phoneEdit.setHint("Must enter 10-digit phone number");
            phoneEdit.setHintTextColor(Color.argb(255,255,0,0));
            validatedClientSide = false;
        }

        if(social.equals("")) {
            socialEdit.setText("");
            socialEdit.setHint("You must enter a Contact name");
            socialEdit.setHintTextColor(Color.argb(255,255,0,0));
            validatedClientSide = false;
        }

        // If the social does not match the social regex
        if (!socialMatcher.find()) {
            socialEdit.setText("");
            socialEdit.setHint("@examplehandle");
            socialEdit.setHintTextColor(Color.argb(255,255,0,0));
            validatedClientSide = false;
        }

        if (validatedClientSide) {

            //Make DatabaseReferences from Strings
            DatabaseReference newUserPhoneInfo = database.getReference("users/" + username + "/phone");
            DatabaseReference newUserSocialInfo = database.getReference("users/" + username + "/social");
            DatabaseReference newUserContactInfo = database.getReference("users/" + username + "/contact");

            System.out.println("ZZZ");

            //Listener to check for existing usernames--we define the method inside addOnCompleteListener parameters
            refDatabase.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if (task.isSuccessful()) { //Successful task case--if the usernames were read
                        System.out.println("AAA");
                        if (!task.getResult().exists()) { //If the username is not found
                            Toast.makeText(ClubInformationGatheringActivity.this, "Failed to Find the account.", Toast.LENGTH_SHORT);

                        } else    { //Phonenumber is not found: user can create account with this Phonenumber
                            // Make object

                                account.setPhoneNumber(phonenumber);
                                account.setMainContactName(name);
                                account.setSocialMediaLink(social);


                            newUserPhoneInfo.setValue(phonenumber);
                            newUserSocialInfo.setValue(social);
                            newUserContactInfo.setValue(name);



                            //THIS HAS BEEN COMMENTED OUT AS THERE IS NOTHING THAT THIS ACTIVITY NEEDS TO RETURN
                            //THE ACCOUNT INFORMATION IS UPDATED IN THIS ACTIVITY
                            /*
                            //Display welcome screen to user and pass account information to welcome screen
                            Intent intent = new Intent(getApplicationContext(), ClubInformationGatheringActivity.class);



                            intent.putExtra("account", account);
                            System.out.println(intent.getSerializableExtra("account"));
                            //intent.putExtra("nextActivity", "UIScreen"); //NOT SURE
                            setResult(1, intent);
                            */


                            finish();
                        }

                    } else { //If the Phonenumbers were not read
                        Toast.makeText(ClubInformationGatheringActivity.this, "Failed to search by Phonenumber.", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }



}
