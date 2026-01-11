package com.example.feriferi

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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.feriferi.model.ProductModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBack: () -> Unit,
    onUpload: (ProductModel, List<Uri>) -> Unit, // Changed to List<Uri>
    isUploading: Boolean
) {
    val context = LocalContext.current
    val brownAccent = Color(0xFF8D736B)
    val lightBrown = Color(0xFFA07870)

    val categoriesMap = mapOf(
        "Clothing" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Footwear" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Accessories" to listOf("Men's", "Women's", "Kid's", "Sales"),
        "Electronics" to listOf("Gadgets", "Oven", "Washing Machines", "Cookware"),
        "Books & Stationery" to listOf("Novel", "Stories", "Essay", "Others")
    )

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var originalPrice by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var description by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("New") }
    var timesWorn by remember { mutableStateOf("") }

    var mainCategory by remember { mutableStateOf("Select Category") }
    var subCategory by remember { mutableStateOf("Select Sub-Category") }
    var size by remember { mutableStateOf("M") }

    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var mainExpanded by remember { mutableStateOf(false) }
    var subExpanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.size in 3..10) {
            selectedImageUris = uris
        } else {
            Toast.makeText(context, "Please select between 3 and 10 images", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List New Product", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // --- IMAGE SECTION ---
            Text("Photos (${selectedImageUris.size}/10) - Select 3-10 images", fontWeight = FontWeight.Bold)
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
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AddAPhoto, null, tint = brownAccent)
                    }
                }
                items(selectedImageUris) { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text("General Information", color = lightBrown, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    prefix = { Text("NPR ", color = brownAccent) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Qty") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Text("Classification", color = lightBrown, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))

            ExposedDropdownMenuBox(
                expanded = mainExpanded,
                onExpandedChange = { mainExpanded = !mainExpanded }
            ) {
                OutlinedTextField(
                    value = mainCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Main Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(mainExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(expanded = mainExpanded, onDismissRequest = { mainExpanded = false }) {
                    categoriesMap.keys.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                mainCategory = cat
                                subCategory = "Select Sub-Category"
                                mainExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = subExpanded,
                onExpandedChange = { if (mainCategory != "Select Category") subExpanded = !subExpanded }
            ) {
                OutlinedTextField(
                    value = subCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sub-Category") },
                    enabled = mainCategory != "Select Category",
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(subExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(expanded = subExpanded, onDismissRequest = { subExpanded = false }) {
                    categoriesMap[mainCategory]?.forEach { sub ->
                        DropdownMenuItem(
                            text = { Text(sub) },
                            onClick = {
                                subCategory = sub
                                subExpanded = false
                            }
                        )
                    }
                }
            }

            Text("Specifications", color = lightBrown, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = size, onValueChange = { size = it }, label = { Text("Size") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") }, modifier = Modifier.weight(1f))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Brand") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = condition, onValueChange = { condition = it }, label = { Text("Condition") }, modifier = Modifier.weight(1f))
            }
            OutlinedTextField(
                value = timesWorn,
                onValueChange = { timesWorn = it },
                label = { Text("Number of times worn (if resale)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().height(120.dp).padding(vertical = 8.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val product = ProductModel(
                        name = name,
                        price = price.toDoubleOrNull() ?: 0.0,
                        originalPrice = originalPrice.toDoubleOrNull(),
                        quantity = quantity.toIntOrNull() ?: 1,
                        category = mainCategory,
                        subCategory = subCategory,
                        size = size,
                        color = color,
                        brand = brand,
                        condition = condition,
                        timesWorn = timesWorn,
                        description = description
                    )
                    onUpload(product, selectedImageUris)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brownAccent),
                shape = RoundedCornerShape(8.dp),
                enabled = !isUploading && selectedImageUris.size >= 3 && name.isNotBlank()
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Publish Product", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
