package com.example.android_workings.data.repositories

import com.example.android_workings.data.api.PhotosApi
import com.example.android_workings.data.models.PhotoModel

class PhotosRepository(val api: PhotosApi) {
    suspend fun getPhotos(): ArrayList<PhotoModel> = api.getPhotos()
}