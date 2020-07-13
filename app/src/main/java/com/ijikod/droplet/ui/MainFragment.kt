package com.ijikod.droplet.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ijikod.droplet.Interface.UserView
import com.ijikod.droplet.R
import com.ijikod.droplet.Utils
import com.ijikod.droplet.model.User
import com.ijikod.droplet.viewmodel.UserViewModel
import com.theartofdev.edmodo.cropper.CropImage
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), UserView {

    lateinit var holder: CoordinatorLayout
    lateinit var detailsHolder: ScrollView
    lateinit var splashView: ConstraintLayout
    lateinit var imageView: ImageView
    lateinit var firstNameTxt: EditText
    lateinit var lastNameTxt: EditText
    lateinit var emailTxt: EditText
    lateinit var uploadTxt: TextView

    var imageUri: Uri? = null
    var signedInPhoneNumber: String? = null
    var signedInUserImage = ""
    var menu: Menu? = null

    val mUserViewModel: UserViewModel by viewModel()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginCheck()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val loginView = inflater.inflate(R.layout.fragment_login, container, false)

        initScreen(loginView)

        mUserViewModel.attachView(this, this)
        return loginView
    }

    //Initialise views
    private fun initScreen(view: View) {
        holder = view.findViewById(R.id.holder_layout)
        detailsHolder = view.findViewById(R.id.app_view_scroll)
        imageView = view.findViewById(R.id.profile_img)
        splashView = view.findViewById(R.id.splash_view)
        firstNameTxt = view.findViewById(R.id.first_name_txt)
        lastNameTxt = view.findViewById(R.id.last_name_txt)
        emailTxt = view.findViewById(R.id.email_txt)
        uploadTxt = view.findViewById(R.id.upload_txt)

        imageView.setOnClickListener {
            showCaptureSelection()
        }
    }

    // Check for login instance of current User
    private fun loginCheck() {
        val fireBaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = fireBaseAuth.currentUser
        if (firebaseUser != null) {
            if (Utils.isNetworkAvailable()) {
                //Already signed in

                // this prevents a window exception when activity context does not exit to show
                // dialog
                try {
                    Utils.getLoadingInstance(requireContext()).show()
                } catch (e: WindowManager.BadTokenException) {
                    Log.d(getString(R.string.app_name), e.toString())
                }

                signedInPhoneNumber = firebaseUser.phoneNumber
                signedInPhoneNumber?.let { mUserViewModel.getProfile(it) }
                showDetailsPage()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.connection_txt),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            // not signed in
            showAuthScreen()
        }
    }

    private fun showAuthScreen() {
        //setting default country code
        val authProvider =
            AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso(getString(R.string.uk_ios))

        startActivityForResult(
            // Get an instance of AuthUI based on the default app
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(
                    arrayListOf(
                        authProvider.build()
                    )
                ).build(), Utils.RC_SIGN_IN
        );
    }

    // show error message in snackbar
    private fun showSnackBar(msg: String, actionMsg: String) {
        val snackbar = Snackbar
            .make(holder, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionMsg) {
                showAuthScreen()
            }
        snackbar.show()
    }

    //check runtime permission and show gallery
    private fun showGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, Utils.IMAGE_PERMISSION_CODE);
            } else {
                //permission already granted
                pickImageFromGallery();
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery();
        }
    }

    //Intent to pick image
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra("crop", "true")
        startActivityForResult(intent, Utils.IMAGE_PICK_CODE)
    }

    //check runtime permission and capture image
    private fun showCamera() {
        //if system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PERMISSION_DENIED ||
                checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                requestPermissions(permission, Utils.CAPTURE_PERMISSION_CODE)
            } else {
                //permission already granted

                openCamera()
            }
        } else {
            //system os is < marshmallow
            openCamera()
        }
    }


    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, getString(R.string.new_picture))
        values.put(MediaStore.Images.Media.DESCRIPTION, getString(R.string.from_camera_txt))
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraIntent.putExtra("crop", "true")
        startActivityForResult(cameraIntent, Utils.IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // checking for reponses from sign auth in fragment
        if (requestCode == Utils.RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                //Store user phone number after successful sign in
                response?.phoneNumber.let {
                    signedInPhoneNumber = it
                    mUserViewModel.getProfile(it!!)
                }

                showDetailsPage()
                Log.d("Login", "Success")
                return
            } else {
                //Sign in failed
                if (response == null) {
                    showSplashPage()
                    // User Pressed back button
                    showSnackBar(
                        getString(R.string.sign_in_continue),
                        getString(R.string.login_txt)
                    )
                    return
                }

                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    // Network Error
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.connection_txt),
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                //Unknown Error
                showSnackBar(getString(R.string.unknown_error), getString(R.string.login_txt))
            }
        }

        // Proccess selected image from image gallery
        if (requestCode == Utils.IMAGE_PICK_CODE) {
            if (data == null || data.data == null) {
                return
            }
            CropImage.activity(data.data!!).start(requireActivity(), this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                uploadTxt.text = getString(R.string.change_profile_image)
                imageUri = result.uri
                Glide.with(this).load(imageUri).circleCrop().into(imageView)
            }

        }

        //Process image from camera
        if (requestCode == Utils.IMAGE_CAPTURE_CODE) {
            if (imageUri == null || imageUri == null) {
                return
            }
            CropImage.activity(imageUri).start(requireActivity(), this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                //set image captured to image view
                uploadTxt.text = getString(R.string.change_profile_image)
                imageUri = result.uri
                Glide.with(this).load(imageUri).circleCrop().into(imageView)
            }
        }
    }

    // Edit image selection
    private fun showCaptureSelection() {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.edit_image_txt))

        val options = arrayOf(getString(R.string.camera_txt), getString(R.string.select_image_txt))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    showCamera()
                }
                1 -> {
                    showGallery()
                }
            }
        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun showDetailsPage() {
        menu?.let {
            val logoutItem = it.findItem(R.id.sign_out)
            val saveItem = it.findItem(R.id.save)
            logoutItem.isVisible = true
            saveItem.isVisible = true
        }

        detailsHolder.visibility = View.VISIBLE
        splashView.visibility = View.GONE
    }


    private fun showSplashPage() {
        menu?.let {
            val logoutItem = it.findItem(R.id.sign_out)
            val saveItem = it.findItem(R.id.save)
            logoutItem.isVisible = false
            saveItem.isVisible = false
        }

        detailsHolder.visibility = View.GONE
        splashView.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        // Check grant status on gallery access permission
        when (requestCode) {
            Utils.IMAGE_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.perms_denied_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Check grant status on camera permission
        when (requestCode) {
            Utils.CAPTURE_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    openCamera()
                } else {
                    //permission from popup was denied
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.perms_denied_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.main_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        // Sign out user
        if (itemId == R.id.sign_out) {
            val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            with(alertBuilder) {
                setTitle(getString(R.string.app_name))
                setMessage(getString(R.string.logout_confirm_txt))
                setCancelable(false)

                setPositiveButton(getString(R.string.logout_txt)) { _, _ ->
                    AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                        showSplashPage()
                        loginCheck()
                    }
                }

                setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                    create().hide()
                }

            }

            //Show only once instance of logout dialog
            val alertDialog: AlertDialog = alertBuilder.create()
            if (!alertDialog.isShowing) alertDialog.show()
        }

        // Save user details
        if (itemId == R.id.save) {
            if (Utils.validatePage(
                    firstName = firstNameTxt,
                    lastName = lastNameTxt,
                    email = emailTxt
                )
            ) {
                Utils.getLoadingInstance(requireContext()).show()
                if (imageUri == null) {
                    val user = User(firstName = firstNameTxt.text.toString())
                    user.lastName = lastNameTxt.text.toString().trim()
                    user.email = emailTxt.text.toString().trim()
                    user.phoneNumber = signedInPhoneNumber
                    user.image = signedInUserImage
                    mUserViewModel.saveUser(user)
                } else {
                    imageUri?.let {
                        mUserViewModel.saveUserImage(it)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUserSaved(success: Boolean) {
        Utils.getLoadingInstance(requireContext()).hide()
    }

    override fun onUser(user: User?) {
        Utils.getLoadingInstance(requireContext()).hide()
        user?.let {
            firstNameTxt.setText(it.firstName)
            lastNameTxt.setText(it.lastName)
            emailTxt.setText(it.email)
            signedInUserImage = it.image.toString()
            if (!it.image?.isEmpty()!!) {
                uploadTxt.text = getText(R.string.change_profile_image)
                Glide.with(this).load(it.image).placeholder(R.drawable.ic_account_circle)
                    .circleCrop().into(imageView)
            }

        }
    }

    // Save user once image is successfully uploaded
    override fun onUserImageSaved(image: String) {
        val user = User(firstName = firstNameTxt.text.toString())
        user.lastName = lastNameTxt.text.toString().trim()
        user.email = emailTxt.text.toString().trim()
        user.phoneNumber = signedInPhoneNumber
        user.image = image
        mUserViewModel.saveUser(user)
    }

}