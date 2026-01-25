package com.example.feriferi.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.model.ProductModel
import com.example.feriferi.viewmodel.AdminDashboardViewModel
import com.example.feriferi.viewmodel.UserAccount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    users: List<UserAccount>,
    products: List<ProductModel>,
    isLoading: Boolean,
    viewModel: AdminDashboardViewModel
) {
    // 1. BRAND COLORS
    val AdminBrown = Color(0xFF8D736B)
    val AdminBgWhite = Color(0xFFFFFFFF)
    val AdminGray = Color(0xFF757575)

    // Tab 0: Home, 1: Messages, 2: Settings, 3: User List, 4: Product List
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = AdminBgWhite,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "फेरिPheri",
                        style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AdminBrown)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(Icons.Default.Menu, null, tint = AdminBrown)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.NotificationsNone, null, tint = AdminBrown)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AdminBgWhite)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = AdminBgWhite, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = selectedTab == 0 || selectedTab > 2,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdminBrown, selectedTextColor = AdminBrown, indicatorColor = AdminBrown.copy(alpha = 0.1f))
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Email, null) },
                    label = { Text("Messages") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdminBrown, selectedTextColor = AdminBrown)
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Settings, null) },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdminBrown, selectedTextColor = AdminBrown)
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AdminBrown)
            } else {
                when (selectedTab) {
                    0 -> AdminHomeContent(users, products, AdminGray, AdminBrown,
                        onNavigateToUsers = { selectedTab = 3 },
                        onNavigateToProducts = { selectedTab = 4 }
                    )
                    1 -> PlaceholderView("Messages Screen")
                    2 -> PlaceholderView("Settings Screen")
                    3 -> UserManagementList(users, viewModel) { selectedTab = 0 }
                    4 -> ProductManagementList(products, viewModel) { selectedTab = 0 }
                }
            }
        }
    }
}

@Composable
fun AdminHomeContent(
    users: List<UserAccount>,
    products: List<ProductModel>,
    AdminGray: Color,
    AdminBrown: Color,
    onNavigateToUsers: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Admin",
            style = TextStyle(fontSize = 14.sp, color = AdminGray, fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(24.dp))

        AdminActionCard("User Management", "Total Users: ${users.size}", Icons.Default.People, AdminBrown, onNavigateToUsers)
        Spacer(modifier = Modifier.height(16.dp))

        AdminActionCard("Product Management", "Total Products: ${products.size}", Icons.Default.ShoppingBag, AdminBrown, onNavigateToProducts)
        Spacer(modifier = Modifier.height(16.dp))

        AdminActionCard("Reports & Complaints", "Review user feedback", Icons.Default.Report, Color.Red, {})
    }
}

@Composable
fun UserManagementList(users: List<UserAccount>, viewModel: AdminDashboardViewModel, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
            Text("Manage Users", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        LazyColumn {
            items(users) { user ->
                ListItem(
                    headlineContent = { Text(user.name) },
                    supportingContent = { Text("Role: ${user.role}") },
                    trailingContent = {
                        Button(
                            onClick = { viewModel.toggleUserBan(user.id, !user.isBanned) },
                            colors = ButtonDefaults.buttonColors(containerColor = if(user.isBanned) Color.Green else Color.Red)
                        ) {
                            Text(if(user.isBanned) "Unban" else "Ban")
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ProductManagementList(products: List<ProductModel>, viewModel: AdminDashboardViewModel, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
            Text("Manage Products", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        LazyColumn {
            items(products) { product ->
                ListItem(
                    headlineContent = { Text(product.name) },
                    supportingContent = { Text("Price: NPR ${product.price}") },
                    trailingContent = {
                        IconButton(onClick = { viewModel.deleteProduct(product.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun AdminActionCard(title: String, subtitle: String, icon: ImageVector, accentColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).background(accentColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, null, tint = accentColor) }

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = subtitle, fontSize = 13.sp, color = Color(0xFF757575))
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Composable
fun PlaceholderView(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, color = Color.Gray)
    }
}
