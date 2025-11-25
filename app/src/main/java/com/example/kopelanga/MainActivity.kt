package com.example.kopelanga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kopelanga.ui.navigation.NavGraph
import com.example.kopelanga.ui.theme.KopelangaTheme

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainer(this)
        enableEdgeToEdge()
        setContent {
            KopelangaTheme {
                NavGraph(appContainer.viewModelFactory)
            }
        }
    }
}