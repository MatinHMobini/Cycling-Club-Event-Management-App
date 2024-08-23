package com.example.grimpeurscyclingclub;

public class RoadStageRace extends Event
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //------------------------
    // CONSTRUCTOR
    //------------------------

    //Zero-arity constructor needed for methods in EventCatalogue
    public RoadStageRace() {

    }

    public RoadStageRace(AdminAccount aAdminAccount, CyclingClubAccount aCyclingClubAccount)
    {
        super(aAdminAccount, aCyclingClubAccount);
    }

    public RoadStageRace(String id, String name, String date, CyclingClubAccount organizerAccount, String location, double registrationFee, int participantLimit) {
        super(id, name, date, organizerAccount, location, registrationFee, participantLimit);
    }

    //------------------------
    // INTERFACE
    //------------------------

    public void delete()
    {
        super.delete();
    }

    //Implementation of superclass's abstract method
    public String getType() {
        return "Road Stage Race";
    }

}