package com.domain.my.giuseppe.kiu.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.kiuwer.MainActivity;
import com.domain.my.giuseppe.kiu.model.User;
import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDBAdapter;
import com.domain.my.giuseppe.kiu.service.MyFirebaseInstanceIDService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class StartActivity extends AppCompatActivity
{

    private static final String TAG = "StartActivity";
    private static final int CLOSE_CODE = 1;
    private static final int RC_SIGN_IN = 9001;

    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailfield;
    private EditText passfield;
    private Button signUp;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        emailfield= (EditText) findViewById(R.id.emailfieldstartactivity);
        passfield=(EditText) findViewById(R.id.passfieldstartactivity);

        Log.d(TAG, "Sono in onCreate()");

        if(getIntent().getBooleanExtra("EXIT", false))
        {finish();}

        // Views
        //mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
        /*findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);*/
        signUp = (Button) findViewById(R.id.signup1);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()) {
                    signIn(emailfield.getText().toString(),passfield.getText().toString());

                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });



        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "Sto creando FirebaseAuthListener");
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                Log.d(TAG, "Sono in onAuthStateChanged()");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    // UserOLD is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    setUserLogginDetails(user);
                    //faccio partire il service per aggiornare il token
                    Intent intent =new Intent(getApplicationContext(),MyFirebaseInstanceIDService.class);
                    startService(intent);
                    jumpToMainActivity();
                }
                else
                {
                    // UserOLD is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // [START customize_button]
        //SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        // [END customize_button]
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "Sono in onStart()");
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.d(TAG, "Sono in onStop()");
        if (mAuthListener != null) {mAuth.removeAuthStateListener(mAuthListener);}
    }

    // [START signIn]
    private void signIn()
    {
        Log.d(TAG, "Sono in signIn()");

        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }
    // [END signIn]

    // [START signOut]
    private void signOut()
    {Log.d(TAG, "Sono in signOut()");}
    // [END signOut]

    private void showProgressDialog()
    {
        Log.d(TAG, "Sono in showProgressDialog()");
        if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog()
    {
        Log.d(TAG, "Sono in hideProgressDialog()");
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {mProgressDialog.hide();}
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


    /*@Override
    public void onClick(View v)
    {
        Log.d(TAG, "Sono in onClick()");
        switch (v.getId())
        {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                break;
        }
    }*/

    public void setUserLogginDetails(FirebaseUser user)
    {
        Log.d(TAG, "Sono in setUserLogginDetails()");
        UserLoggingDetails.getIstance().setUid(FirebaseInstanceId.getInstance().getToken());
        UserLoggingDetails.getIstance().setName(user.getDisplayName());
        UserLoggingDetails.getIstance().setEmail(user.getEmail());
        UserLoggingDetails.getIstance().setPhotoUri(user.getPhotoUrl());

        Log.d(TAG, "####################### DEBUG USER LOGGIN DETAILS ########################");
        Log.d(TAG, "Name: " + user.getDisplayName());
        Log.d(TAG, "UID: " + user.getUid());
        Log.d(TAG, "Email: " + user.getEmail());
        Log.d(TAG, "PhotoUrl: " + user.getPhotoUrl());
        Log.d(TAG, "####################### END DBG ########################");
    }

    public void jumpToMainActivity()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }

    private class RunTransaction extends AsyncTask<UserLoggingDetails, Void, Boolean>
    {
        RemoteDBAdapter remoteDBAdapter;
        User user;

        @Override
        protected void onPreExecute()
        {
            Log.d(TAG, "onPreExecute()");
        }

        @Override
        protected Boolean doInBackground(UserLoggingDetails... params)
        {
            Log.d(TAG, "doInBackground()");
            remoteDBAdapter = new RemoteDBAdapter();
            remoteDBAdapter.writeUser();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            Log.d(TAG, "onPostExecute()");
        }

        @Override
        protected void onCancelled()
        {
            Log.d(TAG, "onCancelled()");
        }
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

        String email = emailfield.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailfield.setError(required);
            valid = false;
        } else {
            emailfield.setError(null);
        }

        String password = passfield.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passfield.setError(required);
            valid = false;
        } else {
            passfield.setError(null);
        }
        return valid;
    }
}
