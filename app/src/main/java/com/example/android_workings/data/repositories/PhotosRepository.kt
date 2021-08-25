package com.example.android_workings.data.repositories

import com.example.android_workings.data.api.PhotosApi
import com.example.android_workings.data.models.PhotoModel
import com.example.android_workings.database.AppDatabase
import com.example.android_workings.database.PhotoEntity

class PhotosRepository(val api: PhotosApi, val appDatabase: AppDatabase) {
    suspend fun getPhotos(): ArrayList<PhotoModel> {
        // Query data from DB. If data exists in DB, return the data,
        // else insert data from API into DB

        val data = appDatabase.photoDao().getAllPhotos()
        val dataList = arrayListOf<PhotoModel>()
        if (data.isNotEmpty()) {
            val list =  data.map {
                PhotoModel(
                   it.albumId,
                   it.id,
                   it.thumbnailUrl,
                   it.title,
                   it.url
                )
            }
            dataList.addAll(list)
        }
        else {
            // Insert API list into DB
            val apiData = api.getPhotos()
            dataList.addAll(apiData)
            val photoEntityList = apiData.map {
                 PhotoEntity(
                    it.id,
                    it.albumId,
                    it.thumbnailUrl,
                    it.title,
                    it.url
                )
            }
            photoEntityList.forEach {
                appDatabase.photoDao().insertAllPhotos(it)
            }
        }

        return dataList
    }
}