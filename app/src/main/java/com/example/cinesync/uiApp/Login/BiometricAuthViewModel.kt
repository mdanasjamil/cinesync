package com.example.cinesync.uiApp.Login

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.cinesync.domain.UseCase.BiometricAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BiometricAuthViewModel @Inject constructor(
    private val authenticateUseCase: BiometricAuthUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(BiometricUiState())
    val uiState: StateFlow<BiometricUiState> = _uiState.asStateFlow()


    fun authenticate(context: Context){
        if(authenticateUseCase.isBiometricAvailable()){
            authenticateUseCase.showBiometricPrompt(
            context as FragmentActivity,
            onSuccess = { _uiState.update{it.copy(biometricAuthenticated = true, loggedIn = true)}
                        Log.d("BiometricAuthViewModel", "Biometric authentication successful")},
            onFailure = { _uiState.update{it.copy(biometricAuthenticated = false, loggedIn = false)}
                        Log.d("BiometricAuthViewModel", "Biometric authentication failed")}
            )
        }else{
            authenticateUseCase.promptEnrollBiometric(context)
            Log.e("BiometricAuthViewModel", "Biometric not available or not configured")
        }
    }
}