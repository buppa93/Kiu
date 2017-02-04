package com.domain.my.giuseppe.kiu.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.domain.my.giuseppe.kiu.R;
import com.domain.my.giuseppe.kiu.helper.ShowRequestDetailActivity;

/**
 * Created by giuseppe on 15/08/16.
 */
public class CustomNotification
{
    private static final int CUSTOM_ACTION_NOTIFICATION = 9567;
    protected Context context;
    NotificationManager notificationManager;
    Intent actionIntentShowDetails;
    PendingIntent actionPendingIntentShowDetails;



    public CustomNotification(Context context)
    {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showCustomNotification()
    {
        //TODO gesture delete vedi pag.583
        //TODO vedi pag.583
        String moreTxt = context.getResources().getString(R.string.more);
        String ignoreTxt = context.getResources().getString(R.string.ignore);
        final String contextText = this.context.getResources().getString(R.string.new_request_for_queue);
        final String contextTitle = this.context.getResources().getString(R.string.new_kiu_request);
        actionIntentShowDetails = new Intent(this.context, ShowRequestDetailActivity.class);
        actionPendingIntentShowDetails = PendingIntent.getActivity(this.context,
                0, actionIntentShowDetails, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(this.context)
                .setSmallIcon(R.drawable.ic_notifica3)
                .setColor(Color.BLUE)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(actionPendingIntentShowDetails)
                .setContentText(contextText)
                .setContentText("dvgeabeab")
                .setContentText("fxbdfsbgbfbbaba")
                .addAction(R.drawable.ic_decline, "", actionPendingIntentShowDetails)
                .addAction(R.drawable.ic_rinegozia, "", actionPendingIntentShowDetails)  //TODO implementazione paolo
                .addAction(R.drawable.ic_accept,"", actionPendingIntentShowDetails)
                .setContentTitle(contextTitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(R.string.kiuwername+":"+"picio \n"+R.string.location+":"+" via milano 11, gallipoli 73014\n"+
                                +R.string.time+":"+"20:34\n"+R.string.date+":"+"13/04/2016\n"+R.string.rate+":euro "+"3.00"))
                .setVibrate(new long[] { 0, 500, 500, 500, 500 }) // { delay, vibrate, sleep, vibrate, sleep }
                //.setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();
        notificationManager.notify(CUSTOM_ACTION_NOTIFICATION, notification);


     /*  NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[6];
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");
        inboxStyle.setBigContentTitle("ciaoooo");
// Moves events into the expanded layout
        for (int i=0; i < events.length; i++) {

            inboxStyle.addLine(events[i]);
        }
// Moves the expanded layout object into the notification object.
        notification.setStyle(inboxStyle);
        notificationManager.notify(CUSTOM_ACTION_NOTIFICATION, notification);

// Issue the notification here.*/
    }
}
