package com.sweetpro.handlemultiplepermissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.sweetpro.handlemultiplepermissions.databinding.ActivityMainBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    // for view binding
    private lateinit var binding: ActivityMainBinding

    // list of runtime permissions
    private val neededRuntimePermissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_CONTACTS
    )

    // for register contract API to request runtime permissions
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d(TAG, ": registerForActivityResult: ${it.key}=${it.value}")

                // if any permission is not granted...
                if (! it.value) {
                    // do anything if needed: ex) display about limitation
                    Snackbar.make(binding.root, R.string.permissions_request, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UI: launch contract for runtime permissions
        binding.button.setOnClickListener {
            // working mechanism of Google's API for requesting runtime permission:
            //  1st launching: request permissions
            //  2nd launching: 'one more' request permissions which is not granted
            //  No re-request after that.
            resultLauncher.launch(neededRuntimePermissions)
        }
    }
}