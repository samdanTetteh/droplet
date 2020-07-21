package com.ijikod.droplet.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.*
import com.ijikod.droplet.Application.DropletApp
import com.ijikod.droplet.R
import com.ijikod.droplet.model.User
import com.ijikod.droplet.repository.UserRepository

class UserViewModel(private val mProfileRepository: UserRepository) : ViewModel() {


    private val mMutableUserData = MutableLiveData<User?>()
    private val mProfileUserSaved = MutableLiveData<Boolean>()
    private val mUserImageSaved = MutableLiveData<String>()
    private val mEmailValidation = MutableLiveData<String>()
    private val mFirstNameValidation = MutableLiveData<String>()
    private val mLastNameValidation = MutableLiveData<String>()


    val emailValidation = mEmailValidation
    val firstNameValidation = mFirstNameValidation
    val lastNameValidation = mLastNameValidation
    val isUserProfileSaved = mProfileUserSaved
    val isUserImageSaved = mUserImageSaved
    val userProfileData = mMutableUserData

    var userEmail = ""
      set(value) {
          validateEmail(value)
      }

    var userFirstName = ""
    set(value) {
        validUserFistName(value)
    }

    var lastName = ""
    set(value) {
        validUserLastName(value)
    }

    fun getProfile(id: String) {
        mProfileRepository.getUserProfile(id) {
            mMutableUserData.postValue(it)
        }
    }

    fun saveUser(user: User) {
        mProfileRepository.saveUserData(user) {
            mProfileUserSaved.postValue(it)
        }
    }

    fun saveUserImage(imageUri: Uri) {
        mProfileRepository.saveUserImage(imageUri) {
            mUserImageSaved.postValue(it)
        }
    }

    /**
     * Validate email Address
     * **/
    private fun validateEmail(value: String){
        val pattern = Patterns.EMAIL_ADDRESS
        if (!pattern.matcher(value).matches()){
            if (value.isEmpty()) {
                emailValidation.value = DropletApp.appContext.getString(R.string.empty_email_txt)
            }else{
                emailValidation.value = DropletApp.appContext.getString(R.string.valid_email_txt)
            }
        }else{
            emailValidation.value = ""
        }
    }

    /**
     * Validate first name
     * **/
    private fun validUserFistName(value: String) : MutableLiveData<String>{
        when {
            value.isEmpty() -> {
                mFirstNameValidation.value = DropletApp.appContext.getString(R.string.first_name_hint_txt)
            }
            value.length < 5 -> {
                mFirstNameValidation.value = DropletApp.appContext.getString(R.string.valid_first_name_txt)
            }
            else -> {
                mFirstNameValidation.value =  ""
            }
        }
        return mFirstNameValidation
    }

    /**
     * Validate last name
     * **/
    private fun validUserLastName(value: String) : MutableLiveData<String> {
        when {
            value.isEmpty() -> {
                lastNameValidation.value = DropletApp.appContext.getString(R.string.enter_last_name)
            }
            value.length < 5 -> {
                lastNameValidation.value = DropletApp.appContext.getString(R.string.valid_last_name_txt)
            }
            else -> {
                lastNameValidation.value = ""
            }
        }
        return lastNameValidation
    }
}