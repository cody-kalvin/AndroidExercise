package com.cody.androidexercise.ui.facebook

import android.content.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.lifecycle.*
import com.cody.androidexercise.databinding.FragmentFacebookLoginBinding

private const val EMAIL = "email"

class FacebookLoginFragment : Fragment() {
    private lateinit var binding: FragmentFacebookLoginBinding

    private lateinit var viewModel: FacebookLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(FacebookLoginViewModel::class.java)
        viewModel.initLoginCallback()

        binding = FragmentFacebookLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.loginButton.apply {
            setPermissions(EMAIL)
            fragment = this@FacebookLoginFragment
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onLoginResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}