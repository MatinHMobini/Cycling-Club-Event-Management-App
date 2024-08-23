package com.example.grimpeurscyclingclub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminEventCatalogue extends EventCatalogue {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get switches from display and get their value from firebase
        Switch timeTrialSwitch = (Switch) findViewById(R.id.timeTrialSwitch);
        setSwitchValue(timeTrialSwitch, "TimeTrialSwitch");
        timeTrialSwitch.setOnCheckedChangeListener(getSwitchListener(timeTrialSwitch, "TimeTrialSwitch"));

        Switch hillClimbSwitch = (Switch) findViewById(R.id.hillClimbSwitch);
        setSwitchValue(hillClimbSwitch, "HillClimbSwitch");
        hillClimbSwitch.setOnCheckedChangeListener(getSwitchListener(hillClimbSwitch, "HillClimbSwitch"));

        Switch roadStageRaceSwitch = (Switch) findViewById(R.id.roadStageRaceSwitch);
        setSwitchValue(roadStageRaceSwitch, "RoadStageSwitch");
        roadStageRaceSwitch.setOnCheckedChangeListener(getSwitchListener(roadStageRaceSwitch, "RoadStageSwitch"));

        Switch roadRaceSwitch = (Switch) findViewById(R.id.roadRaceSwitch);
        setSwitchValue(roadRaceSwitch, "RoadRaceSwitch");
        roadRaceSwitch.setOnCheckedChangeListener(getSwitchListener(roadRaceSwitch, "RoadRaceSwitch"));

        Switch groupRidesSwitch = (Switch) findViewById(R.id.groupRidesSwitch);
        setSwitchValue(groupRidesSwitch, "GroupRidesSwitch");
        groupRidesSwitch.setOnCheckedChangeListener(getSwitchListener(groupRidesSwitch, "GroupRidesSwitch"));

    }

    public void setSwitchValue(Switch s, String n) {
        //Standard firebase boilerplate
        FirebaseDatabase database;
        DatabaseReference refDatabase;
        database = FirebaseDatabase.getInstance();
        refDatabase = database.getReference("args");
        //Firebase Moment
        refDatabase.child(n).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //No need for null check because if these dont exist you have bigger problems
                String switchVal = task.getResult().getValue().toString();
                if(switchVal.equals("true")) {
                    s.setChecked(true);
                } else {
                    s.setChecked(false);
                }
            }
        });
    }

    public CompoundButton.OnCheckedChangeListener getSwitchListener(Switch s, String n) {
        return new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("args").child(n);
                //updating event
                dR.setValue(isChecked);
                Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();;

            }
        };
    }

    protected void onStart() {
        super.onStart();
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
                    //adding product to the list
                    events.add(event);
                }

                //creating adapter
                EventList productsAdapter = new EventList(AdminEventCatalogue.this, events);
                //attaching adapter to the listview
                listViewEvents.setAdapter(productsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
