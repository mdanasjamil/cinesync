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
import com.example.cinesync.uiApp.Signup.SignupScreen
import com.example.cinesync.uiApp.Signup.SignupScreenAction
import com.example.cinesync.uiApp.Signup.SignupScreenEvent
import com.example.cinesync.uiApp.Signup.SignupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

fun NavGraphBuilder.addSignupScreen(
    navController: NavController,
) {
    composable(Screens.SignupScreen.name) { backStackEntry ->
        val viewModel = hiltViewModel<SignupViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current


        LaunchedEffect(lifecycleOwner) {
            lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED){
                withContext(context = Dispatchers.Main.immediate){
                    viewModel.action.collectLatest { action ->
                        when (action){
                            is SignupScreenAction.SignupSuccess -> {
                                navController.navigate(Screens.LoginScreen.name)
                            }
                            else -> Unit
                        }
                    }

                }
            }

        }
        SignupScreen(
            uiState = uiState,
            event = {event->viewModel.onEvent(event)}
        )
    }

}