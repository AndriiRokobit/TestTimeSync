package com.example.testtimesync

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private var clockView: TextView? = null
    private var inputTime: EditText? = null
    private var button: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputTime = findViewById(R.id.startTime)
        button = findViewById(R.id.apply)
        button?.setOnClickListener {
            SNTPClient.getDate(
                Calendar.getInstance().timeZone
            ) { rawDate, dateNtp, ex ->
                val str = inputTime?.text?.toString()
                val formatter: DateFormat = SimpleDateFormat("hh:mm:ss")
                val date: Date = formatter.parse(str)
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, date.hours)
                calendar.set(Calendar.MINUTE, date.minutes)
                calendar.set(Calendar.SECOND, date.seconds)
                lifecycleScope.launchWhenStarted {
                    Log.e("delaySystem",(calendar.timeInMillis-System.currentTimeMillis()).toString())
                    Log.e("TimeNTP", dateNtp.toString())
                    Log.e("TimeNTP", Date().toString())
                    Log.e("delayNTP",(calendar.timeInMillis-dateNtp.time).toString())
                    delay(calendar.timeInMillis-dateNtp.time)
                    Log.e("TimeNow", Date().toString())
                    clockView?.isVisible = true
                }
            }
        }
        clockView = findViewById(R.id.time)
        lifecycleScope.launchWhenStarted {
            while (true) {
                clockView?.text = getTime(Date())
                delay(10)
            }
        }
    }


    fun getTime(date: Date): String {
        val formatter = SimpleDateFormat("HH:mm:ss.SS")
        return formatter.format(date)
    }
}