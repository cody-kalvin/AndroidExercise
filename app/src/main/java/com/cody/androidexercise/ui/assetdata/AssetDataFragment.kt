package com.cody.androidexercise.ui.assetdata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cody.androidexercise.R
import com.cody.androidexercise.databinding.FragmentAssetDataBinding

class AssetDataFragment : Fragment() {

    private lateinit var binding: FragmentAssetDataBinding

    private lateinit var viewModel: AssetDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssetDataBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireActivity()).get(AssetDataViewModel::class.java)
        viewModel.readResult.observe(viewLifecycleOwner) {
            alertReadResult(it)
        }

        binding.viewModel = viewModel
        binding.actionLoad.setOnClickListener {
            viewModel.readFile(requireContext())
        }

        return binding.root
    }

    private fun alertReadResult(result: AssetDataReadResult) {
        val message = when (result) {
            AssetDataReadResult.Initial -> getString(R.string.message_welcome)
            AssetDataReadResult.Ongoing -> getString(R.string.message_reading)
            is AssetDataReadResult.Success -> getString(R.string.message_success)
            is AssetDataReadResult.Failure -> result.error
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}