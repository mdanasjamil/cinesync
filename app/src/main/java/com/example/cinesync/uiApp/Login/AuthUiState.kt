package com.example.cinesync.uiApp.Login

data class AuthUiState (
    val usernameField: String = "",
    val passwordField: String = "",
    val loggedInAndVerifed: Boolean = false,
    val loginErrorMessage:String = "",
    val biometricErrorMessage:String = "",
    val loginError:Boolean = false,
    val biometricError: Boolean = false
){

}