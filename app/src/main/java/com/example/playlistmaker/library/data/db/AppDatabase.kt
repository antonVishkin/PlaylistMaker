package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.dao.PlayListTrackDao
import com.example.playlistmaker.library.data.db.dao.PlayListsDao
import com.example.playlistmaker.library.data.db.dao.TrackDao

@Database(
    version = 1,
    entities = [TrackEntity::class, PlayListsEntity::class, TrackToPlaylistEntity::class, PlaylistsTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playListsDao(): PlayListsDao

    abstract fun playListTrackDao(): PlayListTrackDao

}
