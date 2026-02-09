package com.example.myapplication.presentation.feature.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About BiblioGo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                // App Logo / Icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                Image(
                    painter = painterResource(id = R.drawable.appicon),
                    contentDescription = "App Logo",
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .size(100.dp),
                    contentScale = ContentScale.Crop
                )}

                Spacer(modifier = Modifier.height(16.dp))

                // App Title & Description
                Text(
                    text = "BiblioGo",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A simple book search and save app built with Jetpack Compose, Open Library API, and Room DB and Dagger-Hilt dependency injection." +
                            "The user can search books using the search field. If the title, author or the book cover is to the user liking the user can view its details by clicking on the card." +
                            "In the details screen there is a short description about the book. Moreover the user can save/favorite a book so that the user can check it offline as well." ,

                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Creator Info
                Text(
                    text = "Built by Barischin Miklós",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                //Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.height(32.dp))

                // Optional: GitHub Link or other buttons
                val uriHandler = LocalUriHandler.current

                TextButton(
                    onClick = {
                        uriHandler.openUri("https://github.com/VIAUMB02/viaumb02-2025-android-bmiki01")
                    },
                ) {
                    Text(text = "View on GitHub", textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
