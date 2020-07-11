package com.ijikod.droplet.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ijikod.droplet.Constants
import com.ijikod.droplet.R


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    lateinit var holder : CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val loginView =  inflater.inflate(R.layout.fragment_login, container, false)

        holder = loginView.findViewById(R.id.holder_layout)

        loginCheck()
        return loginView
    }



    // Check for login instance of current User
    private fun loginCheck(){
        val fireBaseAuth = FirebaseAuth.getInstance()
        if(fireBaseAuth.currentUser != null){
            //Already signed in

        }else{
            // not signed in
            showAuthScreen()
        }
    }



    private fun showAuthScreen(){
        //setting default country code
        val authProvider = AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso(getString(R.string.uk_ios))

        startActivityForResult(
            // Get an instance of AuthUI based on the default app
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(arrayListOf(
                    authProvider.build()
                )).build(), Constants.RC_SIGN_IN
        );
    }


    // show error message in snackbar
    private fun showSnackBar(msg: String, actionMsg: String){
        val snackbar = Snackbar
            .make(holder, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionMsg) {
                showAuthScreen()
            }
        snackbar.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // checking for reponses from sign auth in fragment
        if (requestCode == Constants.RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK){

                Log.d("Login", "Success")
            }else{
                //Sign in failed
                if (response == null){
                    // User Pressed back button
                    showSnackBar(getString(R.string.sign_in_continue), getString(R.string.login_txt))
                    return
                }

                if (response.error?.errorCode == ErrorCodes.NO_NETWORK){
                    // Network Error

                    return
                }

                //Unknown Error
                showSnackBar(getString(R.string.unknown_error), getString(R.string.login_txt))
            }
        }
    }


}