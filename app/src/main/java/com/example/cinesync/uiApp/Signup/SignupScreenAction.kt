package com.example.cinesync.uiApp.Signup

sealed interface SignupScreenAction {
    data class SignupSuccess(val username:String, val password:String): SignupScreenAction
}