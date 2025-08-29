package com.example.cinesync.uiApp.Login
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.cinesync.uiApp.Navigation.Screens
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.cinesync.data.Database.User
import com.example.cinesync.uiApp.Navigation.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

fun NavGraphBuilder.addLoginAuthScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    composable(Screens.LoginScreen.name) { backStackEntry ->
        val viewModel = hiltViewModel<AuthViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

        /*TODO shared view model in nav Graph implement*/


        LaunchedEffect(lifecycleOwner) {
            lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED)
            {
                withContext(context = Dispatchers.Main.immediate) {
                    viewModel.action.collectLatest { action ->
                        when (action){
                            is LoginAuthScreenAction.LoginSuccess -> {
                                navController.navigate(Screens.SearchScreen.name)
                                sharedViewModel.setUser(
                                    User(
                                        username = action.username,
                                        password = action.password
                                    )
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
        LoginScreen(
            uiState = uiState,

            event = {event ->
                when(event){
                    is LoginAuthScreenEvent.NavigateToSignupScreen -> {
                        navController.navigate(Screens.SignupScreen.name)
                    }
                    else -> viewModel.onEvent(event,context)
                }
            }
        )

    }

}