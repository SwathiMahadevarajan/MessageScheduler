package com.inquisitive.messagescheduler


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.os.Bundle
import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Intent
import android.provider.ContactsContract.*
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inquisitive.messagescheduler.adapter.ItemAdapter
import com.inquisitive.messagescheduler.adapter.numdrop
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast
lateinit var timeTxt:EditText
lateinit var editText: EditText
lateinit var message:EditText
lateinit var recyclerView:RecyclerView
class MainActivity : AppCompatActivity() {
    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
    private val contactList = ArrayList<ContactModel>()
    private lateinit var Adapter: ItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Message Scheduler"
        recyclerView= findViewById(R.id.contact)
        Adapter = ItemAdapter(contactList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = Adapter
        prepareContactData()
       editText = findViewById(R.id.ContactName)
       message = findViewById(R.id.Message)
        var send = findViewById<Button>(R.id.send)
       timeTxt=findViewById(R.id.TimeTxt)
        with(editText) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        if(editText.length()!=0)
                        recyclerView.visibility=View.VISIBLE
                    filter(s.toString())

                }
                override fun afterTextChanged(s: Editable) {}
            })
        }
        send.setOnClickListener {
            go()

        }


    }

    @SuppressLint("Range")
    private fun prepareContactData()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        else {
            val resolver: ContentResolver = contentResolver;
            val cursor = resolver.query(Contacts.CONTENT_URI, null, null, null, null)
            if (cursor != null) {
                if (cursor.count >0) {
                    while (cursor.moveToNext()) {
                        val id =cursor.getString(cursor.getColumnIndex(Contacts._ID))
                        val name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME))
                        val phoneNumber = (cursor.getString(
                            cursor.getColumnIndex(Contacts.HAS_PHONE_NUMBER))).toInt()

                        if (phoneNumber > 0) {
                            val cursorPhone = contentResolver.query(
                                CommonDataKinds.Phone.CONTENT_URI,
                                null, CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)
                            if (cursorPhone != null) {
                                if(cursorPhone.count>0)
                                    while (cursorPhone.moveToNext()) {
                                        val phoneNumValue = cursorPhone.getString(
                                            cursorPhone.getColumnIndex(CommonDataKinds.Phone.NUMBER)).filterNot { it.isWhitespace() }
                                       val position=contactList.size

                                        if(!(contactList.any{it.getNum()==phoneNumValue}))
                                        {
                                            var contact = ContactModel(name, phoneNumValue)
                                        contactList.add(contact)
                                    }
                                    }
                            }
                            cursorPhone?.close()
                        }
                    }
                } else {
                    Toast.makeText(this,"No contacts to display",Toast.LENGTH_SHORT).show()
                }
            }
            cursor?.close()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prepareContactData()
            } else {
                    Toast.makeText(this,"Permission must be granted in order to display contacts information",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun filter(text: String) {
        var temp = ArrayList<ContactModel>()
        for (d in contactList)
            if (d.getName().contains(text,true) or  d.getNum().contains(text))
                temp.add(d)
        if (temp.size!=0)
            Adapter.updateList(temp)
        else
            recyclerView.visibility=View.GONE
            numdrop=text
    }
    fun go() {
        val time:Int= timeTxt.text.toString().toInt()
        val i = Intent(this, MyReceiver::class.java)
        val pi = PendingIntent.getBroadcast(this, 0, i, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(RTC_WAKEUP,System.currentTimeMillis() + time * 1000, pi)
        Toast.makeText(this@MainActivity, "Alarm set in $time seconds", Toast.LENGTH_SHORT).show()

    }
}

