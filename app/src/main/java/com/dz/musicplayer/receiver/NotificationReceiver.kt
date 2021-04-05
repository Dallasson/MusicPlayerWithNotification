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
                    actionIntent.putExtra("ActionName",Constants.ACTION_PREVIOUS)
                }
                Constants.ACTION_PLAY -> {
                    actionIntent.putExtra("ActionName",Constants.ACTION_PLAY)
                }
                Constants.ACTION_NEXT -> {
                    actionIntent.putExtra("ActionName",Constants.ACTION_NEXT)
                }
            }
        }
    }
}