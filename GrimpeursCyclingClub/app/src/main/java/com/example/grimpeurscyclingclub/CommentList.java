package com.example.grimpeurscyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
public class CommentList extends ArrayAdapter<Comment> {
    private Activity context;
    List<Comment> comments;

    public CommentList(Activity context, List<Comment> comments) {
        //This is different from the super constructor called in EventList; if there is an error in this
        //constructor, it likely comes from this difference.
        super(context, R.layout.comment_list, comments); //name layout comment_list
        this.context = context;
        this.comments = comments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.comment_list, null, true);

        TextView textViewParticipantName = (TextView) listViewItem.findViewById(R.id.textViewParticipantName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewRating);
        TextView textViewComment = (TextView) listViewItem.findViewById(R.id.textViewComment);

        Comment comment = comments.get(position);

        //Populate entry in list
        textViewParticipantName.setText(comment.getReviewer());
        textViewDate.setText(comment.getDate());

        //Build star String; //★☆ stars for the rating
        StringBuilder sB = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < comment.getRating()) { //Full-star case
                sB.append("★");
            } else { //Empty-star case
                sB.append("☆");
            }
        }
        textViewRating.setText(sB.toString());

        textViewComment.setText(comment.getComment());

        return listViewItem;
    }
}
