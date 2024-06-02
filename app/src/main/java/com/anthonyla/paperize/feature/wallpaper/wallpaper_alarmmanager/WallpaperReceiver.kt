package com.anthonyla.paperize.feature.wallpaper.wallpaper_alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.anthonyla.paperize.core.SettingsConstants.WALLPAPER_CHANGE_INTERVAL_DEFAULT
import com.anthonyla.paperize.feature.wallpaper.wallpaper_service.WallpaperService1
import com.anthonyla.paperize.feature.wallpaper.wallpaper_service.WallpaperService2
import dagger.hilt.android.AndroidEntryPoint

/**
 * Receiver for the alarm manager to change wallpaper
 */
@AndroidEntryPoint
class WallpaperReceiver: BroadcastReceiver() {
    enum class Type { HOME, LOCK, BOTH }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val timeInMinutes1 = intent?.getIntExtra("timeInMinutes1", WALLPAPER_CHANGE_INTERVAL_DEFAULT) ?: WALLPAPER_CHANGE_INTERVAL_DEFAULT
            val timeInMinutes2 = intent?.getIntExtra("timeInMinutes2", WALLPAPER_CHANGE_INTERVAL_DEFAULT) ?: WALLPAPER_CHANGE_INTERVAL_DEFAULT
            val scheduleSeparately = intent?.getBooleanExtra("scheduleSeparately", false) ?: false
            val type = intent?.getIntExtra("type", Type.BOTH.ordinal) ?: Type.BOTH.ordinal
            Log.d("PaperizeWallpaperChanger", "onReceive: timeInMinutes1=$timeInMinutes1, timeInMinutes2=$timeInMinutes2, scheduleSeparately=$scheduleSeparately, type=$type")

            val serviceIntent = Intent().apply {
                putExtra("timeInMinutes1", timeInMinutes1)
                putExtra("timeInMinutes2", timeInMinutes2)
                putExtra("scheduleSeparately", scheduleSeparately)
                putExtra("type", type)
            }
            if (type == WallpaperScheduler.Type.BOTH.ordinal || type == WallpaperScheduler.Type.HOME.ordinal) {
                serviceIntent.setClass(context, WallpaperService1::class.java).apply {
                    action = WallpaperService1.Actions.START.toString()
                }
            } else {
                serviceIntent.setClass(context, WallpaperService2::class.java).apply {
                    action = WallpaperService2.Actions.START.toString()
                }
            }
            context.startService(serviceIntent)

            // Schedule next alarm for next wallpaper change
            val origin = intent?.getIntExtra("origin", -1)?.takeIf { it != -1 }
            WallpaperScheduler(context).scheduleWallpaperAlarm(
                WallpaperAlarmItem(
                    timeInMinutes1 = timeInMinutes1,
                    timeInMinutes2 = timeInMinutes2,
                    scheduleSeparately = scheduleSeparately
                ),
                origin
            )
        }
    }
}