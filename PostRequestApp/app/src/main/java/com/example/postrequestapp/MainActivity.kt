package com.example.postrequestapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.example.postrequestapp.ui.theme.PostRequestAppTheme
import java.io.File


class MainActivity : ComponentActivity() {
    private var authToken = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostRequestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PostRequestScreen(
                        authToken = authToken,
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
                authToken = fragment
                Log.d("CustomScheme", "Fragment: $fragment")
                // Use the fragment value here

                initiatePostRequest(authToken, this@MainActivity)
            } else {
                Log.d("CustomScheme", "No fragment found")
            }
        }
    }
}

@Composable
fun PostRequestScreen(authToken: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { initiatePostRequest(authToken, context) }, modifier = modifier) {
            Text(text = "Initiate Post", modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

private fun initiatePostRequest(authToken: String, context: android.content.Context) {
    try {
        val htmlForm = """
                <html>
                <body onload='document.forms[0].submit()'>
                    <form action='https://test-echo.free.beeceptor.com' method='post'>
                        <input type='hidden' name='authToken' value='$authToken'>
                        <input type='hidden' name='param1' value='value1'>
                        <input type='hidden' name='param2' value='value2'>
                    </form>
                    <!-- Fallback if JavaScript is disabled -->
                    <noscript>
                        <p>Please click submit to continue:</p>
                        <input type='submit' value='Submit'>
                    </noscript>
                </body>
                </html>
            """.trimIndent()

        val tempFile = File.createTempFile("post_form", ".html", context.cacheDir).apply {
            writeText(htmlForm)
        }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)

        Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "text/html")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(context, this, null)
        }

    } catch (e: Exception) {
        Toast.makeText(context, "Error initiating request", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PostRequestScreenPreview() {
    PostRequestAppTheme {
        PostRequestScreen(authToken = "123456")
    }
}