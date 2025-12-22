package com.example.feriferi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/* ---------- COLORS ---------- */
private val SoftPink = Color(0xFFFFF1F4)
private val SoftCardPink = Color(0xFFFFE9EE)
private val SoftInputPink = Color(0xFFFFFAFA)

/* ---------- ACTIVITY ---------- */

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DashboardBody() }
    }
}

/* ---------- MAIN ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody() {

    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedBottomItem by remember { mutableIntStateOf(0) }

    val bottomItems = listOf(
        NavItem("Home", R.drawable.baseline_home_24),
        NavItem("Cart", R.drawable.baseline_shopping_cart_24),
        NavItem("Messages", R.drawable.baseline_message_24),
        NavItem("Settings", R.drawable.baseline_settings_24)
    )

    val categories = listOf(
        "Clothing", "Foot-Wear", "Accessories",
        "Furniture", "Electronics", "Books & Stationary"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.background(SoftPink)
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Categories",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
                categories.forEach { category ->
                    Text(
                        text = category,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .clickable {
                                context.startActivity(
                                    Intent(context, CategoryActivity::class.java)
                                        .putExtra("category", category)
                                )
                                scope.launch { drawerState.close() }
                            }
                            .padding(12.dp)
                    )
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
                        IconButton(onClick = {
                            context.startActivity(
                                Intent(context, NotificationActivity::class.java)
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_notifications_24),
                                contentDescription = "Notifications"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(containerColor = SoftPink) {
                    bottomItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedBottomItem == index,
                            onClick = { selectedBottomItem = index },
                            icon = {
                                Icon(
                                    painter = painterResource(item.icon),
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(SoftPink)
            ) {
                when (selectedBottomItem) {
                    0 -> HomeScreen()
                    1 -> CenterText("Cart Screen")
                    2 -> CenterText("Messages Screen")
                    3 -> CenterText("Settings Screen")
                }
            }
        }
    }
}


@Composable
fun HomeScreen() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftPink),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                placeholder = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = SoftInputPink,
                    focusedContainerColor = SoftInputPink
                )
            )
        }

        item { BannerSection() }

        item {
            Text(
                "Shop by choice",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp)
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftPink)
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = banners) { banner ->
                Card(
                    modifier = Modifier
                        .width(320.dp)
                        .height(160.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SoftCardPink
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = painterResource(banner),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun HomeProductsSection() {

    val context = LocalContext.current

    val products = listOf(
        Product("Relaxed Cotton Shirt", "vivienne", R.drawable.cottonshirt),
        Product("Decor Chair", "hooman", R.drawable.decorchair),
        Product("Green Linen Shirt", "lennox", R.drawable.greenshirt),
        Product("Alchemist Book", "elain", R.drawable.book)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .height(650.dp)
            .padding(12.dp)
            .background(SoftPink),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { product ->
            Card(
                modifier = Modifier.clickable {
                    context.startActivity(
                        Intent(context, ItemDescriptionActivity::class.java)
                            .putExtra("productName", product.name)
                    )
                },
                colors = CardDefaults.cardColors(
                    containerColor = SoftCardPink
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(8.dp)) {

                    Image(
                        painter = painterResource(product.image),
                        contentDescription = null,
                        modifier = Modifier
                            .height(140.dp)
                            .fillMaxWidth()
                    )

                    Spacer(Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(product.name, fontWeight = FontWeight.SemiBold)
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(
                                    R.drawable.baseline_favorite_border_24
                                ),
                                contentDescription = "Love"
                            )
                        }
                    }

                    Text(
                        "@${product.username}",
                        modifier = Modifier.clickable {
                            context.startActivity(
                                Intent(context, ProfileActivity::class.java)
                                    .putExtra("username", product.username)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CenterText(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text)
    }
}


data class Product(val name: String, val username: String, val image: Int)
data class NavItem(val label: String, val icon: Int)


@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardBody()
}