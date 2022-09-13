package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.Constants.GLIDE_URL
import com.udacity.Constants.RETROFIT_URL
import com.udacity.Constants.URL
import com.udacity.Constants.STATUS
import com.udacity.Constants.UDACITY_URL
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()

        val intent = intent

        val url = intent.getStringExtra(URL)
        val status = intent.getStringExtra(STATUS)

        var color = 0
        when (status) {
            getString(R.string.success) -> color = resources.getColor(R.color.colorPrimaryDark)
            getString(R.string.fail) -> color = Color.RED
        }
        status_tv.text = status
        status_tv.setTextColor(color)

        when (url) {
            RETROFIT_URL -> filename_tv.text = getString(R.string.retrofit_filename)
            GLIDE_URL -> filename_tv.text = getString(R.string.glide_filename)
            UDACITY_URL -> filename_tv.text = getString(R.string.udacity_filename)
        }

        ok_btn.setOnClickListener {
            onBackPressed()
        }

    }

}
