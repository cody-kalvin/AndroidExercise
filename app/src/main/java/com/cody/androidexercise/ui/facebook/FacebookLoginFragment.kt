package com.cody.androidexercise.ui.facebook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cody.androidexercise.R
import com.cody.androidexercise.databinding.FragmentFacebookLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult

private const val EMAIL = "email"

class FacebookLoginFragment : Fragment() {

    private lateinit var binding: FragmentFacebookLoginBinding

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLoginCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFacebookLoginBinding.inflate(inflater, container, false)
        setupLoginButton()
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupLoginCallback() {
        callbackManager = CallbackManager.Factory.create()
    }

    private fun setupLoginButton() {
        binding.loginButton.apply {
            setPermissions(EMAIL)
            fragment = this@FacebookLoginFragment
            registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken
                    if (accessToken != null && !accessToken.isExpired) {
                        saveToken(accessToken)
                    }
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException?) {}
            })
        }
    }

    private fun saveToken(accessToken: AccessToken) {
        val context = requireContext()
        val credential = context.getSharedPreferences(
            context.resources.getString(R.string.fb_credential),
            Context.MODE_PRIVATE
        )
        val editor = credential.edit()
        accessToken.isExpired
        editor.putString(context.resources.getString(R.string.fb_access_token), accessToken.token)
        editor.apply()
    }
}