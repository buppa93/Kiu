package com.domain.my.giuseppe.kiu.signin;

import android.net.Uri;

/**
 * Created by giuseppe on 29/06/16.
 */

//TODO togliere id
//TODO Implementare lettura e scrittura da file
public class UserLoggingDetails
{
    private static UserLoggingDetails istance;
    private String name;
    private String email;
    private Uri photoUri;
    private String uid;

    private static final String FILENAME = "user_data";
    private static final String LOG_TAG = UserLoggingDetails.class.getSimpleName();

    //TODO add more attributes

    public UserLoggingDetails() {}

    public String getName() {return this.name;}

    public String getEmail() {return this.email;}

    public Uri getPhotoUri() {return this.photoUri;}

    public String getUid() {return this.uid;}

    public static UserLoggingDetails getIstance ()
    {
        if(istance == null)
        {istance = new UserLoggingDetails();}

        return istance;
    }

    public void setName(String name) {this.name = name;}

    public void setEmail(String email) {this.email = email;}

    public void setPhotoUri(Uri photoUri) {this.photoUri = photoUri;}

    public void setUid(String uid) {this.uid = uid;}

   /* public static void storeUserData(Context context, String name, String email, Uri photoUri)
    {
        FileIOManager io = new FileIOManager(context);
        ArrayList<String> toWrite = new ArrayList<>();
        toWrite.add(name);
        toWrite.add(email);
        toWrite.add(uri);
        try {io.write(toWrite);}
        catch (IOException e) {e.printStackTrace();}
    }*/

   /* public static boolean isUserSignedUp(Context context)
    {
        FileIOManager io = new FileIOManager(context);
        StringBuffer dataRead = new StringBuffer("");

        try {dataRead = io.read();}
        catch (IOException e) {e.printStackTrace();}

        if(dataRead.equals(""))
        {return false;}
        else
        {
            ArrayList<String> userDetails = io.resolveDataStringBuffer(dataRead);
            if(userDetails.size() == 0)
            {return false;}
            getIstance().setName(userDetails.get(0));
            getIstance().setEmail(userDetails.get(1));
            //getIstance().setPhotoUri(userDetails.get(2));
            return  true;
        }
    }*/

}
