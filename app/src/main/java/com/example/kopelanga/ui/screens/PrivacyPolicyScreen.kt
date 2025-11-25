package com.example.kopelanga.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF7A48F5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF7A48F5))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Privacy Policy", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Last updated: November 22, 2023", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            Section(title = "Your Privacy Matters") {
                Text("At Kopelanga, privacy isn't just a feature—it's our foundation. We built this app with a user-centric approach to privacy, giving you complete control over your personal information.", color = Color.White)
            }
            Section(title = "Data Storage") {
                Text("All your notes, ideas, and memories are stored locally on your device. Our servers do not have access to your content, and we don't have a database storing your thoughts. Everything stays strictly where it belongs—with you.", color = Color.White)
            }
            Section(title = "What We Don’t Collect") {
                Text("We don't collect, share, or license your personal information, user-provided content or data. We don't track your behavior. We don't use your data for advertising. We don’t use analytics or advertising networks.", color = Color.White)
            }
            Section(title = "What We Do Collect") {
                Text("To improve our service, we log anonymized, aggregated, non-identifying data related to feature usage and crash data. This data is entirely optional and only happens when you initiate contact with us.", color = Color.White)
            }
            Section(title = "Security") {
                Text("Since your data never leaves your device, you have complete control over its security. We recommend using your device's built-in security features, like passwords, biometric authentication, and encryption.", color = Color.White)
            }
            Section(title = "Backups") {
                Text("If you choose to back up your notes through your device's backup system (like iCloud or Google Drive), those backups are subject to the respective platform's privacy policies. We don't control or access these backups.", color = Color.White)
            }
            Section(title = "Children's Privacy") {
                Text("Kopelanga is suitable for all ages. Since we don't collect any personal information, we can confirm that we never knowingly collect data from children using our app.", color = Color.White)
            }
            Section(title = "Changes to This Policy") {
                Text("If we ever make changes to this privacy policy, we'll notify you through the app and update the 'Last updated' date. Your continued use of Kopelanga after changes constitutes acceptance of the updated policy.", color = Color.White)
            }
            Section(title = "Contact Us") {
                Text("If you have any questions about this privacy policy or how Kopelanga works, please contact us at privacy@kopelanga.com.", color = Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Your trust means everything to us. We'll never compromise your privacy.", color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}