package com.example.feriferi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feriferi.R
import com.example.feriferi.viewmodel.AddToCartViewModel

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Get Shipping Details passed from CheckoutActivity
        val name = intent.getStringExtra("NAME") ?: ""
        val phone = intent.getStringExtra("PHONE") ?: ""
        val address = intent.getStringExtra("ADDRESS") ?: ""

        setContent {
            PaymentScreen(
                shippingName = name,
                shippingPhone = phone,
                shippingAddress = address,
                onSuccess = {
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            )
        }
    }
}

// Color constants
val BgBeige = Color(0xFFFDE8C9)
val CardPink = Color(0xFFF9A8D4)
val ButtonBrown = Color(0xFF9E5C2C)

@Composable
fun PaymentScreen(
    shippingName: String,
    shippingPhone: String,
    shippingAddress: String,
    onSuccess: () -> Unit,
    viewModel: AddToCartViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("Cash on Delivery") }

    // --- UPDATED OPTIONS: ONLY COD & E-SEWA ---
    val options = listOf("Cash on Delivery", "e-sewa")

    // ViewModel Data
    val subtotal = viewModel.getSubtotal()
    val shipping = viewModel.shippingFee
    val total = viewModel.getTotal()

    var showSuccessDialog by remember { mutableStateOf(false) }

    // --- LAUNCHER FOR E-SEWA ---
    val paymentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Payment Success -> Place Order
            viewModel.checkout(
                name = shippingName,
                phone = shippingPhone,
                address = shippingAddress,
                paymentMethod = "Online (e-sewa)"
            ) {
                showSuccessDialog = true
            }
        } else {
            Toast.makeText(context, "Payment Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    // --- SUCCESS DIALOG ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Order Placed Successfully!") },
            text = { Text("Your order has been sent to the Seller. Check 'Orders' in Dashboard for updates.") },
            confirmButton = {
                Button(onClick = onSuccess, colors = ButtonDefaults.buttonColors(containerColor = ButtonBrown)) {
                    Text("Go Home")
                }
            },
            containerColor = Color.White
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF7))
            .padding(horizontal = 16.dp)
    ) {
        // --- Top Bar ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("☰", fontSize = 24.sp)
            Text("फेरिPheri", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6D4C41))
            Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Payment Method",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- Payment Options List ---
        options.forEach { text ->
            PaymentMethodItem(
                text = text,
                selected = (text == selectedOption),
                onSelect = { selectedOption = text }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Pricing Summary ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardPink.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            PriceLine(label = "Transfer Amount", amount = subtotal.toInt().toString(), hasIcon = true)
            PriceLine(label = "Additional Cost", amount = shipping.toInt().toString())
            Spacer(modifier = Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))
            Spacer(modifier = Modifier.height(8.dp))
            PriceLine(label = "Total", amount = total.toInt().toString(), isTotal = true)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Pay Button ---
        Button(
            onClick = {
                if (selectedOption == "e-sewa") {
                    // Open Payment Gateway Activity
                    val intent = Intent(context, PaymentActivity::class.java)
                    intent.putExtra("AMOUNT", total.toInt().toString())
                    paymentLauncher.launch(intent)
                } else {
                    // Cash on Delivery -> Place Order Directly
                    viewModel.checkout(
                        name = shippingName,
                        phone = shippingPhone,
                        address = shippingAddress,
                        paymentMethod = "Cash on Delivery"
                    ) {
                        showSuccessDialog = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(65.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBrown),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Pay Rs ${total.toInt()}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PaymentMethodItem(text: String, selected: Boolean, onSelect: () -> Unit) {
    val borderColor = if (selected) ButtonBrown else Color.Transparent
    val backgroundColor = if (selected) CardPink else Color(0xFFF5F5F5)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .selectable(selected = selected, onClick = onSelect)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- UPDATED IMAGES: Only COD and eSewa ---
        val imageRes = when (text) {
            "Cash on Delivery" -> R.drawable.cashondeliver
            "e-sewa" -> R.drawable.eshewa
            else -> R.drawable.google // Just a fallback, won't be used now
        }

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp, 40.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .padding(4.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp).weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = ButtonBrown)
        )
    }
}

@Composable
fun PriceLine(label: String, amount: String, hasIcon: Boolean = false, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 20.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Medium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasIcon) Text("Rs ", fontSize = 16.sp)
            Text(
                text = amount,
                fontSize = if (isTotal) 20.sp else 16.sp,
                fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}