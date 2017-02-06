//package universit.ivasco92.kiu;
package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.model.Feedback;
import com.domain.my.giuseppe.kiu.model.User;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentsShowFeedback extends Fragment implements ValueEventListener
{
    private final static String TAG = "FragmentShowFeedback";
    View rootView;
    ListView myFeedbackList;
    TextView top;
    TextView good;
    TextView decent;
    TextView disappointing;
    TextView bad;
    TextView textFeedback;
    TextView numRate;
    boolean arePresent; // flag rappresentate la presenza o meno di feedback

    ArrayList<Feedback> feedbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_fragment_show_feedback, container, false);

        myFeedbackList = (ListView) rootView.findViewById(R.id.my_feedback_list);
        top = (TextView) rootView.findViewById(R.id.text_top);
        good = (TextView) rootView.findViewById(R.id.text_good);
        decent = (TextView) rootView.findViewById(R.id.text_decent);
        disappointing = (TextView) rootView.findViewById(R.id.text_disappointing);
        bad = (TextView) rootView.findViewById(R.id.text_bad);
        textFeedback = (TextView) rootView.findViewById(R.id.text_feedback);
        numRate = (TextView) rootView.findViewById(R.id.num_rate);

        feedbacks = new ArrayList<>();

        /* istanzio text_feedback */
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userName = User.getUserName(user.getEmail());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).child(userName)
                .child(RemoteDatabaseString.KEY_FEEDBACK_NODE);
        databaseReference.addListenerForSingleValueEvent(this);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        FeedbacksAdapter adapter = new FeedbacksAdapter(getContext(),feedbacks);
        myFeedbackList.setAdapter(adapter);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        ArrayList<Object> data = (ArrayList<Object>) dataSnapshot.getValue();
        Feedback f;

        if(data != null)
        {
            arePresent = true;
            int i = 1;
            for(Object o : data)
            {
                if(o != null)
                {
                    f = new Feedback();
                    Log.d(TAG, "Sto cazzo di o -> " + o.toString());
                    HashMap<String, String> entry = (HashMap<String, String>) o;
                    f.setComment(entry.get(RemoteDatabaseString.KEY_FEEDBACK_COMMENT));
                    f.setRating(entry.get(RemoteDatabaseString.KEY_FEEDBACK_RATING));
                    f.setFeedbackno(String.valueOf(i));
                    feedbacks.add(f);
                    i++;
                }
            }

            if (!arePresent) {makeAlertDialog();}
            else
            {
                textFeedback.setText(String.valueOf(feedbacks.size()));
                numRate.setText(String.valueOf(getAvarage(feedbacks)));

                int[] starts = countNumberOfStart(feedbacks);
                top.setText(String.valueOf(starts[4]));
                good.setText(String.valueOf(starts[3]));
                decent.setText(String.valueOf(starts[2]));
                disappointing.setText(String.valueOf(starts[1]));
                bad.setText(String.valueOf(starts[0]));

                FeedbacksAdapter adapter = new FeedbacksAdapter(getContext(),feedbacks);
                myFeedbackList.setAdapter(adapter);
                printFeedbacks(feedbacks);
            }
        }
        else{arePresent = false;}
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}

    public void printFeedbacks(ArrayList<Feedback> feedbacks)
    {
        for (Feedback f: feedbacks)
        {
            Log.d(TAG, "Comment: " + f.getComment() +", Rating: "
                    + f.getRating());
        }
    }

    private void makeAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage(getResources().getString(R.string.no_feedbacks))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private int getAvarage(ArrayList<Feedback> feedbacks)
    {
        int EXi = 0;
        int n = feedbacks.size();

        for (Feedback f : feedbacks)
        {
            if (f.getRating().equals("null"))
                EXi += 0;
            else
                EXi += Integer.parseInt(f.getRating());
        }

        if(n == 0) {return 0;}
        else {return EXi/n;}
    }

    private int[] countNumberOfStart(ArrayList<Feedback> feedbacks)
    {
        int stars[] = new int[5];
        stars[0] = 0;
        stars[1] = 0;
        stars[2] = 0;
        stars[3] = 0;
        stars[4] = 0;

        for(Feedback f: feedbacks)
        {
            if(f.getRating().equals("1"))
                stars[0] ++;
            if(f.getRating().equals("2"))
                stars[1] ++;
            if(f.getRating().equals("3"))
                stars[2] ++;
            if(f.getRating().equals("4"))
                stars[3] ++;
            if(f.getRating().equals("5"))
                stars[4] ++;
        }

        return stars;
    }


}
