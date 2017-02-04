package com.domain.my.giuseppe.kiu.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.domain.my.giuseppe.kiu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by ivasco92 on 12/01/17.
 */

public class PasswordDialogFragment extends DialogFragment {
    EditText newpass;
    EditText repeatpass;
    View rootview;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        // Get the layout inflater
        LayoutInflater inflater = this.getActivity().getLayoutInflater();

        rootview=inflater.inflate(R.layout.alertdialog_updatepass,null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootview)
                // Add action buttons
                .setPositiveButton(R.string.confirmbutton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                        newpass = (EditText) rootview.findViewById(R.id.newpass);
                        repeatpass = (EditText) rootview.findViewById(R.id.repeatnewpass);
                        if (validateForm()) {
                            user.updatePassword(newpass.getText().toString());
                            Toast.makeText(getContext(),"Password Aggiornata!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(),"Password non aggiornata!", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });

        return builder.create();
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

        String newpassword = newpass.getText().toString();
        if (TextUtils.isEmpty(newpassword)) {
            newpass.setError(required);
            valid = false;
        } else {
            newpass.setError(null);
        }

        String repetePassword = repeatpass.getText().toString();
        if (TextUtils.isEmpty(repetePassword)) {
            repeatpass.setError(required);
            valid = false;
        } else {
            repeatpass.setError(null);
        }

        if (!newpassword.equals(repetePassword)) {
            Toast.makeText(getContext(), pwdNotMatch, Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }
}
