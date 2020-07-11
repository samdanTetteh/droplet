package com.ijikod.droplet.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
        setHasOptionsMenu(true)
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        // Sign out user
        if (itemId == R.id.sign_out){
            val alertBuilder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
            with(alertBuilder){
                setTitle(getString(R.string.app_name))
                setMessage(getString(R.string.logout_confirm_txt))
                setCancelable(false)

                setPositiveButton(getString(R.string.logout_txt)) { _, _ ->
                    AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                        requireActivity().finish()
                    }
                }

                setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                    create().hide()
                }

            }

            //Show only once instance of logout dialog
            val alertDialog : AlertDialog = alertBuilder.create()
            if (!alertDialog.isShowing) alertDialog.show()



        }

        // Save user details
        if (itemId == R.id.save){


        }

        return super.onOptionsItemSelected(item)
    }


}