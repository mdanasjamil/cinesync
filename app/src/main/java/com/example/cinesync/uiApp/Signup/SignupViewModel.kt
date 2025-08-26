package com.example.cinesync.uiApp.Signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.UseCase.UserUseCase
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState = _uiState.asStateFlow()

    fun signup(username: String, name:String,password: String, confirmPassword: String){
        viewModelScope.launch {
            try{

                userUseCase.addUser(User(username = username, password = password))
                _uiState.value = SignupUiState(signedUp = true)
                Log.d("SignupViewModel", "User signed up successfully")
            }catch (e: Exception){
                _uiState.value = SignupUiState(error = true)
                Log.e("SignupViewModel", "Error signing up user", e)
            }

        }
    }
}