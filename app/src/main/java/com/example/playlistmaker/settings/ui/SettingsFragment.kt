package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var backButton: ImageView
    private lateinit var sharingButton: FrameLayout
    private lateinit var writeToHelpDeskButton: FrameLayout
    private lateinit var licenseAgreementButton: FrameLayout
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButtonCreate()
        sharingButtonCreate()
        helpDeskButtonCreate()
        licenseButtonCreate()
        themeSwitcherCreate()
    }

    private fun backButtonCreate() {
        backButton = binding.backButton
    }

    private fun sharingButtonCreate() {
        sharingButton = binding.sharingButton
        sharingButton.setOnClickListener {
            viewModel.shareLink()
        }
    }

    private fun helpDeskButtonCreate() {
        writeToHelpDeskButton = binding.writeHelpDesk
        writeToHelpDeskButton.setOnClickListener {
            viewModel.createMailToSupport()
        }
    }

    private fun licenseButtonCreate() {
        licenseAgreementButton = binding.licenseAgreement
        licenseAgreementButton.setOnClickListener {
            viewModel.openLicense()
        }
    }

    private fun themeSwitcherCreate() {
        val themeSwitcher = binding.themeSwitcher
        themeSwitcher.isChecked = viewModel.observeDarkTheme().value ?: false
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }

}