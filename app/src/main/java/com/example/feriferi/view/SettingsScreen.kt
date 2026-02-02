package com.example.feriferi.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

val AppBrandColor = Color(0xFF5D4037)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    showTopBar: Boolean = true,
    showFavorites: Boolean = true //
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()

    val favoriteRepo = remember { FavoriteRepository() }
    val productRepo = remember { ProductRepoImpl() }
    val userRepo = remember { UserRepoImpl() }

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    // Password State
    var showPasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isChangingPassword by remember { mutableStateOf(false) }

    var likedProducts by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var isUploadingImage by remember { mutableStateOf(false) }

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
            val user = userRepo.getCurrentUser()
            if (user != null) {
                fullName = user.fullName
                username = user.username
                phoneNumber = user.phoneNumber
                profileImageUrl = user.profileImageUrl
                userId = user.userId
            }

            // ONLY fetch favorites if we are allowed to show them
            if (showFavorites) {
                val likedList = favoriteRepo.getFavorites()
                val likedIds = likedList.map { it.productId }

                productRepo.getAllProduct { success, _, products ->
                    if (success && products != null) {
                        likedProducts = products.filter { it.id in likedIds }
                    }
                    isLoading = false
                }
            } else {
                isLoading = false
            }
        }
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isChangingPassword) {
                    showPasswordDialog = false; currentPassword = ""; newPassword = ""; confirmPassword = ""
                }
            },
            title = { Text("Change Password") },
            text = {
                Column {
                    OutlinedTextField(value = currentPassword, onValueChange = { currentPassword = it }, label = { Text("Current Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("New Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm New Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    if (isChangingPassword) { Spacer(modifier = Modifier.height(16.dp)); LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = AppBrandColor) }
                }
            },
            confirmButton = {
                Button(enabled = !isChangingPassword, onClick = {
                    val user = FirebaseAuth.getInstance().currentUser; val email = user?.email
                    when {
                        email == null -> Toast.makeText(context, "Error: User not found", Toast.LENGTH_SHORT).show()
                        currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        newPassword != confirmPassword -> Toast.makeText(context, "New passwords don't match", Toast.LENGTH_SHORT).show()
                        newPassword.length < 6 -> Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                        else -> {
                            isChangingPassword = true
                            val credential = EmailAuthProvider.getCredential(email, currentPassword)
                            user.reauthenticate(credential).addOnSuccessListener {
                                user.updatePassword(newPassword).addOnSuccessListener {
                                    isChangingPassword = false; Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_LONG).show()
                                    showPasswordDialog = false; currentPassword = ""; newPassword = ""; confirmPassword = ""
                                }.addOnFailureListener { e -> isChangingPassword = false; Toast.makeText(context, "Update Failed: ${e.message}", Toast.LENGTH_LONG).show() }
                            }.addOnFailureListener { isChangingPassword = false; Toast.makeText(context, "Wrong Current Password", Toast.LENGTH_LONG).show() }
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = AppBrandColor)) { Text("Change") }
            },
            dismissButton = { TextButton(enabled = !isChangingPassword, onClick = { showPasswordDialog = false; currentPassword = ""; newPassword = ""; confirmPassword = "" }) { Text("Cancel", color = AppBrandColor) } }
        )
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AppBrandColor)
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(if (showFavorites) "Settings & Favorites" else "Settings") },
                    navigationIcon = {
                        IconButton(onClick = { activity?.finish() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                    }
                )
            }

            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {

                // --- PROFILE ROW (Always Visible) ---
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box {
                            val model = if (profileImageUrl.isNotEmpty()) profileImageUrl else R.drawable.profile
                            AsyncImage(model = model, contentDescription = "Profile Picture", contentScale = ContentScale.Crop, modifier = Modifier.size(120.dp).clip(CircleShape).border(1.dp, Color.LightGray, CircleShape))
                            if (isUploadingImage) CircularProgressIndicator(modifier = Modifier.size(120.dp).align(Alignment.Center), color = AppBrandColor)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { imagePickerLauncher.launch("image/*") }, colors = ButtonDefaults.buttonColors(containerColor = AppBrandColor), shape = RoundedCornerShape(8.dp), modifier = Modifier.width(120.dp), enabled = !isUploadingImage) {
                            Icon(painterResource(id = R.drawable.baseline_upload_file_24), null, Modifier.size(12.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("Upload", fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        EditableProfileField("Full Name", fullName) { fullName = it }
                        EditableProfileField("Username", username) { username = it }
                        EditableProfileField("Phone Number", phoneNumber) { phoneNumber = it }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            if (userId.isEmpty()) { Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show(); return@Button }
                            isSaving = true
                            val updates = hashMapOf<String, Any>("fullName" to fullName, "phoneNumber" to phoneNumber)
                            userRepo.updateSellerProfile(userId, updates) { _, message -> isSaving = false; Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
                        }, modifier = Modifier.width(200.dp), enabled = !isSaving, colors = ButtonDefaults.buttonColors(containerColor = AppBrandColor), shape = RoundedCornerShape(10.dp)) {
                            if (isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White) else Text("Save Changes")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = { showPasswordDialog = true }, modifier = Modifier.width(200.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = AppBrandColor), border = androidx.compose.foundation.BorderStroke(1.dp, AppBrandColor)) {
                            Icon(painterResource(id = R.drawable.baseline_lock_24), null, Modifier.size(18.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Password")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = {
                            userRepo.logout(); Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, LoginActivity::class.java); intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK; context.startActivity(intent)
                        }, modifier = Modifier.width(200.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red), border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)) {
                            Icon(painterResource(id = R.drawable.baseline_logout_24), null, Modifier.size(18.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Log Out")
                        }
                    }
                }

                if (showFavorites) {
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Your Favorites", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = AppBrandColor)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (likedProducts.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                            Text("No favorite items yet", color = Color.Gray)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.height(500.dp)
                        ) {
                            items(likedProducts) { product ->
                                LikedProductItem(product = product, onUnlike = {
                                    scope.launch {
                                        favoriteRepo.toggleFavorite(LikedProducts(productId = product.id), isFavorite = false)
                                        likedProducts = likedProducts.filter { it.id != product.id }
                                        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                                    }
                                }, onClick = {
                                    val intent = Intent(context, ItemDescriptionActivity::class.java)
                                    intent.putExtra("productId", product.id)
                                    context.startActivity(intent)
                                })
                            }
                        }
                    }
                } // End of showFavorites check
            }
        }
    }
}

@Composable
fun EditableProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        OutlinedTextField(value = value, onValueChange = onValueChange, singleLine = true, modifier = Modifier.width(200.dp).height(56.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AppBrandColor, unfocusedBorderColor = Color.LightGray))
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun LikedProductItem(product: ProductModel, onUnlike: () -> Unit, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Box {
            Column {
                AsyncImage(model = product.imageUrls.firstOrNull() ?: R.drawable.placeholder_image, contentDescription = product.name, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(130.dp))
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = product.name, fontWeight = FontWeight.Bold, maxLines = 1, fontSize = 14.sp)
                    Text(text = "Rs. ${product.price}", color = AppBrandColor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
            }
            IconButton(onClick = onUnlike, modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).background(Color.White.copy(alpha = 0.7f), CircleShape).size(32.dp)) {
                Icon(painterResource(id = R.drawable.baseline_favorite_24), contentDescription = "Unlike", tint = Color.Red, modifier = Modifier.size(20.dp))
            }
        }
    }
}