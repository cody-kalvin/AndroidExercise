package com.cody.androidexercise.ui.facebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.cody.androidexercise.databinding.FragmentFacebookDataBinding

class FacebookDataFragment : Fragment() {

    private lateinit var binding: FragmentFacebookDataBinding

    private lateinit var viewModel: FacebookDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = FacebookDataViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(FacebookDataViewModel::class.java)

        binding = FragmentFacebookDataBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.fetchMe()

        return binding.root
    }
}