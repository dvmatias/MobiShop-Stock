package com.cmdv.data.datasources.firebase

import androidx.lifecycle.MutableLiveData
import com.cmdv.data.entities.firebase.DateEntity
import com.cmdv.data.entities.firebase.UserFirebaseEntity
import com.cmdv.data.mappers.UserMapper
import com.cmdv.domain.datasources.firebase.UserFirebaseSource
import com.cmdv.domain.datasources.firebase.UserStoreListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT_DD_MM_YY = "dd-MM-yyyy'T'HH:mm:ss.SSS"

class FirebaseUserSourceImpl : UserFirebaseSource {

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dbWhiteListRef: DatabaseReference = dbRootRef.getReference("whitelist")
    private val dbUsersRef: DatabaseReference = dbRootRef.getReference("users")

    /**
     *
     */
    override fun get(uid: String): MutableLiveData<Any> {
        TODO("Not yet implemented")
    }

    /**
     *
     */
    override fun isWhiteListed(email: String): MutableLiveData<Boolean> {
        val mutableLiveData = MutableLiveData<Boolean>()

        dbWhiteListRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isWhitelisted = false
                for (element in snapshot.children) {
                    if (element.child("email").value == email) {
                        isWhitelisted = true
                        break
                    }
                }
                mutableLiveData.value = isWhitelisted
            }

            override fun onCancelled(error: DatabaseError) {
                mutableLiveData.value = false
            }
        })

        return mutableLiveData
    }

    /**
     *
     */
    override fun storeUser(firebaseUser: FirebaseUser?, userStoreListener: UserStoreListener) {
        if (firebaseUser != null) {
            val user: UserFirebaseEntity = getRegisteredUser(firebaseUser)
            if (user.uid != null) {
                dbUsersRef.child(user.uid).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userStoreListener.onSuccess(
                            UserMapper().transformEntityToModel(user)
                        )
                    } else {
                        userStoreListener.onError("Firebase user registration error. Fail adding user to DB.")
                    }
                }
            } else {
                userStoreListener.onError("Firebase user registration error. Fail adding user to DB.")
            }
        } else {
            userStoreListener.onError("Firebase user uid null error")
        }
    }

    /**
     *
     */
    private fun getRegisteredUser(firebaseUser: FirebaseUser): UserFirebaseEntity {
        val dateString = SimpleDateFormat(DATE_FORMAT_DD_MM_YY, Locale.getDefault()).format(Date().time)

        return UserFirebaseEntity(
            uid = firebaseUser.uid,
            name = firebaseUser.displayName,
            email = firebaseUser.email,
            isAdmin = false,
            canRead = true,
            canWrite = false,
            date = DateEntity(dateString, dateString)
        )
    }

}