package com.max.mvvmsample

import android.app.Application
import com.max.mvvmsample.data.db.AppDatabase
import com.max.mvvmsample.data.network.ApiService
import com.max.mvvmsample.data.network.NetworkConnectionInterceptor
import com.max.mvvmsample.data.permission.PermissionCheckProvider
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.data.repositories.*
import com.max.mvvmsample.data.resource.ResourceProvider
import com.max.mvvmsample.data.time.TimeProvider
import com.max.mvvmsample.ui.main.MainViewModelFactory
import com.max.mvvmsample.ui.main.home.HomeViewModelFactory
import com.max.mvvmsample.ui.main.list.ListViewModelFactory
import com.max.mvvmsample.ui.welcome.WelcomeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

@Suppress("unused")
class MVVMApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { ResourceProvider(instance()) }
        bind() from singleton { PermissionCheckProvider(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { TimeProvider() }

        //Repository
        bind() from singleton { TimeRepository(instance()) }
        bind() from singleton { WelcomeRepository(instance(), instance(), instance(), instance(), instance()) }
        bind() from singleton { MainRepository(instance(), instance(), instance()) }
        bind() from singleton { HomeRepository(instance(), instance(), instance(), instance()) }
        bind() from singleton { ListRepository(instance(), instance(), instance(), instance()) }

        //Factory
        bind() from provider { WelcomeViewModelFactory(instance(), instance()) }
        bind() from provider { MainViewModelFactory(instance(), instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance(), instance()) }
        bind() from provider { ListViewModelFactory(instance()) }
    }
}