package com.example.shilpakalaapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Combined Login & Register Screen
        composable("login") {
            AuthScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Main Dashboard
        composable("dashboard") {
            DashboardScreen(
                navController = navController, 
                viewModel = viewModel
            )
        }
        
        // Upload Screen (Camera + Gallery + Bitmap Processing)
        composable("upload") {
            UploadScreen(
                viewModel = viewModel,
                onUploadSuccess = {
                    navController.navigate("gallery")
                }
            )
        }
        
        // Public Gallery
        composable("gallery") {
            GalleryScreen(viewModel = viewModel)
        }
    }
}
