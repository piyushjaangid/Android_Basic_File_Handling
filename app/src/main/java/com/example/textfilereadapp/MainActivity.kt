package com.example.textfilereadapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.textfilereadapp.ui.theme.TextFileReadAppTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextFileReadAppTheme {
                FileReaderApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileReaderApp() {
    var fileContent by remember { mutableStateOf("No file selected") }
    val context = LocalContext.current

    // Launcher to handle file selection
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            fileContent = readFileContent(uri, context)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Text File Reader") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { filePickerLauncher.launch(arrayOf("text/plain")) },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Select Text File")
            }
            Text(
                text = fileContent,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun readFileContent(uri: Uri, context: Context): String {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readText()
            }
        } ?: "Unable to read file"
    } catch (e: Exception) {
        "Error reading file: ${e.message}"
    }
}

@Preview(showBackground = true)
@Composable
fun FileReaderAppPreview() {
    TextFileReadAppTheme {
        FileReaderApp()
    }
}
