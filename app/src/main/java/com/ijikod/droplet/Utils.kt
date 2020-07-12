package com.ijikod.droplet

import android.app.Application
import android.util.Patterns
import android.widget.EditText
import com.ijikod.droplet.Application.DropletApp

/**
 * Utilities
 * */
class Utils {

    companion object{

        // Arbitrary request code value
        const val RC_SIGN_IN = 123
        //image pick code
        const val IMAGE_PICK_CODE = 1000;
        //Permission code
        const val IMAGE_PERMISSION_CODE = 1001;

        const val CAPTURE_PERMISSION_CODE = 1003;

        const val IMAGE_CAPTURE_CODE = 1004

        //Validate values on details page
        fun validatePage(firstName : EditText, lastName: EditText, email : EditText) : Boolean{
            var isPageValid = true
            val patten = Patterns.EMAIL_ADDRESS

            if (firstName.text.isEmpty()){
                firstName.error = DropletApp.appContext.getString(R.string.first_name_txt)
                isPageValid = false
            }

            if (lastName.text.isEmpty()){
                lastName.error = DropletApp.appContext.getString(R.string.enter_last_name)
                isPageValid = false
            }

            if (!patten.matcher(email.text.trim()).matches()){
                email.error = DropletApp.appContext.getString(R.string.valid_email_txt)
                isPageValid = false
            }

            return isPageValid
        }

    }



}