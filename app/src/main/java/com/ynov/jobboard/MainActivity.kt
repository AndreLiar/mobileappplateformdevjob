package com.ynov.jobboard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    // ✅ Hoist the ViewModel at the Activity level
    private val jobViewModel: JobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Firebase
        FirebaseApp.initializeApp(this)
        Log.d("FIREBASE", "✅ Firebase initialized")

        // ✅ Set Compose UI with Navigation
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    // ✅ Single NavHost using the same ViewModel instance
                    NavHost(navController = navController, startDestination = "jobs") {
                        composable("jobs") {
                            JobsScreen(navController = navController, viewModel = jobViewModel)
                        }
                        composable("feedback") {
                            FeedbackScreen(navBack = { navController.popBackStack() })
                        }
                        composable(
                            "jobDetail/{jobId}",
                            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId")
                            JobDetailScreen(jobId = jobId, viewModel = jobViewModel)
                        }
                    }
                }
            }
        }
    }
}
