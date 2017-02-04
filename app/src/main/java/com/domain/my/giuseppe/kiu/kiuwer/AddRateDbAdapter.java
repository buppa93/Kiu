package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.model.User;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by ivasco92 on 02/09/16.
 */
public class AddRateDbAdapter extends CursorAdapter {
    TextView namefield;
    TextView filapresso;
    TextView addressfield;
    String name;
    String address;
    RemoteDBAdapter remoteDBAdapter;
    de.hdodenhof.circleimageview.CircleImageView imageView;
    public static User currentUserIstance;

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
    public void bindView(View view, Context context, Cursor cursor) {
        currentUserIstance = new User();
        remoteDBAdapter = new RemoteDBAdapter();
        remoteDBAdapter.getDb();
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

        //TODO cambiare e mettere foto utente
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference thumbnailRef = storageRef.child("user1.png");

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
            {
                Log.d("AddRateDbAdapter", "Errore download thumbnail");
            }
        });


        // Populate fields with extracted properties
        namefield.setText(name);
        addressfield.setText(address);
       // tvBody.setText(body);
        //tvPriority.setText(String.valueOf(priority));
    }
}
