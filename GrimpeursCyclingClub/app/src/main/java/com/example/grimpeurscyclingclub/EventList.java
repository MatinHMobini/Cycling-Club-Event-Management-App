package com.example.grimpeurscyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
public class EventList extends ArrayAdapter<Event> {
    private Activity context;
    List<Event> Events;

    public EventList(Activity context, List<Event> Events) {
        super(context, R.layout.event_list, Events); //name layout event_list
        this.context = context;
        this.Events = Events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.event_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewEventName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView textViewLocation = (TextView) listViewItem.findViewById(R.id.textViewLocation);
        TextView textViewRegistrationFee = (TextView) listViewItem.findViewById(R.id.textViewRegistrationFee);
        TextView textViewParticipantLimit = (TextView) listViewItem.findViewById(R.id.textViewParticipantLimit);
        TextView textViewEventType = (TextView) listViewItem.findViewById(R.id.textViewEventType);

        Event event = Events.get(position);
        textViewName.setText("Name: " + event.getEventName());
        textViewDate.setText("Date: " + String.valueOf(event.getDate()));
        textViewLocation.setText("Location: " + event.getLocation());
        textViewRegistrationFee.setText("Fee: " + String.valueOf(event.getRegistrationFee()));
        textViewParticipantLimit.setText("Limit: " + String.valueOf(event.getParticipantLimit()));
        textViewEventType.setText("Event Type: " + String.valueOf(event.getType()));



        return listViewItem;
    }
}
