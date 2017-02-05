package com.domain.my.giuseppe.kiu.kiuwer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.helper.FragmentHomeHelper;
import com.domain.my.giuseppe.kiu.helper.FragmentYourKiuwer;
import com.domain.my.giuseppe.kiu.helper.SettingsActivity;
import com.domain.my.giuseppe.kiu.localdatabase.DatabaseListKiuer;
import com.domain.my.giuseppe.kiu.model.Feedback;
import com.domain.my.giuseppe.kiu.model.User;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;
import com.domain.my.giuseppe.kiu.service.LatLngService;
import com.domain.my.giuseppe.kiu.service.MyFirebaseInstanceIDService;
import com.domain.my.giuseppe.kiu.signin.StartActivity;
import com.domain.my.giuseppe.kiu.signin.UserLoggingDetails;
import com.domain.my.giuseppe.kiu.utils.FileIOManager;
import com.domain.my.giuseppe.kiu.utils.PasswordDialogFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Kiuwer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    public static final String TAG = "KiuwerActivity";
    private static final int CLOSE_CODE = 1;
    public static User currentUserIstance;

    Bitmap bitmap;
    de.hdodenhof.circleimageview.CircleImageView firstProfile;
    Menu nav_menu;

    public TextView request;
    DatabaseListKiuer databaseListKiuer;
    DrawerLayout drawer;
    boolean cameFromNotification;

    View headerLayout;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*checkUserSignedUp();*/

        RemoteDBAdapter remoteDBAdapter = new RemoteDBAdapter();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.visibility_off).setVisible(false);
        navigationView.setNavigationItemSelectedListener(this);
        request=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_your_kiuwer));
        initializeCountDrawer();
        /*if(user != null){
            final Intent LatLngServiceIntent = new Intent(getApplicationContext(), LatLngService.class);
            final Intent regTokenIntent = new Intent(getApplicationContext(),
                    MyFirebaseInstanceIDService.class);
            startService(LatLngServiceIntent);
            startService(regTokenIntent);
        }
        else{
            Intent start = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(start);
        }*/

        if(user != null)
        {
            Log.d(TAG, "Utente riconosciuto in kiuwer");
            SharedPreferences sharedPref = getSharedPreferences("com.example.xyz", MODE_PRIVATE);
            Boolean avail = sharedPref.getBoolean("NameOfThingToSave", true);
            //dopo lo start service in startactivity, ricevo il nuovo eventuale token dalle preferences e lo aggiorno sul database
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String newtoken=preferences.getString("registration_id", null);
            sendRegistrationToServer(newtoken);
            if (avail) {
                nav_menu.findItem(R.id.visibility_off).setVisible(false);
                nav_menu.findItem(R.id.visibility_on).setVisible(true);
                final Intent LatLngServiceIntent = new Intent(getApplicationContext(), LatLngService.class);
                startService(LatLngServiceIntent);
                remoteDBAdapter.SetAvailabilityAttribute(true);
            } else {
                nav_menu.findItem(R.id.visibility_on).setVisible(false);
                nav_menu.findItem(R.id.visibility_off).setVisible(true);
            }

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                @Override
                public void onDrawerOpened(View drawerView) {
                    initializeCountDrawer();
                }
            };
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            View header = navigationView.getHeaderView(0);
            RatingBar rb= (RatingBar) header.findViewById(R.id.ratingBar);
            rb.setRating(3);

            Fragment fragment;
            FragmentManager fm= getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            fragment= new FragmentHome();
            ft.replace(R.id.content_search_frame_layout, fragment);
            ft.commit();

            new GetProfileImg().execute();
            Log.d(TAG, "Debug helpers research");
            remoteDBAdapter.searchHelpers(new LatLng(0,0), 0);

            Log.d(TAG,"RICEVO INTENT");
            if (getIntent().getExtras() != null) {
                Log.d(TAG,"ESISTE UN INTENT");
                Bundle b = getIntent().getExtras();
                cameFromNotification = b.getBoolean("fromNotification");
                if (cameFromNotification){
                    drawer.openDrawer(Gravity.LEFT);
                    cameFromNotification=false;
                }
            }
        }
        else
        {
            Intent start = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(start);
        }
    }

    @Override
    public void onStart()
    {super.onStart();}

    @Override
    public void onResume()
    {
        Log.d(TAG, "Sono in On Resume");
        super.onResume();
        /*if (getIntent().getExtras() != null) {
            Log.d(TAG, "ESISTE UN INTENT");
            Bundle b = getIntent().getExtras();
            boolean cameFromNotification = b.getBoolean("fromNotification");*/
        if (cameFromNotification) {
            drawer.openDrawer(Gravity.LEFT);
        }
        //}
    }

    private class GetProfileImg extends AsyncTask<Void, Void, Void> implements OnSuccessListener,
            OnFailureListener, ValueEventListener
    {
        NavigationView navigationView;
        View headerLayout;
        TextView kiuwerName;
        TextView kiuwerMail;
        de.hdodenhof.circleimageview.CircleImageView firstProfile;
        byte[] imgBuffer;
        Bitmap bitmap;
        FirebaseAuth auth;
        FirebaseUser user;
        RatingBar rb;
        int average;

        @Override
        protected Void doInBackground(Void... voids)
        {
            Log.d(TAG, "Lancio GetProfileImg (doInBackground)");
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            Log.d(TAG,"l'usermail: "+user.getEmail());
            String email = user.getEmail().toString();
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
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            headerLayout = navigationView.getHeaderView(0);
            kiuwerName = (TextView) headerLayout.findViewById(R.id.kiuwername);
            kiuwerMail = (TextView) headerLayout.findViewById(R.id.kiuwermail);

            kiuwerName.setText(User.getUserName(user.getEmail()));
            kiuwerMail.setText(user.getEmail());

        }

        @Override
        public void onFailure(@NonNull Exception e)
        {
            Log.d(TAG, "GetProfileImg (onFailure)");
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.user1);
            firstProfile = (de.hdodenhof.circleimageview.CircleImageView) headerLayout
                    .findViewById(R.id.firstprofile);
            firstProfile.setImageBitmap(bitmap);
            routineFeedbacksAverage();
        }

        @Override
        public void onSuccess(Object o)
        {
            Log.d(TAG, "GetProfileImg (onSuccess)");
            this.imgBuffer = (byte[]) o;
            bitmap = BitmapFactory.decodeByteArray(this.imgBuffer, 0, this.imgBuffer.length);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            headerLayout = navigationView.getHeaderView(0);
           /* kiuwerName = (TextView) headerLayout.findViewById(R.id.kiuwername);
            kiuwerMail = (TextView) headerLayout.findViewById(R.id.kiuwermail);

            kiuwerName.setText(User.getUserName(user.getEmail()));
            kiuwerMail.setText(user.getEmail());
*/
            firstProfile = (de.hdodenhof.circleimageview.CircleImageView) headerLayout
                    .findViewById(R.id.firstprofile);
            firstProfile.setImageBitmap(bitmap);
            routineFeedbacksAverage();


        }

        public void routineFeedbacksAverage()
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference()
                    .child(RemoteDatabaseString.KEY_USER_NODE)
                    .child(User.getUserName(user.getEmail()))
                    .child(RemoteDatabaseString.KEY_FEEDBACK_NODE);
            databaseReference.addListenerForSingleValueEvent(this);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            Log.d(TAG, "GetProfileImg (onDataChange)");
            Log.d(TAG, "GetProfileImg (onDataChange) " + dataSnapshot.getValue().toString());
            ArrayList<Object> data = (ArrayList<Object>) dataSnapshot.getValue();
            Feedback f = new Feedback();
            rb= (RatingBar) headerLayout.findViewById(R.id.ratingBar);

            if(!(data == null))
            {
                Log.d(TAG, "GetProfileImg (onDateChange)- Feedbacks present");
                average = 0;
                int nFeedback = 0;

                for(Object o : data)
                {
                    if(o != null)
                    {
                        Log.d(TAG, "Sto cazzo di o -> " + o.toString());
                        HashMap<String, String> entry = (HashMap<String, String>) o;
                        average += Integer.parseInt(entry.get(RemoteDatabaseString.KEY_FEEDBACK_RATING)
                                .toString());
                        nFeedback++;
                    }
                }

                average /= nFeedback;
                rb.setRating(average);
            }
            else
            {
                Log.d(TAG, "GetProfileImg (onDateChange)- Feddbacks not present");
                rb.setRating(0);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {Log.d(TAG, "GetProfileImg (onCancelled)");}
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home_helper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the fragment_home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Intent intent = new Intent(this,SettingsActivity.class);
            //startActivity(intent);
            PasswordDialogFragment update= new PasswordDialogFragment();
            update.show(getSupportFragmentManager(),"update password");
        }
        else if(id == R.id.action_request) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            setTitle("Your Kiuwer");
            FragmentYourKiuwer fragment_kiuwer = new FragmentYourKiuwer();
            ft.replace(R.id.content_search_frame_layout,fragment_kiuwer);
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        // boolean isChecked = false;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        final RemoteDBAdapter re= new RemoteDBAdapter();
        if(id==R.id.visibility_on)
        {
            nav_menu.findItem(R.id.visibility_on).setVisible(false);
            nav_menu.findItem(R.id.visibility_off).setVisible(true);
            final Intent LatLngServiceIntent = new Intent(getApplicationContext(), LatLngService.class);
            SharedPreferences.Editor editor = getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
            editor.putBoolean("NameOfThingToSave", false);
            editor.commit();
            re.SetAvailabilityAttribute(false);
            stopService(LatLngServiceIntent);

        }
        if(id==R.id.visibility_off)
        {
            nav_menu.findItem(R.id.visibility_off).setVisible(false);
            nav_menu.findItem(R.id.visibility_on).setVisible(true);
            final Intent LatLngServiceIntent = new Intent(getApplicationContext(), LatLngService.class);
            SharedPreferences.Editor editor = getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
            editor.putBoolean("NameOfThingToSave", true);
            editor.commit();
            re.SetAvailabilityAttribute(true);
            startService(LatLngServiceIntent);

        }
        if (id == R.id.search_helper)//R.id.home
        {
            setTitle(R.string.userkiuer);
            showallitameenabled();
            item.setEnabled(false);
            fragment = new FragmentHome();
            ft.replace(R.id.content_search_frame_layout, fragment);
            ft.commit();
        }
        if (id == R.id.helpers) {
            setTitle(R.string.your_helpers);
            showallitameenabled();
            item.setEnabled(false);
            fragment = new FragmentYourHelpers();
            ft.replace(R.id.content_search_frame_layout, fragment); //R.id.content_main
            ft.commit();
        }
        if (id == R.id.current_queue) //addrate
        {
            setTitle(R.string.current_queue2);
            showallitameenabled();
            item.setEnabled(false);
            FragmentHomeHelper fragment_home = new FragmentHomeHelper();
            ft.replace(R.id.content_search_frame_layout, fragment_home);
            ft.commit();
        }
        if (id == R.id.nav_your_kiuwer) //feedbackview
        {
            setTitle(R.string.action_kiuwer_requests);
            showallitameenabled();
            item.setEnabled(false);
            FragmentYourKiuwer fragment_kiuwer = new FragmentYourKiuwer();
            ft.replace(R.id.content_search_frame_layout, fragment_kiuwer);
            ft.commit();
        }
        if (id == R.id.addrate) {
            setTitle(R.string.addrate);
            showallitameenabled();
            item.setEnabled(false);
            fragment = new FragmentAddRate();
            ft.replace(R.id.content_search_frame_layout, fragment);
            ft.commit();
        }
        if (id == R.id.feedbackview) {
            setTitle(R.string.showfeedback);
            showallitameenabled();
            item.setEnabled(false);
            fragment = new FragmentsShowFeedback();
            ft.replace(R.id.content_search_frame_layout, fragment);
            ft.commit();
        }

        if (id == R.id.log_out)
        {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.logout)
                    .setMessage(R.string.dialogmessage).setIcon(R.drawable.ic_logout)
                    .setPositiveButton(R.string.confirmbutton, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {/* continue with delete */
                            re.SetAvailabilityAttribute(false); //disattivo la visibilità prima del logout
                            FirebaseAuth.getInstance().signOut(); //faccio il logout


                            final Intent LatLngServiceIntent = new Intent(getApplicationContext(), LatLngService.class);
                            stopService(LatLngServiceIntent);


                            Intent intent= new Intent(getApplication(), StartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancelbutton, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {/* do nothing */}
                    })
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showallitameenabled()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.search_helper).setEnabled(true);
        navigationView.getMenu().findItem(R.id.helpers).setEnabled(true);
        navigationView.getMenu().findItem(R.id.current_queue).setEnabled(true);
        navigationView.getMenu().findItem(R.id.nav_your_kiuwer).setEnabled(true);
        navigationView.getMenu().findItem(R.id.addrate).setEnabled(true);
        navigationView.getMenu().findItem(R.id.feedbackview).setEnabled(true);
        navigationView.getMenu().findItem(R.id.log_out).setEnabled(true);

    }

    private void checkUserSignedUp()
    {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user == null)
        {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Log.d(TAG, "Current User -> " + user.getEmail());
        }
    }

    private void initializeCountDrawer() {
        //Gravity property aligns the text
        request.setGravity(Gravity.CENTER_VERTICAL);
        request.setTypeface(null, Typeface.BOLD);
        request.setTextColor(getResources().getColor(R.color.colorAccent));
        databaseListKiuer = new DatabaseListKiuer(getApplicationContext());
        databaseListKiuer.read();
        Cursor c = databaseListKiuer.countRequestSeenFalse();
        if(c.moveToFirst()) {
            request.setText(c.getString(0));
        }
        if(c.getString(0).equals("0"))
            request.setVisibility(View.INVISIBLE);
        else request.setVisibility(View.VISIBLE);
    }


    private class LoadImage extends AsyncTask<String, String, Bitmap>
    {
        @Override
        protected void onPreExecute() {super.onPreExecute();}

        protected Bitmap doInBackground(String... args)
        {
            try {bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());}
            catch (Exception e) {e.printStackTrace();}
            return bitmap;
        }

        protected void onPostExecute(Bitmap image)
        {
            if(image != null) {firstProfile.setImageBitmap(image);}
            else
            {
                Toast.makeText(Kiuwer.this, "Image Does Not exist or Network Error",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    /**
     * manda il nuovo token al database
     * @param newToken nuovo tken da inviare al database
     */
    private void sendRegistrationToServer(String newToken)
    {
        Log.d(TAG,"Sto per cambiare token");
        RemoteDBAdapter remoteDBAdapter = new RemoteDBAdapter();
        remoteDBAdapter.updateRegistrationToken(Kiuwer.currentUserIstance, newToken);
    }


}
