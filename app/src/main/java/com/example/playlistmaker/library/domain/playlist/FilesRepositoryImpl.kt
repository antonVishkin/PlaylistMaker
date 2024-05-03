package com.example.playlistmaker.library.domain.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class FilesRepositoryImpl(private val context: Context):FilesRepository {
    override suspend fun addFileToPrivateStorage(uri: Uri): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists images")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${System.currentTimeMillis()}.jpg")
        val inputStream = context.contentResolver?.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.absolutePath
    }
}