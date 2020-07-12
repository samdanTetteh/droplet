package com.ijikod.droplet.Interface

import com.ijikod.droplet.model.User

/**
 * Notification for view on  user data state Changes.
 */
interface UserView {
    fun onUserSaved(success: Boolean)
    fun onUser(user: User?)
    fun onUserImageSaved(image: String)
}