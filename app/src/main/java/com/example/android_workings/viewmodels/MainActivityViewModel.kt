package com.example.android_workings.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_workings.data.Resource
import com.example.android_workings.data.models.PhotoModel
import com.example.android_workings.data.repositories.PhotosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivityViewModel(val repository: PhotosRepository) : ViewModel() {
    private val _photosListLiveData: MutableLiveData<Resource<ArrayList<PhotoModel>>> = MutableLiveData()
    val photosListLiveData: LiveData<Resource<ArrayList<PhotoModel>>> = _photosListLiveData;

    // Fetches List<PhotoModel> from repository
    fun getPhotos() {
        viewModelScope.launch(Dispatchers.Main) {
            _photosListLiveData.postValue(Resource.loading(null))

            try {
                withContext(Dispatchers.IO) {
                    val photosList = repository.getPhotos()
                    withContext(Dispatchers.Main) {
                        if (photosList.isNotEmpty()) {
                            _photosListLiveData.postValue(Resource.success(photosList))
                        }
                        else {
                            _photosListLiveData.postValue(Resource.error("Photos List Empty", null))
                        }
                    }
                }
            }

            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _photosListLiveData.postValue(Resource.error(e.message!!, null))
                }
            }
        }
    }
}