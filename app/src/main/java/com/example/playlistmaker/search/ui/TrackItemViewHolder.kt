package com.example.playlistmaker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName = itemView.findViewById<TextView>(R.id.track_name)
    private val groupName = itemView.findViewById<TextView>(R.id.group_name)
    private val albumArt = itemView.findViewById<ImageView>(R.id.album_art)
    private val trackTime = itemView.findViewById<TextView>(R.id.track_time)


    fun bind(track: Track) {
        trackName.text = track.trackName
        groupName.text = track.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.album_placeholder)
            .centerInside()
            .into(albumArt)
    }
}