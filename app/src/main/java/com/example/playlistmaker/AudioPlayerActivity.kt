package com.example.playlistmaker

import android.media.MediaPlayer
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
import com.google.gson.Gson
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
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var track = TrackItemAdapter.track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        contentCreation()
        backButtonCreate()

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun contentCreation() {
        playButton = findViewById(R.id.playButton)
        playButton.setOnClickListener {
            playbackControl()
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
        outState.putString(TRACK_DATA, Gson().toJson(track))
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        val json = savedInstanceState?.getString(TRACK_DATA)
        track = Gson().fromJson(json, Track::class.java)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_button)
            playingTime.text = "00:00"
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        startTimer()
        playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        pauseTimer()
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        mainThreadHandler?.post(timerRunnable)
    }

    private fun pauseTimer() {
        mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                val playedTime = mediaPlayer.currentPosition / DELAY
                playingTime?.text =
                    String.format("%d:%02d", playedTime / 60, playedTime % 60)
                mainThreadHandler?.postDelayed(this, DELAY)
            }
        }
    }

    companion object {
        const val TRACK_DATA = "TRACK_DATA"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L

    }
}