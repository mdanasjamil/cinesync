package com.example.cinesync.uiApp.Login.Biometric
import android.content.Context
import android.content.Intent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import android.provider.Settings
import android.util.Log

// This class handles all direct interaction with the BiometricPrompt API
class BiometricAuthenticator(
    private val context: Context
) {
    private val _authResultChannel = Channel<BiometricAuthResult>()
    val authResultFlow = _authResultChannel.receiveAsFlow()

    fun isBiometricAvailable(): BiometricAuthResult {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthResult.AUTHENTICATION_NOT_NEEDED
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthResult.ENROLLMENT_NEEDED
            else -> BiometricAuthResult.NOT_AVAILABLE
        }
    }

    fun promptToAuthenticate() {
        val activity = context as FragmentActivity
        val executor = ContextCompat.getMainExecutor(activity)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your biometric credential")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    _authResultChannel.trySend(BiometricAuthResult.AUTHENTICATION_SUCCESS)
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    _authResultChannel.trySend(BiometricAuthResult.AUTHENTICATION_FAILED)
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                        _authResultChannel.trySend(BiometricAuthResult.AUTHENTICATION_ERROR)
                    }
                }
            })

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

sealed class BiometricAuthResult {
    data object AUTHENTICATION_SUCCESS : BiometricAuthResult()
    data object AUTHENTICATION_FAILED : BiometricAuthResult()
    data object AUTHENTICATION_ERROR : BiometricAuthResult()
    data object AUTHENTICATION_NOT_NEEDED : BiometricAuthResult()
    data object NOT_AVAILABLE : BiometricAuthResult()
    data object ENROLLMENT_NEEDED: BiometricAuthResult()
}