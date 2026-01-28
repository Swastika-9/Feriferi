package com.example.feriferi.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.feriferi.model.ProductModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBack: () -> Unit,
    onUpload: (ProductModel, List<Uri>) -> Unit,
    isUploading: Boolean
) {
    val context = LocalContext.current

    val AdminBrown = Color(0xFF8D736B)
    val AdminBgWhite = Color(0xFFFFFFFF)
    val AdminGray = Color(0xFF757575)

    val categoriesMap = mapOf(
        "Clothing" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Footwear" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Accessories" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Electronics" to listOf("Gadgets", "Oven", "Washing Machines", "Cookware"),
        "Books" to listOf("Novel", "Stories", "Essay", "Others")
    )

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var originalPrice by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var description by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var timesWorn by remember { mutableStateOf("") }
    var mainCat by remember { mutableStateOf("Select Category") }
    var subCat by remember { mutableStateOf("Select Sub-Category") }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var mainExpanded by remember { mutableStateOf(false) }
    var subExpanded by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            if (it.size in 3..10) selectedImageUris = it
            else Toast.makeText(context, "Select 3–10 images", Toast.LENGTH_SHORT).show()
        }

    Scaffold(
        containerColor = AdminBgWhite,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "फेरिPheri",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = AdminBrown
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = AdminBrown)
                    }
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, "Cancel", tint = AdminBrown)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AdminBgWhite
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Text(
                "Add New Product",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = AdminGray,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Photos (${selectedImageUris.size}/10) - Min 3", fontWeight = FontWeight.Bold)

            LazyRow(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF5F5F5))
                            .border(
                                1.dp,
                                Color.LightGray,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = AdminBrown
                        )
                    }
                }

                items(selectedImageUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    prefix = { Text("NPR ") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = originalPrice,
                    onValueChange = { originalPrice = it },
                    label = { Text("Original") },
                    prefix = { Text("NPR ") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val product = ProductModel(
                        name = name,
                        price = price.toDoubleOrNull() ?: 0.0,
                        originalPrice = originalPrice.toDoubleOrNull(),
                        quantity = quantity.toIntOrNull() ?: 1,
                        category = mainCat,
                        subCategory = subCat,
                        brand = brand,
                        color = color,
                        tag = timesWorn,
                        description = description
                    )
                    onUpload(product, selectedImageUris)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminBrown),
                enabled = !isUploading && selectedImageUris.size >= 3 && name.isNotBlank()
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Publish Product", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}