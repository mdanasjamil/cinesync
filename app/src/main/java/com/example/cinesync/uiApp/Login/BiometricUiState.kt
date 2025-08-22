package com.example.cinesync.uiApp.Login

data class BiometricUiState (
    val usernameField: String = "",
    val passwordField: String = "",
    val loggedIn: Boolean = false,
    val error:String = ""
){

}