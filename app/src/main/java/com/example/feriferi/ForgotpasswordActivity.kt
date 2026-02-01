package com.example.feriferi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ForgotPasswordScreen {

                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(onSendClick: () -> Unit) {

    var email by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)) // grey background
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Forget Password",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { finish() }) {
            Text("Back to sign in")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSendClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }
    }
}

private fun ColumnScope.finish() {
    TODO("Not yet implemented")
}


@Preview
@Composable
fun PreviewForgotPassword() {
    ForgotPasswordScreen {

    }
}
