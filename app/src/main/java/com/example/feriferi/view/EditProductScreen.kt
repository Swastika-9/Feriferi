package com.example.feriferi.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.feriferi.viewmodel.EditProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    viewModel: EditProductViewModel,
    onBack: () -> Unit,
    onNavigateToMessages: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val product = viewModel.productState
    val pheriBrown = Color(0xFF8D736B)
    val inactiveGray = Color(0xFF757575)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("फेरिPheri", color = pheriBrown, fontWeight = FontWeight.Bold, fontSize = 32.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = pheriBrown)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications", tint = pheriBrown)
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
                    selected = true,
                    onClick = { /* Stay here or go back to Home */ },
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
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = inactiveGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToSettings,
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = inactiveGray)
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
                text = "Edit Product",
                fontSize = 26.sp,
                color = pheriBrown,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.shoes),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(width = 240.dp, height = 200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.size(32.dp).offset(x = 8.dp, y = 8.dp)
                    ) {
                        IconButton(onClick = { /* Pick new image */ }) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(containerColor = pheriBrown),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text("Choose file", fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Default.FileUpload, null, modifier = Modifier.size(16.dp))
                }
            }

            PheriProductInput(label = "Color", value = product.color ?: "") { viewModel.updateColor(it) }
            PheriProductInput(label = "Condition", value = product.condition ?: "") { viewModel.updateCondition(it) }
            PheriProductInput(label = "Number of times worn", value = product.timesWorn ?: "") { viewModel.updateTimesWorn(it) }
            PheriProductInput(label = "Company", value = product.brand ?: "") { viewModel.updateBrand(it) }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PheriDropdownField(label = "Category", value = product.category, modifier = Modifier.weight(1f))
                PheriDropdownField(label = "Status", value = product.status, modifier = Modifier.weight(1f))
            }

            Button(
                onClick = { viewModel.saveChanges { onBack() } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 24.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = pheriBrown),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Done", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun PheriProductInput(label: String, value: String, onValueChange: (String) -> Unit) {
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

@Composable
fun PheriDropdownField(label: String, value: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        OutlinedCard(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                Text(text = value.ifEmpty { "Select" }, fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProductPreview() {
    val mockVM = EditProductViewModel()
    EditProductScreen(viewModel = mockVM, onBack = {})
}