package com.example.kotlin_project.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageUtil{


    companion object {

        suspend fun uploadToStorage(uri: Uri, context: Context, type: String): String? {
            val storage = Firebase.storage

            // Create a storage reference from our app
            val storageRef = storage.reference

            val uniqueImageName = UUID.randomUUID()
            val spaceRef: StorageReference = if (type == "image") {
                storageRef.child("images/$uniqueImageName.jpg")
            } else {
                storageRef.child("videos/$uniqueImageName.mp4")
            }

            val byteArray: ByteArray? = context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }

            return if (byteArray != null) {
                try {
                    spaceRef.putBytes(byteArray).await() // Await the upload task
                    val downloadUrl = spaceRef.downloadUrl.await() // Await the download URL
                    //Toast.makeText(context, "Upload succeeded", Toast.LENGTH_SHORT).show()
                    downloadUrl.toString() // Return the download URL as a String
                } catch (e: Exception) {
                    Toast.makeText(context, "Upload to DB failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    null // Return null if there was an error
                }
            } else {
                Toast.makeText(context, "Failed to read file", Toast.LENGTH_SHORT).show()
                null // Return null if byteArray is null
            }
        }

    }
}