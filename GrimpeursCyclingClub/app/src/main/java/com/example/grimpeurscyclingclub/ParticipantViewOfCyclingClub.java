package com.example.grimpeurscyclingclub;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ParticipantViewOfCyclingClub extends AppCompatActivity {


    //-------
    // INSTANCE VARIABLES
    //-------



    private ParticipantAccount participantAccount;

    private CyclingClubAccount clubAccount;

    private DatabaseReference clubReference;


    ListView listViewComments;
    Button buttonCreateComment;
    List<Comment> reviews;


    TextView clubUsername;
    TextView clubEmail;
    TextView clubSocial;
    TextView clubPhoneNumber;


    //-------
    // METHODS
    //-------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.participant_cycling_club_profile_view);

        //Get relevant clubs from Intent
        participantAccount = (ParticipantAccount) intent.getSerializableExtra("participantAccount");
        clubAccount = (CyclingClubAccount) intent.getSerializableExtra("eventOrganizer");

        clubReference = FirebaseDatabase.getInstance().getReference().child("users").child(clubAccount.getUsername());

        //Instantiate TextViews
        clubUsername = (TextView) findViewById(R.id.usernameShow);
        clubEmail = (TextView) findViewById(R.id.emailshow);
        clubSocial = (TextView) findViewById(R.id.socialmediaHandleShow);
        clubPhoneNumber = (TextView) findViewById(R.id.phonenumberShow);
        listViewComments = (ListView) findViewById(R.id.commentSection);
        buttonCreateComment = (Button) findViewById(R.id.commentButton);





    }

    @Override
    protected void onStart() {
        super.onStart();

        //Associate this instance variable with the clubAccount's list
        reviews = clubAccount.getReviews();

        clubReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Rebuild comment list when Club is modified in Firebase
                reviews.clear();
                //Iterate through all nodes
                for (DataSnapshot postSnapshot : snapshot.child("reviews").getChildren()) {
                    //Getting Comment
                    Comment review = postSnapshot.getValue(Comment.class);
                    reviews.add(0,review);
                }

                CommentList adapter = new CommentList(ParticipantViewOfCyclingClub.this, reviews);
                listViewComments.setAdapter(adapter);

                clubUsername.setText(clubAccount.getUsername());
                //As none of these values are instantiated in CyclingClubAccount, they must be grabbed from Firebase
                clubEmail.setText(snapshot.child("email").getValue(String.class));
                clubSocial.setText(snapshot.child("social").getValue(String.class));
                clubPhoneNumber.setText(snapshot.child("phone").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonCreateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateCommentDialog();
            }
        });


    }

    //TODO: Implement Create Comment Dialog
    private void showCreateCommentDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.comment_dialog, null);
        dialogBuilder.setView(dialogView);

        Button postComment = (Button) dialogView.findViewById(R.id.postCommentButton);
        EditText typableComment = (EditText) dialogView.findViewById(R.id.typableComment);

        //get the spinner from the xml.
        Spinner ratingDropdown = (Spinner) dialogView.findViewById(R.id.rateStars);
        //Build spinner list
        List<String> ratingList = buildRatingSpinnerList();

        //create an adapter to describe how the items are displayed; adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ratingList);
        //set the spinners adapter to the previously created one.
        ratingDropdown.setAdapter(adapter);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        //
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If the comment has text
                if (typableComment.getText() != null && !typableComment.getText().toString().equals("")) {

                    String date = Date.getTodaysDate();

                    //public Comment(String reviewer, String clubAccount, String comment, String date, int rating)
                    Comment newComment = new Comment(participantAccount.getUsername(),
                                                        clubAccount.getUsername(),
                                                        typableComment.getText().toString(),
                                                        date,
                                                        Integer.parseInt(ratingDropdown.getSelectedItem().toString()));

                    //This method adds the Comment in clubAccount object and in Firebase
                    if (clubAccount.addReview(newComment)) { //If Comment is successfully added
                        //Inform user of success through Toast
                        Toast.makeText(getApplicationContext(), "Comment added", Toast.LENGTH_LONG).show();
                        //Dismiss the dialog
                        b.dismiss();
                    }
                } else { //Empty comment case
                        typableComment.setText("");
                        typableComment.setHint("Empty comment text");
                        typableComment.setHintTextColor(Color.argb(255,255,0,0));
                }
            }
        });
    }

    public List<String> buildRatingSpinnerList() {
        List<String> ratingList = new ArrayList<>();

        for (int i = 5; i > 0; i--) {
            ratingList.add(Integer.toString(i));
        }

        return ratingList;
    }
}
