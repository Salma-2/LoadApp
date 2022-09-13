package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.Constants.URL
import com.udacity.Constants.STATUS

private const val NOTIFICATION_ID = 0



fun NotificationManager.sendNotification(appContext: Context, filename: String, status: String) {
    // create intent
    val detailIntent = Intent(appContext, DetailActivity::class.java)
    detailIntent.putExtra(URL, filename)
    detailIntent.putExtra(STATUS, status)

    // create PendingIntent
    val detailPendingIntent = PendingIntent.getActivity(
        appContext,
        NOTIFICATION_ID,
        detailIntent,
        PendingIntent.FLAG_ONE_SHOT
    )


    val builder = NotificationCompat.Builder(
        appContext,
        appContext.getString(R.string.channel_id)
    )
        .setAutoCancel(true)
        .setContentIntent(detailPendingIntent)

        //set title, text, icon
        .setContentTitle(appContext.getString(R.string.notification_title))
        .setContentText(appContext.getText(R.string.notification_description))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)

        // add check status action
        .addAction(
            0,
            appContext.getString(R.string.notification_button),
            detailPendingIntent
        )


        // set priority
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)


// call notify
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
