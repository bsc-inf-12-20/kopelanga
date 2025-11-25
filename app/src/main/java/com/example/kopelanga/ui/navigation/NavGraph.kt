package com.example.kopelanga.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kopelanga.ui.screens.AboutScreen
import com.example.kopelanga.ui.screens.AddEditNoteScreen
import com.example.kopelanga.ui.screens.DevelopersScreen
import com.example.kopelanga.ui.screens.FAQScreen
import com.example.kopelanga.ui.screens.NotesScreen
import com.example.kopelanga.ui.screens.PrivacyPolicyScreen
import com.example.kopelanga.ui.screens.TermsOfServiceScreen
import com.example.kopelanga.ui.screens.WelcomeScreen
import com.example.kopelanga.viewmodel.ViewModelFactory

@Composable
fun NavGraph(viewModelFactory: ViewModelFactory) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(onGetStartedClick = { navController.navigate("notes") })
        }
        composable("notes") {
            NotesScreen(
                viewModel = viewModel(factory = viewModelFactory),
                onNoteClick = { note -> navController.navigate("addEditNote?noteId=${note.id}") },
                onAddNoteClick = { navController.navigate("addEditNote") },
                onAboutClick = { navController.navigate("about") },
                onDevelopersClick = { navController.navigate("developers") },
                onFAQClick = { navController.navigate("faq") }
            )
        }
        composable(
            route = "addEditNote?noteId={noteId}",
            arguments = listOf(navArgument("noteId") { nullable = true; type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            AddEditNoteScreen(
                viewModel = viewModel(factory = viewModelFactory),
                noteId = noteId,
                onSaveClick = { navController.popBackStack() } 
            )
        }
        composable("about") {
            AboutScreen(
                onBackClick = { navController.popBackStack() },
                onPrivacyPolicyClick = { navController.navigate("privacyPolicy") },
                onTermsOfServiceClick = { navController.navigate("termsOfService") }
            )
        }
        composable("developers") {
            DevelopersScreen(onBackClick = { navController.popBackStack() })
        }
        composable("faq") {
            FAQScreen(onBackClick = { navController.popBackStack() })
        }
        composable("privacyPolicy") {
            PrivacyPolicyScreen(onBackClick = { navController.popBackStack() })
        }
        composable("termsOfService") {
            TermsOfServiceScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
