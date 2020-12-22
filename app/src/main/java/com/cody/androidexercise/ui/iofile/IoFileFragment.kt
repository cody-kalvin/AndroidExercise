package com.cody.androidexercise.ui.iofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cody.androidexercise.databinding.FragmentIoFileBinding

class IoFileFragment : Fragment() {
    private lateinit var binding: FragmentIoFileBinding

    private lateinit var viewModel: IoFileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIoFileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireActivity()).get(IoFileViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.writeResult.observe(viewLifecycleOwner) {
            alertWriteResult(it)
        }

        viewModel.loadResult.observe(viewLifecycleOwner) {
            alertLoadResult(it)
        }

        binding.actionSave.setOnClickListener {
            viewModel.saveFile(requireContext())
        }

        binding.actionLoad.setOnClickListener {
            viewModel.loadFile(requireContext())
        }

        return binding.root
    }

    private fun alertWriteResult(result: IoFileWriteResult) {
        val message = when (result) {
            IoFileWriteResult.Initial -> "Welcome"
            IoFileWriteResult.Ongoing -> "Writing.."
            is IoFileWriteResult.Success -> "Saved!"
            is IoFileWriteResult.Failure -> result.error
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun alertLoadResult(result: IoFileLoadResult) {
        val message = when (result) {
            IoFileLoadResult.Initial -> "Welcome"
            IoFileLoadResult.Ongoing -> "Loading.."
            is IoFileLoadResult.Success -> "Loaded!"
            is IoFileLoadResult.Failure -> result.error
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}