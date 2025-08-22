package com.example.cinesync.data.ApiService

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.cinesync.MainActivity
import android.provider.Settings

class BiometricDataSource(private val context: Context) {
    fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL

        return when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("BiometricDataSource", "App can authenticate using biometrics or device credential.")
                true
            }
            else -> {
                Log.d("BiometricDataSource", "No biometrics or device credential available.")
                false
            }
        }
    }

    fun showBiometricPrompt(activity: FragmentActivity, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("BiometricDataSource", "Authentication error: $errString")
                onFailure()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("BiometricDataSource", "Authentication succeeded!")
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BiometricDataSource", "Authentication failed")
                onFailure()
            }

        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("BiometricAuthentication")
            .setSubtitle("Log in using your biometric credential")
//            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun promptEnrollBiometric(context: Context){
        try {
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
            }
            if (context is FragmentActivity){
                context.startActivity(enrollIntent)
            }
        } catch (e: Exception) {
            Log.e("BiometricDataSource", "Error during biometric enrollment", e)
            val fallbackIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
            if(context is FragmentActivity){
                context.startActivity(fallbackIntent)
            }
        }

    }

}