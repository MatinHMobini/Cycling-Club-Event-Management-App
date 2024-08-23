/**
 * This interface simplifies the methods that use CyclingClubAccounts and ParticipantAccounts.
 *
 * */

package com.example.grimpeurscyclingclub;
public interface PublicUser {


    //Getter/setter methods for implementing classes
    String getUsername();
    boolean setUsername(String username);

    String getSocialMediaLink();
    void setSocialMediaLink(String link);
    String getMainContactName();
    void setMainContactName(String name);
    String getPhoneNumber();
    void setPhoneNumber(String number);
}
