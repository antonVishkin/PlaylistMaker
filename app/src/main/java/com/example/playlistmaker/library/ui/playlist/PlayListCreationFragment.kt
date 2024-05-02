package com.example.playlistmaker.library.ui.playlist

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListCreationFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistCreationBinding
    private val viewModel: PlayListCreationViewModel by viewModel()
    private var imageUri: Uri? = null
    private val nameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?, start: Int, count: Int, after: Int
        ) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.createButton.isEnabled = !s.isNullOrEmpty()
        }

        override fun afterTextChanged(s: Editable?) {
            //empty
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            showDialog()
        }
        binding.createButton.isEnabled = false
        binding.nameEditText.addTextChangedListener(
            nameTextWatcher
        )
        binding.createButton.setOnClickListener {
            viewModel.createPlaylist(
                binding.nameEditText.text.toString(),
                binding.descriptionEditText.text.toString(),
                imageUri
            )
            showToast(binding.nameEditText.text.toString())
            findNavController().popBackStack()
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
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showDialog()
                }

            }
        )
    }

    private fun showDialog() {
        val dialog =
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.AlertDialogTheme
            ).setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNegativeButton("Отмена") { dialog, which ->
                    dialog.cancel()
                }.setPositiveButton("Завершить") { dialog, which ->
                    findNavController().popBackStack()
                }
        if (!canClose()) dialog.show()
        else findNavController().popBackStack()
    }

    private fun canClose(): Boolean {
        return binding.nameEditText.text.isNullOrEmpty()
                && binding.descriptionEditText.text.isNullOrEmpty()
                && (imageUri == null || imageUri?.toString().isNullOrEmpty())
    }

    private fun showToast(playlistName: String) {
        Toast.makeText(
            requireContext(),
            "Плейлист $playlistName успешно создан",
            Toast.LENGTH_SHORT
        ).show()
    }

}