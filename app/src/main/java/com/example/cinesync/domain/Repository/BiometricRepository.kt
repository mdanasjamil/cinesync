package com.example.cinesync.domain.Repository

import android.content.Context
import androidx.fragment.app.FragmentActivity

interface BiometricRepository {
    fun isBiometricAvailable(): Boolean
    fun promptEnrollBiometric(context: Context)
    fun showBiometricPrompt(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )
}