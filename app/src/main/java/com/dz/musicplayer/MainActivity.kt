package com.dz.musicplayer

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dz.musicplayer.databinding.ActivityMainBinding
import com.dz.musicplayer.extras.ActionPlaying
import com.dz.musicplayer.receiver.NotificationReceiver
import com.dz.musicplayer.service.MusicBoundService
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity(),ActionPlaying , ServiceConnection{
    private lateinit var tracksList : MutableList<TrackModel>
    private lateinit var tracksUrl : MutableList<Uri>
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private var isServiceBound = false
    private var currentPosition = 0
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var musicBoundService  : MusicBoundService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tracksList = mutableListOf()
        tracksUrl = mutableListOf()
        mediaSession = MediaSessionCompat(this,"TAG")
        populateTracksList()
        populateTracksUrls()

        binding.skipNext.setOnClickListener {
            nextClicked()
        }
        binding.skipPrevious.setOnClickListener {
             previousClicked()
        }
        binding.play.setOnClickListener {
            playClicked()
        }
        
    }

    private fun populateTracksUrls(){
        tracksUrl.add(Uri.parse("https://cdnm.meln.top/mr/Akon%20-%20Burning%20Alive.mp3?session_key=665c0455ef61afa2b129a03a06c00098&hash=5c1ad259810a0af78881366cf7bdd3e4"))
        tracksUrl.add(Uri.parse("https://cdnm.meln.top/mr/Akon%20-%20Right%20Now.mp3?session_key=665c0455ef61afa2b129a03a06c00098&hash=e3be2d34dc8764b8aa721d60852dfa15"))
        tracksUrl.add(Uri.parse("https://cdnm.meln.top/mr/Akon%20-%20Akon%20-%20Struggle%20everyday.mp3?session_key=665c0455ef61afa2b129a03a06c00098&hash=135e4297cc38cabf0a6eafe8b4c5468f"))
        tracksUrl.add(Uri.parse("https://cdnm.meln.top/mr/Akon%20-%20Bananza%20(Belly%20Dancer).mp3?session_key=665c0455ef61afa2b129a03a06c00098&hash=cfe0ce236c5c492200568bb9276b57b1"))
    }
    private fun populateTracksList() {
        tracksList.add(TrackModel("Beautiful","Akon",R.drawable.bg))
        tracksList.add(TrackModel("Dangerous","Akon",R.drawable.bg))
        tracksList.add(TrackModel("Indilla","SOS",R.drawable.bg))
        tracksList.add(TrackModel("50 Cent","Candy Shop",R.drawable.bg))
    }
    override fun nextClicked() {
        if(currentPosition == 3)
            currentPosition = 0
        else
            currentPosition++

        binding.trackName.text = tracksList[currentPosition].singer
        if(!isPlaying){
            isPlaying = true
            binding.play.setImageResource(R.drawable.ic_play_black)
            showNotification(R.drawable.ic_play_black)
            mediaPlayer = MediaPlayer.create(this,tracksUrl[currentPosition])
            mediaPlayer.start()
        } else {
            isPlaying = false
            binding.play.setImageResource(R.drawable.ic_pause_black)
            showNotification(R.drawable.ic_pause_black)
            mediaPlayer.pause()
        }
    }

    override fun previousClicked() {
        if(currentPosition == 0)
            currentPosition = 3
        else
            currentPosition--
        binding.trackName.text = tracksList[currentPosition].singer
        if(!isPlaying){
            isPlaying = true
            binding.play.setImageResource(R.drawable.ic_play_black)
            showNotification(R.drawable.ic_play_black)
            mediaPlayer = MediaPlayer.create(this,tracksUrl[currentPosition])
            mediaPlayer.start()
        } else {
            isPlaying = false
            binding.play.setImageResource(R.drawable.ic_pause_black)
            showNotification(R.drawable.ic_pause_black)
            mediaPlayer.pause()
        }
    }

    override fun playClicked() {
        if(!isPlaying){
            isPlaying = true
            binding.play.setImageResource(R.drawable.ic_play_black)
            showNotification(R.drawable.ic_play_black)
            mediaPlayer = MediaPlayer.create(this,tracksUrl[currentPosition])
            mediaPlayer.start()
        } else {
            isPlaying = false
            binding.play.setImageResource(R.drawable.ic_pause_black)
            showNotification(R.drawable.ic_pause_black)
            mediaPlayer.pause()
        }
    }


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val iBinder = service as MusicBoundService.myBinder
        musicBoundService = iBinder.getService()
        isServiceBound = true
        musicBoundService.actionCallBack(this)
        Log.d("ServiceTag","Service is Bound")

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isServiceBound = false
        Log.d("ServiceTag","Service is unBound")
    }
    private fun showNotification(playPauseBtn : Int){
        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)

        // TODO : Buttons Intent
        val intentNextBtn = Intent(this,NotificationReceiver::class.java).apply {
            action = Constants.ACTION_NEXT
        }
        val nextBtnPendingInt = PendingIntent.getBroadcast(this,1,intentNextBtn,0)

        //-------------------
        val intentPrevBtn = Intent(this,NotificationReceiver::class.java).apply {
            action = Constants.ACTION_PREVIOUS
        }
        val prevBtnPendingInt = PendingIntent.getBroadcast(this,1,intentPrevBtn,0)

        ///--------------------
        val intentPlayBtn = Intent(this,NotificationReceiver::class.java).apply {
            action = Constants.ACTION_PLAY
        }
        val playBtnPendingInt = PendingIntent.getBroadcast(this,1,intentPlayBtn,0)


        val bitMapThumbnail = BitmapFactory.decodeResource(resources,
        tracksList[currentPosition].thumbNail)
        val notification = NotificationCompat.Builder(this,Constants.CHANNEL_ID1)
                .setContentTitle(tracksList[currentPosition].title)
                .setContentText(tracksList[currentPosition].singer)
                .setLargeIcon(bitMapThumbnail)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .addAction(R.drawable.ic_skip_previous_black,"PREVIOUS",prevBtnPendingInt)
                .addAction(playPauseBtn,"PLAY",playBtnPendingInt)
                .addAction(R.drawable.ic_skip_next_black,"NEXT",nextBtnPendingInt)
                .setAutoCancel(false)
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionToken))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)

        NotificationManagerCompat.from(this).notify(100,notification.build())
    }
    override fun onStart() {
        super.onStart()
        if(!isServiceBound){
            Intent(this,MusicBoundService::class.java).apply {
                bindService(this,this@MainActivity, BIND_AUTO_CREATE)
            }

        }
    }
    override fun onResume() {
        super.onResume()
        Intent(this,MusicBoundService::class.java).apply {
            bindService(this,this@MainActivity, BIND_AUTO_CREATE)
        }

    }
    override fun onStop() {
        super.onStop()
        if(isServiceBound){
            unbindService(this)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
        }

    }


}