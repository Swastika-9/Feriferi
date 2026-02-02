package com.example.feriferi.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feriferi.viewmodel.AddToCartViewModel

class CheckoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheckoutScreen(
                onBack = { finish() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    viewModel: AddToCartViewModel = viewModel()
) {
    val context = LocalContext.current

    // Theme Colors
    val pheriBrown = Color(0xFF8D736B)
    val bgBeige = Color(0xFFF9F3F0)

    // Form State
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // Data from ViewModel
    val subtotal = viewModel.getSubtotal()
    val total = viewModel.getTotal()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Shipping Details",
                        color = pheriBrown,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = pheriBrown)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = bgBeige)
            )
        },
        bottomBar = {
            // Fixed Bottom Bar for "Proceed"
            Surface(
                shadowElevation = 12.dp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount", color = Color.Gray, fontSize = 16.sp)
                        Text("Rs ${total.toInt()}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = pheriBrown)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // 1. Validation
                            if (fullName.isBlank() || phoneNumber.isBlank() || address.isBlank() || city.isBlank()) {
                                Toast.makeText(context, "Please fill in all delivery details", Toast.LENGTH_SHORT).show()
                            } else {
                                // 2. Navigate to Payment Activity with Details
                                val fullAddress = "$address, $city"
                                val intent = Intent(context, PaymentActivity::class.java).apply {
                                    putExtra("NAME", fullName)
                                    putExtra("PHONE", phoneNumber)
                                    putExtra("ADDRESS", fullAddress)
                                }
                                context.startActivity(intent)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = pheriBrown),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text("Proceed to Payment", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- SECTION 1: DELIVERY ADDRESS ---
            SectionHeader("Shipping Address", Icons.Default.LocalShipping, pheriBrown)

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(value = fullName, onValueChange = { fullName = it }, label = "Full Name")
            CustomTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = "Phone Number", isNumber = true)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    CustomTextField(value = city, onValueChange = { city = it }, label = "City")
                }
                Box(Modifier.weight(1f)) {
                    CustomTextField(value = address, onValueChange = { address = it }, label = "Street / Tole")
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- SECTION 2: ORDER SUMMARY ---
            Card(
                colors = CardDefaults.cardColors(containerColor = bgBeige),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = pheriBrown)
                    Spacer(modifier = Modifier.height(12.dp))

                    SummaryItem("Subtotal", "Rs ${subtotal.toInt()}")
                    SummaryItem("Delivery Fee", "Rs ${viewModel.shippingFee.toInt()}")

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Rs ${total.toInt()}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = pheriBrown)
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun SectionHeader(title: String, icon: ImageVector, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isNumber: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Phone) else KeyboardOptions.Default,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF8D736B),
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color(0xFF8D736B)
        )
    )
}

@Composable
fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF666666), fontSize = 15.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 15.sp)
    }
}