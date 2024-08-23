package com.example.grimpeurscyclingclub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivity extends AppCompatActivity {

    //-------
    // INSTANCE VARIABLES
    //-------

    private ParticipantAccount participantAccount;

    //Database reference
    private DatabaseReference participantReference;

    //XML item instance variables
    //Text fields
    TextView usernameShow;
    TextView emailShow;
    TextView socialMediaLinkShow;
    TextView mainContactNameShow;
    TextView phoneNumberShow;

    //Buttons
    Button buttonChangePersonalInformation;
    Button searchForEvents;

    //ListView
    ListView listViewRegisteredEvents;
    List<Event> registeredEvents;

    //Used to get Event from listener
    private Event searchedForEvent;


    //-------
    // METHODS
    //-------

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_ui);
        Intent intent = getIntent();
        //Get user account from intent
        participantAccount = (ParticipantAccount) intent.getSerializableExtra("account");
        //Get user account database reference from username
        participantReference = FirebaseDatabase.getInstance().getReference().child("users").child(participantAccount.getUsername());


        //Attempt to fill participant account information from Firebase
        /* Check Firebase's storage of this account for following information fields:
         *   ParticipantAccount.socialMediaLink
         *   ParticipantAccount.mainContactName
         *   ParticipantAccount.phoneNumber
         *
         *  If any of these fields are empty (null), launches the information gathering
         *  Activity to acquire information.
         *
         */

        // Use final array to bypass the limits of a parameterized method
        final boolean[] dontComeBack = {false};

        participantReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                System.out.println("AAAz");
                //Check each field for a null value
                String socialMediaLink = snapshot.child("social").getValue(String.class);
                if (socialMediaLink == null && !dontComeBack[0]) { //If the Firebase has null
                    launchClubInformationGatheringActivity(participantAccount, ClubInformationGatheringActivity.class);
                    dontComeBack[0] = true;
                    return; // Will come back to this method because we change the data in the above method
                } else { //Else the Firebase has information in this field
                    participantAccount.setSocialMediaLink(socialMediaLink);
                }
                //If one is null all are null
                //If one is not null all are not null
                String mainContactName = snapshot.child("contact").getValue(String.class);
                participantAccount.setMainContactName(mainContactName);

                String phoneNumber = snapshot.child("phone").getValue(String.class);
                participantAccount.setPhoneNumber(phoneNumber);

                //Reset participant list
                participantAccount.clearRegisteredEvents();

                for (DataSnapshot sh : snapshot.child("registeredEvents").getChildren()) {
                    participantAccount.setRegistrationToEvent(sh.getValue(String.class));
                    System.out.println("HGERE IN LOOP: " + participantAccount.getRegisteredEvents());
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("HGERE: " + participantAccount.getRegisteredEvents());


        //Instantiate XML buttons and their listeners
        searchForEvents = (Button) findViewById(R.id.searchEvents);
        buttonChangePersonalInformation = (Button) findViewById(R.id.editPersonalInfo);


        //Button to edit events
        searchForEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSearchForEventsActivity(participantAccount);
            }
        });

        //Button to call personal information activity
        buttonChangePersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchClubInformationGatheringActivity(participantAccount,ClubInformationGatheringActivity.class);
            }
        });

        //Instantiate registeredEvents list and its listener
        listViewRegisteredEvents = (ListView) findViewById(R.id.ListViewEvents);


        //Listener to display registered Events
        listViewRegisteredEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                Event e = registeredEvents.get(i);

                showParticipantViewOfEventDialog(e);
                return true;
            }
        });

        //Uncomment this if the database reference is needed to exist before a child can be called of it
        //DatabaseReference newUserPhoneInfo = FirebaseDatabase.getInstance().getReference("users/" + username + "/associatedEventTypes");

        //Listener to keep associated ParticipantAccount up to date with Firebase
        participantReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotOfAccount) {

                //Refresh account information on page
                usernameShow = findViewById(R.id.userNameShow);
                emailShow = findViewById(R.id.emailShow);
                socialMediaLinkShow = findViewById(R.id.socialMediaShow);
                mainContactNameShow = findViewById(R.id.contactNameShow);
                phoneNumberShow = findViewById(R.id.phoneNumberShow);

                usernameShow.setText(participantAccount.getUsername());
                emailShow.setText(participantAccount.getEmail());
                socialMediaLinkShow.setText(participantAccount.getSocialMediaLink());
                mainContactNameShow.setText(participantAccount.getMainContactName());
                phoneNumberShow.setText(participantAccount.getPhoneNumber());

                //Instantiate ArrayList
                registeredEvents = new ArrayList<>();

                //Refresh Event list
                //participantAccount.getRegisteredEvents().clear();
                //DataSnapshot snapshot = snapshotOfAccount.child("registeredEvents");
                /*
                //Iterate through all nodes
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    List<Event> list = buildEventListFromKeys()

                    //Adding Event to list
                    //participantAccount.addEventToRegistrations(createEventOfProperType(postSnapshot).getType());
                }*/

                //Build list from Listener
                FirebaseDatabase.getInstance().getReference().child("events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Iterate through eventIds stored in participantAccount's registeredEvents
                        for (DataSnapshot ref : snapshotOfAccount.child("registeredEvents").getChildren()) {
                            String eventId = ref.getValue(String.class);
                            //Iterate through events stored in Firebase
                            for (DataSnapshot sH : snapshot.getChildren()) {
                                //If the event id matches an event id the participant is registered to
                                if (eventId.equals(sH.child("id").getValue(String.class))) {
                                    Event e = createEventOfProperType(sH);
                                    System.out.println(e.getEventName());
                                    registeredEvents.add(e);
                                }
                            }
                        }
                        //Creating adapter
                        EventList eventTypesAdapter = new EventList(ParticipantActivity.this, registeredEvents);
                        //Attaching adapter to listView
                        listViewRegisteredEvents.setAdapter(eventTypesAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("HGERE OS: " + participantAccount.getRegisteredEvents());
    }

    private void showParticipantViewOfEventDialog(Event event) {

        searchedForEvent = event;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.participant_event_view_dialog, null);
        dialogBuilder.setView(dialogView);

        //get TextViews from XML
        final TextView eventName = (TextView) dialogView.findViewById(R.id.eventNameShow);
        final TextView eventLocation = (TextView) dialogView.findViewById(R.id.LocationShow);
        final TextView eventDate = (TextView) dialogView.findViewById(R.id.DateShow);
        final TextView eventFee = (TextView) dialogView.findViewById(R.id.registrationFeeShow);
        final TextView eventType = (TextView) dialogView.findViewById(R.id.eventTypeShow);
        final TextView eventOrganizer = (TextView) dialogView.findViewById(R.id.organizerNameShow);

        FirebaseDatabase.getInstance().getReference().child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    //Adding Event to list
                    if (postSnapshot.child("id").getValue(String.class).equals(event.getId())) {
                        setSearchForEvents(postSnapshot);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Display Event values to TextViews
        eventName.setText(searchedForEvent.getEventName());
        eventLocation.setText(searchedForEvent.getLocation());
        eventDate.setText(searchedForEvent.getDate());
        eventFee.setText(Double.toString(searchedForEvent.getRegistrationFee()));
        eventType.setText(searchedForEvent.getType());
        eventOrganizer.setText(searchedForEvent.getOrganizerAccount().getUsername());

        //Get buttons
        final Button leaveButton = (Button) dialogView.findViewById(R.id.JoinEventButton);
        final Button viewOrganizer = (Button) dialogView.findViewById(R.id.viewOrganizerButton);

        //THIS SHOULD SAY "Join Event" in the search bar dialog
        leaveButton.setText("Leave Event");

        //Show Dialog
        final AlertDialog b = dialogBuilder.create();
        b.show();


        //THIS SHOULD LET THE ACCOUNT LEAVE THE EVENT
        //Attempt to register to the Event
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Event.removeParticipant(ParticipantAccount) removes participant in Event's Firebase as well
                if (searchedForEvent.removeParticipant(participantAccount)) { //If successfully removed

                    //Update registeredEvents list in object and in Firebase
                    participantAccount.removeEventFromRegistrations(searchedForEvent.getId());

                    Toast.makeText(getApplicationContext(), "Successfully unregistered from " + searchedForEvent.getEventName(), Toast.LENGTH_LONG).show();
                    //Dismiss dialog
                    b.dismiss();
                }
            }
        });

        //Launch Activity(no return) to view CyclingClub: ParticipantViewOfCyclingClub.java
        viewOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchViewOrganizer(participantAccount, searchedForEvent);
            }
        });



    }


    private void setSearchForEvents(DataSnapshot postSnapshot) {
        searchedForEvent = createEventOfProperType(postSnapshot);
    }

    private void launchClubInformationGatheringActivity(ParticipantAccount participantAccount, Class c) {
        // Immediately Start ClubInformationGatheringActivity
        Intent intent = new Intent(this, c);
        intent.putExtra("participantAccount", participantAccount);
        startActivityForResult(intent, 10);
    }


    /**
     * This method launches the searching for events Activity.
     *
     * @param participantAccount the ParticipantAccount of the user searching for events
     */
    private void launchSearchForEventsActivity(ParticipantAccount participantAccount) {
        Intent intent = new Intent(this, ParticipantSearch.class);
        intent.putExtra("participantAccount",participantAccount);
        startActivity(intent);
    }

    private void launchViewOrganizer(ParticipantAccount participantAccount, Event event) {

        Intent intent = new Intent(this, ParticipantViewOfCyclingClub.class);
        intent.putExtra("eventOrganizer",event.getOrganizerAccount());
        intent.putExtra("participantAccount",participantAccount);
        startActivity(intent);

    }

    protected Event createEventOfProperType(DataSnapshot postSnapshot) {
        switch (postSnapshot.child("type").getValue().toString()) {
            case "Road Race": //Road Race case
                return postSnapshot.getValue(RoadRace.class);
            case "Road Stage Race": //Road Stage Race
                return postSnapshot.getValue(RoadStageRace.class);
            case "Group Ride": //Group Ride case
                return postSnapshot.getValue(GroupRide.class);
            case "Hill Climb": //Hill Climb case
                return postSnapshot.getValue(HillClimb.class);
            default: //Time Trial case
                return postSnapshot.getValue(TimeTrial.class);
        }


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
        //If below if statement gives NullPointerException, remove method from below if statement
        if (data == null) {
            System.out.println("HERE");
            return;
        }

        //Else the data was returned
        ParticipantAccount account = (ParticipantAccount) data.getSerializableExtra("account");


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



    }
    */
}
