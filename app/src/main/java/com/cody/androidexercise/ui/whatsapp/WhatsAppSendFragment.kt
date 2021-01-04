package com.cody.androidexercise.ui.whatsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cody.androidexercise.databinding.FragmentWhatsAppSendBinding

class WhatsAppSendFragment : Fragment() {
    private lateinit var binding: FragmentWhatsAppSendBinding

    private lateinit var viewModel: WhatsAppSendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(WhatsAppSendViewModel::class.java)

        binding = FragmentWhatsAppSendBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.actionSend.setOnClickListener {
            val context = requireContext()
            if (viewModel.isPackageInstalled(context.packageManager)) {
                startActivity(viewModel.sendIntent())
            } else {
                Toast.makeText(context, "Can't send with WhatsApp.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}