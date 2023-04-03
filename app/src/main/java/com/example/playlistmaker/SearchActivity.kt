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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }
        val searchClearButton = findViewById<ImageView>(R.id.search_clear)
        val searchEditText = findViewById<EditText>(R.id.search_form)
        val simpleTextWatcher = object : TextWatcher {
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
        searchClearButton.setOnClickListener { searchEditText.setText("") }
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }
}