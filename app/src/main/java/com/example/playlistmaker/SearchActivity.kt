package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.iTunesApi.ITunesApi
import com.example.playlistmaker.iTunesApi.SearchResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var searchClearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var trackItemsRecyclerView: RecyclerView
    private lateinit var noSearchResult: LinearLayout
    private lateinit var noInternet: LinearLayout
    private lateinit var refreshButton: Button

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
        GsonConverterFactory.create()
    ).build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val trackProvider = arrayListOf<Track>()
    private val trackItemAdapter = TrackItemAdapter(trackProvider)
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

    private fun search(searchText: String) {
        iTunesService.search(searchText).enqueue(object : Callback<SearchResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        noInternet.visibility = View.INVISIBLE
                        if (response.body()?.results!!.isNotEmpty()) {
                            trackProvider.clear()
                            trackProvider.addAll(response.body()?.results!!)
                            noSearchResult.visibility = View.INVISIBLE
                            trackItemsRecyclerView.visibility = View.VISIBLE
                            trackItemAdapter.notifyDataSetChanged()
                        } else {
                            trackProvider.clear()
                            trackItemAdapter.notifyDataSetChanged()
                            trackItemsRecyclerView.visibility = View.INVISIBLE
                            noSearchResult.visibility = View.VISIBLE
                        }
                    }
                    else -> {
                        trackItemsRecyclerView.visibility = View.INVISIBLE
                        noSearchResult.visibility = View.INVISIBLE
                        noInternet.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                trackItemsRecyclerView.visibility = View.INVISIBLE
                noSearchResult.visibility = View.INVISIBLE
                noInternet.visibility = View.VISIBLE
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        backButtonCreate()
        searchEditTextCreate()
        trackItemsRecyclerView = findViewById(R.id.trackList)
        trackItemsRecyclerView.adapter = trackItemAdapter
        noSearchResult = findViewById(R.id.no_search_result)
        noInternet = findViewById(R.id.no_internet)
        refreshButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener { search(searchEditText.text.toString()) }
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
            searchEditText.clearFocus()
            trackProvider.clear()
            trackItemAdapter.notifyDataSetChanged()
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
        searchEditText.requestFocus()
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(searchEditText.text.toString())
                true
            }
            false
        }
    }

    private fun visibilityClearButton(s: CharSequence?) {
        if (s.isNullOrEmpty())
            searchClearButton.visibility = View.INVISIBLE
        else
            searchClearButton.visibility = View.VISIBLE
        searchText = s.toString()
    }

    companion object {
        private const val SEARCH_VALUE = "SEARCH_VALUE"
        private var searchText: String? = null
    }
}