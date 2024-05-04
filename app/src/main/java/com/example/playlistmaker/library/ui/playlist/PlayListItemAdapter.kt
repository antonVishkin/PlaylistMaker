package com.example.playlistmaker.library.ui.playlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.playlist.Playlist


class PlayListItemAdapter(private val onItemClicked: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlayListItemViewHolder>() {
    val playListsList = arrayListOf<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlayListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playListsList.size
    }

    override fun onBindViewHolder(holder: PlayListItemViewHolder, position: Int) {
        holder.bind(playListsList[position])
        holder.itemView.setOnClickListener {
            Log.v("PLAYLIST","clicked to playlist $position")
            val playlist = playListsList[position]
            onItemClicked.invoke(playlist)
        }
    }
}