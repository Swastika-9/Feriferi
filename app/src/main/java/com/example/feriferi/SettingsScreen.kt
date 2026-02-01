package com.example.feriferi.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.feriferi.R
import com.example.feriferi.model.LikedProducts
import com.example.feriferi.model.ProductModel
import com.example.feriferi.repository.FavoriteRepository
import com.example.feriferi.repository.ProductRepoImpl
import com.example.feriferi.repository.UserRepoImpl
import kotlinx.coroutines.launch

// Specific brown color from the "फेरिPheri" image
val AppBrandColor = Color(0xFF5D4037)

@Composable
fun SettingsScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val favoriteRepo = remember { FavoriteRepository() }
    val productRepo = remember { ProductRepoImpl() }
    val userRepo = remember { UserRepoImpl() }

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var likedProducts by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var isUploadingImage by remember { mutableStateOf(false) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isUploadingImage = true
            scope.launch {
                val uploadedUrl = userRepo.uploadProfileImage(it)
                if (uploadedUrl != null) {
                    profileImageUrl = uploadedUrl
                    Toast.makeText(context, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
                isUploadingImage = false
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            // Get current user data
            val user = userRepo.getCurrentUser()
            if (user != null) {
                fullName = user.fullName
                username = user.username
                phoneNumber = user.phoneNumber
                profileImageUrl = user.profileImageUrl
                userId = user.userId
            }

            val likedList = favoriteRepo.getFavorites()
            val likedIds = likedList.map { it.productId }

            productRepo.getAllProduct { success, _, products ->
                if (success && products != null) {
                    likedProducts = products.filter { it.id in likedIds }
                }
                isLoading = false
            }
        }
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                showPasswordDialog = false
                currentPassword = ""
                newPassword = ""
                confirmPassword = ""
            },
            title = { Text("Change Password") },
            text = {
                Column {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        when {
                            currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            }
                            newPassword != confirmPassword -> {
                                Toast.makeText(context, "New passwords don't match", Toast.LENGTH_SHORT).show()
                            }
                            newPassword.length < 6 -> {
                                Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                userRepo.changePassword(newPassword,
                                    onSuccess = {
                                        Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                        showPasswordDialog = false
                                        currentPassword = ""
                                        newPassword = ""
                                        confirmPassword = ""
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppBrandColor)
                ) {
                    Text("Change Password")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPasswordDialog = false
                    currentPassword = ""
                    newPassword = ""
                    confirmPassword = ""
                }) {
                    Text("Cancel", color = AppBrandColor)
                }
            }
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AppBrandColor)
        }
        return
    }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            // Profile Image with upload button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    if (profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Default Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                    }

                    if (isUploadingImage) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.Center),
                            color = AppBrandColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = AppBrandColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(120.dp),
                    enabled = !isUploadingImage
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_upload_file_24),
                        contentDescription = "Choose File",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Choose file", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                EditableProfileField("Full Name", fullName) { fullName = it }
                EditableProfileField("Username", username) { username = it }
                EditableProfileField("Phone Number", phoneNumber) { phoneNumber = it }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (userId.isEmpty()) {
                            Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isSaving = true

                        val updates = hashMapOf<String, Any>(
                            "fullName" to fullName,
                            "phoneNumber" to phoneNumber
                        )

                        userRepo.updateSellerProfile(userId, updates) { success, message ->
                            isSaving = false
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.width(220.dp),
                    enabled = !isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppBrandColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Save Changes", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showPasswordDialog = true },
                    modifier = Modifier.width(220.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AppBrandColor
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AppBrandColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_lock_24),
                        contentDescription = "Change Password",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Password")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Logout Button
                OutlinedButton(
                    onClick = {
                        userRepo.logout()
                        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    },
                    modifier = Modifier.width(220.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFDC2626)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_logout_24),
                        contentDescription = "Logout",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ───── FAVORITE SECTION ───── */
        Text("Favorite", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(12.dp))

        if (likedProducts.isEmpty()) {
            Text("No favorite items yet", color = Color.Gray)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(400.dp)
            ) {
                items(likedProducts) { product ->
                    LikedProductItem(
                        product = product,
                        onUnlike = {
                            scope.launch {
                                favoriteRepo.toggleFavorite(
                                    LikedProducts(productId = product.id),
                                    isFavorite = false
                                )
                                likedProducts = likedProducts.filter { it.id != product.id }
                                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onClick = {
                            context.startActivity(
                                Intent(context, ItemDescriptionActivity::class.java)
                                    .putExtra("productId", product.id)
                            )
                        }
                    )
                }
            }
        }
    }
}

/* ───────── COMPONENTS ───────── */

@Composable
fun EditableProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label, style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.width(220.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppBrandColor,
                unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
fun LikedProductItem(
    product: ProductModel,
    onUnlike: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            Column {
                AsyncImage(
                    model = product.imageUrls.firstOrNull(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(product.name, fontWeight = FontWeight.SemiBold)
                    Text(
                        "Rs. ${product.price}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(
                onClick = onUnlike,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_favorite_24),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}