package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.models.PlayerState
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var viewModel: AudioPlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java) as Track
        } else {
            intent.getParcelableExtra(TRACK)!!
        }
        viewModel = getViewModel() {
            parametersOf(track)
        }
        viewModel.preparePlayer()
        binding.backButton.setOnClickListener {
            this.finish()
        }
        binding.playButton.setOnClickListener {
            viewModel.playClick()
        }
        viewModel.observePlayerState().observe(this) { render(it) }
        viewModel.observeTimer().observe(this) { changeTimer(it) }
        viewModel.observePlayerState().value?.let { render(it) }
        changeFavorite(track.isFavorite)
        viewModel.observeIsFavorite().observe(this) { changeFavorite(it) }
        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPlaying()
    }

    private fun changeFavorite(isFavorite: Boolean) {
        if (isFavorite)
            binding.likeButton.setImageResource(R.drawable.like_button_pressed)
        else
            binding.likeButton.setImageResource(R.drawable.like_button)
    }

    private fun showPause() {
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun showPlaying() {
        binding.playButton.setImageResource(R.drawable.pause_button)
    }

    private fun changeTimer(time: String) {
        binding.playingTime?.text = time
    }

    private fun showContent(track: Track) {
        binding.trackName.text = track.trackName
        binding.groupName.text = track.artistName
        binding.durationValue.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis)
        if (track.collectionName == null) {
            binding.albumName.visibility = View.GONE
            binding.albumNameValue.visibility = View.GONE
        } else {
            binding.albumName.text = track.collectionName
        }
        binding.yearValue.text = track.releaseDate.subSequence(0, 4)
        binding.genreValue.text = track.primaryGenreName
        binding.countryValue.text = track.country
        Glide.with(binding.albumArt)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_placeholder_big)
            .centerInside()
            .into(binding.albumArt)
    }

    private fun render(state: PlayerState) {
        when (state) {
            PlayerState.Pause -> showPause()
            PlayerState.Playing -> showPlaying()
            is PlayerState.Prepared -> showContent(state.track)
        }
    }

    companion object {
        private const val TRACK = "TRACK"
        fun createArgs(track: Track): Bundle = bundleOf(
            TRACK to track
        )
    }
}