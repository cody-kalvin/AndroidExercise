package com.cody.androidexercise.ui.rawdata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cody.androidexercise.R
import com.cody.androidexercise.databinding.FragmentRawDataBinding

class RawDataFragment : Fragment() {

    private lateinit var binding: FragmentRawDataBinding

    private lateinit var viewModel: RawDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRawDataBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireActivity()).get(RawDataViewModel::class.java)
        viewModel.readResult.observe(viewLifecycleOwner) {
            alertReadResult(it)
        }

        binding.viewModel = viewModel
        binding.actionLoad.setOnClickListener {
            viewModel.readFile(requireContext())
        }

        return binding.root
    }

    private fun alertReadResult(result: RawDataReadResult) {
        val message = when (result) {
            RawDataReadResult.Initial -> getString(R.string.message_welcome)
            RawDataReadResult.Ongoing -> getString(R.string.message_reading)
            is RawDataReadResult.Success -> getString(R.string.message_success)
            is RawDataReadResult.Failure -> result.error
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}