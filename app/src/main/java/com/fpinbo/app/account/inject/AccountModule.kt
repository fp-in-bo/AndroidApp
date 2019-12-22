package com.fpinbo.app.account.inject

import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.fpinbo.app.account.AccountViewModel
import com.fpinbo.app.account.Authenticator
import com.fpinbo.app.account.FirebaseAuthWrapper
import com.fpinbo.app.inject.ViewModelKey
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AccountModule {

    @Provides
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    internal fun bindAccountViewModel(impl: AccountViewModel): ViewModel = impl

    @Provides
    internal fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    internal fun provideAuthUI(): AuthUI = AuthUI.getInstance()

    @Provides
    internal fun provideAuthenticator(impl: FirebaseAuthWrapper): Authenticator = impl
}