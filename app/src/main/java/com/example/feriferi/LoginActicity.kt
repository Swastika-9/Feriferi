package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickableQ
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.ui.theme.FeriferiTheme

// ✅ Custom Colors (add these if missing)
val PrimaryDarkBrown = Color(0xFF5D4037)
val GoogleBlue = Color(0xFF4285F4)
val LightTanBackground = Color(0xFFF5F0EB)

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FeriferiTheme {
                LoginPageUi()
            }
        }
    }
}

@Composable
fun LoginPageUi() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(64.dp))

            // ✅ LOGO / TITLE
            Text(
                text = "फेरिPheri",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryDarkBrown,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // ✅ EMAIL
            Text(
                text = "Email",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            StyledTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ PASSWORD
            Text(
                text = "Password",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            StyledTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )

            // ✅ FORGOT PASSWORD
            TextButton(
                onClick = { },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot password?", color = PrimaryDarkBrown)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ✅ LOGIN BUTTON
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryDarkBrown),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Log in", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "OR", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ GOOGLE LOGIN
            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "G",
                    color = GoogleBlue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Log in with Google")
            }

            Spacer(modifier = Modifier.weight(1f))

            // ✅ SIGN UP
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an Account? ", color = Color.Black)
                Text(
                    text = "Sign Up",
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }
        }
    }
}

// ✅ CUSTOM TEXT FIELD
@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = LocalTextStyle.current.copy(color = Color.DarkGray, fontSize = 16.sp),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(LightTanBackground)
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label,
                    tint = PrimaryDarkBrown,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(text = label, color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginPageUi() {
    LoginPageUi()
}
