package com.example.grimpeurscyclingclub;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ParticipantAccount extends Account implements PublicUser
{

    //------------------------
    // INSTANCE VARIABLES
    //------------------------


    //List of keys of Events the ParticipantAccount is registered to
    List<String> registeredEvents;
    //Social media link
    String socialMediaLink;
    String mainContactName;
    String phoneNumber;


    //------------------------
    // CONSTRUCTOR
    //------------------------

    public ParticipantAccount(String aUsername, String aPassword, String aEmail)
    {
        super(aUsername, aPassword, aEmail);

        //Instantiate registeredEvents list
        registeredEvents = new ArrayList<>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    public String getSocialMediaLink() {
        return socialMediaLink;
    }
    public void setSocialMediaLink(String link) {
        socialMediaLink = link;
    }
    public String getMainContactName() {
        return mainContactName;
    }
    public void setMainContactName(String name) {
        mainContactName = name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }

    public List<String> getRegisteredEvents() {return registeredEvents;}
    public void clearRegisteredEvents() {registeredEvents.clear();}

    public void setRegistrationToEvent(String id) {registeredEvents.add(id); System.out.println("In setReg.To.Ev.: " + this.getRegisteredEvents());}
    public void addEventToRegistrations(String id) {
        //Update registeredEvents in object
        registeredEvents.add(id);
        //Update registeredEvents in Firebase
        FirebaseDatabase.getInstance().getReference("users/"+getUsername()+"/registeredEvents").setValue(getRegisteredEvents());
    }
    public void removeEventFromRegistrations(String id) {
        //Update registeredEvents in object
        registeredEvents.remove(id);
        //Update registeredEvents in Firebase
        FirebaseDatabase.getInstance().getReference("users/"+getUsername()+"/registeredEvents").setValue(getRegisteredEvents());
    }

    public void delete()
    {
        super.delete();
    }

}