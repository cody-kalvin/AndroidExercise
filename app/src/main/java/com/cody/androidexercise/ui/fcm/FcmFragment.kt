package com.cody.androidexercise.ui.fcm

import android.app.*
import android.content.*
import android.os.*
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cody.androidexercise.R
import com.cody.androidexercise.databinding.FragmentFcmBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class FcmFragment : Fragment() {
    private lateinit var viewBinding: FragmentFcmBinding

    private lateinit var viewModel: FcmViewModel

    private lateinit var broadCastReceiver: FcmReceiver

    private inner class FcmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // TODO: do something to notification data
            // val body = intent.getStringExtra(FcmService.EXTRA_DATA) ?: "Empty body"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentFcmBinding.inflate(inflater, container, false)
        viewBinding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(FcmViewModel::class.java)
        viewBinding.viewModel = viewModel

        broadCastReceiver = FcmReceiver()

        createNotificationChannel()

        viewBinding.actionStart.setOnClickListener {
            Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result
                Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
                viewModel.setBody(token)
            })
        }

        return viewBinding.root
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channelId = getString(R.string.fcm_notification_channel_id)

        val channelName = getString(R.string.fcm_notification_channel_name)

        val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        )
    }
}