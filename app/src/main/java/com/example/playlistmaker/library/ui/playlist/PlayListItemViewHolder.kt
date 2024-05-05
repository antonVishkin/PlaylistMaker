package com.example.playlistmaker.library.ui.playlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.playlist.Playlist
import java.io.File


class PlayListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.findViewById<ImageView>(R.id.playlist_image)
    private val name = itemView.findViewById<TextView>(R.id.playlist_name)
    private val trackNumber = itemView.findViewById<TextView>(R.id.track_number)

    fun bind(playlist: Playlist) {
        name.text = playlist.name
        trackNumber.text = tracksText(playlist.list.size)
        if (playlist.imagePath?.isNotEmpty() == true) {
            Glide.with(itemView.context)
                .load(File(playlist.imagePath))
                .placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder)
                .fitCenter()
                .transform(RoundedCorners(30))
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.album_placeholder)
        }
    }

    private fun tracksText(n: Int): String {
        return when {
            n % 10 == 1 && n % 100 != 11 -> "$n трек"
            n % 10 in 2..4 && n % 100 !in 12..14 -> "$n трека"
            else -> "$n треков"
        }
    }
}