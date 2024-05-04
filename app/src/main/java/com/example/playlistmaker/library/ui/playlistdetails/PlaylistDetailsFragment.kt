package com.example.playlistmaker.library.ui.playlistdetails

import android.annotation.SuppressLint
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlistdetails.PlaylistDetailsState
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.ui.SearchFragment
import com.example.playlistmaker.search.ui.TrackItemAdapter
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailsFragment:Fragment() {
    private var _binding:FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel:PlaylistDetailsViewModel
    private lateinit var trackItemAdapter:TrackItemAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<*>



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
        onTrackClickDebounce =
            debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                val args = Bundle()
                args.putParcelable(TRACK, it)
                findNavController().navigate(
                    R.id.action_playlistDetailsFragment_to_audioPlayerFragment,
                    args
                )
            }
        val playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PLAYLIST, Playlist::class.java) as Playlist
        } else {
            arguments?.getParcelable(PLAYLIST)!!
        }
        viewModel = getViewModel {
            parametersOf(playlist)
        }
        trackItemAdapter = TrackItemAdapter {
            onTrackClickDebounce(it)
        }
        trackItemAdapter.trackItems.clear()
        trackItemAdapter.trackItems.addAll(playlist.list)
        binding.playlistRecycler.adapter = trackItemAdapter
        val bottomSheetContainer = binding.trackListBottomContainer
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
                    BottomSheetBehavior.STATE_COLLAPSED -> {
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
        val menuBottomSheetContainer = binding.menuBottomContainer
        menuBottomSheetBehavior = BottomSheetBehavior.from(menuBottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        menuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.playlistDetails.setOnClickListener {
            if (viewModel.observeState().value is PlaylistDetailsState.Content)
                menuBottomSheetContent((viewModel.observeState().value as PlaylistDetailsState.Content).playlist)
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        viewModel.observeState().observe(viewLifecycleOwner){render(it)}
        binding.shareBottom.setOnClickListener {
            sharePlaylist()
        }
        binding.playlistShare.setOnClickListener {
            sharePlaylist()
        }
    }

    private fun sharePlaylist(){
        viewModel.sharePlaylist()
    }

    @SuppressLint("SuspiciousIndentation")
    fun showContent(playlist: Playlist){
        binding.apply {
            if (!playlist.imagePath.isNullOrEmpty()){
                playlistImage.setImageURI(Uri.parse(playlist.imagePath))
                playlistImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            playlistName.text = playlist.name
            if (playlist.description.isNullOrEmpty())
                playlistDescription.visibility = View.GONE
            else {
                playlistDescription.visibility = View.VISIBLE
                playlistDescription.text = playlist.description
            }
            tracksNumber.text = viewModel.makeTrackNumberText(playlist.list.size)
            playlistTimer.text = viewModel.countUniteTime()
        }
    }

    private fun menuBottomSheetContent(playlist: Playlist){
        binding.apply {
            if (!playlist.imagePath.isNullOrEmpty())
                playlistBottomImage.setImageURI(Uri.parse(playlist.imagePath))
            playlistBottomName.text = playlist.name
            trackBottomNumber.text = viewModel.makeTrackNumberText(playlist.list.size)

        }
    }

    private fun render(state: PlaylistDetailsState){
        when(state){
            PlaylistDetailsState.Empty -> TODO()
            PlaylistDetailsState.Loading -> TODO()
            is PlaylistDetailsState.Content -> showContent(state.playlist)
        }
    }

companion object{
    private const val PLAYLIST = "PLAYLIST"
    private const val TRACK = "TRACK"
    private const val CLICK_DEBOUNCE_DELAY = 1000L
}
}