package com.zarisa.permissiontest


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zarisa.permissiontest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher :ActivityResultLauncher<Array<String>>
    var isLocationPermissionGranted=false
    var isCameraPermissionGranted=false
    var isStoragePermissionGranted=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissionLauncher =registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
            isLocationPermissionGranted=permissions[Manifest.permission.ACCESS_FINE_LOCATION]?:isLocationPermissionGranted
            isCameraPermissionGranted=permissions[Manifest.permission.CAMERA]?:isCameraPermissionGranted
            isStoragePermissionGranted=permissions[Manifest.permission.READ_EXTERNAL_STORAGE]?:isStoragePermissionGranted
        }
        initViews()
    }

    private fun initViews() {
        binding.buttonAccess.setOnClickListener { requestPermissions() }
    }
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //if user already granted any of the permission
            isLocationPermissionGranted=ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            isCameraPermissionGranted=ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            isStoragePermissionGranted=ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED


            val permissionsNeeded= mutableListOf<String>()
            if(!isCameraPermissionGranted)
                permissionsNeeded.add(Manifest.permission.CAMERA)
            if(!isLocationPermissionGranted)
                permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            if(!isStoragePermissionGranted)
                permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)

            if(permissionsNeeded.isNotEmpty()){
                requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
            }
        }
    }
}

