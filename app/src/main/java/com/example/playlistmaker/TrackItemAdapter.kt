package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

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
            val playerIntent = Intent(it.context,AudioPlayerActivity::class.java)
            it.context.startActivity(playerIntent)
        }
    }
    companion object{
        var track: Track? = null
    }
}