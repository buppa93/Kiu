package com.domain.my.giuseppe.kiu.helper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.R;

public class FragmentCurrentQueue extends Fragment {

    private static TextView name_kiuwer;
    private static TextView location;
    private static TextView date;
    private static TextView start;
    private static TextView rate;
    private static RatingBar feedback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_queue, container, false);
    }

    public void setCurrent(){
        name_kiuwer = (TextView) getView().findViewById(R.id.name_kiuwer);
        location = (TextView) getView().findViewById(R.id.textView_locate);
        date = (TextView) getView().findViewById(R.id.textView_data);
        start = (TextView) getView().findViewById(R.id.textView_rate);
        rate = (TextView) getView().findViewById(R.id.textView_time);
        feedback = (RatingBar) getView().findViewById(R.id.ratingBar2);

        //impostare i valori
    }


}
