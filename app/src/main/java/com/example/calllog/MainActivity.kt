package com.example.calllog

import android.Manifest;
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val READ_CALL_LOG_PERMISSION_REQUEST = 1
    private lateinit var callLogAdapter: CallLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val callLogListView: ListView = findViewById(R.id.callLogListView)
        callLogAdapter =  CallLogAdapter(this)
        callLogListView.adapter = callLogAdapter

        if(checkCallLogPermission()){
            loadCallLogs()
        } else{
            requestCallLogPermission()
        }
    }

    private fun loadCallLogs(){
        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,CallLog.Calls.DATE + " DESC"
        )

        if (cursor != null && cursor.moveToFirst()){
            do{
                val number: String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                val duration: String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))

                callLogAdapter.addCallLog(number, duration)
            } while (cursor.moveToNext())

            cursor.close()
        }
    }

    private fun checkCallLogPermission(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCallLogPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CALL_LOG),
            READ_CALL_LOG_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_CALL_LOG_PERMISSION_REQUEST){
            if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                loadCallLogs()
            }
        }
    }
}