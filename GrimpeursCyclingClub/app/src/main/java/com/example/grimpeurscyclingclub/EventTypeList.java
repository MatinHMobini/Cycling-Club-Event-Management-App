package com.example.grimpeurscyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventTypeList extends ArrayAdapter<String> {
    private Activity context;
    List<String> eventTypes;

    public EventTypeList(Activity context, List<String> eventTypes) {
        super(context, R.layout.layout_event_type_list, eventTypes); //name layout layout_event_type_list
        this.context = context;
        this.eventTypes = eventTypes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_event_type_list, null, true);

        TextView textViewEventType = (TextView) listViewItem.findViewById(R.id.textViewEventType);

        String eventType = eventTypes.get(position);
        textViewEventType.setText(eventType);

        return listViewItem;
    }
}
