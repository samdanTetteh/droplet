package com.ijikod.droplet.Interface

import com.ijikod.droplet.model.User

/**
 * View for User data state Changes.
 */
interface UserDataView {
    fun onUserSaved(success: Boolean)
    fun onUser(user: User?)
    fun onUserImageSaved(image : String)
}