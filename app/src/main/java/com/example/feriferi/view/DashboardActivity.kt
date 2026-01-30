package com.example.feriferi.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import coil.compose.AsyncImage
import com.example.feriferi.R
import com.example.feriferi.model.LikedProducts
import com.example.feriferi.model.ProductModel
import com.example.feriferi.repository.FavoriteRepository
import com.example.feriferi.repository.ProductRepoImpl
import com.example.feriferi.ui.theme.CardPink
import com.example.feriferi.view.com.example.feriferi.view.SettingsScreen

private val SoftPink = Color(0xFFFFF1F4)
private val cardpink = Color(0xFFFFE9EE)
private val OffWhiteCard = Color(0xFFFFFAFA)

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody() {

    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedBottomItem by remember { mutableIntStateOf(0) }

    val categories = listOf(
        "Clothing", "Footwear", "Accessories",
        "Furniture", "Electronics", "Books & Stationery"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SoftPink)
                ) {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Categories",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )

                    categories.forEach { category ->
                        Text(
                            text = category,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    context.startActivity(
                                        Intent(context, CategoryActivity::class.java)
                                            .putExtra("category", category)
                                    )
                                    scope.launch { drawerState.close() }
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    ) {

        Scaffold(
            containerColor = SoftPink,

            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = SoftPink
                    ),
                    title = {
                        Text("फेरीPheri", fontWeight = FontWeight.Bold)
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { selectedBottomItem = 4 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_notifications_24),
                                contentDescription = "Notifications"
                            )
                        }
                    }
                )
            },

            bottomBar = {
                NavigationBar(containerColor = SoftPink) {
                    NavigationBarItem(
                        selected = selectedBottomItem == 0,
                        onClick = { selectedBottomItem = 0 },
                        icon = { Icon(painterResource(id = R.drawable.baseline_home_24), null) },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = selectedBottomItem == 1,
                        onClick = { selectedBottomItem = 1 },
                        icon = { Icon(painterResource(id = R.drawable.baseline_shopping_cart_24), null) },
                        label = { Text("Cart") }
                    )
                    NavigationBarItem(
                        selected = selectedBottomItem == 2,
                        onClick = { selectedBottomItem = 2 },
                        icon = { Icon(painterResource(id = R.drawable.baseline_message_24), null) },
                        label = { Text("Messages") }
                    )
                    NavigationBarItem(
                        selected = selectedBottomItem == 3,
                        onClick = { selectedBottomItem = 3 },
                        icon = { Icon(painterResource(id = R.drawable.baseline_settings_24), null) },
                        label = { Text("Settings") }
                    )
                }
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Brush.verticalGradient(listOf(SoftPink, SoftPink)))
            ) {
                when (selectedBottomItem) {
                    0 -> HomeScreen()
                    1 -> CartScreen()
                    2 -> MessageScreen()
                    3 -> SettingsScreen()      // ✅ FROM SettingsScreen.kt
                    4 -> NotificationScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Search"
                    )
                },
                placeholder = { Text("Search products") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }

        item { BannerSection() }

        item {
            Text(
                "Shop by choice",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        item { HomeProductsSection() }
    }
}

@Composable
fun BannerSection() {

    val banners = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(banners) { banner ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CardPink),
                modifier = Modifier
                    .width(320.dp)
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = banner),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun HomeProductsSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val productRepo = remember { ProductRepoImpl() }
    val favoriteRepo = remember { FavoriteRepository() }

    var products by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    val likedIds = remember { mutableStateListOf<String>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {

                val favorites = favoriteRepo.getFavorites()
                likedIds.addAll(favorites.map { it.productId })

                productRepo.getAllProduct { success, _, fetchedProducts ->
                    if (success && fetchedProducts != null) {
                        products = fetchedProducts
                    }
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                val isLiked = likedIds.contains(product.id)

                Card(
                    colors = CardDefaults.cardColors(containerColor = OffWhiteCard),
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(context, ItemDescriptionActivity::class.java)
                                .putExtra("productId", product.id)
                        )
                    }
                ) {
                    Column(Modifier.padding(8.dp)) {
                        AsyncImage(
                            model = product.imageUrls.firstOrNull(),
                            contentDescription = product.name,
                            modifier = Modifier
                                .height(140.dp)
                                .fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(product.name, fontWeight = FontWeight.SemiBold)

                            IconButton(onClick = {
                                scope.launch {
                                    val likedProduct = LikedProducts(productId = product.id)

                                    if (isLiked) {
                                        favoriteRepo.toggleFavorite(likedProduct, isFavorite = false)
                                        likedIds.remove(product.id)
                                    } else {
                                        favoriteRepo.toggleFavorite(likedProduct, isFavorite = true)
                                        likedIds.add(product.id)
                                    }
                                }
                            }) {
                                Icon(
                                    painter = painterResource(
                                        if (isLiked)
                                            R.drawable.baseline_favorite_24
                                        else
                                            R.drawable.baseline_favorite_border_24
                                    ),
                                    tint = if (isLiked) Color.Red else Color.Gray,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartScreen() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Cart Screen")
    }

@Composable
fun MessageScreen() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Messages Screen")
    }

@Composable
fun NotificationScreen() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Notifications")
    }

data class Product(
    val name: String,
    val username: String,
    val image: Int
)

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardBody()
}