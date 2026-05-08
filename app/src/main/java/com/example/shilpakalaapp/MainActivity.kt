package com.example.shilpakalaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shilpakalaapp.data.AppDatabase
import com.example.shilpakalaapp.data.ImageRepository
import com.example.shilpakalaapp.ui.AppNavigation
import com.example.shilpakalaapp.ui.MainViewModel
import com.example.shilpakalaapp.ui.MainViewModelFactory
import com.example.shilpakalaapp.ui.theme.ShilpaKalaAppTheme

class MainActivity :
    ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Database, Repository and Factory once
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ImageRepository(database.imageDao())
        val factory = MainViewModelFactory(repository)

        enableEdgeToEdge()

        setContent {
            ShilpaKalaAppTheme {
                // Get the ViewModel using the factory
                val viewModel: MainViewModel = viewModel(factory = factory)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
