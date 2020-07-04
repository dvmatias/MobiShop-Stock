package com.cmdv.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.repositories.AuthRepository
import com.cmdv.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser

class SplashActivityViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var email: String = ""
    private var password: String = ""

    /**
     * User login.
     */
    private var _userLoginMutableLiveData = MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>()
    val userLoginLiveData: LiveData<LiveDataStatusWrapper<FirebaseUser>>
        get() = _userLoginMutableLiveData
    fun login(email: String, password: String) {
        this.email = email
        this.password = password
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
        _userRegisterMutableLiveData = authRepository.register(email, password)
    }

    /**
     * Check if email to register is whitelisted
     */
    private var _isWhiteListed = MutableLiveData<Boolean>()
    val isWhiteListed: LiveData<Boolean>
        get() = _isWhiteListed
    fun checkIfEmailIsWhitelisted(email: String) {
        _isWhiteListed = userRepository.isWhiteListed(email)
    }

}