package com.fpinbo.app.account.inject

import com.fpinbo.app.account.view.AccountFragment
import com.fpinbo.app.inject.FragmentScope
import com.fpinbo.app.inject.SubComponentBuilder
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AccountModule::class])
interface AccountSubComponent {

    fun inject(accountFragment: AccountFragment)

    @Subcomponent.Builder
    interface Builder : SubComponentBuilder<AccountSubComponent> {
        fun accountModule(eventsModule: AccountModule): Builder
    }

}