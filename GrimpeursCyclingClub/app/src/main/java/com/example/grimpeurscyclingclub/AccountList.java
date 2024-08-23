package com.example.grimpeurscyclingclub;

/**
 * Copied from SEG2105 lab 5 (Firebase lab) by Quinn Slavish on 2023-11-06.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AccountList extends ArrayAdapter<Account> {
private Activity context;
        List<Account> accounts;

public AccountList(Activity context, List<Account> accounts) {
        super(context, R.layout.layout_user_list, accounts);
        this.context = context;
        this.accounts = accounts;
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);

        Account account = accounts.get(position);
        textViewName.setText(account.getUsername());
        textViewEmail.setText(account.getEmail());
        return listViewItem;
 }
}

