package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by ivasco92 on 04/09/16.
 */
public class YourHelpersDbAdapter extends CursorAdapter
{
    TextView namefield;
    TextView via;
    TextView data;
    TextView ora;
    TextView tariffa;
    RatingBar ratingBar;
    ImageButton messagebutton;
    TextView euroora;
    TextView infilapressotext;
    TextView orariotext;
    TextView datatext;
    String name;
    String address;
    String date;
    String time;
    String money;
    Button pay;
    Intent emailIntent;

    public YourHelpersDbAdapter(Context context, Cursor c) {
        super(context, c);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_helper, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        namefield = (TextView) view.findViewById(R.id.nametext);
        via = (TextView) view.findViewById(R.id.viatext);
        data = (TextView) view.findViewById(R.id.datatext);
        ora = (TextView) view.findViewById(R.id.oratext);
        tariffa = (TextView) view.findViewById(R.id.tariffatext);
        ratingBar=(RatingBar) view.findViewById(R.id.ratingBar2);
        messagebutton=(ImageButton) view.findViewById(R.id.messagebutton);
        euroora=(TextView) view.findViewById(R.id.textView8);
        infilapressotext= (TextView)view.findViewById(R.id.textView);
        orariotext= (TextView)view.findViewById(R.id.textView3);
        datatext= (TextView)view.findViewById(R.id.textView2);
        pay= (Button)view.findViewById(R.id.paybutton);

        //TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        //TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        // Extract properties from cursor
        name= cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))); //"username"
        address= cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));//"address"
        date=cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));//"date"
        time=cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));//"time"
        money=cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5)));//"money"

        // Populate fields with extracted properties
        namefield.setText(name);
        via.setText(address);
        data.setText(date);
        ora.setText(time);
        tariffa.setText(money);
        ratingBar.setIsIndicator(true);
        messagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getMailbyname(name, v);}
        });
        //SE LA FILA E' CONCLUSA, RENDI IL BOTTONE CLICCABILE E CON IL TESTO VERDE,ALTRIMENTI E' NON CLICCABILE E CON IL TESTO ROSSO
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent gotopay = new Intent(v.getContext(), PayPalTestActivity.class);
                context.startActivity(gotopay);
            }
        });
        // tvBody.setText(body);
        //tvPriority.setText(String.valueOf(priority));
    }

    private void getMailbyname (String name, final View v){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).child(name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<?,?> data= (HashMap<?, ?>) dataSnapshot.getValue();
                String mail= data.get(RemoteDatabaseString.KEY_EMAIL).toString();

                emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mail});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.oggetto);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.message);
                v.getContext().startActivity(Intent.createChooser(emailIntent, v.getResources().getString(R.string.chooser)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

