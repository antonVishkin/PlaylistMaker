package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.models.PlayerState
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment:Fragment() {
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(TRACK, Track::class.java) as Track
        } else {
            arguments?.getParcelable(TRACK)!!
        }
        viewModel = getViewModel() {
            parametersOf(track)
        }
        viewModel.preparePlayer()
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.playButton.setOnClickListener {
            viewModel.playClick()
        }
        viewModel.observePlayerState().observe(viewLifecycleOwner) { render(it) }
        viewModel.observeTimer().observe(viewLifecycleOwner) { changeTimer(it) }
        viewModel.observePlayerState().value?.let { render(it) }
        viewModel.observeIsFavorite().observe(viewLifecycleOwner) { changeFavorite(it) }
        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
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
            binding.albumNameValue.text = track.collectionName
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

    override fun onPause() {
        super.onPause()
        viewModel.stopPlaying()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    }
}