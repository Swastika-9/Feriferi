package com.example.feriferi.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.feriferi.model.ProductModel
import com.example.feriferi.repository.NotificationRepository
import com.example.feriferi.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardScreen()
        }
    }
}

data class MenuCategory(
    val name: String,
    val subCategories: List<String> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val allProducts by viewModel.allProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var notificationCount by remember { mutableIntStateOf(0) }

    // FIXED: Explicitly typed 'count: Int' to fix inference error
    LaunchedEffect(Unit) {
        NotificationRepository.getUnreadCount { count: Int ->
            notificationCount = count
        }
    }

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedSubCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val menuData = listOf(
        MenuCategory("Clothing", listOf("Men", "Women", "Kid's", "Sale")),
        MenuCategory("Foot-Wear", listOf("Sneakers", "Formal", "Sandals")),
        MenuCategory("Accessories", listOf("Bags", "Watches", "Jewelry")),
        MenuCategory("Furniture", listOf("Living Room", "Bedroom")),
        MenuCategory("Vehicle", listOf("Cars", "Bikes")),
        MenuCategory("Electronics", listOf("Phones", "Laptops")),
        MenuCategory("Books & Stationary", listOf("Fiction", "Academic"))
    )

    val filteredProducts = remember(allProducts, selectedCategory, selectedSubCategory, searchQuery) {
        allProducts.filter { product ->
            val matchCat = selectedCategory == "All" || product.category.equals(selectedCategory, ignoreCase = true)
            val matchSub = selectedSubCategory == "All" || product.subCategory.equals(selectedSubCategory, ignoreCase = true)
            val matchSearch = searchQuery.isEmpty() || product.name.contains(searchQuery, ignoreCase = true)
            matchCat && matchSub && matchSearch
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFF4EDE4),
                modifier = Modifier.width(280.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text("Item Categories", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.padding(start = 20.dp, bottom = 20.dp))
                LazyColumn(modifier = Modifier.padding(horizontal = 20.dp)) {
                    item {
                        Text("All Products", fontSize = 20.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(vertical = 12.dp).clickable {
                            selectedCategory = "All"
                            selectedSubCategory = "All"
                            scope.launch { drawerState.close() }
                        })
                    }
                    items(menuData) { category ->
                        SidebarCategoryItem(category, selectedCategory == category.name, { selectedCategory = category.name }, { sub ->
                            selectedSubCategory = sub
                            scope.launch { drawerState.close() }
                        })
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("फेरिPheri", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Color(0xFF5D4037)) },
                    navigationIcon = { IconButton(onClick = { scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, "Menu", tint = Color(0xFF5D4037)) } },
                    actions = {
                        IconButton(onClick = {
                            NotificationRepository.markAllAsRead()
                            // FIXED: Explicit class reference
                            context.startActivity(Intent(context, NotificationActivity::class.java))
                        }) {
                            if (notificationCount > 0) {
                                BadgedBox(badge = { Badge { Text("$notificationCount") } }) {
                                    Icon(Icons.Default.Notifications, "Alerts", tint = Color(0xFF5D4037))
                                }
                            } else {
                                Icon(Icons.Default.Notifications, "Alerts", tint = Color(0xFF5D4037))
                            }
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
                    // FIXED: Activity class references for all bottom items
                    NavigationBarItem(selected = false, onClick = { context.startActivity(Intent(context, AddToCartActivity::class.java)) }, icon = { Icon(Icons.Default.ShoppingCart, null) }, label = { Text("Cart") })
                    NavigationBarItem(selected = false, onClick = { context.startActivity(Intent(context, ChatActivity::class.java)) }, icon = { Icon(Icons.Default.Chat, null) }, label = { Text("Chat") })
                    NavigationBarItem(selected = false, onClick = { context.startActivity(Intent(context, SettingsActivity::class.java)) }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Settings") })
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(modifier = Modifier.fillMaxWidth().height(48.dp), color = Color(0xFFF3E5D8), shape = RoundedCornerShape(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Icon(Icons.Default.Search, null, tint = Color.Gray)
                        androidx.compose.foundation.text.BasicTextField(value = searchQuery, onValueChange = { searchQuery = it }, modifier = Modifier.padding(start = 8.dp).fillMaxWidth(), decorationBox = { inner ->
                            if (searchQuery.isEmpty()) Text("Search...", color = Color.Gray)
                            inner()
                        })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Color(0xFF5D4037)) }
                } else if (filteredProducts.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No items found.", color = Color.Gray) }
                } else {
                    LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                        items(filteredProducts) { product ->
                            DashboardProductItem(product) {
                                val intent = Intent(context, ItemDescriptionActivity::class.java)
                                intent.putExtra("itemId", product.id)
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SidebarCategoryItem(category: MenuCategory, isExpanded: Boolean, onCategoryClick: () -> Unit, onSubCategoryClick: (String) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth().clickable { onCategoryClick() }.padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = category.name, fontSize = 22.sp, fontWeight = if (isExpanded) FontWeight.Bold else FontWeight.Medium)
        }
        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                category.subCategories.forEach { sub ->
                    Text(text = sub, fontSize = 18.sp, color = Color.DarkGray, modifier = Modifier.fillMaxWidth().clickable { onSubCategoryClick(sub) }.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun DashboardProductItem(product: ProductModel, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column {
            val imageUrl = if (product.imageUrls.isNotEmpty()) product.imageUrls[0] else ""
            AsyncImage(model = imageUrl, contentDescription = product.name, modifier = Modifier.fillMaxWidth().height(160.dp), contentScale = ContentScale.Crop)
            Column(modifier = Modifier.padding(8.dp)) {
                Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Rs ${product.price}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF5D4037))
            }
        }
    }
}