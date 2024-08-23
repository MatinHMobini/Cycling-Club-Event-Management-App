package com.example.grimpeurscyclingclub;

public class TimeTrial extends Event
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //------------------------
    // CONSTRUCTOR
    //------------------------


    //Zero-arity constructor needed for methods in EventCatalogue
    public TimeTrial() {

    }

    public TimeTrial(AdminAccount aAdminAccount, CyclingClubAccount aCyclingClubAccount)
    {
        super(aAdminAccount, aCyclingClubAccount);
    }

    public TimeTrial(String id, String name, String date, CyclingClubAccount organizerAccount, String location, double registrationFee, int participantLimit) {
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
        return "Time Trial";
    }

}