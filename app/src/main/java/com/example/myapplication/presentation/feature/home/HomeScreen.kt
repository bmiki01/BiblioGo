package  com.example.myapplication.presentation.feature.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun HomeScreen(onBookListClick: () -> Unit,
               onSavedBooksClick: () -> Unit,
               onAboutClick: () -> Unit
                ){
    val context = LocalContext.current
    Box() {

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Title
            Spacer(modifier = Modifier.height(64.dp)) // Add some top padding
            Text(
                text = "BiblioGo",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // Center Buttons
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onBookListClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Browse Books")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSavedBooksClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Saved Books")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAboutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "About")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = { throw RuntimeException("Test Crash")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ){
                Text("Crash me")
            }
        }

        /*Surface {
        Text(text = "Welcome to BiblioGo!")
    }*/
    }
}

/*@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}*/