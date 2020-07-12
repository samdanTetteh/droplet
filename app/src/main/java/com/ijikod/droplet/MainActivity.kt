package com.ijikod.droplet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ijikod.droplet.repository.UserRepository
import com.ijikod.droplet.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {


    /**
     * Main activity that holds fragment host
     * **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}