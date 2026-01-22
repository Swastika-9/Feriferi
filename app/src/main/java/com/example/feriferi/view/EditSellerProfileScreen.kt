package com.example.feriferi.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.R
import com.example.feriferi.viewmodel.SellerProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSellerProfileScreen(
    viewModel: SellerProfileViewModel,
    onBack: () -> Unit,
    onNavigateToMessages: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val seller by viewModel.seller.collectAsState()

    // Local UI State for text fields
    var nameState by remember(seller) { mutableStateOf(seller.name) }
    var usernameState by remember(seller) { mutableStateOf(seller.username) }

    // Brand Colors
    val pheriBrown = Color(0xFF8D736B)
    val inactiveGray = Color(0xFF757575)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("फेरिPheri", color = pheriBrown, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, null, tint = pheriBrown)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.NotificationsNone, null, tint = pheriBrown)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = true, // Highlighted because we are in a sub-section of Home/Profile
                    onClick = onBack,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = pheriBrown,
                        selectedTextColor = pheriBrown,
                        indicatorColor = pheriBrown.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToMessages,
                    icon = { Icon(Icons.Default.Email, contentDescription = "Messages") },
                    label = { Text("Messages") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = inactiveGray,
                        unselectedTextColor = inactiveGray
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToSettings,
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = inactiveGray,
                        unselectedTextColor = inactiveGray
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 26.sp,
                color = pheriBrown,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            // --- Profile Picture Section ---
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = seller.profileImage),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                // Small Edit Icon Overlay
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(32.dp).offset(x = (-4).dp, y = (-4).dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit photo",
                        modifier = Modifier.padding(6.dp),
                        tint = Color.Black
                    )
                }
            }

            // --- Choose File Button ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { /* Handle File Picker */ },
                    colors = ButtonDefaults.buttonColors(containerColor = pheriBrown),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text("Choose file", fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Default.FileUpload, null, modifier = Modifier.size(16.dp))
                }
            }

            // --- Form Fields ---
            PheriLabelledInput(label = "Full Name", value = nameState) { nameState = it }
            PheriLabelledInput(label = "Username", value = usernameState) { usernameState = it }
            PheriLabelledInput(label = "Phone Number", value = "970000000") { /* Static for now */ }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Change Password", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    PheriLabelledInput("Current Password", "xxxxx") {}
                }
                Box(modifier = Modifier.weight(1f)) {
                    PheriLabelledInput("New Password", "xxxxxx") {}
                }
            }

            // --- Save Button ---
            Button(
                onClick = {
                    viewModel.saveSellerProfile(nameState, usernameState)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 20.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = pheriBrown),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Done", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PheriLabelledInput(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(4.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfilePreview() {
    EditSellerProfileScreen(
        viewModel = SellerProfileViewModel(), // Fixed: Instantiate directly for preview
        onBack = {}
    )
}
