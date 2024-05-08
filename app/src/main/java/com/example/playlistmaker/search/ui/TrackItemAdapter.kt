package com.example.playlistmaker.search.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.Track

class TrackItemAdapter(
    private val onTrackClicked: (Track) -> Unit,private val onLongClicked:(Track)->Unit
) : Adapter<TrackItemViewHolder>() {
    val trackItems = arrayListOf<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item_view, parent, false)
        return TrackItemViewHolder(view)
    }

    override fun getItemCount() = trackItems.size


    override fun onBindViewHolder(holder: TrackItemViewHolder, position: Int) {
        holder.bind(trackItems[position])
        holder.itemView.setOnClickListener {
            track = trackItems[position]
            onTrackClicked.invoke(track!!)
        }
        holder.itemView.setOnLongClickListener {
            track = trackItems[position]
            onLongClicked.invoke(track!!)
            return@setOnLongClickListener true
        }
    }

    companion object {
        var track: Track? = null
    }
}