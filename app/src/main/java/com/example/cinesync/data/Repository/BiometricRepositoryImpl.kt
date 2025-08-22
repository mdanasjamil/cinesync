package com.example.cinesync.data.Repository

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.cinesync.data.ApiService.BiometricDataSource
import com.example.cinesync.domain.Repository.BiometricRepository

class BiometricRepositoryImpl (private val dataSource: BiometricDataSource): BiometricRepository{

    override fun isBiometricAvailable(): Boolean {
        return dataSource.isBiometricAvailable()
    }

    override fun showBiometricPrompt(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        dataSource.showBiometricPrompt(activity, onSuccess, onFailure)
    }

    override fun promptEnrollBiometric(context: Context) {
        dataSource.promptEnrollBiometric(context)
    }
}