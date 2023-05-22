package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var albumImage:ImageView
    private lateinit var trackNameText:TextView
    private lateinit var groupNameText:TextView
    private lateinit var durationText:TextView
    private lateinit var collectionText:TextView
    private lateinit var collectionTitle:TextView
    private lateinit var yearText:TextView
    private lateinit var genreText:TextView
    private lateinit var countrtry:TextView
    private lateinit var backButton:ImageView
    private lateinit var playingTime:TextView

    private var track = TrackItemAdapter.track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        contentCreation()
        backButtonCreate()
    }

    private fun contentCreation() {
        trackNameText = findViewById(R.id.track_name)
        trackNameText.setText(track?.trackName)
        groupNameText = findViewById(R.id.group_name)
        groupNameText.setText(track?.artistName)
        durationText = findViewById(R.id.durationValue)
        durationText.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis))
        collectionText = findViewById(R.id.albumNameValue)
        if (track?.collectionName == null){
            collectionTitle = findViewById(R.id.albumName)
            collectionText.visibility = View.GONE
            collectionTitle.visibility = View.GONE
        } else {
            collectionText.setText(track?.collectionName)
        }
        yearText = findViewById(R.id.yearValue)
        yearText.setText(track?.releaseDate?.subSequence(0,4))
        genreText = findViewById(R.id.genreValue)
        genreText.setText(track?.primaryGenreName)
        countrtry = findViewById(R.id.countryValue)
        countrtry.setText(track?.country)
        albumImage = findViewById(R.id.album_art)
        Glide.with(albumImage)
            .load(track?.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.album_placeholder_big)
            .centerInside()
            .into(albumImage)
        playingTime = findViewById(R.id.playingTime)
        playingTime.setText("00:30")
    }
    private fun backButtonCreate() {
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            this.finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(TRACK_DATA,Gson().toJson(track))
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        val json = savedInstanceState?.getString(TRACK_DATA)
        track = Gson().fromJson(json, Track::class.java)
    }
    companion object{
        const val TRACK_DATA = "TRACK_DATA"
    }
}