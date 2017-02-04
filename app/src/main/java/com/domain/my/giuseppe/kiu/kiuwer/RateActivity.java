//package universit.ivasco92.kiu;
package com.domain.my.giuseppe.kiu.kiuwer;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.MainActivity;
import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.model.Feedback;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;
import com.domain.my.giuseppe.kiu.utils.SingletonObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class RateActivity extends AppCompatActivity
{
    private static final String NAME="name";
    private static final String SURNAME="surname";

    private static TextView name;
    private static TextView name_feedback;
    private static EditText usertext;
    private static Button button;
    private RatingBar ratingBar;
    private ImageView thumbnail;

    public String email;
    public String username;

    Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.ratehelper);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        name = (TextView)findViewById(R.id.text_name);
        //name_feedback = (TextView) findViewById(R.id.textView_your_feedback);

        usertext= (EditText) findViewById(R.id.editText);// v 1.4 ivan
        button= (Button) findViewById(R.id.button3);  //v 1.4 ivan

        //setto il nome dell'utente a cui lasciareil feedback
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString(NAME);
        name.setText(username);

        //setto il thumbnail
        thumbnail = (ImageView) findViewById(R.id.imageView2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE).child(/*params[0]*/ "buppa93")
                .child(RemoteDatabaseString.KEY_EMAIL); //TODO change to username

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                SingletonObject.getInstance().setObj(dataSnapshot.getValue());
                Log.d("RateActivity", "EMAIL -> " + (String) SingletonObject.getInstance().getObj());

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                String remotePath = (String) SingletonObject.getInstance().getObj();
                remotePath += ".jpg";
                StorageReference thumbnailRef = storageRef.child(remotePath);

                Log.d("RateActivity", "EMAIL FUORI -> " + remotePath);

                final long ONE_MEGABYTE = 1024 * 1024;

                thumbnailRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>()
                {
                    @Override
                    public void onSuccess(byte[] bytes)
                    {
                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        thumbnail.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.d("AddRateDbAdapter", "Errore download thumbnail");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        // finito si settare il thumbnail

        // istanzio la rating bar
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // publish feedback
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference().child(RemoteDatabaseString
                        .KEY_USER_NODE).child("buppa93").child(RemoteDatabaseString.KEY_FEEDBACK_NODE); //TODO change to username

                Query query = databaseReference.limitToLast(1);

                query.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Log.d("RateActivity", "datasnapshot -> " + dataSnapshot.getValue().toString());
                        HashMap<String, Object> items = (HashMap<String, Object>) dataSnapshot.getValue();
                        Set<String> keySet = items.keySet();
                        String feedbackno = "";
                        for(String key : keySet) {feedbackno = key;}
                        String rate = String.valueOf((int) ratingBar.getRating());
                        RemoteDBAdapter dbAdapter = new RemoteDBAdapter();
                        int no = Integer.parseInt(feedbackno);
                        no += 1;
                        feedbackno = String.valueOf(no);
                        dbAdapter.leaveFeedback("buppa93", new Feedback(feedbackno,
                                rate, usertext.getText().toString())); //TODO change to username
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

