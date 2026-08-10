package com.kiraieee.wibuggy

import android.os.Build
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log

class WifiDebugTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()
        try {
            val enabled = isWirelessDebuggingEnabled()
            Settings.Global.putInt(contentResolver, "adb_wifi_enabled", if (enabled) 0 else 1)
            updateTile()
        } catch (e: SecurityException) {
            Log.e("WiBuggy", "Missing WRITE_SECURE_SETTINGS permission", e)
        }
    }

    private fun isWirelessDebuggingEnabled(): Boolean {
        return try {
            Settings.Global.getInt(contentResolver, "adb_wifi_enabled", 0) == 1
        } catch (e: SecurityException) {
            false
        }
    }

    private fun updateTile() {
        val tile = qsTile ?: return
        val enabled = isWirelessDebuggingEnabled()

        tile.label = "WiBuggy"
        tile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            tile.stateDescription = if (enabled) "On" else "Off"
        }

        tile.updateTile()
    }
}
