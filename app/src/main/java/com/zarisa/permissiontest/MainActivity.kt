package com.zarisa.permissiontest


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zarisa.permissiontest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val READ_EXTERNAL_STORAGE_RQ = 1
    val CAMERA_RQ = 2
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.buttonCamera.setOnClickListener {
            requestPermissions(
                Manifest.permission.CAMERA,
                "camera",
                CAMERA_RQ
            )
        }
        binding.buttonMedia.setOnClickListener {
            requestPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                "media",
                READ_EXTERNAL_STORAGE_RQ
            )
        }
    }

    private fun continueActions() {
        Toast.makeText(
            this,
            "continueActions",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun requestPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        this,
                        "you have already granted $name permission",
                        Toast.LENGTH_SHORT
                    ).show()
                    continueActions()
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permission
                ) -> {
                    showRationDialog(permission, name, requestCode)
                }
                else -> {
                    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                }
            }
        }
    }

    private fun showRationDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage(R.string.permission_required)
            setTitle("permission required")
            setPositiveButton("ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    "you have denied $name permission",
                    Toast.LENGTH_SHORT
                ).show()
            } else{
                Toast.makeText(
                    this,
                    "you have granted $name permission",
                    Toast.LENGTH_SHORT
                ).show()
                continueActions()
            }
        }
        when (requestCode) {
            CAMERA_RQ -> innerCheck("camera")
            READ_EXTERNAL_STORAGE_RQ -> innerCheck("media")
        }

    }
}

