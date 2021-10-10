package com.fpinbo.app.account.inject

import com.firebase.ui.auth.AuthUI
import com.fpinbo.app.account.Authenticator
import com.fpinbo.app.account.FirebaseAuthWrapper
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AccountModule {

    @Provides
    internal fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    internal fun provideAuthUI(): AuthUI = AuthUI.getInstance()

    @Provides
    internal fun provideAuthenticator(impl: FirebaseAuthWrapper): Authenticator = impl
}
