package com.example.grimpeurscyclingclub;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an abstract parent class of all possible events in the system. If need be, this class
 *  can be made real (no longer abstract) by removing the keyword from the class definition and
 *  from the getType() method declaraction.
 *
 *  The abstract nature prevents programmers from creating another subclass without implementing
 *  the getType() method. As all the possible subclasses have alrady been created, the abstract
 *  modifier is not needed but remains good practice.
 */

public abstract class Event
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------
    //Currently used for adding event to Firebase
    private String eventName;

    private String date;

    private String location;

    private double registrationFee;

    private int participantLimit;

    private CyclingClubAccount organizerAccount;

    private String id;

    //Event Associations
    private AdminAccount adminAccount;

    //list of keys of usernames of registeredUsers
    private List<String> registeredUsers;

    //------------------------
    // CONSTRUCTORS
    //------------------------

    //Zero-arity constructor needed for methods in EventCatalogue
    public Event() {
        registeredUsers = new ArrayList<>();
    }

    public Event(AdminAccount aAdminAccount, CyclingClubAccount aCyclingClubAccount)
    {
        boolean didAddAdminAccount = setAdminAccount(aAdminAccount);
        if (!didAddAdminAccount)
        {
            throw new RuntimeException("Unable to create event due to adminAccount. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }

        if (aCyclingClubAccount == null) { //Null CyclingClubAccount parameter
            throw new RuntimeException("Missing CyclingClubAccount instance to create event.");
        }
        //Set CyclingClubAccount association
        organizerAccount = aCyclingClubAccount;
        //Instantiate registeredUsers List
        registeredUsers = new ArrayList<>();
    }
    public Event(String id, String eventName, String date, CyclingClubAccount organizerAccount,String location,double registrationFee,int participantLimit){
        this.id = id;
        setDate(date);
        setEventName(eventName);
        this.location = location;
        this.organizerAccount = organizerAccount;
        this.participantLimit = participantLimit;
        this.registrationFee = registrationFee;
        //Instantiate registeredUsers List
        this.registeredUsers = new ArrayList<>();
    }

    //------------------------
    // INTERFACE
    //------------------------
    public double getRegistrationFee(){
        return this.registrationFee;
    }
    public int getParticipantLimit(){
        return this.participantLimit;
    }
    public CyclingClubAccount getOrganizerAccount(){
        return this.organizerAccount;
    }
    public String getLocation() {
        return this.location;
    }
    public String getDate() {
        return this.date;
    }
    public String getEventName() {
        return this.eventName;
    }
    public String getId() {
        return this.id;
    }
    public void setDate(String d) {
        this.date = d;
    }
    public void setEventName(String n) {
        this.eventName = n;
    }
    /* Code from template association_GetOne */
    public AdminAccount getAdminAccount()
    {
        return adminAccount;
    }
    /* Code from template association_SetOneToMany */
    public boolean setAdminAccount(AdminAccount aAdminAccount)
    {
        boolean wasSet = false;
        if (aAdminAccount == null)
        {
            return wasSet;
        }

        AdminAccount existingAdminAccount = adminAccount;
        adminAccount = aAdminAccount;
        if (existingAdminAccount != null && !existingAdminAccount.equals(aAdminAccount))
        {
            existingAdminAccount.removeEvent(this);
        }
        adminAccount.addEvent(this);
        wasSet = true;
        return wasSet;
    }

    public void delete()
    {
        AdminAccount placeholderAdminAccount = adminAccount;
        this.adminAccount = null;
        if(placeholderAdminAccount != null)
        {
            placeholderAdminAccount.removeEvent(this);
        }
    }

    //FOR SOME REASON, RETURNING THE registeredUsers WILL BREAK FIREBASE, SO WE RETURN JUST THE SIZE
    //WHICH IS FINE FOR SOME REASON
    public int getRegisteredUsersSize() {return registeredUsers.size();}

    //Used to build the list from memory without affecting memory
    public void addToParticipant(String partId) {
        registeredUsers.add(partId);
    }


    /**
     *
     * @param pA account to try to be added to the registeredUsers
     * @return true if account was added successfully; false otherwise
     */
    public boolean addParticipant(ParticipantAccount pA) {
        if (registeredUsers.size() >= getParticipantLimit()) {
            return false;
        }

        if (pA == null) {
            return false;
        }

        if (registeredUsers.contains(pA.getUsername())) {
            return false;
        }

        //Update value in Event object
        addToParticipant(pA.getUsername());
        //Update value in Firebase
        FirebaseDatabase.getInstance().getReference("events/" + this.getId() + "/registeredUsers").setValue(registeredUsers);

        return true;
    }

    /**
     *
     * @param pA account to try to be removed from registeredUsers
     * @return true if account was removed successfully; false otherwise
     */
    public boolean removeParticipant(ParticipantAccount pA) {
        //Update Value in Event object
        registeredUsers.remove(pA.getUsername());
        //Update value in Firebase
        FirebaseDatabase.getInstance().getReference("events/" + this.getId() + "/registeredUsers").setValue(registeredUsers);

        return true;
    }


    //Abstract method for subclasses to implement
    public abstract String getType();

}