package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.player.data.track.Track
import com.example.playlistmaker.player.ui.models.PlayerState
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
    private lateinit var country: TextView
    private lateinit var backButton: ImageView
    private lateinit var playingTime: TextView
    private lateinit var playButton: ImageButton
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        viewModel = ViewModelProvider(this,AudioPlayerViewModel.getViewModelFactory())[AudioPlayerViewModel::class.java]
        playButton = findViewById(R.id.playButton)
        trackNameText = findViewById(R.id.track_name)
        groupNameText = findViewById(R.id.group_name)
        durationText = findViewById(R.id.durationValue)
        collectionText = findViewById(R.id.albumNameValue)
        yearText = findViewById(R.id.yearValue)
        genreText = findViewById(R.id.genreValue)
        country = findViewById(R.id.countryValue)
        albumImage = findViewById(R.id.album_art)
        playingTime = findViewById(R.id.playingTime)
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            this.finish()
        }
        playButton.setOnClickListener {
            viewModel.playClick()
        }
        viewModel.observePlayerState().observe(this){render(it)}
        viewModel.observeTimer().observe(this){changeTimer(it)}
        viewModel.observePlayerState().value?.let { render(it) }
    }

    override fun onPause() {
        super.onPause()
        showPause()
    }


    private fun showPause(){
        playButton.setImageResource(R.drawable.play_button)
    }

    private fun showPlaying(){
        playButton.setImageResource(R.drawable.pause_button)
    }

    private fun changeTimer(time:String){
        Log.v("timer",time)
        playingTime?.text = time
    }

    private fun showContent(track: Track){
        trackNameText.text = track.trackName
        groupNameText.text = track.artistName
        durationText.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis)
        if (track.collectionName == null) {
            collectionTitle = findViewById(R.id.albumName)
            collectionText.visibility = View.GONE
            collectionTitle.visibility = View.GONE
        } else {
            collectionText.text = track.collectionName
        }
        yearText.text = track.releaseDate.subSequence(0, 4)
        genreText.text = track.primaryGenreName
        country.text = track.country
        Glide.with(albumImage)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_placeholder_big)
            .centerInside()
            .into(albumImage)
    }

    private fun render(state: PlayerState){
        when (state){
            PlayerState.Pause -> showPause()
            PlayerState.Playing -> showPlaying()
            is PlayerState.Prepared -> showContent(state.track)
        }
    }
}