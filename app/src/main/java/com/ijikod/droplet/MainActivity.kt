package com.ijikod.droplet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.ijikod.droplet.Constants.Companion.RC_SIGN_IN

class MainActivity : AppCompatActivity() {


    /**
     * Main activity that holds fragment host
     * **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}