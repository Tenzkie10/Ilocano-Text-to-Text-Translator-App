package com.example.ilocanotext_to_texttranslator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ilocanotext_to_texttranslator.ui.theme.IlocanoTexttoTextTranslatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        val splashScreen: SplashScreen = installSplashScreen()

        enableEdgeToEdge()
        setContent {
            IlocanoTexttoTextTranslatorTheme {
                Scaffold(
                    topBar = {
                        TopBar()
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // Convert arrays to lists and flatten them
                    val englishArrays = listOf(
                        resources.getStringArray(R.array.greetings_general_english).toList(),
                        resources.getStringArray(R.array.basic_greetings_english).toList(),
                        resources.getStringArray(R.array.compliments_english).toList(),
                        resources.getStringArray(R.array.directions_english).toList(),
                        resources.getStringArray(R.array.things_english).toList(),
                        resources.getStringArray(R.array.dining_english).toList(),
                        resources.getStringArray(R.array.emergency_english).toList(),
                        resources.getStringArray(R.array.directions_english).toList(),
                        resources.getStringArray(R.array.greetings_english).toList(),
                        resources.getStringArray(R.array.weather_english).toList(),
                        resources.getStringArray(R.array.family_english).toList(),
                        resources.getStringArray(R.array.numbers_english).toList(),
                        resources.getStringArray(R.array.days_english).toList(),
                        resources.getStringArray(R.array.pronoun_english).toList(),
                        resources.getStringArray(R.array.conj_english).toList()
                    ).flatten()

                    val ilocanoArrays = listOf(
                        resources.getStringArray(R.array.greetings_general_ilocano).toList(),
                        resources.getStringArray(R.array.basic_greetings_ilocano).toList(),
                        resources.getStringArray(R.array.compliments_ilocano).toList(),
                        resources.getStringArray(R.array.directions_ilocano).toList(),
                        resources.getStringArray(R.array.things_ilocano).toList(),
                        resources.getStringArray(R.array.directions_ilocano).toList(),
                        resources.getStringArray(R.array.emergency_ilocano).toList(),
                        resources.getStringArray(R.array.directions_ilocano).toList(),
                        resources.getStringArray(R.array.greetings_ilocano).toList(),
                        resources.getStringArray(R.array.weather_ilocano).toList(),
                        resources.getStringArray(R.array.family_ilocano).toList(),
                        resources.getStringArray(R.array.numbers_ilocano).toList(),
                        resources.getStringArray(R.array.days_ilocano).toList(),
                        resources.getStringArray(R.array.pronoun_ilocano).toList(),
                        resources.getStringArray(R.array.conj_ilocano).toList()
                    ).flatten()
                    // Pass it to the TranslatorLayout function
                    TranslatorLayout(
                        modifier = Modifier.padding(innerPadding),
                        englishArray = englishArrays.toTypedArray(),
                        ilocanoArray = ilocanoArrays.toTypedArray()
                    )
                }
            }
        }
    }
}

// TopBar with App name
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Ilocano Text-to-Text Translator") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

// Translator Layout Function
@Composable
fun TranslatorLayout(
    modifier: Modifier = Modifier,
    englishArray: Array<String>,
    ilocanoArray: Array<String>
) {
    // Remember state for the input text and translation
    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input TextField
        @OptIn(ExperimentalMaterial3Api::class)
        TextField(
            value = inputText,
            onValueChange = { text ->
                inputText = text
                isLoading = true // Start loading when input changes
                translatedText = ""
            },
            label = { Text("Enter text") },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // Trigger translation with a delay
        LaunchedEffect(inputText) {
            if (inputText.isNotBlank()) {
                kotlinx.coroutines.delay(1000) // 1-second delay
                translatedText = translateText(inputText, englishArray, ilocanoArray)
                isLoading = false
            } else {
                isLoading = false // No loading if input is blank
                translatedText = ""
            }
        }

        // Show loading spinner or translation result
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.primary
            )
        } else if (translatedText.isNotBlank()) {
            Text(
                text = "Translation: $translatedText",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Translation logic using the provided arrays
fun translateText(input: String, englishArray: Array<String>, ilocanoArray: Array<String>): String {
    // Normalize the input to lowercase, trim extra spaces, and remove punctuation
    val normalizedInput = input.trim().lowercase().replace(Regex("[^a-zA-Z0-9\\s]"), "")

    // Search for the first match in the English array, ignoring case and whitespace
    val index = englishArray.indexOfFirst {
        it.trim().lowercase().replace(Regex("[^a-zA-Z0-9\\s]"), "") == normalizedInput
    }

    return if (index != -1 && index < ilocanoArray.size) {
        ilocanoArray[index] // Return the corresponding Ilocano translation
    } else {
        "Translation not found"
    }
}

// Just a preview for the layout
@Preview(showBackground = true)
@Composable
fun TranslatorLayoutPreview() {
    IlocanoTexttoTextTranslatorTheme {
        val englishArray = listOf(
            "Hello", "Good morning", "Good afternoon", "Thank you",
            "How are you", "What's your name", "Good night"
        ).toTypedArray()

        val ilocanoArray = listOf(
            "Kablaaw", "Naimbag a bigat", "Naimbag a malem", "Agyamanak",
            "Kumusta ka?", "Ania ti nagan mo?", "Naimbag a rabii"
        ).toTypedArray()

        TranslatorLayout(
            englishArray = englishArray,
            ilocanoArray = ilocanoArray
        )
    }
}