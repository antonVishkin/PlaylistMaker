package com.example.playlistmaker.library.ui.playlistdetails

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlistdetails.PlaylistDetailsState
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.ui.TrackItemAdapter
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailsFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlaylistDetailsViewModel
    private lateinit var trackItemAdapter: TrackItemAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onLongClicked: (Track) -> Unit
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<*>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
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
        onLongClicked = {
            showRemoveTrackDialog(it)
        }
        val playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PLAYLIST, Playlist::class.java) as Playlist
        } else {
            arguments?.getParcelable(PLAYLIST)!!
        }
        viewModel = getViewModel {
            parametersOf(playlist)
        }
        trackItemAdapter = TrackItemAdapter(
            onTrackClickDebounce, onLongClicked
        )
        trackItemAdapter.trackItems.clear()
        trackItemAdapter.trackItems.addAll(playlist.list)
        binding.playlistRecycler.adapter = trackItemAdapter
        val bottomSheetContainer = binding.trackListBottomContainer
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val overlay = binding.overlay
        overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        menuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
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

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.playlistDetails.setOnClickListener {
            if (viewModel.observeState().value is PlaylistDetailsState.Content)
                menuBottomSheetContent((viewModel.observeState().value as PlaylistDetailsState.Content).playlist)
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        binding.shareBottom.setOnClickListener {
            sharePlaylist()
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.playlistShare.setOnClickListener {
            sharePlaylist()
        }
        binding.removeBottomPlaylist.setOnClickListener {
            showRemovePlaylistDialog()
        }
        binding.editInformationBottom.setOnClickListener {
            val args = Bundle()
            args.putParcelable(
                PLAYLIST,
                (viewModel.observeState().value as PlaylistDetailsState.Content).playlist
            )
            findNavController().navigate(R.id.action_playlistDetailsFragment_to_playListEditFragment,args)
        }
    }

    private fun sharePlaylist() {
        if (viewModel.observeState().value is PlaylistDetailsState.Content) {
            val playlist = (viewModel.observeState().value as PlaylistDetailsState.Content).playlist
            if (playlist.list.isEmpty())
                Toast.makeText(
                    requireContext(),
                    R.string.no_tracks_in_playlist_share_toast,
                    Toast.LENGTH_SHORT
                ).show()
            else
                viewModel.sharePlaylist()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun showContent(playlist: Playlist) {
        trackItemAdapter.trackItems.clear()
        trackItemAdapter.trackItems.addAll(playlist.list)
        trackItemAdapter.notifyDataSetChanged()
        binding.apply {
            if (!playlist.imagePath.isNullOrEmpty()) {
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

    private fun menuBottomSheetContent(playlist: Playlist) {
        binding.apply {
            if (!playlist.imagePath.isNullOrEmpty())
                playlistBottomImage.setImageURI(Uri.parse(playlist.imagePath))
            playlistBottomName.text = playlist.name
            trackBottomNumber.text = viewModel.makeTrackNumberText(playlist.list.size)

        }
    }

    private fun showRemoveTrackDialog(track: Track) {
        val dialog =
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.AlertDialogTheme
            ).setTitle(R.string.track_removing_dialog_title)
                .setMessage(R.string.track_removing_dialog_message)
                .setNegativeButton(R.string.track_removing_dialog_negative) { dialog, which ->
                    dialog.cancel()
                }.setPositiveButton(R.string.track_removing_dialog_positive) { dialog, which ->
                    viewModel.removeTrack(track)
                }
        dialog.show()
    }

    private fun showRemovePlaylistDialog() {
        val dialog =
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.AlertDialogTheme
            ).setTitle(R.string.dialog_remove_playlist_title)
                .setMessage(R.string.dialog_remove_playlist_message)
                .setNegativeButton(R.string.dialog_remove_playlist_negative_button) { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.dialog_remove_playlist_positive_button) { dialog, which ->
                    viewModel.removePlaylist()
                }
        dialog.show()
    }

    private fun render(state: PlaylistDetailsState) {
        when (state) {
            PlaylistDetailsState.Empty -> findNavController().popBackStack()
            PlaylistDetailsState.Loading -> TODO()
            is PlaylistDetailsState.Content -> showContent(state.playlist)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshPlaylist()
    }


    companion object {
        private const val PLAYLIST = "PLAYLIST"
        private const val TRACK = "TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}