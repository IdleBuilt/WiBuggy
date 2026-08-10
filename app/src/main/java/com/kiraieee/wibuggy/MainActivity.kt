package com.kiraieee.wibuggy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton

class MainActivity : ComponentActivity() {

    private lateinit var statusText: TextView
    private lateinit var copyButton: MaterialButton
    private lateinit var titleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        copyButton = findViewById(R.id.copyButton)
        titleText = findViewById(R.id.titleText)

        copyButton.setOnClickListener {
            val cmd = "adb shell pm grant com.kiraieee.wibuggy android.permission.WRITE_SECURE_SETTINGS"
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("adb command", cmd))
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        updateStatus()
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {
        if (hasPermission()) {
            titleText.text = "You're All Set!"
            statusText.text = "Permission granted!\n\nPull down the notification shade, tap Edit, and add the \"WiBuggy\" tile."
            copyButton.visibility = View.GONE
        } else {
            titleText.text = "Setup Required"
            statusText.text = "Connect your phone to a PC via USB and run this command in a terminal:"
            copyButton.visibility = View.VISIBLE
        }
    }

    private fun hasPermission(): Boolean {
        return try {
            Settings.Global.putInt(contentResolver, "adb_wifi_enabled",
                Settings.Global.getInt(contentResolver, "adb_wifi_enabled", 0))
            true
        } catch (e: SecurityException) {
            false
        }
    }
}
