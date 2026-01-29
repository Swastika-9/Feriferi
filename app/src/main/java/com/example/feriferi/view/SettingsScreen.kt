package com.example.feriferi.view

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.feriferi.R
import com.example.feriferi.model.LikedProducts
import com.example.feriferi.model.ProductModel
import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.FavoriteRepository
import com.example.feriferi.repository.ProductRepoImpl
import com.example.feriferi.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Repositories - now using your combined UserRepository!
    val userRepo = remember { UserRepository() }
    val favoriteRepo = remember { FavoriteRepository() }
    val productRepo = remember { ProductRepoImpl() }

    // User profile state
    var user by remember { mutableStateOf<UserModel?>(null) }
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Password change state
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordChangeMessage by remember { mutableStateOf("") }

    // Liked products state
    var likedProducts by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            scope.launch {
                val downloadUrl = userRepo.uploadProfileImage(it)
                if (downloadUrl != null) {
                    profileImageUrl = downloadUrl
                }
            }
        }
    }

    // Fetch user profile and liked posts
    LaunchedEffect(Unit) {
        try {
            // Fetch user profile using combined UserRepository
            user = userRepo.getCurrentUser()
            fullName = user?.fullName ?: ""
            username = user?.username ?: ""
            phoneNumber = user?.phoneNumber ?: ""
            profileImageUrl = user?.profileImageUrl ?: ""

            // Fetch liked products using FavoriteRepository
            val likedProductsList = favoriteRepo.getFavorites()
            val likedProductIds = likedProductsList.map { it.productId }

            // Get all products and filter by liked IDs
            productRepo.getAllProduct { success, _, products ->
                if (success && products != null) {
                    likedProducts = products.filter { it.id in likedProductIds }
                }
                isLoading = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isLoading = false
        }
    }

    // Save profile function
    fun saveProfile() {
        scope.launch {
            try {
                isSaving = true
                val success = userRepo.updateUserProfile(
                    fullName = fullName,
                    username = username,
                    phoneNumber = phoneNumber
                )

                passwordChangeMessage = if (success) {
                    "Profile updated successfully"
                } else {
                    "Failed to update profile"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                passwordChangeMessage = "Failed to update profile"
            } finally {
                isSaving = false
            }
        }
    }

    // Change password function (secure version)
    fun changePassword() {
        if (currentPassword.isEmpty() || newPassword.isEmpty()) {
            passwordChangeMessage = "Please fill in both password fields"
            return
        }

        scope.launch {
            val (success, message) = userRepo.changePassword(currentPassword, newPassword)
            passwordChangeMessage = message
            if (success) {
                currentPassword = ""
                newPassword = ""
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
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

                    // Profile Image
                    if (profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = selectedImageUri ?: profileImageUrl,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ─────────── CHOOSE FILE BUTTON ───────────
                    OutlinedButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_upload_file_24),
                            contentDescription = "Upload Icon",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Choose File")
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    EditableProfileField(
                        label = "Full Name",
                        value = fullName,
                        onValueChange = { fullName = it }
                    )
                    EditableProfileField(
                        label = "Username",
                        value = username,
                        onValueChange = { username = it }
                    )
                    EditableProfileField(
                        label = "Phone Number",
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Save Button
                    Button(
                        onClick = { saveProfile() },
                        enabled = !isSaving,
                        modifier = Modifier.width(220.dp)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Save Changes")
                        }
                    }
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
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                PasswordField(
                    placeholder = "New Password",
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Change Password Button
            Button(
                onClick = { changePassword() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Password")
            }

            // Password change message
            if (passwordChangeMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = passwordChangeMessage,
                    color = if (passwordChangeMessage.contains("success"))
                        Color.Green else Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ─────────── LIKED POSTS ───────────
            Text(
                text = "Liked Posts",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (likedProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No liked posts yet", color = Color.Gray)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(likedProducts) { product ->
                        LikedProductItem(
                            product = product,
                            onUnlike = {
                                scope.launch {
                                    // Use FavoriteRepository to unlike
                                    favoriteRepo.toggleFavorite(
                                        LikedProducts(productId = product.id),
                                        isFavorite = false
                                    )
                                    // Remove from local list
                                    likedProducts = likedProducts.filter { it.id != product.id }
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
}


@Composable
fun EditableProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(220.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
fun PasswordField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = modifier,
        singleLine = true
    )
}

/* ───────────────── LIKED POSTS ───────────────── */

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
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Text(
                        text = "Rs. ${product.price}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Unlike button
            IconButton(
                onClick = onUnlike,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_favorite_24),
                    contentDescription = "Unlike",
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}