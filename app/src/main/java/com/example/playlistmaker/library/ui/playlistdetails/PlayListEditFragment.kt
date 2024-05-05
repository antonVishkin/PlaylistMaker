package com.example.playlistmaker.library.ui.playlistdetails

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.ui.playlist.PlayListCreationFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class PlayListEditFragment : PlayListCreationFragment() {
    private var imageUri: Uri? = null
    override lateinit var viewModel: PlayListEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
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
        viewModel.observePlayList().observe(viewLifecycleOwner) {
            showContent(it)
        }
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.playlistImage.setImageURI(uri)
                    imageUri = uri
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.imageFrame.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.createButton.setOnClickListener {
            viewModel.createPlaylist(
                binding.nameEditText.text.toString(),
                binding.descriptionEditText.text.toString(),
                imageUri
            )
            findNavController().popBackStack()
        }
        showContent(playlist)
    }

    private fun showContent(playlist: Playlist) {
        binding.apply {
            if (!playlist.imagePath.isNullOrEmpty()){
                playlistImage.setImageURI(Uri.parse(playlist.imagePath))
            }
            nameEditText.setText(playlist.name)
            descriptionEditText.setText(playlist.description)
            createButton.setText(R.string.playlist_edit_save_button)
        }
    }


    companion object {
        private const val PLAYLIST = "PLAYLIST"
    }
}