package com.example.cinesync.domain.UseCase

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.cinesync.domain.Repository.BiometricRepository

class BiometricAuthUseCase(
    private val repository: BiometricRepository
) {
    fun isBiometricAvailable(): Boolean {
        return repository.isBiometricAvailable()
    }

    fun promptEnrollBiometric(context: Context) {
        repository.promptEnrollBiometric(context)
    }

    fun showBiometricPrompt(activity: FragmentActivity, onSuccess: () -> Unit, onFailure: () -> Unit){
        repository.showBiometricPrompt(activity, onSuccess, onFailure)
    }
}