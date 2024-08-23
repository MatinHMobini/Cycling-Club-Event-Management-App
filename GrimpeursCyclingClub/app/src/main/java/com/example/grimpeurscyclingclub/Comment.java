package com.example.grimpeurscyclingclub;

public class Comment {

    //-------
    // INSTANCE VARIABLES
    //-------

    private String reviewer;

    private String clubAccount;

    private String comment;

    private String date;

    private int rating;


    //-------
    // CONSTRUCTORS
    //-------

    public Comment() { //Zero-arity constructor

    }

    public Comment(String reviewer) { //Single-arity constructor
        this.reviewer = reviewer;
    }

    public Comment(String reviewer, String clubAccount, String comment, String date, int rating) {//Five-arity constructor
        this.reviewer = reviewer;
        this.clubAccount = clubAccount;
        this.comment = comment;
        this.date = date;
        this.rating = rating;
    }


    //-------
    // ACCESSORS & MODIFIERS
    //-------
    public String getReviewer() {return this.reviewer;}
    //public void setReviewer(ParticipantAccount reviewer) {this.reviewer = reviewer;}

    public String getClubAccount() {return this.clubAccount;}
    //public void setClubAccount(CyclingClubAccount clubAccount) {this.clubAccount = clubAccount}

    public String getComment() {return this.comment;}
    //public void setReviewer(String comment) {this.comment = comment;}

    public String getDate() {return this.date;}
    //public void setDate(String date) {this.date = date;}

    public int getRating() {return rating;}
    //public void setRating(int rating) {this.rating = rating;}


    //-------
    // METHODS
    //-------

}
