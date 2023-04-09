package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var searchClearButton: ImageView
    private lateinit var searchEditText: EditText
    private val simpleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            visibilityClearButton(s)
        }

        override fun afterTextChanged(s: Editable?) {
            //empty
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        backButtonCreate()
        searchEditTextCreate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_VALUE)
        searchEditText.setText(searchText)
    }

    private fun backButtonCreate() {
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            this.finish()
        }
    }

    private fun searchEditTextCreate() {
        searchClearButton = findViewById(R.id.search_clear)
        searchEditText = findViewById(R.id.search_form)
        searchClearButton.setOnClickListener {
            searchEditText.setText("")
            it.hideKeyboard()
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
        searchEditText.requestFocus()
    }

    private fun visibilityClearButton(s: CharSequence?) {
        if (s.isNullOrEmpty())
            searchClearButton.visibility = View.INVISIBLE
        else
            searchClearButton.visibility = View.VISIBLE
        searchText = s.toString()
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        var searchText: String? = null
    }
}