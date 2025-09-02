package com.example.cinesync.uiApp.Signup

sealed interface SignupScreenEvent{
    data object NavigateToLoginScreen: SignupScreenEvent
    data object PerformSignup: SignupScreenEvent

    data class NameChanged(val name: String): SignupScreenEvent
    data class UsernameChanged(val username: String): SignupScreenEvent
    data class PasswordChanged(val password: String): SignupScreenEvent
    data class ConfirmPasswordChanged(val confirmPassword: String): SignupScreenEvent
}