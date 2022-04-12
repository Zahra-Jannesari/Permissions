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
    private lateinit var requestPermissionLauncher :ActivityResultLauncher<String>
    var isLocationPermissionGranted=false
    var isCameraPermissionGranted=false
    var isStoragePermissionGranted=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.buttonAccess.setOnClickListener { requestPermissions() }
    }
    private fun continueActions() {
        Toast.makeText(
            this,
            "continueActions",
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                //if user already granted the permission
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        this,
                        "you have already granted this permission",
                        Toast.LENGTH_SHORT
                    ).show()
                    continueActions()
                }
                //if user already denied the permission once
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                ) -> {
                    //you can show rational massage in any form you want
                    showRationDialog()
//                    Snackbar.make(
//                        binding.buttonCamera,
//                        getString(R.string.permission_required),
//                        Snackbar.LENGTH_LONG
//                    ).show()
                }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA,
                    )
                }
            }
        }
    }
    private fun showRationDialog() {
        val builder= AlertDialog.Builder(this)
        builder.apply {
            setMessage(R.string.permission_required)
            setTitle("permission required")
            setPositiveButton("ok"){dialog,which->
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA,
                )
            }
        }
        builder.create().show()
    }
}

