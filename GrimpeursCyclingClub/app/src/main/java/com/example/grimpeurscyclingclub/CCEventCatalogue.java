package com.example.grimpeurscyclingclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CCEventCatalogue extends EventCatalogue {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Changes "Event Types Viewablity" to 'Event Associations" to reuse xml from admin
        TextView eventText = (TextView) findViewById(R.id.eventTypeAssociation);
        eventText.setText("Event Associations");

        //Get switches from display and get their value from firebase
        Switch timeTrialSwitch = (Switch) findViewById(R.id.timeTrialSwitch);
        setSwitchValue(timeTrialSwitch, "TimeTrialSwitch");
        setSwitchVisibility(timeTrialSwitch, "TimeTrialSwitch");
        timeTrialSwitch.setOnCheckedChangeListener(getSwitchListener(timeTrialSwitch, "TimeTrialSwitch"));

        Switch hillClimbSwitch = (Switch) findViewById(R.id.hillClimbSwitch);
        setSwitchValue(hillClimbSwitch, "HillClimbSwitch");
        setSwitchVisibility(hillClimbSwitch, "HillClimbSwitch");
        hillClimbSwitch.setOnCheckedChangeListener(getSwitchListener(hillClimbSwitch, "HillClimbSwitch"));

        Switch roadStageRaceSwitch = (Switch) findViewById(R.id.roadStageRaceSwitch);
        setSwitchValue(roadStageRaceSwitch, "RoadStageSwitch");
        setSwitchVisibility(roadStageRaceSwitch, "RoadStageSwitch");
        roadStageRaceSwitch.setOnCheckedChangeListener(getSwitchListener(roadStageRaceSwitch, "RoadStageSwitch"));

        Switch roadRaceSwitch = (Switch) findViewById(R.id.roadRaceSwitch);
        setSwitchValue(roadRaceSwitch, "RoadRaceSwitch");
        setSwitchVisibility(roadRaceSwitch, "RoadRaceSwitch");
        roadRaceSwitch.setOnCheckedChangeListener(getSwitchListener(roadRaceSwitch, "RoadRaceSwitch"));

        Switch groupRidesSwitch = (Switch) findViewById(R.id.groupRidesSwitch);
        setSwitchValue(groupRidesSwitch, "GroupRidesSwitch");
        setSwitchVisibility(groupRidesSwitch, "GroupRidesSwitch");
        groupRidesSwitch.setOnCheckedChangeListener(getSwitchListener(groupRidesSwitch, "GroupRidesSwitch"));

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
                } else {
                    // Removes from view
                    s.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setSwitchValue(Switch s, String n) {
        Intent intent = getIntent();
        CyclingClubAccount account = (CyclingClubAccount) intent.getSerializableExtra("account");
        System.out.println("HERE SVAL");

        //Standard firebase boilerplate
        FirebaseDatabase database;
        DatabaseReference refDatabase;
        database = FirebaseDatabase.getInstance();
        refDatabase = database.getReference("users/" + account.getUsername() + "/args");
        //Firebase Moment
        refDatabase.child(n).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                System.out.println("COMP SVAL");
                //Null matters here as no value implies no association
                if(!task.getResult().exists()) {
                    s.setChecked(false);
                    return;
                }
                // ELse just read the value from the users args
                String switchVal = task.getResult().getValue().toString();
                if(switchVal.equals("true")) {
                    s.setChecked(true);
                } else {
                    s.setChecked(false);
                }
                System.out.println("DONE SVAL");
            }
        });
    }

    public CompoundButton.OnCheckedChangeListener getSwitchListener(Switch s, String n) {
        return new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Get account
                Intent intent = getIntent();
                CyclingClubAccount account = (CyclingClubAccount) intent.getSerializableExtra("account");

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users/" + account.getUsername() + "/args").child(n);
                //updating event
                dR.setValue(isChecked);
                Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();;

            }
        };
    }

    protected void onStart() {

        super.onStart();
        Intent intent = getIntent();
        CyclingClubAccount account = (CyclingClubAccount) intent.getSerializableExtra("account");

        //attaching value event listener
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous event list
                events.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting event
                    Event event = createEventOfProperType(postSnapshot);
                    if (event.getOrganizerAccount().getUsername().equals(account.getUsername())) {
                        //adding product to the list
                        events.add(event);
                    }
                }

                //creating adapter
                EventList productsAdapter = new EventList(CCEventCatalogue.this, events);
                //attaching adapter to the listview
                listViewEvents.setAdapter(productsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
