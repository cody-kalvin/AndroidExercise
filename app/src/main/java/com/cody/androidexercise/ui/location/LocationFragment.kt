package com.cody.androidexercise.ui.location

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cody.androidexercise.R
import com.cody.androidexercise.databinding.FragmentLocationBinding
import com.cody.androidexercise.service.ForegroundLocationService
import com.cody.androidexercise.util.SharedPrefUtil
import com.google.android.material.snackbar.Snackbar

private const val FOREGROUND_LOCATION_PERMISSION_REQUEST_CODE = 11

class LocationFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var viewBinding: FragmentLocationBinding

    private lateinit var viewModel: LocationViewModel

    private lateinit var locationService: ForegroundLocationService

    private lateinit var broadCastReceiver: ForegroundLocationBroadcastReceiver

    private inner class ForegroundLocationBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                ForegroundLocationService.EXTRA_LOCATION
            )
            viewModel.setCurrentLocation(location)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLocationBinding.inflate(inflater, container, false)
        viewBinding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        viewBinding.viewModel = viewModel

        broadCastReceiver = ForegroundLocationBroadcastReceiver()

        setupObservers()
        setupInteractions()

        return viewBinding.root
    }

    override fun onStart() {
        super.onStart()
        registerSharedPreferencesListener()
    }

    override fun onResume() {
        super.onResume()
        checkLocationService()
        checkNeededPermission()
        registerReceiver()
        bindService()
    }

    override fun onPause() {
        unregisterReceiver()
        super.onPause()
    }

    override fun onStop() {
        unbindService()
        unregisterSharedPreferencesListener()
        super.onStop()
    }

    private fun setupObservers() {
        viewModel.binder.observe(viewLifecycleOwner) { serviceBinder ->
            locationService = serviceBinder.service
        }
    }

    private fun setupInteractions() {
        viewBinding.actionRequestPermission.setOnClickListener {
            val isEnabled = SharedPrefUtil.locationTracking
            val isGranted = viewModel.isPermissionGranted.value ?: false
            val service = if (this::locationService.isInitialized) {
                locationService
            } else {
                null
            }
            when {
                isEnabled -> service?.unsubscribeToLocationUpdates()
                isGranted -> service?.subscribeToLocationUpdates()
                else -> requestNeededPermission()
            }
        }
    }

    private fun registerSharedPreferencesListener() {
        SharedPrefUtil.registerOnSharedPreferenceChangeListener(this)
    }

    private fun unregisterSharedPreferencesListener() {
        SharedPrefUtil.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun checkLocationService() {
        viewModel.setTrackingEnabled(SharedPrefUtil.locationTracking)
    }

    private fun registerReceiver() {
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadCastReceiver,
            IntentFilter(ForegroundLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    private fun unregisterReceiver() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadCastReceiver)
    }

    private fun bindService() {
        val context = requireContext()
        val intent = Intent(context, ForegroundLocationService::class.java)
        val connection = viewModel.serviceConnection
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        requireContext().unbindService(viewModel.serviceConnection)
    }

    private fun checkNeededPermission() {
        val isGranted = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        viewModel.setPermissionGranted(isGranted)
    }

    private fun requestNeededPermission() {
        val provideRationale = viewModel.isPermissionGranted.value ?: false

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar
                .make(
                    viewBinding.root,
                    R.string.permission_detail,
                    Snackbar.LENGTH_LONG
                )
                .setAction(R.string.action_ok) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        FOREGROUND_LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
                .show()

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FOREGROUND_LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == FOREGROUND_LOCATION_PERMISSION_REQUEST_CODE
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.setPermissionGranted(true)
        } else {
            viewModel.setPermissionGranted(false)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == SharedPrefUtil.FOREGROUND_LOCATION_TRACKING) {
            viewModel.setTrackingEnabled(SharedPrefUtil.locationTracking)
        }
    }
}