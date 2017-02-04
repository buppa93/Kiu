package com.domain.my.giuseppe.kiu.service;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Per le immagini di profilo
 */
public class UploadService extends MyCustomService
{
    private static final String TAG = "UploadService";
    private static final int NOTIFY_ID_DOWNLOAD = 0;


    /* intent actions */
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    /* intent extras */
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    public static final String PHOTOS_NODE = "photos";

    private StorageReference storageReference;

    public UploadService() {}

    @Override
    public void onCreate()
    {
        super.onCreate();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(TAG, "onStartCommand: " + intent + " : " + startId);
        if(ACTION_UPLOAD.equals(intent.getAction()))
        {
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
            uploadFromUri(fileUri);
        }
        return START_REDELIVER_INTENT;
    }

    private void uploadFromUri(final Uri fileUri)
    {
        Log.d(TAG, "updateFromUri: src: " + fileUri.toString());

        taskStarted();

        final StorageReference photoRef = storageReference.child(PHOTOS_NODE)
                .child(fileUri.getLastPathSegment());

        Log.d(TAG, "uploadFromUri: dst: " + photoRef.getPath());
        photoRef.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Log.d(TAG, "uploadFromUri: onSuccess");

                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                broadcastUploadFinished(downloadUri, fileUri);

                taskCompleted();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.d(TAG, "uploadFromUri: onFailure");

                broadcastUploadFinished(null, fileUri);
                taskCompleted();
            }
        });
    }

    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri)
    {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
    }
}
