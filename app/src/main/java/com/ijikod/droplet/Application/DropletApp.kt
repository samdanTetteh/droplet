package com.ijikod.droplet.Application

import android.app.Application
import android.content.Context
import com.ijikod.droplet.repository.UserRepository
import com.ijikod.droplet.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

/**
 * Custom application class for DI
 * **/
class DropletApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        initKoin()
    }

    private fun initKoin() {
        val myModule = module {
            viewModel {
                UserViewModel(get())
            }
            single {
                UserRepository.getInstance()
            }
        }

        // start Koin!
        startKoin {
            // declare used Android context
            androidContext(applicationContext)
            // declare modules
            modules(myModule)
        }

    }


    companion object {
        lateinit var appContext: Context
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }


}