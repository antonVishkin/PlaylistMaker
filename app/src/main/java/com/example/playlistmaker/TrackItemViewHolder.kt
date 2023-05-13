package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName = itemView.findViewById<TextView>(R.id.track_name)
    private val groupName = itemView.findViewById<TextView>(R.id.group_name)
    private val albumArt = itemView.findViewById<ImageView>(R.id.album_art)
    private val trackTime = itemView.findViewById<TextView>(R.id.track_time)


    fun bind(track: Track) {
        trackName.text = track.trackName
        groupName.text = track.artistName
        trackTime.text = track.trackTime
        Glide.with(itemView)
            .load(track.artworkUrl)
            .centerInside()
            .into(albumArt)
    }
}