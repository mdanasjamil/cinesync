package com.example.cinesync.uiApp.Navigation

import androidx.lifecycle.ViewModel
import com.example.cinesync.data.Database.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _loginType = MutableStateFlow<Int?>(null)
    val loginType = _loginType.asStateFlow()

    fun setUser(user: User) {
        _currentUser.value = user
    }

    fun clearUser() {
        _currentUser.value = null
    }

    fun setLoginType(type: Int){
        _loginType.value= type
    }
}