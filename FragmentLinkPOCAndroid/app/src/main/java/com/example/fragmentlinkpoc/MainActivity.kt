package com.example.fragmentlinkpoc

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fragmentlinkpoc.ui.theme.FragmentLinkPOCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FragmentLinkPOCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        handleCustomUrl()
    }

    private fun handleCustomUrl() {
        val intent = intent
        val data: Uri? = intent.data
        if (data != null) {
            val fragment = data.fragment
            if (fragment != null) {
                Log.d("CustomScheme", "Fragment: $fragment")
                // Use the fragment value here
            } else {
                Log.d("CustomScheme", "No fragment found")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FragmentLinkPOCTheme {
        Greeting("Android")
    }
}