package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class VerificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                VerificationScreen {
                    startActivity(
                        Intent(this, NewPasswordActivity::class.java)
                    )
                }
            }
        }
    }
}

@Composable
fun VerificationScreen(onVerifyClick: () -> Unit) {

    var c1 by remember { mutableStateOf("") }
    var c2 by remember { mutableStateOf("") }
    var c3 by remember { mutableStateOf("") }
    var c4 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)) // grey background
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Verification",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Enter Verification Code")

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            listOf(c1, c2, c3, c4).forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        when (index) {
                            0 -> c1 = it
                            1 -> c2 = it
                            2 -> c3 = it
                            3 -> c4 = it
                        }
                    },
                    modifier = Modifier
                        .width(56.dp)
                        .padding(4.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { }) {
            Text("Resend")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onVerifyClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }
    }
}

@Preview
@Composable
fun PreviewVerification() {
    VerificationScreen {

    }
}






