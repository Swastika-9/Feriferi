package com.example.feriferi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.feriferi.R

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val categoryName = intent.getStringExtra("category") ?: "Category"

        setContent {
            CategoryBody(categoryName)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBody(category: String) {

    val context = LocalContext.current
    val activity = context as Activity

    val products = remember {
        sampleCategoryProducts().filter {
            it.category == category
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(category) },
                navigationIcon = {
                    IconButton(onClick = { activity.finish() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No products found")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(padding)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    CategoryProductCard(product)
                }
            }
        }
    }
}

@Composable
fun CategoryProductCard(product: CategoryProduct) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, ItemDescriptionActivity::class.java)
                intent.putExtra("productName", product.name)
                context.startActivity(intent)
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {

            Image(
                painter = painterResource(product.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = product.name,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "@${product.username}",
                modifier = Modifier.clickable {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("username", product.username)
                    context.startActivity(intent)
                }
            )
        }
    }
}

data class CategoryProduct(
    val name: String,
    val username: String,
    val category: String,
    val image: Int
)

fun sampleCategoryProducts(): List<CategoryProduct> {
    return listOf(
        CategoryProduct(
            "Relaxed Cotton Shirt",
            "vivienne",
            "Clothing",
            R.drawable.cottonshirt
        ),
        CategoryProduct(
            "Green Linen Shirt",
            "lennox",
            "Clothing",
            R.drawable.greenshirt
        ),
        CategoryProduct(
            "Decor Chair",
            "hooman",
            "Furniture",
            R.drawable.decorchair
        ),
        CategoryProduct(
            "Alchemist Book",
            "ealain",
            "Books & Stationary",
            R.drawable.book
        )
    )
}
