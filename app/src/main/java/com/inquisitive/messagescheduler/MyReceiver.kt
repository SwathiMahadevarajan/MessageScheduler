package com.inquisitive.messagescheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast
import com.inquisitive.messagescheduler.adapter.numdrop

@Suppress("DEPRECATION")
class MyReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            Toast.makeText(p0, "Alarm Ringing...", Toast.LENGTH_SHORT).show()
            println("message sent")
            var obj = SmsManager.getDefault()
            obj.sendTextMessage(numdrop, null, message.text.toString(), null, null)
            Toast.makeText(p0, "Message sent successfully", Toast.LENGTH_LONG).show()



        }
    }
