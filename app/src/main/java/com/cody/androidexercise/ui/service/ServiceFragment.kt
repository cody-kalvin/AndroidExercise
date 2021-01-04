package com.cody.androidexercise.ui.service

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cody.androidexercise.databinding.FragmentServiceBinding
import com.cody.androidexercise.service.MyService

class ServiceFragment : Fragment() {

    private lateinit var viewBinding: FragmentServiceBinding

    private lateinit var viewModel: ServiceViewModel

    private lateinit var service: MyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentServiceBinding.inflate(inflater, container, false)
        viewBinding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(ServiceViewModel::class.java)
        viewBinding.viewModel = viewModel

        setupObservers()
        setupInteractions()

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        startService()
        bindService()
    }

    override fun onStop() {
        super.onStop()
        requireContext().unbindService(viewModel.serviceConnection)
    }

    private fun setupObservers() {
        viewModel.binder.observe(viewLifecycleOwner) { serviceBinder ->
            service = serviceBinder.service
        }

        viewModel.isProgressBarUpdating.observe(viewLifecycleOwner) { isUpdating ->
            if (isUpdating) {
                runUpdate()
            }
        }
    }

    private fun setupInteractions() {
        viewBinding.actionToggle.setOnClickListener {
            toggleUpdate()
        }
    }

    private fun startService() {
        val context = requireContext()
        val intent = Intent(context, MyService::class.java)
        context.startService(intent)
    }

    private fun bindService() {
        val context = requireContext()
        val intent = Intent(context, MyService::class.java)
        val connection = viewModel.serviceConnection
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun toggleUpdate() {
        if (this::service.isInitialized) {
            when {
                service.progress == service.maxValue -> {
                    service.resetTask()
                    viewModel.setIsProgressBarUpdating(true)
                }

                service.isPaused -> {
                    service.unPauseTask()
                    viewModel.setIsProgressBarUpdating(true)
                }

                else -> {
                    service.pauseTask()
                    viewModel.setIsProgressBarUpdating(false)
                }
            }
        }
    }

    private fun runUpdate() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (viewModel.isProgressBarUpdating.value == true) {
                    if (viewModel.binder.value != null) {
                        viewModel.setProgress(service.progress)
                        viewModel.setMaxValue(service.maxValue)
                        if (service.progress == service.maxValue) {
                            viewModel.setIsProgressBarUpdating(false)
                        }
                    }
                    handler.postDelayed(this, 100)
                } else {
                    handler.removeCallbacks(this)
                }
            }
        }
        handler.postDelayed(runnable, 100)
    }
}