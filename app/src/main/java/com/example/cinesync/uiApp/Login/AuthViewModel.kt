package com.example.cinesync.uiApp.Login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.UseCase.UserUseCase
import com.example.cinesync.uiApp.Login.Biometric.BiometricAuthResult
import com.example.cinesync.uiApp.Login.Biometric.BiometricAuthenticator
import com.example.cinesync.uiApp.Signup.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BiometricAuthEvent {
    data object ShowBiometricPrompt : BiometricAuthEvent()
    data object NavigateToEnrollment : BiometricAuthEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<BiometricAuthEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _action = Channel<LoginAuthScreenAction>(capacity=Channel.BUFFERED) /*TODO:Read*/
    val action = _action.receiveAsFlow()

    private lateinit var biometricAuthenticator: BiometricAuthenticator

    fun onEvent(loginAuthScreenEvent: LoginAuthScreenEvent,context: Context){
        when(loginAuthScreenEvent){
            is LoginAuthScreenEvent.PerformLogin -> {onLoginClicked(_uiState.value.usernameField,
                _uiState.value.passwordField, context)}
            is LoginAuthScreenEvent.UsernameChanged -> {
                _uiState.update { it.copy(usernameField = loginAuthScreenEvent.username) }
            }
            is LoginAuthScreenEvent.PasswordChanged -> {
                _uiState.update { it.copy(passwordField = loginAuthScreenEvent.password) }
            }
            else -> Unit
        }
    }

    suspend fun verifyLoginDetails(username: String, password: String): Boolean {
        val user = userUseCase.verifyLogin(username = username, password = password)
        return if (user != null) {
            _uiState.update { it.copy(usernameField = username, passwordField = password, loginError = false) }
            Log.d("AuthViewModel", "User login details verified successfully for: ${user.username}")
            true
        } else {
            _uiState.update { it.copy(loginError = true, loginErrorMessage = "Invalid username or password") }
            Log.e("AuthViewModel", "Invalid credentials for username: $username")
            false
        }
    }

    suspend fun verifyBiometric(context: Context){
        biometricAuthenticator = BiometricAuthenticator(context)

        when (biometricAuthenticator.isBiometricAvailable()) {
            BiometricAuthResult.AUTHENTICATION_NOT_NEEDED -> {
                biometricAuthenticator.promptToAuthenticate()
            }
            BiometricAuthResult.ENROLLMENT_NEEDED -> {
                viewModelScope.launch {
                    biometricAuthenticator.promptEnrollBiometric(context)
                }
            }
            BiometricAuthResult.NOT_AVAILABLE -> {
                _uiState.update { it.copy(biometricError = true, biometricErrorMessage = "Biometric authentication is not available on this device.") }
            }
            else -> Unit
        }

        biometricAuthenticator.authResultFlow
            .onEach { result ->
                when (result) {
                    is BiometricAuthResult.AUTHENTICATION_SUCCESS -> {
                        sendAction(action = LoginAuthScreenAction.LoginSuccess(
                            username =_uiState.value.usernameField,password = _uiState.value.passwordField))
//                        _uiState.update { it.copy( biometricError = false) }
                    }
                    is BiometricAuthResult.AUTHENTICATION_FAILED, is BiometricAuthResult.AUTHENTICATION_ERROR -> {
                        _uiState.update { it.copy(biometricError = true, biometricErrorMessage = "Biometric Authentication failed.") }
                    }
                    else -> Unit
                }
            }.launchIn(viewModelScope)
    }

    fun onLoginClicked(username:String, password:String,context: Context) {
        viewModelScope.launch{
            val loginSuccessful = verifyLoginDetails(username, password)
            if (loginSuccessful) {
                verifyBiometric(context)
            }
        }
    }

    private fun sendAction(action: LoginAuthScreenAction){
        _action.trySend(action)
    }

}