package com.dz.musicplayer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.security.Provider

class MusicBoundService : Service() {

      private var iBinder : IBinder = myBinder()

    class myBinder(): Binder() {
         fun getService() : MusicBoundService {
             return MusicBoundService()
         }
    }

    override fun onBind(intent: Intent?): IBinder? = iBinder


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}