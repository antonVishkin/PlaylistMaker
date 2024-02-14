package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Track

class TrackItemAdapter(
    private val trackItems: List<Track>,
    private val onTrackClicked: (Track) -> Unit
) : Adapter<TrackItemViewHolder>() {
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
    }

    companion object {
        var track: Track? = null
    }
}