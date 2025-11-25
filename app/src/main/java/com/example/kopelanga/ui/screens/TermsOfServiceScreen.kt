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
fun TermsOfServiceScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms of Service") },
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
            Text("Terms of Service", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Last updated: November 22, 2023", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            Section(title = "Agreement to Terms") {
                Text("By downloading and using Kopelanga, you agree to be bound by these Terms of Service. If you do not agree with these terms, please do not use our app.", color = Color.White)
            }
            Section(title = "Description of Service") {
                Text("Kopelanga is a local-first note-taking application designed to help you capture and organize your thoughts, ideas, and memories. Our service is provided as-is, and we do not make any guarantees about the availability or reliability of our app.", color = Color.White)
            }
            Section(title = "Your Responsibilities") {
                Text("You are solely responsible for maintaining the security of your device and the confidentiality of your data. You agree to use Kopelanga only for lawful purposes and in accordance with these terms. You are responsible for any content you create and manage through our app.", color = Color.White)
            }
            Section(title = "Intellectual Property") {
                Text("Kopelanga and its original content, features, and functionality are owned by us and are protected by international copyright, trademark, and other intellectual property laws. Your notes are your own content, and we claim no ownership over what you create.", color = Color.White)
            }
            Section(title = "License to Use") {
                Text("We grant you a personal, non-exclusive, non-transferable license to use Kopelanga on a single device that you own and control. You agree not to copy, modify, distribute, sell, or lease any part of our service or attempt to extract the source code of our app.", color = Color.White)
            }
            Section(title = "Disclaimer of Warranties") {
                Text("Kopelanga is provided 'as is' and 'as available', without any warranties, express or implied, including the warranty of merchantability, fitness for a particular purpose, or non-infringement. We don’t guarantee that our app will be uninterrupted, secure, or error-free. We don’t warrant that the results of using our app will be accurate or reliable.", color = Color.White)
            }
            Section(title = "Limitation of Liability") {
                Text("To the fullest extent permitted by law, Kopelanga shall not be liable for any indirect, incidental, special, consequential, or punitive damages, or any loss of profits or revenues, whether incurred directly or indirectly, or any loss of data or use.", color = Color.White)
            }
            Section(title = "Data Loss") {
                Text("Since Kopelanga is designed to keep your data on your device, you acknowledge that data loss can occur due to device failure, software errors, or other unforeseen circumstances. We are not responsible for lost notes or data. We strongly recommend that you regularly back up your device through your device’s backup system.", color = Color.White)
            }
            Section(title = "Updates and Changes") {
                Text("We may update Kopelanga from time to time to add features, fix bugs, or improve performance. We may also modify these Terms of Service at any time. We will notify you of any changes to our app or by updating the ‘Last updated’ date.", color = Color.White)
            }
            Section(title = "Termination") {
                Text("You can terminate your use of our app at any time by deleting the app from your device. We may terminate or suspend our service without notice if we believe you have violated these Terms of Service. Upon termination, your right to use the app will cease, and you must delete the app for your device.", color = Color.White)
            }
            Section(title = "Governing Law") {
                Text("These Terms are governed by and construed in accordance with the laws of the jurisdiction in which Kopelanga is based, without regard to its conflict of law provisions. Any disputes that cannot be resolved in accordance with the laws of the jurisdiction shall be subject to arbitration.", color = Color.White)
            }
            Section(title = "Contact Information") {
                Text("If you have any questions about these Terms of Service, please contact us at legal@kopelanga.com.", color = Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Thank you for choosing Kopelanga. We're honored to be your partner in capturing your thoughts.", color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
    }
}