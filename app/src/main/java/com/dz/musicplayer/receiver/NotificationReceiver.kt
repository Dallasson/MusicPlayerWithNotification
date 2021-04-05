package com.dz.musicplayer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.dz.musicplayer.Constants
import com.dz.musicplayer.service.MusicBoundService

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val actionIntent = Intent(context,MusicBoundService::class.java)
        if(intent?.action != null){
            when(intent.action){
                Constants.ACTION_PREVIOUS -> {
                    actionIntent.putExtra("ActionName",intent.action)
                    context?.startService(intent)
                }
                Constants.ACTION_PLAY -> {
                    actionIntent.putExtra("ActionName",intent.action)
                    context?.startService(intent)
                }
                Constants.ACTION_NEXT -> {
                    actionIntent.putExtra("ActionName",intent.action)
                    context?.startService(intent)
                }
            }
        }
    }
}