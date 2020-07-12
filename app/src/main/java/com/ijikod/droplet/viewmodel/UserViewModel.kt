package com.ijikod.droplet.viewmodel

import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ijikod.droplet.Interface.UserView
import com.ijikod.droplet.base.BaseViewModel
import com.ijikod.droplet.model.User
import com.ijikod.droplet.repository.UserRepository

class UserViewModel(private val mProfileRepository: UserRepository) : BaseViewModel<UserView>() {


    private val mMutableUserData = MutableLiveData<User?>()
    private val mProfileUserSaved = MutableLiveData<Boolean>()
    private val mUserImageSaved = MutableLiveData<String>()
    private val mUserObserver: Observer<in User?> = Observer {
        getView().onUser(it)
    }
    private val mUserDataUpdateObserver: Observer<in Boolean> = Observer {
        getView().onUserSaved(it)
    }
    private val mUserImageUploadObserver: Observer<in String> = Observer {
        getView().onUserImageSaved(it)
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

    override fun attachView(view: UserView, lifecycleOwner: LifecycleOwner) {
        super.attachView(view, lifecycleOwner)

        mMutableUserData.observe(lifecycleOwner, mUserObserver)

        mProfileUserSaved.observe(lifecycleOwner, mUserDataUpdateObserver)

        mUserImageSaved.observe(lifecycleOwner, mUserImageUploadObserver)


    }


    override fun onCleared() {
        super.onCleared()
        mMutableUserData.removeObserver(mUserObserver)
        mProfileUserSaved.removeObserver(mUserDataUpdateObserver)
        mUserImageSaved.removeObserver(mUserImageUploadObserver)
    }
}