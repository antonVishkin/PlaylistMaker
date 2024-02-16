package com.example.playlistmaker.player.ui.models

import com.example.playlistmaker.player.data.track.Track
sealed interface PlayerState{
    object Playing:PlayerState

    object Pause:PlayerState
    data class Prepared(val track: Track):PlayerState


}
