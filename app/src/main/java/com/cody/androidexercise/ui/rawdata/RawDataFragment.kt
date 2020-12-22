package com.cody.androidexercise.ui.rawdata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
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
            setupReadResult(it)
        }

        binding.viewModel = viewModel
        binding.actionLoad.setOnClickListener {
            viewModel.readFile(requireContext())
        }

        return binding.root
    }

    private fun setupReadResult(result: RawDataReadResult) {
        val context = requireContext()
        when (result) {
            RawDataReadResult.Initial -> {
                Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show()
            }
            RawDataReadResult.Ongoing -> {
                Toast.makeText(context, "Reading...", Toast.LENGTH_SHORT).show()
            }
            is RawDataReadResult.Success -> {
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
            }
            is RawDataReadResult.Failure -> {
                Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}