@file:OptIn(ExperimentalMaterial3Api::class)

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

@Composable
fun AddProductScreen(
    onBack: () -> Unit,
    onUpload: (ProductModel, List<Uri>) -> Unit,
    isUploading: Boolean
) {
    val context = LocalContext.current
    val AdminBrown = Color(0xFF8D736B)
    val AdminBgWhite = Color(0xFFFFFFFF)

    // --- 1. STATE MAPPED TO YOUR MODEL ---
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var originalPrice by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var brand by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var timesWorn by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    // --- 2. CATEGORY HIERARCHY ---
    val categoriesMap = mapOf(
        "Clothing" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Footwear" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Accessories" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Electronics" to listOf("Gadgets", "Oven", "Washing Machines", "Cookware"),
        "Books" to listOf("Novel", "Stories", "Essay", "Others")
    )
    var mainCat by remember { mutableStateOf("Select Category") }
    var subCat by remember { mutableStateOf("Select Sub-Category") }

    // --- 3. LAPTOP-FRIENDLY IMAGE PICKER ---
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) selectedImageUris = uris else Toast.makeText(context, "No images selected", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        containerColor = AdminBgWhite,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("फेरिPheri", style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AdminBrown)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = AdminBrown) } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {

            // Photo Selection Section
            Text("Photos (${selectedImageUris.size}/10) - Min 1", fontWeight = FontWeight.Bold, color = AdminBrown)
            LazyRow(modifier = Modifier.padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF5F5F5)).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).clickable { launcher.launch("image/*") }, contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.AddAPhoto, null, tint = AdminBrown)
                    }
                }
                items(selectedImageUris) { uri ->
                    AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                }
            }

            // Category & Sub-Category Dropdowns
            CategoryDropdown("Category", categoriesMap.keys.toList(), mainCat) {
                mainCat = it
                subCat = "Select Sub-Category"
            }

            Spacer(modifier = Modifier.height(8.dp))

            CategoryDropdown("Sub-Category", categoriesMap[mainCat] ?: emptyList(), subCat) {
                subCat = it
            }

            // Input Fields
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (NPR)") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = originalPrice, onValueChange = { originalPrice = it }, label = { Text("Original Price") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Company / Brand") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = size, onValueChange = { size = it }, label = { Text("Size") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") }, modifier = Modifier.weight(1f))
            }

            OutlinedTextField(value = timesWorn, onValueChange = { timesWorn = it }, label = { Text("Number of times worn") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    val product = ProductModel(
                        name = name,
                        price = price.toDoubleOrNull() ?: 0.0,
                        originalPrice = originalPrice.toDoubleOrNull(),
                        quantity = quantity.toIntOrNull() ?: 1,
                        category = mainCat,
                        subCategory = subCat,
                        size = size,
                        color = color,
                        brand = brand,
                        timesWorn = timesWorn,
                        description = description,
                        gender = gender
                    )
                    onUpload(product, selectedImageUris)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminBrown),
                enabled = !isUploading && selectedImageUris.isNotEmpty() && name.isNotBlank()
            ) {
                if (isUploading) CircularProgressIndicator(color = Color.White) else Text("Publish Product")
            }
        }
    }
}

// --- HELPER DROPDOWN COMPONENT ---
@Composable
fun CategoryDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { IconButton(onClick = { expanded = !expanded }) { Icon(Icons.Default.ArrowDropDown, null) } }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth(0.9f)) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}