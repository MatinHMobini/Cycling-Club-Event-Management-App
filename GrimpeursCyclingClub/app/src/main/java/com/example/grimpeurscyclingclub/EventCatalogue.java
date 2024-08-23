package com.example.grimpeurscyclingclub;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class EventCatalogue extends AppCompatActivity {

    //EventTypes array of Strings
    static String[] eventTypes = {"TimeTrial", "HillClimb", "RoadStageRace", "RoadRace", "GroupRide"};

    Button buttonAddEvent;
    ListView listViewEvents;
    List<Event> events;
    DatabaseReference databaseEvents;

    Account baseAccount;


    //DatePicker instance variables
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_event_catalogue);
        Intent intent = getIntent();
        Account baseAccount = (Account) intent.getSerializableExtra("account");
        //Items in change_event_catalogue.xml
        listViewEvents = (ListView) findViewById(R.id.ListViewEvents);
        buttonAddEvent = (Button) findViewById(R.id.addButton);

        //Firebase reference
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        //Instanciate events ArrayList
        events = new ArrayList<>();


        //DatePicker initialization
        initDatePicker();




        //adding an onclicklistener to button
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            //Call add dialogue on click
            public void onClick(View view) {

                if (createSwitchList().size() == 0) { //If no switches are enabled

                    //Inform user via toast why the button click will not go through
                    Toast.makeText(getApplicationContext(), "Need enabled Event Types", Toast.LENGTH_LONG).show();

                    //Exit onClick method
                    return;
                }

                showAddDialog();
            }
        });

        //LongClickListener to trigger update and delete dialogue
        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (createSwitchList().size() == 0) { //If no switches are enabled

                    //Inform user via toast why the button click will not go through
                    Toast.makeText(getApplicationContext(), "Need enabled Event Types", Toast.LENGTH_LONG).show();

                    //Exit onLongClick method
                    return false;
                }

                Event event = events.get(i);
                //Pass event object into dialogue
                showUpdateDeleteDialog(event);
                return true;
            }
        });
    }


    /**
     * This onStart method is called when the Activity is on-screen. This means the method
     * is called after onCreate is called. This method creates a listener connected to the
     * /events subsection of the Firebase database and recreates the list of users in the
     * database whenever the /events subsection of the database changes.
     */
    @Override
    protected void onStart() {

        super.onStart();

    }


    private void showUpdateDeleteDialog(Event event) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_event_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editEventName);
        final EditText editTextLocation = (EditText) dialogView.findViewById(R.id.editEventLocation);
        final EditText editTextRegistrationFee = (EditText) dialogView.findViewById(R.id.editEventRegistrationFee);
        final EditText editTextParticipantLimit = (EditText) dialogView.findViewById(R.id.editEventParticipantLimit);


        editTextName.setText(event.getEventName());
        editTextLocation.setText(event.getLocation());
        editTextRegistrationFee.setText(Double.toString(event.getRegistrationFee()));
        editTextParticipantLimit.setText(Integer.toString(event.getParticipantLimit()));


        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.updateEventButton);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteEventButton);

        //DatePicker
        dateButton = dialogView.findViewById(R.id.datePickerButton);
        //Set value from date in Event instance
        if (event.getDate() != null)
            dateButton.setText(event.getDate());

        //get the spinner from the xml.
        Spinner dropdown = dialogView.findViewById(R.id.editEventType);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, createSwitchList());
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        dialogBuilder.setTitle(event.getEventName());
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //need to fix so that price is date now

                String name = editTextName.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) { //If the event name is not empty

                    //Make regex for int and double
                    Pattern intPattern = Pattern.compile("^\\d+$");
                    Pattern doublePattern = Pattern.compile("^(\\d+(\\.\\d*)?|\\.\\d+)$");

                    if (editTextLocation.getText().toString().trim().equals("")) { //If location is empty
                        editTextLocation.setText("");
                        editTextLocation.setHint("Event location");
                        editTextLocation.setHintTextColor(Color.argb(255,255,0,0));
                        return;
                    }
                    if (!doublePattern.matcher(editTextRegistrationFee.getText().toString().trim()).find()) { //If registration fee is empty
                        editTextRegistrationFee.setText("");
                        editTextRegistrationFee.setHint("Event registration fee");
                        editTextRegistrationFee.setHintTextColor(Color.argb(255,255,0,0));
                        return;
                    }
                    if (!intPattern.matcher(editTextParticipantLimit.getText().toString().trim()).find()) { //If participant limit is empty
                        editTextParticipantLimit.setText("");
                        editTextParticipantLimit.setHint("Event participant limit");
                        editTextParticipantLimit.setHintTextColor(Color.argb(255,255,0,0));
                        return;
                    }

                    //Variables to provide to updateEvent
                    String id = event.getId();
                    String date = dateButton.getText().toString();
                    //TODO: REMOVE THIS COMMENT ONCE DatePicker IMPLEMENTED
                    // int date = Integer.parseInt(editTextDate.getText().toString().trim());
                    String location = editTextLocation.getText().toString().trim();
                    double registrationFee = Double.parseDouble(editTextRegistrationFee.getText().toString().trim());
                    int participantLimit = Integer.parseInt(editTextParticipantLimit.getText().toString().trim());
                    CyclingClubAccount cyclingClubAccount = event.getOrganizerAccount();

                    //Conditions for Event subclass
                    Event newEvent;

                    if (dropdown.getSelectedItem().toString().equals(eventTypes[0])) { //TimeTrial case
                        newEvent = new TimeTrial(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                    } else if (dropdown.getSelectedItem().toString().equals(eventTypes[1])) { //HillClimb case
                        newEvent = new HillClimb(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                    } else if (dropdown.getSelectedItem().toString().equals(eventTypes[2])) { //RoadStageRace case
                        newEvent = new RoadStageRace(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                    } else if (dropdown.getSelectedItem().toString().equals(eventTypes[3])) { //RoadRace case
                        newEvent = new RoadRace(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                    } else { //GroupRide case
                        newEvent = new GroupRide(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                    }

                    updateEvent(newEvent);
                    b.dismiss();
                } else { //if the Event name is empty

                    editTextName.setText("");
                    editTextName.setHint("Event name");
                    editTextName.setHintTextColor(Color.argb(255,255,0,0));
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvent(event.getId());
                b.dismiss();
            }
        });
    }

    void showAddDialog() {
        baseAccount = (Account) getIntent().getSerializableExtra("account");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.event_add_dialog, null);
        dialogBuilder.setView(dialogView);

        //get the spinner from the xml.
        Spinner dropdown = (Spinner) dialogView.findViewById(R.id.chooseEventType);

        //create an adapter to describe how the items are displayed; adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, createSwitchList());
        //set the spinners adapter to the previously created one.
        System.out.println(adapter);
        //DEBUG:
        dropdown.setAdapter(adapter);


        final EditText editTextName = (EditText) dialogView.findViewById(R.id.addEventName);
        final EditText editTextLocation = (EditText) dialogView.findViewById(R.id.addEventLocation);
        final EditText editTextRegistrationFee = (EditText) dialogView.findViewById(R.id.addEventRegistrationFee);
        final EditText editTextParticipantLimit = (EditText) dialogView.findViewById(R.id.searchBox);

        //DatePicker
        dateButton = dialogView.findViewById(R.id.datePickerButton);
        //Set default date to day of creation
        dateButton.setText(Date.getTodaysDate());

        final Button buttonCreate = (Button) dialogView.findViewById(R.id.searchButton);


        dialogBuilder.setTitle("Create event");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verify all the text fields have inputs
                if (editTextName.getText().toString().trim().equals("")) {
                    editTextName.setText("");
                    editTextName.setHint("Event name");
                    editTextName.setHintTextColor(Color.argb(255,255,0,0));
                }
                //Make regex for int and double
                Pattern intPattern = Pattern.compile("^\\d+$");
                Pattern doublePattern = Pattern.compile("^(\\d+(\\.\\d*)?|\\.\\d+)$");

                //Verify all the text fields have inputs
                if (editTextLocation.getText().toString().trim().equals("")) { //If location is empty
                    editTextLocation.setText("");
                    editTextLocation.setHint("Event location");
                    editTextLocation.setHintTextColor(Color.argb(255,255,0,0));
                    return;
                }
                if (!doublePattern.matcher(editTextRegistrationFee.getText().toString().trim()).find()) { //If registration fee is empty
                    editTextRegistrationFee.setText("");
                    editTextRegistrationFee.setHint("Event registration fee");
                    editTextRegistrationFee.setHintTextColor(Color.argb(255,255,0,0));
                    return;
                }
                if (!intPattern.matcher(editTextParticipantLimit.getText().toString().trim()).find()) { //If participant limit is empty
                    editTextParticipantLimit.setText("");
                    editTextParticipantLimit.setHint("Event participant limit");
                    editTextParticipantLimit.setHintTextColor(Color.argb(255,255,0,0));
                    return;
                }


                //getting the values to save -- pass in by event or by id reference? We cannot get them from the activity, only from the dialogue
                String name = editTextName.getText().toString().trim();
                String date = dateButton.getText().toString();
                //TODO: REMOVE THIS COMMENT ONCE DatePicker IMPLEMENTED
                // int date = Integer.parseInt(editTextDate.getText().toString().trim());
                String location = editTextLocation.getText().toString().trim();
                int participantLimit = Integer.parseInt(editTextParticipantLimit.getText().toString().trim());
                double registrationFee = Double.parseDouble(editTextRegistrationFee.getText().toString().trim());

                //Passwords and emails are not needed so kept null
                CyclingClubAccount cyclingClubAccount = new CyclingClubAccount(baseAccount.getUsername(), "null", "null");

                //For  Deliverable 2, as CyclingClub functionality has not been implemented, this does not
                // check signup qualifications--this is hardcoded to an existing Club account.




                //getting a unique id using push().getKey() method
                //it will create a unique id and we will use it as the Primary Key for our Event
                String id = databaseEvents.push().getKey();

                //Conditions for EventType
                Event newEvent;
                if (dropdown.getSelectedItem().toString().equals(eventTypes[0])) { //TimeTrial case
                    newEvent = new TimeTrial(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                } else if (dropdown.getSelectedItem().toString().equals(eventTypes[1])) { //HillClimb case
                    newEvent = new HillClimb(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                } else if (dropdown.getSelectedItem().toString().equals(eventTypes[2])) { //RoadStageRace case
                    newEvent = new RoadStageRace(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                } else if (dropdown.getSelectedItem().toString().equals(eventTypes[3])) { //RoadRace case
                    newEvent = new RoadRace(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                } else { //GroupRide case
                    newEvent = new GroupRide(id, name, date, cyclingClubAccount, location, registrationFee, participantLimit);
                }

                addEvent(newEvent);

                //Clear text fields
                editTextName.setText("");

                editTextParticipantLimit.setText("");
                editTextRegistrationFee.setText("");
                editTextLocation.setText("");

                b.dismiss();
            }
        });

    }

    private void updateEvent(Event newEvent) {
        //getting the specified event reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(newEvent.getId());
        //updating event
        dR.setValue(newEvent);
        Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
    }

    private boolean deleteEvent(String id) {
        //getting the specified event reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(id);
        //removing event
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    private void addEvent(Event newEvent) {
        //checking if the name is provided
        if (!TextUtils.isEmpty(newEvent.getEventName())) {
            databaseEvents.child(newEvent.getId()).setValue(newEvent);
            Toast.makeText(this, "Event added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    List<String> createSwitchList() {
        //Get switches from display
        Switch timeTrialSwitch = (Switch) findViewById(R.id.timeTrialSwitch);
        Switch hillClimbSwitch = (Switch) findViewById(R.id.hillClimbSwitch);
        Switch roadStageRaceSwitch = (Switch) findViewById(R.id.roadStageRaceSwitch);
        Switch roadRaceSwitch = (Switch) findViewById(R.id.roadRaceSwitch);
        Switch groupRidesSwitch = (Switch) findViewById(R.id.groupRidesSwitch);

        //Initialize switchList to return
        List<String> switchList = new ArrayList<>();

        //Build list from checked switches
        if (timeTrialSwitch.isChecked()) {switchList.add(eventTypes[0]);}
        if (hillClimbSwitch.isChecked()) {switchList.add(eventTypes[1]);}
        if (roadStageRaceSwitch.isChecked()) {switchList.add(eventTypes[2]);}
        if (roadRaceSwitch.isChecked()) {switchList.add(eventTypes[3]);}
        if (groupRidesSwitch.isChecked()) {switchList.add(eventTypes[4]);}

        //Return list
        return switchList;
    }


    /**
     * Method to handle Event creation as subclasses only
     *  This switch is necessary to determine the subclass of Event to create
     *  as Firebase's DataSnapShot.getValue(T t) method returns an object of
     *  the specified class and cannot process subclasses
     *
     * @param postSnapshot the location in memory of the Event to be created
     *
     * @return Event the Event of proper subclass
    */

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
     * Code created by Callum Hill, December 18th, 2020: https://github.com/codeWithCal/DatePickerTutorial/tree/master
     *
     * */

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = Date.makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }



    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

}