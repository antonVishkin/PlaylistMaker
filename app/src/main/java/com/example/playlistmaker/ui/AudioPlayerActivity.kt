package com.example.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.Creator
import com.example.playlistmaker.Creator.provideMediaPlayerInteractor
import com.example.playlistmaker.R
import com.example.playlistmaker.TrackItemAdapter
import com.example.playlistmaker.domain.PlayerStatus.STATE_PLAYING
import com.example.playlistmaker.domain.api.TrackInteractor
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var albumImage: ImageView
    private lateinit var trackNameText: TextView
    private lateinit var groupNameText: TextView
    private lateinit var durationText: TextView
    private lateinit var collectionText: TextView
    private lateinit var collectionTitle: TextView
    private lateinit var yearText: TextView
    private lateinit var genreText: TextView
    private lateinit var countrtry: TextView
    private lateinit var backButton: ImageView
    private lateinit var playingTime: TextView
    private lateinit var playButton: ImageButton
    private var mediaPlayer = provideMediaPlayerInteractor()
    private lateinit var trackInteractor: TrackInteractor
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var track = TrackItemAdapter.track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackInteractor = Creator.provideTrackInteractor(this)
        setContentView(R.layout.activity_audio_player)
        contentCreation()
        backButtonCreate()

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun contentCreation() {
        playButton = findViewById(R.id.playButton)
        playButton.setOnClickListener {
            when (mediaPlayer.playbackControl()) {
                STATE_PLAYING -> {
                    startTimer()
                    playButton.setImageResource(R.drawable.pause_button)
                }
                else -> {
                    pauseTimer()
                    playButton.setImageResource(R.drawable.play_button)
                }
            }
        }
        preparePlayer()
        trackNameText = findViewById(R.id.track_name)
        trackNameText.text = track?.trackName
        groupNameText = findViewById(R.id.group_name)
        groupNameText.text = track?.artistName
        durationText = findViewById(R.id.durationValue)
        durationText.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track?.trackTimeMillis)
        collectionText = findViewById(R.id.albumNameValue)
        if (track?.collectionName == null) {
            collectionTitle = findViewById(R.id.albumName)
            collectionText.visibility = View.GONE
            collectionTitle.visibility = View.GONE
        } else {
            collectionText.text = track?.collectionName
        }
        yearText = findViewById(R.id.yearValue)
        yearText.text = track?.releaseDate?.subSequence(0, 4)
        genreText = findViewById(R.id.genreValue)
        genreText.text = track?.primaryGenreName
        countrtry = findViewById(R.id.countryValue)
        countrtry.text = track?.country
        albumImage = findViewById(R.id.album_art)
        Glide.with(albumImage)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_placeholder_big)
            .centerInside()
            .into(albumImage)
        playingTime = findViewById(R.id.playingTime)
        playingTime.text = "00:00"
    }

    private fun backButtonCreate() {
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            this.finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        track?.let { trackInteractor.trackPut(it) }
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        track = trackInteractor.trackGet()
    }

    private fun preparePlayer() {
        track?.previewUrl?.let { mediaPlayer.prepare(it, onPrepared(), onCompletion()) }
    }

    private fun onPrepared(): () -> Unit = {
        playButton.isEnabled = true
    }

    private fun onCompletion(): () -> Unit = {
        playButton.setImageResource(R.drawable.play_button)
        playingTime.text = "00:00"
    }


    private fun startTimer() {
        mainThreadHandler?.post(timerRunnable)
    }

    private fun pauseTimer() {
        mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.playerStatus == STATE_PLAYING) {
                val playedTime = mediaPlayer.currentPosition / DELAY
                playingTime?.text =
                    String.format("%d:%02d", playedTime / 60, playedTime % 60)
                mainThreadHandler?.postDelayed(this, DELAY)
            }
        }
    }

    companion object {
        const val TRACK_DATA = "TRACK_DATA"
        private const val DELAY = 1000L

    }
}