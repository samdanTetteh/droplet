package com.ijikod.droplet

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.EditText
import com.ijikod.droplet.Application.DropletApp

/**
 * Utilities
 * */
class Utils {

    companion object {

        // Arbitrary request code value
        const val RC_SIGN_IN = 123

        //image pick code
        const val IMAGE_PICK_CODE = 1000;

        //Permission code
        const val IMAGE_PERMISSION_CODE = 1001;

        const val CAPTURE_PERMISSION_CODE = 1003;

        const val IMAGE_CAPTURE_CODE = 1004

        private var dialog: Dialog? = null

        //loading dialog view
        fun getLoadingInstance(context: Context): Dialog {
            if (dialog == null)
                dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_layout, null)
            dialog!!.setContentView(inflate)
            dialog!!.setCancelable(false)
            dialog!!.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            return dialog!!
        }

        // Check for active network
        @Suppress("DEPRECATION")
         fun isNetworkAvailable() : Boolean{
            val connectivityManager = DropletApp.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnectedOrConnecting ?: false
        }


    }


}