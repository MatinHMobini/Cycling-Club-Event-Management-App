package com.example.grimpeurscyclingclub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParticipantSearch extends AppCompatActivity {
    private DatabaseReference participantReference;
    DatabaseReference databaseEvents;
    List<Event> events;
    ListView listViewEvents;

    String searchQuery;

    DataSnapshot lastDataSnapshot;

    private ParticipantAccount participantAccount;
    Switch timeTrialSwitch;
    Switch hillClimbSwitch;
    Switch roadStageSwitch;
    Switch roadRaceSwitch;
    Switch groupRidesSwitch;
    /*
    private CyclingClubAccount clubAccount;

    private DatabaseReference clubReference;*/

    boolean tts;
    boolean hcs;
    boolean rss;
    boolean rrs;
    boolean grs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //Get user account from intent
        participantAccount = (ParticipantAccount) intent.getSerializableExtra("participantAccount");

        setContentView(R.layout.participant_search_bar);
        participantReference = FirebaseDatabase.getInstance().getReference().child("users");
        listViewEvents = (ListView) findViewById(R.id.ListViewEvents);
        //Firebase reference
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        //Instantiate events ArrayList
        events = new ArrayList<>();

        //Load switches
        timeTrialSwitch = (Switch) findViewById(R.id.timeTrialSwitch2);
        setSwitchVisibility(timeTrialSwitch, "TimeTrialSwitch");
        tts = false;
        timeTrialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tts = isChecked;
            }
        });

        hillClimbSwitch = (Switch) findViewById(R.id.hillClimbSwitch2);
        setSwitchVisibility(hillClimbSwitch, "HillClimbSwitch");
        hcs = false;
        hillClimbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hcs = isChecked;
            }
        });

        roadStageSwitch = (Switch) findViewById(R.id.roadStageRaceSwitch2);
        setSwitchVisibility(roadStageSwitch, "RoadStageSwitch");
        rss = false;
        roadStageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rss = isChecked;
            }
        });

        roadRaceSwitch = (Switch) findViewById(R.id.roadRaceSwitch2);
        setSwitchVisibility(roadRaceSwitch, "RoadRaceSwitch");
        rrs = false;
        roadRaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rrs = isChecked;
            }
        });

        groupRidesSwitch = (Switch) findViewById(R.id.groupRidesSwitch2);
        setSwitchVisibility(groupRidesSwitch, "GroupRidesSwitch");
        grs = false;
        groupRidesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                grs = isChecked;
            }
        });




        searchQuery = "";

        //Instantiate XML buttons and their listeners
        Button searchForEvents = (Button) findViewById(R.id.searchButton);

        //Button to edit events
        searchForEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lastDataSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = events.get(i);
                //Pass event object into dialogue
                showJoinDialog(event);
                return true;
            }
        });

    }


    public void setSwitchVisibility(Switch s, String n) {
        //Standard firebase boilerplate
        FirebaseDatabase database;
        DatabaseReference refDatabase;
        database = FirebaseDatabase.getInstance();
        refDatabase = database.getReference("args");
        //Firebase Moment
        refDatabase.child(n).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                String switchVal = task.getResult().getValue().toString();

                if(switchVal.equals("true")) {
                    s.setChecked(true);
                } else {
                    // Removes from view
                    s.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void showJoinDialog(Event event) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.participant_event_view_dialog, null);
        dialogBuilder.setView(dialogView);

        System.out.println("JoinDialog");

        final TextView textName = (TextView) dialogView.findViewById(R.id.eventNameShow);
        final TextView textLocation = (TextView) dialogView.findViewById(R.id.LocationShow);
        final TextView textRegistrationFee = (TextView) dialogView.findViewById(R.id.registrationFeeShow);
        final TextView textParticipantLimit = (TextView) dialogView.findViewById(R.id.DateShow);
        final TextView textTypeShow = (TextView) dialogView.findViewById(R.id.eventTypeShow);
        final TextView textOrganizer = (TextView) dialogView.findViewById(R.id.organizerNameShow);

        final Button joinButton = (Button) dialogView.findViewById(R.id.JoinEventButton);
        final Button organizerButton = (Button) dialogView.findViewById(R.id.viewOrganizerButton);

        textName.setText(event.getEventName());
        textLocation.setText(event.getLocation());
        textRegistrationFee.setText(Double.toString(event.getRegistrationFee()));
        textParticipantLimit.setText(Integer.toString(event.getParticipantLimit()));

        textTypeShow.setText(event.getType());
        textOrganizer.setText(event.getOrganizerAccount().getUsername());

        dialogBuilder.setTitle(event.getEventName());
        final AlertDialog b = dialogBuilder.create();
        b.show();

        //Build event's registeredUsers list so to preserve registered ParticipantAccount keys

        databaseEvents.child(event.getId() + "/registeredUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    event.addToParticipant(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        organizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                launchViewOrganizer(event);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("E size: " + event.getRegisteredUsersSize());

                 if (participantAccount.getRegisteredEvents().contains(event.getId())) { //If already joined case
                    Toast.makeText(getApplicationContext(), "Already registered to " + event.getEventName(), Toast.LENGTH_LONG).show();
                } else if (event.getRegisteredUsersSize() >= event.getParticipantLimit()) { //If full case
                     Toast.makeText(getApplicationContext(), event.getEventName() + " is already full ", Toast.LENGTH_LONG).show();
                 } else { //Else can join case
                    joinEvent(event);
                }
                b.dismiss(); //Dismiss dialog either way
            }
        });



    }

    protected void joinEvent(Event event) {
        event.addParticipant(participantAccount);
        System.out.println(event);
        System.out.println(participantAccount);
        participantAccount.addEventToRegistrations(event.getId());
    }

    private void launchViewOrganizer(Event event) {

        Intent intent = new Intent(this, ParticipantViewOfCyclingClub.class);
        intent.putExtra("eventOrganizer", event.getOrganizerAccount());
        intent.putExtra("participantAccount", participantAccount);
        startActivity(intent);

    }

    public void search() {

        //clearing the previous event list
        events.clear();

        EditText search = (EditText) findViewById(R.id.searchBox);
        searchQuery = search.getText().toString();
        System.out.println("Searching for " + searchQuery);
        //attaching value event listener

        //iterating through all the nodes
        for (DataSnapshot postSnapshot : lastDataSnapshot.getChildren()) {
            //getting event
            Event event = createEventOfProperType(postSnapshot);
            boolean typeSelected = false;

            switch (event.getType()) {
                case "Time Trial":
                    typeSelected = tts;
                    break;
                case "Hill Climb":
                    typeSelected = hcs;
                    break;
                case "Road Stage Race":
                    typeSelected = rss;
                    break;
                case "Road Race":
                    typeSelected = rrs;
                    break;
                case "Group Ride":
                    typeSelected = grs;
                    break;
                default:
                    System.out.println("Not Found");
                    break;
            }

            if(!typeSelected) {
                System.out.println(event.getEventName());
                System.out.println(event.getType());
                continue;
            }

            if (event.getOrganizerAccount().getUsername().contains(searchQuery)) {
                //adding product to the list
                events.add(event);
            } else if (event.getEventName().contains(searchQuery)) {
                //adding product to the list
                events.add(event);
            }
        }

        if (events.isEmpty()) {
            System.out.println("Nothing Found");
        } else {
            //creating adapter
            EventList productsAdapter = new EventList(ParticipantSearch.this, events);

            //attaching adapter to the listview
            listViewEvents.setAdapter(productsAdapter);
        }


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

}
