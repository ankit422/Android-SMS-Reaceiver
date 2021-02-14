package com.smsreader

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.smsreader.data.AppDatabase
import com.smsreader.data.MessageDao
import com.smsreader.data.Messages
import com.smsreader.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), SmsReceiver.Listener {
    lateinit var binding: ActivityMainBinding
    val REQUEST_CODE_ASK_PERMISSIONS: Int = 1
    lateinit var messageDoa: MessageDao
    lateinit var messageAdapter: MessageAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageDoa = AppDatabase.getDatabase(this@MainActivity).messageDao()
        messageAdapter = MessageAdapter()
        binding.listApp.addItemDecoration(
            DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL
            )
        )

        binding.listApp.adapter = messageAdapter

        getMessagesFromDb()
        //== Proceed only if granted SMS permission===
        askForPermission()
    }

    private fun getMessagesFromDb() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                val list = withContext(Dispatchers.IO) {
                    messageDoa.getAll()
                }

                if (list.isNotEmpty()) {
                    messageAdapter.setData(list)
                    binding.message.visibility = View.GONE
                } else
                    binding.message.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun saveMessage(message: String?, date: String?, price: String?) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                val message = withContext(Dispatchers.IO) {
                    val entityConfig = Messages()
                    entityConfig.message = message
                    entityConfig.date = date
                    entityConfig.cost = price
                    messageDoa.insert(entityConfig)

                    entityConfig
                }

                messageAdapter.addNewMessage(message)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSMSReceived(message: String?, date: String?, price: String?) {
        Log.e("onSMSReceived", "$date    $price")
        binding.message.visibility = View.GONE

        //====save in DB===
        saveMessage(message, date, price)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askForPermission() {
        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECEIVE_SMS), REQUEST_CODE_ASK_PERMISSIONS
            )
        } else {
            registerSmsListener()
        }
    }

    private fun registerSmsListener() {
        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        filter.priority = 2147483647
        val receiver = SmsReceiver(this)
        registerReceiver(receiver, filter)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerSmsListener()
            } else {
                Toast.makeText(
                    this,
                    "Please Allow permission from Settings to proceed with  App.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}