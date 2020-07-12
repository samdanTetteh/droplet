package com.ijikod.droplet.repository

import android.net.Uri
import com.ijikod.droplet.base.BaseRepository
import com.ijikod.droplet.model.User

/**
 *  The repository handling database operations for [User] flow.
 */
class UserRepository private constructor() : BaseRepository() {


    fun getUserProfile(id: String, onUser: ((User?) -> Unit)) {
        appDatabase.getUser(id, onUser)
    }

    fun saveUserData(user: User, onSuccess: ((Boolean) -> Unit)? = null) {
        appDatabase.saveUser(user, onSuccess)
    }

    fun saveUserImage(uri: Uri, onSuccess: ((String) -> Unit)? = null) {
        appDatabase.saveUserImage(uri, onSuccess)
    }






    companion object {
        private var instance: UserRepository? = null

        fun getInstance(): UserRepository {
            if (instance == null)
                instance = UserRepository()
            return instance!!
        }
    }
}