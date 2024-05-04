package com.example.playlistmaker.library.ui.playlistdetails

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.ui.TrackItemAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailsFragment:Fragment() {
    private var _binding:FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel:PlaylistDetailsViewModel
    private lateinit var trackItemAdapter:TrackItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PLAYLIST, Playlist::class.java) as Playlist
        } else {
            arguments?.getParcelable(PLAYLIST)!!
        }
        viewModel = getViewModel {
            parametersOf(playlist)
        }
        trackItemAdapter = TrackItemAdapter {
            //TODO: onclicked
        }
        trackItemAdapter.trackItems.clear()
        trackItemAdapter.trackItems.addAll(playlist.list)
        binding.playlistRecycler.adapter = trackItemAdapter
        val bottomSheetContainer = binding.bottomContainer
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val overlay = binding.overlay
        overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN,BottomSheetBehavior.STATE_COLLAPSED -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

    }

companion object{
    private const val PLAYLIST = "PLAYLIST"
}
}