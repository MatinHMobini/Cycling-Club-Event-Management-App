package com.example.grimpeurscyclingclub;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CyclingClubAccount extends Account implements PublicUser
{

    //------------------------
    // INSTANCE VARIABLES
    //------------------------

    /*
    //List of Events the CyclingClubAccount organizes
    List<Event> organizedEvents;*/

    //List of Reviews of CyclingClubAccount by ParticipantAccounts
    List<Comment> reviews;

    //Social media link
    String socialMediaLink;
    String mainContactName;
    String phoneNumber;

    //------------------------
    // CONSTRUCTORS
    //------------------------

    //Zero-arity constructor needed for methods in EventCatalogue
    public CyclingClubAccount() {
        //Instantiate reviews
        reviews = new ArrayList<>();
    }

    public CyclingClubAccount(String aUsername, String aPassword, String aEmail)
    {
        super(aUsername, aPassword, aEmail);

        //Instantiate reviews
        reviews = new ArrayList<>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    public String getSocialMediaLink() {
        return socialMediaLink;
    }
    public void setSocialMediaLink(String link) {
        //This condition was added in testing
        if (link.charAt(0) != '@') {
            return;
        }
        System.out.println("VALID STRING");
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

    public List<Comment> getReviews() {return reviews;}

    public boolean addReview(Comment c) {
        if (c == null) { return false;}

        //if (!c.getClubAccount().equals(this)) {return false;}

        //Update value in CyclingClubAccount object
        reviews.add(c);
        //Update value in Firebase
        FirebaseDatabase.getInstance().getReference("users/" + c.getClubAccount()
                                                    + "/" + "/reviews").setValue(reviews);
        System.out.println("IN addReview");
        return true;
    }


    public void delete()
    {
        super.delete();
    }

}