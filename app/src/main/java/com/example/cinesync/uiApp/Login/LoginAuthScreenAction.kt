package com.example.cinesync.uiApp.Login

sealed interface LoginAuthScreenAction {
    data class LoginSuccess(val username:String, val password:String): LoginAuthScreenAction
}