package com.example.grimpeurscyclingclub;

import java.util.*;

//FIREBASE IMPLEMENTATION
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
public class AdminAccount extends Account
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //AdminAccount Associations
    private List<Event> events;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public AdminAccount(String aUsername, String aPassword, String aEmail)
    {
        super(aUsername, aPassword, aEmail);
        events = new ArrayList<Event>();
    }

    //------------------------
    // INTERFACE
    //------------------------
    /* Code from template association_GetMany */
    public Event getEvent(int index)
    {
        Event aEvent = events.get(index);
        return aEvent;
    }

    public List<Event> getEvents()
    {
        List<Event> newEvents = Collections.unmodifiableList(events);
        return newEvents;
    }

    public int numberOfEvents()
    {
        int number = events.size();
        return number;
    }

    public boolean hasEvents()
    {
        boolean has = events.size() > 0;
        return has;
    }

    public int indexOfEvent(Event aEvent)
    {
        int index = events.indexOf(aEvent);
        return index;
    }
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfEvents()
    {
        return 0;
    }
    /* Code from template association_AddManyToOne *//*
    public Event addEvent()
    {
        return new Event(this);
    }*/ 

    public boolean addEvent(Event aEvent)
    {
        boolean wasAdded = false;
        if (events.contains(aEvent)) {
            //TODO: Toast to inform admin the event already exists
            return false; }
        AdminAccount existingAdminAccount = aEvent.getAdminAccount();
        boolean isNewAdminAccount = existingAdminAccount != null && !this.equals(existingAdminAccount);
        if (isNewAdminAccount)
        {
            aEvent.setAdminAccount(this);
        }
        else
        {
            //Add the event case--I think?

            //Make DatabaseReferences from identifier and variables
            DatabaseReference newEventNameInfo = FirebaseDatabase.getInstance().getReference("users/" + aEvent.getId() + "/eventName");
            DatabaseReference newEventDateInfo = FirebaseDatabase.getInstance().getReference("users/" + aEvent.getId() + "/date");

            newEventNameInfo.setValue(aEvent.getEventName());
            newEventDateInfo.setValue(aEvent.getDate());
            events.add(aEvent);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeEvent(Event aEvent)
    {
        boolean wasRemoved = false;
        //Unable to remove aEvent, as it must always have a adminAccount
        if (!this.equals(aEvent.getAdminAccount()))
        {
            //Getting specified identifier reference
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(aEvent.getId());
            //Deleting event by identifier
            dR.removeValue();
            events.remove(aEvent);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addEventAt(Event aEvent, int index)
    {
        boolean wasAdded = false;
        if(addEvent(aEvent))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfEvents()) { index = numberOfEvents() - 1; }
            events.remove(aEvent);
            events.add(index, aEvent);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveEventAt(Event aEvent, int index)
    {
        boolean wasAdded = false;
        if(events.contains(aEvent))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfEvents()) { index = numberOfEvents() - 1; }
            events.remove(aEvent);
            events.add(index, aEvent);
            wasAdded = true;
        }
        else
        {
            wasAdded = addEventAt(aEvent, index);
        }
        return wasAdded;
    }

    public void delete()
    {
        for(int i=events.size(); i > 0; i--)
        {
            Event aEvent = events.get(i - 1);
            aEvent.delete();
        }
        super.delete();
    }


    /*
     * This method removes a user from the database by their username.
     * This method currently does not check if the user it attempts to remove exists.
     *
     * @param username the username of the user to be removed
     * @return boolean value of whether the account was removed or not
     * *//*
    public boolean removeUser(String username) {

        //Declare result boolean
        boolean result = false;

        if (username == "admin") { //Catch admin attempting to remove their account
            //TODO: Implement a Toast to tell admin they cannot remove the admin account
            return false;
        }

        //TODO: Implement a check for if the user to be removed actually exists


        //Getting specified username reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username);
        //Deleting username
        dR.removeValue();
        result = true;

        //TODO: Possibly implement a search for the now-removed user to verify they cannot be found?

        //Return result
        return result;
    }*/

}