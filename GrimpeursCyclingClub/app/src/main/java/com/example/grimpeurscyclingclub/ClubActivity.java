package com.example.grimpeurscyclingclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubActivity extends AppCompatActivity {


    //DatabaseReference instance variable
    private CyclingClubAccount cyclingAccount;
    //This DatabaseReference is currently unused
    //private DatabaseReference database;
    private DatabaseReference cyclingClubReference;

    //XML item instance variables
    //Text fields
    TextView usernameShow;
    TextView emailShow;
    TextView socialMediaLinkShow;
    TextView mainContactNameShow;
    TextView phoneNumberShow;

    //Buttons
    Button buttonChangePersonalInformation;
    Button buttonChangeEvents;


    //EventType list
    ListView listViewEventTypes;
    List<String> eventTypes;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: replace with cycling club activity
        setContentView(R.layout.cycling_club_ui);

        Intent intent = getIntent();
        cyclingAccount = (CyclingClubAccount) intent.getSerializableExtra("account");

        cyclingClubReference = FirebaseDatabase.getInstance().getReference().child("users").child(cyclingAccount.getUsername());

        //Attempt to fill cycling club account information from Firebase

        /* Check Firebase's storage of this account for following information fields:
         *   CyclingClubAccount.socialMediaLink
         *   CyclingClubAccount.mainContactName
         *   CyclingClubAccount.phoneNumber
         *
         *  If any of these fields are empty (null), launches the information gathering
         *  Activity to acquire information.
         *
         */

        // Use final array to bypass the limits of a parameterized method
        final boolean[] dontComeBack = {false};

        cyclingClubReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                System.out.println("AAAz");
                //Check each field for a null value
                String socialMediaLink = snapshot.child("social").getValue(String.class);
                if (socialMediaLink == null && !dontComeBack[0]) { //If the Firebase has null
                    launchClubInformationGatheringActivity(cyclingAccount, ClubInformationGatheringActivity.class);
                    dontComeBack[0] = true;
                    return; // Will come back to this method because we change the data in the above method
                } else { //Else the Firebase has information in this field
                    cyclingAccount.setSocialMediaLink(socialMediaLink);
                }
                //If one is null all are null
                //If one is not null all are not null
                String mainContactName = snapshot.child("contact").getValue(String.class);
                cyclingAccount.setMainContactName(mainContactName);

                String phoneNumber = snapshot.child("phone").getValue(String.class);
                cyclingAccount.setPhoneNumber(phoneNumber);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Instantiate xml buttons and list
        buttonChangeEvents = (Button) findViewById(R.id.searchEvents);
        buttonChangePersonalInformation = (Button) findViewById(R.id.editPersonalInfo);

        listViewEventTypes = (ListView) findViewById(R.id.ListViewEvents);
        eventTypes = new ArrayList<>();


        //Button to edit events
        buttonChangeEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchChangeEvents(cyclingAccount);
            }
        });

        //Button to call personal information activity
        buttonChangePersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchClubInformationGatheringActivity(cyclingAccount,ClubInformationGatheringActivity.class);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        //Uncomment this if the database reference is needed to exist before a child can be called of it
        //DatabaseReference newUserPhoneInfo = FirebaseDatabase.getInstance().getReference("users/" + username + "/associatedEventTypes");

        //Listener to keep associated ClubActivity up to date with Firebase
        cyclingClubReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotOfAccount) {

                //Refresh account information on page
                usernameShow = findViewById(R.id.userNameShow);
                emailShow = findViewById(R.id.emailShow);
                socialMediaLinkShow = findViewById(R.id.socialMediaShow);
                mainContactNameShow = findViewById(R.id.contactNameShow);
                phoneNumberShow = findViewById(R.id.phoneNumberShow);

                usernameShow.setText(cyclingAccount.getUsername());
                emailShow.setText(cyclingAccount.getEmail());
                socialMediaLinkShow.setText(cyclingAccount.getSocialMediaLink());
                mainContactNameShow.setText(cyclingAccount.getMainContactName());
                phoneNumberShow.setText(cyclingAccount.getPhoneNumber());

                //Refresh EventType list
                eventTypes.clear();
                DataSnapshot snapshot = snapshotOfAccount.child("args");
                //Iterate through all nodes
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Getting arg--if the arg is true (associated by CyclingClub), add to display list
                    if (postSnapshot.getValue(Boolean.class)) {
                        //Adding eventType String to list
                        eventTypes.add(postSnapshot.getKey());
                    }

                }

                //Creating adapter
                EventTypeList eventTypesAdapter = new EventTypeList(ClubActivity.this, eventTypes);
                //Attaching adapter to listview
                listViewEventTypes.setAdapter(eventTypesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void launchClubInformationGatheringActivity(CyclingClubAccount cyclingAccount, Class c) {
        // Immediately Start ClubInformationGatheringActivity
        Intent intent = new Intent(this, c);
        intent.putExtra("cyclingAccount", cyclingAccount);
        startActivityForResult(intent, 10);
    }

    private void launchChangeEvents(CyclingClubAccount cyclingAccount) {
        System.out.println("HERE");
        Intent intent = new Intent(this, CCEventCatalogue.class);
        intent.putExtra("account", cyclingAccount);
        startActivity(intent);
    }

    /**
     * This method receives the information from the ClubInformationGatheringActivity.
     * Right now, this is not needed as ClubInformationGatheringActivity already updates
     * account variables in Firebase.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //If there is no data returned case
        if (data == null) {
            System.out.println("HERE");
            return;

        }



        //Else the data was returned
        CyclingClubAccount account = (CyclingClubAccount) data.getSerializableExtra("account");


        FirebaseDatabase database;
        //DatabaseReference refDatabase;
        database = FirebaseDatabase.getInstance();
        //refDatabase = database.getReference("users");

        DatabaseReference socialDB = database.getReference("users/" + account.getUsername() + "/social");


        socialDB.setValue(account.getSocialMediaLink());

        DatabaseReference phoneDB = database.getReference("users/" + account.getUsername() + "/phone");
        phoneDB.setValue(account.getPhoneNumber());

        DatabaseReference contactDB = database.getReference("users/" + account.getUsername() + "/contact");
        contactDB.setValue(account.getMainContactName());



    }*/

}
