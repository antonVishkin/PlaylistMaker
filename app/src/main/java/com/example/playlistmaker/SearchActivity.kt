package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var searchClearButton: ImageView
    private lateinit var searchEditText: EditText
    private val simpleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty())
                searchClearButton.visibility = View.INVISIBLE
            else
                searchClearButton.visibility = View.VISIBLE
        }

        override fun afterTextChanged(s: Editable?) {
            //empty
        }
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        backButtonCreate()
        searchEditTextCreate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditText.setText(savedInstanceState.getString(SEARCH_VALUE))
    }

    private fun backButtonCreate() {
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }
    }

    private fun searchEditTextCreate() {
        searchClearButton = findViewById(R.id.search_clear)
        searchEditText = findViewById(R.id.search_form)
        searchClearButton.setOnClickListener { searchEditText.setText("") }
        searchEditText.addTextChangedListener(simpleTextWatcher)
        searchEditText.requestFocus()
    }
}