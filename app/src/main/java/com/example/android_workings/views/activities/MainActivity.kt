package com.example.android_workings.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.android_workings.database.AppDatabase
import com.example.android_workings.database.getDatabase


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
        setContentView(binding?.root)

        // Initialize RecyclerView and Adapter
        adapter = PhotosRecyclerViewAdapter(arrayListOf(), object : RecyclerItemClickListener {
            override fun onClick(photoModel: PhotoModel) {
                // Open DetailsFragment on item click
                supportFragmentManager.beginTransaction()
                    .replace(
                        android.R.id.content,
                        DetailsFragment.newInstance(photoModel.url, photoModel.title)
                    )
                    .addToBackStack(null)
                    .commit()
            }
        })

        binding?.recyclerview?.adapter = adapter
        binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding?.recyclerview?.context,
            (binding?.recyclerview?.layoutManager as LinearLayoutManager).orientation
        )
        binding?.recyclerview?.addItemDecoration(dividerItemDecoration)

        // Start downloading of file using DownloadForegroundService
        binding?.downloadFab?.setOnClickListener {
            DownloadForegroundService.startService(this, "Service started")
        }

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

        val appDatabase = getDatabase(applicationContext)
        val photosRepository = PhotosRepository(api, appDatabase)
        val viewModelFactory = MainActivityViewModelFactory(photosRepository)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainActivityViewModel::class.java)
    }

    private fun initData() {
        viewModel.getPhotos()
    }

    private fun initObservers() {
        // Observe status of photosListLiveData
        viewModel.photosListLiveData.observe(this, {
            when (it.status) {
                // Show a circular progress bar since it is loading
                Status.LOADING -> {
                    binding?.circularProgressBar?.visibility = View.VISIBLE
                }

                // Hide progressbar and update item in RecyclerView adapter
                Status.SUCCESS -> {
                    it.data?.let { arrayList ->
                        adapter?.updateList(arrayList)
                        binding?.circularProgressBar?.visibility = View.GONE
                    }
                }

                // Show a toast in case of error
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "initObservers: " + it.message)
                    binding?.circularProgressBar?.visibility = View.GONE
                }
            }
        })
    }

    // Handles fragment backstack popping
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