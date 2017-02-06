package com.domain.my.giuseppe.kiu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by giuseppe on 07/07/16.
 */
public class FileIOManager implements OnSuccessListener, OnFailureListener
{
    private static final String TAG = FileIOManager.class.getSimpleName();
    private static final String FILENAME = "user_details.txt";
    private static final String PROFILE_PICTURE_FILE = "profilePicture.jpg";
    private byte[] BUFFER;
    private byte[] imgBuffer;

    private Context context;
    private StringBuffer fileContent;

    public FileIOManager(Context context) {this.context = context;}

    public void write(ArrayList<String> data) throws IOException
    {
        FileOutputStream fos = null;

        fos = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

        for(int i=0; i<data.size(); i++)
        {fos.write((data.get(i)+"*").getBytes());}

        fos.close();
    }

    public void writeImg(Bitmap img)
    {
        File pictureFile = getOutputMediaFile();

        if(pictureFile == null)
        {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }

        try
        {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            img.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            Log.d(TAG, "File not found: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile()
    {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                        + "/Android/data/"
                        + context.getPackageName()
                        + "/Files");

        if(!mediaStorageDir.exists())
        {
            if(!mediaStorageDir.mkdirs()) {return null;}
        }

        File mediaFile;
        String mImageName = PROFILE_PICTURE_FILE;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public Bitmap readImg()
    {
        Bitmap img = null;
        File pictureFile = getInputMediaFile();

        if(pictureFile == null)
        {
            Log.d(TAG, "Error reading media file, check storage permissions: ");
            return null;
        }

        try
        {
            FileInputStream fis = new FileInputStream(pictureFile);
            img = BitmapFactory.decodeStream(fis);
            fis.close();
        }
        catch (FileNotFoundException e) {Log.d(TAG, "File not found: " + e.getMessage());}
        catch (IOException e) {Log.d(TAG, "Error accessing file: " + e.getMessage());}

        return img;
    }

    private File getInputMediaFile()
    {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                        + "/Android/data"
                        + context.getPackageName()
                        + "Files");
        if(!mediaStorageDir.exists())
        {
            if(!mediaStorageDir.mkdirs()) {return null;}
        }

        File mediaFile;
        String mImageName = PROFILE_PICTURE_FILE;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public StringBuffer read() throws IOException
    {
        FileInputStream fis = null;
        this.fileContent = new StringBuffer("");
        int n = 0;

        fis = context.openFileInput(FILENAME);
        BUFFER = new byte[fis.available()];

        while((n = fis.read(BUFFER)) != -1)
        {
            fileContent.append(new String(BUFFER));
        }

        fis.close();
        debugReadFile(fileContent);
        return fileContent;
    }

    public ArrayList<String> resolveDataStringBuffer (StringBuffer stringBuffer)
    {
        ArrayList<String> resolvedData = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(stringBuffer.toString(), "*");

        while (stringTokenizer.hasMoreTokens())
        {resolvedData.add(stringTokenizer.nextToken());}

        return resolvedData;
    }

    public Bitmap getProfileImg()
    {
        String email = "";
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        email = user.getEmail();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference reference = storageReference.child(email + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(this);

        Bitmap bitmap = BitmapFactory.decodeByteArray(this.imgBuffer, 0, this.imgBuffer.length);
        return bitmap;
    }

    public Bitmap getOtherUserProfileImg(String email)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        email = user.getEmail();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference reference = storageReference.child(email + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(this);

        Bitmap bitmap = BitmapFactory.decodeByteArray(this.imgBuffer, 0, this.imgBuffer.length);
        return bitmap;
    }



    public void debugReadFile(StringBuffer sb)
    {Log.d(TAG, "LETTO: " + sb);}

    @Override
    public void onFailure(@NonNull Exception e) {}

    @Override
    public void onSuccess(Object o)
    {
        this.imgBuffer = (byte[]) o;

    }
}

