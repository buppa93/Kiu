package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.model.Feedback;

import java.util.ArrayList;

/**
 * Created by giuseppe on 04/10/16
 */
public class FeedbacksAdapter extends ArrayAdapter<Feedback>
{
    TextView commentView;
    RatingBar ratingView;

    public FeedbacksAdapter(Context context, ArrayList<Feedback> feedbacks)
    {super(context, 0, feedbacks);}

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_feedback_layout, null);
        }

        Feedback feedback = getItem(position);

        commentView = (TextView) convertView.findViewById(R.id.comment);
        ratingView = (RatingBar) convertView.findViewById(R.id.ratingBarView);

        float rateFloat = 0; //rating
        if(!(feedback.getRating().equals("null")))
            rateFloat = Float.parseFloat(feedback.getRating());
        commentView.setText(feedback.getComment()); //comment
        ratingView.setRating(rateFloat);

        return convertView;
    }

    /*@Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        commentView = (TextView) view.findViewById(R.id.comment);
        ratingView = (RatingBar) view.findViewById(R.id.ratingBarView);

        float rateFloat = Float.parseFloat(cursor.getString(2)); //rating
        commentView.setText(cursor.getString(1)); //comment
        ratingView.setRating(rateFloat);
    }*/
}
