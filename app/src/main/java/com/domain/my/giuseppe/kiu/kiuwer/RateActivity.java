//package universit.ivasco92.kiu;
package com.domain.my.giuseppe.kiu.kiuwer;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.model.Feedback;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Set;

public class RateActivity extends AppCompatActivity implements ValueEventListener,
        View.OnClickListener
{
    private static final String TAG = "RateActivity";
    private static final String NAME="name";
    private static final String PROFILE_IMG = "profileImg";
    private static final String SURNAME="surname";

    private static TextView name;
    private static TextView name_feedback;
    private static EditText usertext;
    private static Button button;
    private RatingBar ratingBar;
    private ImageView thumbnail;

    public String email;
    public String username;

    byte[] bytes;
    Bitmap bmp;

    FirebaseAuth auth;
    FirebaseUser user;


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
        new GetProfileImg().execute();
        // finito di settare il thumbnail

        // istanzio la rating bar
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // publish feedback
        button.setOnClickListener(this);
    }

    private class GetProfileImg extends AsyncTask<Void, Void, Void> implements OnSuccessListener,
            OnFailureListener, ValueEventListener
    {
        byte[] imgBuffer;
        Bitmap bitmap;
        String email;

        @Override
        protected Void doInBackground(Void... voids)
        {
            Log.d(TAG, "Lancio GetProfileImg (doInBackground)");
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            //recupero il riferimento all'email
            DatabaseReference dbReference = database.getReference()
                    .child(RemoteDatabaseString.KEY_USER_NODE)
                    .child(username)
                    .child(RemoteDatabaseString.KEY_EMAIL);
            dbReference.addListenerForSingleValueEvent(this);

            //recupero il riferimento al file del thumbnail
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference reference = storageReference.child(email + ".jpg");

            final long ONE_MEGABYTE = 1024 * 1024;
            reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(this);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Log.d(TAG, "GetProfileImg (onPostExecute)");

        }

        @Override
        public void onFailure(@NonNull Exception e)
        {
            Log.d(TAG, "GetProfileImg (onFailure)");
        }

        @Override
        public void onSuccess(Object o)
        {
            Log.d(TAG, "GetProfileImg (onSuccess)");
            //setto il thumbnail
            thumbnail = (ImageView) findViewById(R.id.imageView2);
            this.imgBuffer = (byte[]) o;
            bitmap = BitmapFactory.decodeByteArray(this.imgBuffer, 0, this.imgBuffer.length);
            thumbnail.setImageBitmap(bitmap);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {email = dataSnapshot.getValue(String.class);} //recupero l'email

        @Override
        public void onCancelled(DatabaseError databaseError) {}
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

    @Override
    public void onClick(View view)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child(RemoteDatabaseString
                .KEY_USER_NODE).child(username).child(RemoteDatabaseString.KEY_FEEDBACK_NODE);

        Query query = databaseReference.limitToLast(1);

        query.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        Log.d(TAG, "dataSnapshot -> " + dataSnapshot.getValue().toString());

        //Prendo l'ultimo feedback lasciato
        HashMap<String,Object> data = (HashMap<String,Object>) dataSnapshot.getValue();
        Set<String> keySet = data.keySet(); //Recupero il keySet per identificare l'id

        RemoteDBAdapter adapter = new RemoteDBAdapter();

        //Recupero i campi
        String rate = String.valueOf((int) ratingBar.getRating());
        String comment = usertext.getText().toString();
        //se non sono presenti feedback
        if(data == null)
        {
            String no = "1";
            adapter.leaveFeedback(username, new Feedback(no, rate, comment));
        }
        else
        {
            //Estraggo l'id dell'ultimo feedback
            String feedbackNo = "";
            for (String id : keySet) {feedbackNo = id;}

            //Aggiorno l'id e Lascio il feedback
            int no = Integer.parseInt(feedbackNo) + 1;
            adapter.leaveFeedback(username, new Feedback(String.valueOf(no), rate, comment));
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}
}

