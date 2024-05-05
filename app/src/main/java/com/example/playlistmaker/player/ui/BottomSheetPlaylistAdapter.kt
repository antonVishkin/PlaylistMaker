package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.playlist.Playlist

class BottomSheetPlaylistAdapter(private val onPlaylistClicked: (Playlist) -> Unit) :
    RecyclerView.Adapter<BottomSheetPlaylistItemViewHolder>() {
    val playListsList = arrayListOf<Playlist>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bottomsheet_playlist_item, parent, false)
        return BottomSheetPlaylistItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playListsList.size
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistItemViewHolder, position: Int) {
        holder.bind(playListsList[position])
        holder.itemView.setOnClickListener {
            val playlist = playListsList[position]
            onPlaylistClicked.invoke(playlist)
        }
    }
}