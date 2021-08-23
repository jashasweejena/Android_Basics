package com.example.android_workings.data.api

import com.example.android_workings.data.models.PhotoModel
import retrofit2.http.GET

interface PhotosApi {

    @GET("/photos")
    suspend fun getPhotos() : ArrayList<PhotoModel>
}