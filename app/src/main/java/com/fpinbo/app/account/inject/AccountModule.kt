package com.fpinbo.app.account.inject

import androidx.lifecycle.ViewModel
import com.fpinbo.app.account.AccountViewModel
import com.fpinbo.app.inject.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AccountModule {

    @Provides
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    internal fun bindAccountViewModel(impl: AccountViewModel): ViewModel = impl
}