package com.example.feriferi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {

    val likedPosts = listOf(
        LikedPost("Jewellery Holder", R.drawable.item1),
        LikedPost("Three drawer set", R.drawable.item2),
        LikedPost("Antique Watch", R.drawable.item3),
        LikedPost("Linen set", R.drawable.item4)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F3))
            .padding(16.dp)
    ) {
        // ─────────── SEARCH BAR ───────────
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ─────────── PROFILE SECTION ───────────
        Row(verticalAlignment = Alignment.Top) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ─────────── CHOOSE FILE BUTTON ───────────
                OutlinedButton(
                    onClick = { /* TODO: open file picker */ },
                    modifier = Modifier
                        .height(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_upload_file_24), // replace with your upload icon
                        contentDescription = "Upload Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Choose File")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                ProfileField("Full Name", "Sienna Miller")
                ProfileField("Username", "siennam")
                ProfileField("Phone Number", "97000000")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ─────────── CHANGE PASSWORD ───────────
        Text(
            text = "Change Password",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            PasswordField(
                placeholder = "Current Password",
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            PasswordField(
                placeholder = "New Password",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ─────────── LIKED POSTS ───────────
        Text(
            text = "Liked Posts",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(likedPosts) { post ->
                LikedPostItem(post)
            }
        }
    }
}


@Composable
fun ProfileField(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.width(220.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
fun PasswordField(
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(placeholder) },
        modifier = modifier
    )
}

/* ───────────────── LIKED POSTS ───────────────── */

data class LikedPost(
    val title: String,
    val image: Int
)

@Composable
fun LikedPostItem(post: LikedPost) {
    Column {
        Image(
            painter = painterResource(id = post.image),
            contentDescription = post.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = post.title)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}