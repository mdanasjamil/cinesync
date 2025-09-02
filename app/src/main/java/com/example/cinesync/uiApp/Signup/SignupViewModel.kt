package com.example.cinesync.uiApp.Signup

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.UseCase.UserUseCase
import androidx.lifecycle.viewModelScope
import com.example.cinesync.uiApp.Login.LoginAuthScreenAction
import com.example.cinesync.uiApp.Login.LoginAuthScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState = _uiState.asStateFlow()

    private val _action = Channel<SignupScreenAction>(capacity=Channel.BUFFERED) /*TODO:Read*/
    val action = _action.receiveAsFlow()

    fun signup(username: String, password: String){
        viewModelScope.launch {
            try{
                userUseCase.addUser(User(username = username, password = password))
                sendAction(action = SignupScreenAction.SignupSuccess(username, password))
                Log.d("SignupViewModel", "User signed up successfully")
            }catch (e: Exception){
                _uiState.value = SignupUiState(error = true)
                Log.e("SignupViewModel", "Error signing up user", e)
            }

        }
    }

    fun onEvent(signupScreenEvent: SignupScreenEvent){
        when(signupScreenEvent){
            is SignupScreenEvent.PerformSignup -> { signup(_uiState.value.username,
                _uiState.value.password)}
            is SignupScreenEvent.UsernameChanged -> {
                _uiState.update { it.copy(username = signupScreenEvent.username) }
            }
            is SignupScreenEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = signupScreenEvent.password) }
            }
            is SignupScreenEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = signupScreenEvent.confirmPassword) }
            }
//            is SignupScreenEvent.NameChanged -> {
//                _uiState.update { it.copy(name = signupScreenEvent.name) }
//            }
            else-> Unit
        }
    }

    private fun sendAction(action: SignupScreenAction){
        _action.trySend(action)
    }
}