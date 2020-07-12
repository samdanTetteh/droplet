package com.ijikod.droplet.database

import android.net.Uri
import android.system.Os.close
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ijikod.droplet.model.User
import java.util.*

class AppDatabase private constructor(){

    private val database  = FirebaseDatabase.getInstance()
    private val databaseRef = database.reference
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val userNode = databaseRef.child("Users")
    private lateinit var onUser : ((User?) -> Unit)
    private lateinit var userId : String



    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            val user = p0.child(userId).getValue(User::class.java)
            onUser(user)
            close()
        }

        override fun onCancelled(p0: DatabaseError) {
            onUser(null)
            close()
        }
    }

    fun saveUser(user: User, onSuccess: ((Boolean) -> Unit)? = null) {
        user.phoneNumber?.let {
            userNode.child(it).setValue(user)
                .addOnCompleteListener {
                    onSuccess?.invoke(it.isSuccessful)
                }
        }
    }


    fun saveUserImage(imageUri: Uri, onSuccess: ((String) -> Unit)? = null) {
                val ref = storageReference.child("uploads/" + UUID.randomUUID().toString())
                val uploadTask = ref.putFile(imageUri)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        onSuccess?.invoke(downloadUri.toString())
                    } else {
                        // Handle failures
                    }
                }.addOnFailureListener{
                       // handle failure
                }
    }




    fun getUser(id: String, onUser: ((User?) -> Unit)) {
        this.onUser = onUser
        this.userId = id
        userNode.addValueEventListener(valueEventListener)

    }

    private fun close() {
        userNode.removeEventListener(valueEventListener)
    }


    companion object {
        private var appDatabase: AppDatabase? = null

        fun getInstance(): AppDatabase {
            if (appDatabase == null)
                appDatabase =
                    AppDatabase()
            return appDatabase!!
        }
    }


}