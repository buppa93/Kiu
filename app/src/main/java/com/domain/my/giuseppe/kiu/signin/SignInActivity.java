package com.domain.my.giuseppe.kiu.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.kiuwer.MainActivity;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.domain.my.giuseppe.kiu.utils.FileIOManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by giuseppe on 21/09/16.
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignInActivity";
    private static final int REQUEST_ID = 6582;
    private static final int REQUEST_RESOLVE_ERROR = 5;
    private static final int RESULT_LOAD_IMAGE = 4;
    private static final double THUMBNAILS_SIZE = 85/*256*/;

    Uri selectedImage = null;

    private ProgressDialog mProgressDialog;

    EditText emailField;
    EditText pwdField;
    EditText repetePwdField;
    CircleImageView profilePhoto;
    Button signinBttn;
    ImageButton chooseFromGalleryBttn;
    ImageButton takePictureBttn;

    Bitmap img;

    double latitude;
    double longitude;
    GoogleApiClient googleApiClient;
    boolean resolvingError;

    LocationRequest mLocationRequest;
    Location mLocation;

    private FirebaseAuth mAuth;
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);


        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Log.d(TAG, "Sono in onCreate()");

        if (getIntent().getBooleanExtra("EXIT", false)) {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);

            finish();
        }

        emailField = (EditText) findViewById(R.id.email_field);
        pwdField = (EditText) findViewById(R.id.pwd_field);
        repetePwdField = (EditText) findViewById(R.id.repetepwd_field);
        profilePhoto = (CircleImageView)
                findViewById(R.id.profile_photo);

        signinBttn = (Button) findViewById(R.id.signin_bttn);
        chooseFromGalleryBttn = (ImageButton) findViewById(R.id.choose_from_gallery_bttn);
        takePictureBttn = (ImageButton) findViewById(R.id.take_a_picture_bttn);


        signinBttn.setOnClickListener(this);
        chooseFromGalleryBttn.setOnClickListener(this);
        takePictureBttn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Log.d(TAG, "Sono in onStart()");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Sono in onStop()");
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Sono in onClick()");
        switch (v.getId()) {
            case R.id.signin_bttn:
                createAccount(emailField.getText().toString(), pwdField.getText().toString());
                break;
            case R.id.choose_from_gallery_bttn:
                pickFromMediaGallery();
                break;
            case R.id.take_a_picture_bttn:
                pickProfilePhoto();
                break;
        }

    }

    /**
     * Create a new Firebase Account with email and password
     *
     * @param email
     * @param password
     */
    private void createAccount(final String email, final String password) {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        Log.d(TAG, "Credential: " + email + ", " + password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.auth_failed,
                                    Toast.LENGTH_LONG).show();
                        }
                        /*Log.d(TAG, "User details: " + mAuth.getCurrentUser().getEmail() + ", " +
                                mAuth.getCurrentUser().getDisplayName() + ", " +
                                mAuth.getCurrentUser().getPhotoUrl());
*/
                        storeUserInDb(email);
                        if (img != null) {
                            storeProfileImg(img);
                        }

                        signIn(email, password);
                        hideProgressDialog();
                        //setUpCurrentUser();
                        jumpToKiuwerActivity();

                    }
                });
    }

    /**
     * Sign in with Firebase registered credentials
     *
     * @param email
     * @param password
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * Create the intent for start the start activity
     */
    private void firstActivity() {
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }


    /**
     * Validate the register form
     *
     * @return true if all is ok false else.
     */
    private boolean validateForm() {
        boolean valid = true;
        String required = getResources().getString(R.string.required);
        String pwdNotMatch = getResources().getString(R.string.pwdDifferent);

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError(required);
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = pwdField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pwdField.setError(required);
            valid = false;
        } else {
            pwdField.setError(null);
        }

        String repetePassword = repetePwdField.getText().toString();
        if (TextUtils.isEmpty(repetePassword)) {
            repetePwdField.setError(required);
            valid = false;
        } else {
            repetePwdField.setError(null);
        }

        if (!password.equals(repetePassword)) {
            Toast.makeText(getApplicationContext(), pwdNotMatch, Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    /**
     * Show the progress dialog
     */
    private void showProgressDialog() {
        Log.d(TAG, "Sono in showProgressDialog()");
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    /**
     * Hide the progress dialog
     */
    private void hideProgressDialog() {
        Log.d(TAG, "Sono in hideProgressDialog()");
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    /**
     * Pick a profile photo from camera
     */
    private void pickProfilePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_ID);
    }

    private void pickFromMediaGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a picture"), RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent databack) {
        Log.d(TAG, "Sono in onActivityResult()");
        if (requestCode == REQUEST_ID) {
            img = (Bitmap) databack.getExtras().get("data");
            Log.d(TAG, "Profile Photo: " + img.toString());
            profilePhoto.setImageBitmap(img);
        }
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            resolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!googleApiClient.isConnecting() && !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            selectedImage = databack.getData();

            try {
                img = getThumbnail(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (img != null) profilePhoto.setImageBitmap(img);

        }
    }

    private Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ?
                onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAILS_SIZE) ? (originalSize / THUMBNAILS_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampreRatio(ratio);
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return bitmap;
    }

    private static int getPowerOfTwoForSampreRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    /**
     * Store the profile image into external storage
     *
     * @param img
     */
    private void storeProfileImg(Bitmap img) {
        FileIOManager fileIOManager = new FileIOManager(getApplicationContext());
        fileIOManager.writeImg(img);

        //Store to firebase storage
        Bitmap toStore = img;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        toStore.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
        StorageReference reference = firebaseStorage.child(emailField.getText().toString() + ".jpg");
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO gestire sta cosa
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });


    }

    /**
     * Store the registered user in the Firebase database
     *
     * @param email
     */
    private void storeUserInDb(String email) {
        ArrayList<Double> coord = getCoord();
        RemoteDBAdapter remoteDBAdapter = new RemoteDBAdapter();
        Log.d(TAG, "Sign in reg tok: " + FirebaseInstanceId.getInstance().getToken());
        remoteDBAdapter.writeUser();

    }

    //TODO complete this function
    private ArrayList<Double> getCoord() {
        return new ArrayList<>();
    }

    /**
     * Jump to the kiuwer Activity
     */
    private void jumpToKiuwerActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SignIn Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
