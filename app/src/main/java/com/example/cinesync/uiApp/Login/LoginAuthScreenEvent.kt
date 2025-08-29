package com.example.cinesync.uiApp.Login

sealed interface LoginAuthScreenEvent {
    data object NavigateToSignupScreen : LoginAuthScreenEvent
    data object PerformLogin : LoginAuthScreenEvent
    data class UsernameChanged(val username: String) : LoginAuthScreenEvent
    data class PasswordChanged(val password: String) : LoginAuthScreenEvent

}