package com.example.grimpeurscyclingclub;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;


public class AdminActivity extends AppCompatActivity {
    AdminAccount account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //System.out.println("IN ADMIN");
        Intent intent = getIntent();
        account = (AdminAccount) intent.getSerializableExtra("account");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_ui);

    }


    /**
     * This method calls the UserCatalogue activity.
     *
     * */
    public void buttonUserCatalogueClick (View view) {
        Intent intent = new Intent(getApplicationContext(), UserCatalogue.class);

        startActivity(intent);
    }

    /**
     * This method calls the EventCatalogue activity.
     *
     * */
    public void buttonEventCatalogueClick (View view) {
        Intent intent = new Intent(getApplicationContext(), AdminEventCatalogue.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }



}
