package com.example.android_workings.views.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_workings.R
import com.example.android_workings.data.Status
import com.example.android_workings.data.api.PhotosApi
import com.example.android_workings.data.models.PhotoModel
import com.example.android_workings.data.repositories.PhotosRepository
import com.example.android_workings.databinding.ActivityMainBinding
import com.example.android_workings.services.DownloadForegroundService
import com.example.android_workings.viewmodels.MainActivityViewModel
import com.example.android_workings.viewmodels.MainActivityViewModelFactory
import com.example.android_workings.views.adapters.PhotosRecyclerViewAdapter
import com.example.android_workings.views.adapters.RecyclerItemClickListener
import com.example.android_workings.views.fragments.DetailsFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    lateinit var viewModel: MainActivityViewModel
    var binding: ActivityMainBinding? = null
    var adapter: PhotosRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        adapter = PhotosRecyclerViewAdapter(arrayListOf(), object: RecyclerItemClickListener {
            override fun onClick(photoModel: PhotoModel) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, DetailsFragment.newInstance(photoModel.url, photoModel.title))
                    .addToBackStack(null)
                    .commit()
            }

        })

        binding?.recyclerview?.adapter = adapter
        binding?.recyclerview?.layoutManager = LinearLayoutManager(this)

        binding?.downloadFab?.setOnClickListener {
            DownloadForegroundService.startService(this, "Service started")
        }

        setContentView(binding?.root)
        initViewModel()
        initData()
        initObservers()
    }

    private fun initViewModel() {
        val api = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotosApi::class.java)

        val photosRepository = PhotosRepository(api)
        val viewModelFactory = MainActivityViewModelFactory(photosRepository)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainActivityViewModel::class.java)
    }

    private fun initData() {
        viewModel.getPhotos()
    }

    private fun initObservers() {
        viewModel.photosListLiveData.observe(this, {
            when(it.status) {
                Status.LOADING -> {
                    binding?.circularProgressBar?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    it.data?.let { arrayList ->
                        adapter?.updateList(arrayList)
                        binding?.circularProgressBar?.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "initObservers: " + it.message)
                    binding?.circularProgressBar?.visibility = View.GONE
                }
            }
        })
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            this.finish();
        } else {
            supportFragmentManager.popBackStack();
        }
    }

    // Lifecycle methods

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "onRestart called", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "onStart called", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "onResume called", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "onPause called", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "onStop called", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "onDestroy called", Toast.LENGTH_SHORT).show()
    }
}