package com.cmdv.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser

class SplashActivityViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    /**
     * User login.
     */
    private var _userLoginMutableLiveData = MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>()
    val userLoginLiveData: LiveData<LiveDataStatusWrapper<FirebaseUser>>
        get() = _userLoginMutableLiveData
    fun login(email: String, password: String) {
        _userLoginMutableLiveData.value = LiveDataStatusWrapper.loading(null)
        _userLoginMutableLiveData = authRepository.login(email, password)
    }

    /**
     * User register.
     */
    private var _userRegisterMutableLiveData = MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>()
    val userRegisterMutableLiveData: LiveData<LiveDataStatusWrapper<FirebaseUser>>
        get() = _userRegisterMutableLiveData
    fun register(email: String, password: String) {
        _userRegisterMutableLiveData.value = LiveDataStatusWrapper.loading(null)
        _userRegisterMutableLiveData = authRepository.register(email, password)
    }
}