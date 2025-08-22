package com.example.cinesync.uiApp.Login

data class BiometricUiState (
    val usernameField: String = "",
    val passwordField: String = "",
    val loggedIn: Boolean = false,
    val askBiometryEnrollment: Boolean = false,
    val biometricAuthenticated: Boolean = false
){

}