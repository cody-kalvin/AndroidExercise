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
            setupWriteResult(it)
        }

        viewModel.loadResult.observe(viewLifecycleOwner) {
            setupLoadResult(it)
        }

        binding.actionSave.setOnClickListener {
            viewModel.saveFile(requireContext())
        }

        binding.actionLoad.setOnClickListener {
            viewModel.loadFile(requireContext())
        }

        return binding.root
    }

    private fun setupWriteResult(result: IoFileWriteResult) {
        when (result) {
            IoFileWriteResult.Initial -> binding.barLoading.visibility = View.GONE

            IoFileWriteResult.Ongoing -> binding.barLoading.visibility = View.VISIBLE

            IoFileWriteResult.Success -> {
                viewModel.content.value = ""
                binding.barLoading.visibility = View.GONE
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            }

            is IoFileWriteResult.Failure -> {
                binding.barLoading.visibility = View.GONE
                Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupLoadResult(result: IoFileLoadResult) {
        when (result) {
            IoFileLoadResult.Initial -> binding.barLoading.visibility = View.GONE

            IoFileLoadResult.Ongoing -> binding.barLoading.visibility = View.VISIBLE

            is IoFileLoadResult.Success -> {
                viewModel.content.value = result.content
                binding.barLoading.visibility = View.GONE
                Toast.makeText(requireContext(), "Loaded", Toast.LENGTH_SHORT).show()
            }

            is IoFileLoadResult.Failure -> {
                binding.barLoading.visibility = View.GONE
                Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}