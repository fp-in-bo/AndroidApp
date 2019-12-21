package com.fpinbo.app.account.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.fpinbo.app.R
import com.fpinbo.app.account.AccountViewModel
import com.fpinbo.app.account.inject.AccountModule
import com.fpinbo.app.account.inject.AccountSubComponent
import com.fpinbo.app.utils.exhaustive
import com.fpinbo.app.utils.subComponentBuilder
import javax.inject.Inject

class AccountFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AccountViewModel

    override fun onAttach(context: Context) {
        val accountSubComponent =
            context.subComponentBuilder<AccountSubComponent.Builder>()
                .accountModule(AccountModule())
                .build()
        accountSubComponent.inject(this)
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.account_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountViewModel::class.java)

        viewModel.state.observe(this, Observer {

            exhaustive..when (it) {
                else -> {
                }
            }
        })
    }

}
