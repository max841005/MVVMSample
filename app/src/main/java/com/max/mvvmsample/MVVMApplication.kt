package com.max.mvvmsample

import android.app.Application
import com.max.mvvmsample.data.db.AppDatabase
import com.max.mvvmsample.data.network.ApiService
import com.max.mvvmsample.data.network.NetworkConnectionInterceptor
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.data.repositories.HomeRepository
import com.max.mvvmsample.data.repositories.ListRepository
import com.max.mvvmsample.data.repositories.MainRepository
import com.max.mvvmsample.data.repositories.WelcomeRepository
import com.max.mvvmsample.data.resource.ResourceProvider
import com.max.mvvmsample.ui.main.MainViewModelFactory
import com.max.mvvmsample.ui.main.home.HomeViewModelFactory
import com.max.mvvmsample.ui.main.list.ListViewModelFactory
import com.max.mvvmsample.ui.welcome.WelcomeViewModelFactory
import com.max.mvvmsample.utils.*
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

@Suppress("unused")
class MVVMApplication : Application(), DIAware {

    override val di by DI.lazy {

        import(androidXModule(this@MVVMApplication))

        bind { singleton { ResourceProvider(instance()) } }
        bind { singleton { TimeUtils(instance()) } }
        bind { singleton { PermissionCheckUtils(instance()) } }
        bind { singleton { PreferenceProvider(instance()) } }
        bind { singleton { BroadcastUtils(instance()) } }
        bind { singleton { NetworkConnectionInterceptor(instance()) } }
        bind { singleton { ApiService(instance()) } }
        bind { singleton { AppDatabase(instance()) } }
        bind { singleton { PortraitPhotoUtils(instance()) } }
        bind { singleton { PhotoUtils(instance()) } }

        //Repository
        bind { singleton { WelcomeRepository(instance(), instance(), instance(), instance()) } }
        bind { singleton { MainRepository(instance(), instance(), instance(), instance()) } }
        bind { singleton { HomeRepository(instance(), instance(), instance(), instance()) } }
        bind { singleton { ListRepository(instance(), instance(), instance()) } }

        //Factory
        bind { provider { WelcomeViewModelFactory(instance()) } }
        bind { provider { MainViewModelFactory(instance()) } }
        bind { provider { HomeViewModelFactory(instance()) } }
        bind { provider { ListViewModelFactory(instance()) } }
    }
}