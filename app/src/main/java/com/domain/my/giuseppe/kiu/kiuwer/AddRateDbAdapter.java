package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by ivasco92 on 02/09/16.
 */
public class AddRateDbAdapter extends CursorAdapter implements ValueEventListener
{
    private static final String TAG = "AddRateDbAdapter";

    TextView namefield;
    TextView filapresso;
    TextView addressfield;
    String name;
    String address;
    String email;
    RemoteDBAdapter remoteDBAdapter;
    de.hdodenhof.circleimageview.CircleImageView imageView;
    Bitmap bmp;
    public AddRateDbAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_addrate, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        remoteDBAdapter = new RemoteDBAdapter();
        // Find fields to populate in inflated template
        namefield=(TextView) view.findViewById(R.id.namefieldaddrate);
        filapresso=(TextView) view.findViewById(R.id.textView10);
        addressfield=(TextView) view.findViewById(R.id.addressfieldaddrate);
        imageView=(de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.firstprofile);

        //TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        //TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        // Extract properties from cursor

        name= cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))); //"username"
        address= cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));//"address"

        //Recupero l'email dal database remoto
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference()
                .child(RemoteDatabaseString.KEY_USER_NODE)
                .child(name)
                .child(RemoteDatabaseString.KEY_EMAIL);
        reference.addListenerForSingleValueEvent(this);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference thumbnailRef = storageRef.child(email + ".png");

        final long ONE_MEGABYTE = 1024 * 1024;

        thumbnailRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>()
        {
            @Override
            public void onSuccess(byte[] bytes)
            {
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {Log.d("AddRateDbAdapter", "Errore download thumbnail");}
        });


        // Populate fields with extracted properties
        namefield.setText(name);
        addressfield.setText(address);
       // tvBody.setText(body);
        //tvPriority.setText(String.valueOf(priority));
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        email = dataSnapshot.getValue(String.class);
        Log.d(TAG, "email -> " + email);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}
}
