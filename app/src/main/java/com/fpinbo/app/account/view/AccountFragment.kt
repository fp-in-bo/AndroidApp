package com.fpinbo.app.account.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.TransitionManager
import com.fpinbo.app.R
import com.fpinbo.app.account.*
import com.fpinbo.app.utils.exhaustive
import com.fpinbo.app.utils.hide
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.account_fragment.*

@AndroidEntryPoint
class AccountFragment : Fragment() {

    companion object {
        private const val RC_SIGN_IN = 2500
    }

    private val viewModel: AccountViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.account_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, {

            TransitionManager.beginDelayedTransition(root, MaterialFadeThrough())

            exhaustive..when (it) {
                is Logged -> showLoggedUser(it.user)
                NotLogged -> showNotLoggedUser()
                Loading -> showLoading()
                is Error -> showError(it.message)
            }
        })

        viewModel.viewEvent.observe(viewLifecycleOwner, { event ->
            event.consume {
                exhaustive..when (it) {
                    is PerformLogin -> performLogin(it.intent)
                }
            }
        })

    }

    private fun showLoggedUser(user: User) {
        logged_view.isVisible = true
        logged_view.bind(
            user = user,
            onLogout = viewModel::logOut
        )
        hide(loader, error, not_logged_view)
    }

    private fun showNotLoggedUser() {
        not_logged_view.isVisible = true
        not_logged_view.bind(onLogin = viewModel::logIn)
        hide(loader, error, logged_view)
    }


    private fun showLoading() {
        loader.isVisible = true
        hide(error, logged_view, not_logged_view)
    }

    private fun showError(message: String?) {
        val errorMessage =
            if (message.isNullOrBlank()) getString(R.string.error_generic) else message
        error.isVisible = true
        error.text = errorMessage
        hide(loader, logged_view, not_logged_view)
    }


    private fun performLogin(intent: Intent) {
        startActivityForResult(
            intent,
            RC_SIGN_IN
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            viewModel.onLoginResult(resultCode, data)
        }
    }
}

