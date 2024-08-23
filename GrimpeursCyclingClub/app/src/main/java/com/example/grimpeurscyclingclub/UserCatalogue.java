package com.example.grimpeurscyclingclub;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserCatalogue extends AppCompatActivity {

    //TODO: Make this a singleton?

    ListView listViewUsers;

    List<Account> accounts;

    //DatabaseReference instance variable
    DatabaseReference databaseAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_user_catalogue);

        listViewUsers = (ListView) findViewById(R.id.ListViewUsers);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("users");

        accounts = new ArrayList<>();

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Account user = accounts.get(i);
                showUpdateDeleteDialog(user.getUsername(), user.getEmail());
                return true;
            }
        });
    }

    /**
     *  This onStart method is called when the Activity is on-screen. This means the method
     *  is called after onCreate is called. This method creates a listener connected to the
     *  /users subsection of the Firebase database and recreates the list of users in the
     *  database whenever the /users subsection of the database changes.
     *
     *
     */
    @Override
    protected void onStart() {
        super.onStart();

        databaseAccounts.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accounts.clear();

                //Iterate through all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting product
                    Account account = postSnapshot.getValue(Account.class);
                    //Grab account's username from key
                    account.setUsername(postSnapshot.getKey());
                    if (!account.getUsername().equals("admin")) { //This condition excludes admin account from user list
                        //Adding account to list
                        accounts.add(account);
                    }
                }

                //Creating adapter
                AccountList productsAdapter = new AccountList(UserCatalogue.this, accounts);
                //Attaching adapter to listview
                listViewUsers.setAdapter(productsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });
    }


    private void showUpdateDeleteDialog(final String userId, String userEmail) {

        //TODO: TEST IF THIS CONDITION CAN BE DELETED?
        /*
        * For deliverable 2, the Events that the Administrator makes are hard coded to this CyclingClubAccount.
        * To prevent errors, this account cannot be deleted in deliverable 2. Once CyclingClub functionality has
        * been implemented, this will be removed.
        * */
        if (userId.equals("eventplaceholder")) {
            Toast.makeText(getApplicationContext(), "For Deliverable 2, " + userId + " cannot be deleted.", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_user_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        //Fill TextViews with username and email of account to delete
        final TextView displayUser = (TextView) dialogView.findViewById(R.id.displayUserName);
        displayUser.setText(userId);
        final TextView displayEmail = (TextView) dialogView.findViewById(R.id.displayUserEmail);
        displayEmail.setText(userEmail);

        dialogBuilder.setTitle(userId);


        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(userId);
                b.dismiss();
            }
        });
    }

    /**
     *
     *
     * @param id username of user to be deleted
     * @return returns boolean value true if the user is successfully deleted
     */
    private boolean deleteUser(String id) {
        //Getting specified user reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);
        //Deleting user
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "User " + id + " Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

}