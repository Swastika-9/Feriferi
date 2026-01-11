package com.example.feriferi//package com.example.feriferi
//
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.feriferi.model.CartItem
//import com.example.feriferi.viewmodel.BuyerCartViewModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//
//@Composable
//fun AddToCartScreen(viewModel: BuyerCartViewModel) {
//    val context = LocalContext.current
//    var currentPage by remember { mutableStateOf("Cart") }
//    var selectedItems by remember { mutableStateOf(setOf<CartItem>()) }
//    var showDeleteDialog by remember { mutableStateOf(false) }
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Color(0xFFF2E7D9))) {
//
//        Column(modifier = Modifier.fillMaxSize().padding(bottom = 70.dp)) {
//            when (currentPage) {
//                "Cart" -> CartContent(
//                    cartItems = viewModel.cartItems,
//                    selectedItems = selectedItems,
//                    onItemSelected = { item, selected ->
//                        selectedItems = if (selected) selectedItems + item else selectedItems - item
//                    },
//                    onIncrease = { item ->
//                        viewModel.increaseQuantity(item)
//                        viewModel.syncCartWithFirebase()
//                    },
//                    onDecrease = { item ->
//                        viewModel.decreaseQuantity(item)
//                        viewModel.syncCartWithFirebase()
//                    },
//                    onDelete = { item ->
//                        viewModel.removeItem(item)
//                        selectedItems = selectedItems - item
//                        viewModel.syncCartWithFirebase()
//                        Toast.makeText(context, "${item.name} deleted", Toast.LENGTH_SHORT).show()
//                    },
//                    onCheckout = { currentPage = "Checkout" },
//                    onImageClick = { /* TODO: Navigate to ProductDescriptionPage */ },
//                    showDeleteDialog = { showDeleteDialog = true },
//                    viewModel = viewModel
//                )
//
//                "Checkout" -> PlaceholderScreen("Checkout") { currentPage = "Cart" }
//                "Home" -> PlaceholderScreen("Home") { currentPage = "Cart" }
//                "Messages" -> PlaceholderScreen("Messages") { currentPage = "Cart" }
//                "Settings" -> PlaceholderScreen("Settings") { currentPage = "Cart" }
//            }
//        }
//
//        if (showDeleteDialog) {
//            AlertDialog(
//                onDismissRequest = { showDeleteDialog = false },
//                title = { Text("Delete selected items?") },
//                text = { Text("Are you sure you want to delete the selected products from cart?") },
//                confirmButton = {
//                    TextButton(onClick = {
//                        viewModel.removeItems(selectedItems.toList())
//                        viewModel.syncCartWithFirebase()
//                        selectedItems = emptySet()
//                        showDeleteDialog = false
//                        Toast.makeText(context, "Selected items deleted", Toast.LENGTH_SHORT).show()
//                    }) { Text("Yes") }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
//                }
//            )
//        }
//
//        BottomNavigationBar(
//            onHomeClick = { currentPage = "Home" },
//            onMessagesClick = { currentPage = "Messages" },
//            onSettingsClick = { currentPage = "Settings" }
//        )
//    }
//}
//
//@Composable
//fun CartContent(
//    cartItems: List<CartItem>,
//    selectedItems: Set<CartItem>,
//    onItemSelected: (CartItem, Boolean) -> Unit,
//    onIncrease: (CartItem) -> Unit,
//    onDecrease: (CartItem) -> Unit,
//    onDelete: (CartItem) -> Unit,
//    onCheckout: () -> Unit,
//    onImageClick: (CartItem) -> Unit,
//    showDeleteDialog: () -> Unit,
//    viewModel: BuyerCartViewModel
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(16.dp)
//    ) {
//        Text("Shopping Cart", fontSize = 22.sp, fontWeight = FontWeight.Bold)
//        Spacer(modifier = Modifier.height(12.dp))
//
//        cartItems.forEach { item ->
//            var showItemDeleteDialog by remember { mutableStateOf(false) }
//
//            CartItemRow(
//                item = item,
//                isSelected = selectedItems.contains(item),
//                onItemSelected = { selected -> onItemSelected(item, selected) },
//                onIncrease = { onIncrease(item) },
//                onDecrease = { onDecrease(item) },
//                onDelete = { showItemDeleteDialog = true },
//                onImageClick = { onImageClick(item) },
//                viewModel = viewModel
//            )
//
//            if (showItemDeleteDialog) {
//                AlertDialog(
//                    onDismissRequest = { showItemDeleteDialog = false },
//                    title = { Text("Delete item?") },
//                    text = { Text("Are you sure you want to delete ${item.name}?") },
//                    confirmButton = {
//                        TextButton(onClick = {
//                            onDelete(item)
//                            showItemDeleteDialog = false
//                        }) { Text("Yes") }
//                    },
//                    dismissButton = {
//                        TextButton(onClick = { showItemDeleteDialog = false }) { Text("Cancel") }
//                    }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//
//        Divider()
//        Spacer(modifier = Modifier.height(12.dp))
//
//        PriceRow("Subtotal", viewModel.getFormattedSubtotal())
//        PriceRow("Delivery Fee", viewModel.getFormattedDelivery())
//        PriceRow("Total", viewModel.getFormattedTotal(), bold = true)
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        if (selectedItems.isNotEmpty()) {
//            Button(onClick = showDeleteDialog, modifier = Modifier.fillMaxWidth()) {
//                Text("Delete Selected (${selectedItems.size})")
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//
//        Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth()) {
//            Text("Check Out")
//        }
//    }
//}
//
//@Composable
//fun CartItemRow(
//    item: CartItem,
//    isSelected: Boolean,
//    onItemSelected: (Boolean) -> Unit,
//    onIncrease: () -> Unit,
//    onDecrease: () -> Unit,
//    onDelete: () -> Unit,
//    onImageClick: () -> Unit,
//    viewModel: BuyerCartViewModel
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Checkbox(checked = isSelected, onCheckedChange = { onItemSelected(it) })
//        Spacer(modifier = Modifier.width(8.dp))
//
//        Image(
//            painter = painterResource(id = item.image),
//            contentDescription = null,
//            modifier = Modifier.size(60.dp).clickable { onImageClick() }
//        )
//        Spacer(modifier = Modifier.width(12.dp))
//
//        Column(modifier = Modifier.weight(1f)) {
//            Text(item.name, fontWeight = FontWeight.Medium)
//            Text("Rs ${viewModel.getFormattedPrice(item)}")
//        }
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(text = "-", modifier = Modifier.clickable { onDecrease() }.padding(8.dp))
//            Text(text = item.quantity.toString(), modifier = Modifier.padding(8.dp))
//            Text(text = "+", modifier = Modifier.clickable { onIncrease() }.padding(8.dp))
//        }
//
//        Spacer(modifier = Modifier.width(8.dp))
//
//        Text(text = "✕", fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onDelete() })
//    }
//}
//
//@Composable
//fun PriceRow(label: String, amount: String, bold: Boolean = false) {
//    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//        Text(text = label)
//        Text(text = amount, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
//    }
//}
//
//@Composable
//fun BottomNavigationBar(
//    onHomeClick: () -> Unit,
//    onMessagesClick: () -> Unit,
//    onSettingsClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(60.dp)
//            .background(Color.White)
//            .align(Alignment.BottomCenter)
//            .padding(horizontal = 16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceAround
//    ) {
//        Text("Home", modifier = Modifier.clickable { onHomeClick() }, fontWeight = FontWeight.Medium)
//        Text("Messages", modifier = Modifier.clickable { onMessagesClick() }, fontWeight = FontWeight.Medium)
//        Text("Settings", modifier = Modifier.clickable { onSettingsClick() }, fontWeight = FontWeight.Medium)
//    }
//}
//
//@Composable
//fun PlaceholderScreen(name: String, onBack: () -> Unit) {
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text("$name Page", fontSize = 22.sp, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = onBack) { Text("Back to Cart") }
//        }
//    }
//}
