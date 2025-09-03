package com.example.chatapp.core.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.chatscreen.presenation.ChatScreen
import com.example.chatapp.chatscreen.presenation.ChatViewModel
import com.example.chatapp.homescreen.presenation.HomeScreen
import com.example.chatapp.homescreen.presenation.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val viewModel = hiltViewModel<HomeViewModel>()
            val state by viewModel.state.collectAsState()
            HomeScreen(
                onUserClick = { recieverId ->
                    navController.navigate("user_detail/${recieverId}")
                }, state = state, onEvent = viewModel :: onEvent
            )
        }
        composable(
            route = "user_detail/{userId}",
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ChatViewModel>()

            val state by viewModel.state.collectAsState()

            val recieverId = backStackEntry.arguments?.getString("userId")
            ChatScreen(recieverId = recieverId ?: "", onBackCLick = {
                navController.popBackStack()
            }, onEvent = viewModel::onEvent, state = state)
        }
    }
}
