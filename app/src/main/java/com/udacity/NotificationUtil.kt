package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0


fun NotificationManager.sendNotification(appContext: Context) {
    // create intent
    val contentIntent = Intent(appContext, DetailActivity::class.java)

    // create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        appContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    val builder = NotificationCompat.Builder(
        appContext,
        appContext.getString(R.string.channel_id)
    )
        //set title, text, icon
        .setContentTitle(appContext.getString(R.string.notification_title))
        .setContentText(appContext.getText(R.string.notification_description))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)


        // add check status action
        .setAutoCancel(true)


// call notify
    notify(NOTIFICATION_ID, builder.build())
}
