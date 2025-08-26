package com.example.cinesync.uiApp.Signup

data class SignupUiState (
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: Boolean = false,
    val signedUp: Boolean = false
){
}