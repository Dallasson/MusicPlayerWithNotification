package com.dz.musicplayer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.dz.musicplayer.Constants
import com.dz.musicplayer.extras.ActionPlaying
import java.security.Provider

class MusicBoundService : Service() {

      private var iBinder : IBinder = myBinder()
      private lateinit var actionPlaying: ActionPlaying

    class myBinder(): Binder() {
         fun getService() : MusicBoundService {
             return MusicBoundService()
         }
    }

    override fun onBind(intent: Intent?): IBinder? = iBinder


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val actionName = intent?.getStringExtra("ActionName")
        if(actionName != null){
            when(actionName){
                Constants.ACTION_PREVIOUS -> {
                    actionPlaying.previousClicked()
                }
                Constants.ACTION_PLAY -> {
                    actionPlaying.playClicked()
                }
                Constants.ACTION_NEXT -> {
                    actionPlaying.nextClicked()
                }
            }
        }
        return START_STICKY
    }

    fun actionCallBack(actionPlaying: ActionPlaying){
        this.actionPlaying = actionPlaying
    }
}