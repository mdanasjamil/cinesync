package com.example.cinesync.uiApp.Login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BiometricAuthEvent {
    data object ShowBiometricPrompt : BiometricAuthEvent()
    data object NavigateToEnrollment : BiometricAuthEvent()
}

@HiltViewModel
class BiometricAuthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BiometricUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<BiometricAuthEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private lateinit var biometricAuthenticator: BiometricAuthenticator

    fun onLoginClicked(context: Context) {
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
                _uiState.update { it.copy(error = "Biometric authentication is not available on this device.") }
            }
            else -> Unit
        }

        biometricAuthenticator.authResultFlow
            .onEach { result ->
                when (result) {
                    is BiometricAuthResult.AUTHENTICATION_SUCCESS -> {
                        _uiState.update { it.copy(loggedIn = true) }
                    }
                    is BiometricAuthResult.AUTHENTICATION_FAILED, is BiometricAuthResult.AUTHENTICATION_ERROR -> {
                        _uiState.update { it.copy(loggedIn = false, error = "Authentication failed.") }
                    }
                    else -> Unit
                }
            }.launchIn(viewModelScope)
    }
}