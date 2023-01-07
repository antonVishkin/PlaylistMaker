package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_button)
        val libraryButton = findViewById<Button>(R.id.library_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)
        //unanimous class realise
        val searchOnClickListener:View.OnClickListener = object :View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(
                    this@MainActivity,
                    "Hey this is search!",
                    Toast.LENGTH_LONG).show()
            }
        }
        searchButton.setOnClickListener(searchOnClickListener)
        //lambda magic
        libraryButton.setOnClickListener{
            Toast.makeText(
                this@MainActivity,
                "hey this is library",
                Toast.LENGTH_LONG).show()
        }
        settingsButton.setOnClickListener{
            Toast.makeText(
                this@MainActivity,
                "hey this is settings",
                Toast.LENGTH_LONG).show()
        }
    }
}