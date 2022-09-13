package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.Constants.GLIDE_URL
import com.udacity.Constants.RETROFIT_URL
import com.udacity.Constants.UDACITY_URL
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url = ""
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onStop() {
        unregisterReceiver(receiver)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager =
            ContextCompat.getSystemService(this,
                NotificationManager::class.java) as NotificationManager

        createChannel(getString(R.string.channel_id),
            getString(R.string.channel_name),
            getString(R.string.channel_decription))


        rb_group.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.retrofit_rb -> url = RETROFIT_URL
                R.id.udacity_rb -> url = UDACITY_URL
                R.id.glide_rb -> url = GLIDE_URL
            }
        }

        custom_button.setOnClickListener {
            if (url != "") {
                Log.d(TAG, "url is -> $url")
                download(url)
            } else {
                Toast.makeText(this, getString(R.string.alert_toast), Toast.LENGTH_SHORT).show()
                custom_button.downloadCompleted()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.d(TAG, "onReceive -> DownloadId = $id")

            getDownloadStatus(id!!, context, intent)

        }
    }

    private fun getDownloadStatus(id: Long, context: Context, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            custom_button.downloadCompleted()
            val query = DownloadManager.Query()
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            query.setFilterById(id)
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                if (cursor.count > 0) {
                    val status =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Log.d(TAG, "File is downloaded successfully")
                        notificationManager.sendNotification(context,
                            url,
                            getString(R.string.success))
                    } else {
                        val message =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                        Log.d(TAG, "Error while downloading $message")
                        notificationManager.sendNotification(context, url, getString(R.string.fail))
                    }
                }
            }
        }

    }

    private fun download(URL: String) {
        notificationManager.cancelAll()
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "code.zip")


        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        Log.d(TAG, "Download Id = $downloadID")
    }

    private fun createChannel(channelId: String, channelName: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = channelDescription
                setShowBadge(false)
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
