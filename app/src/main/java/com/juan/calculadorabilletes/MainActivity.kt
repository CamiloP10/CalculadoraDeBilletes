package com.juan.calculadorabilletes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.juan.calculadorabilletes.ui.theme.CalculadoraBilletesTheme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.Icons

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraBilletesTheme {
                CalculadoraBilletesApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraBilletesApp() {
    val denominaciones = listOf(100000, 50000, 20000, 10000, 5000, 2000)
    val cantidades = remember { mutableStateMapOf<Int, String>() }
    var totalMonedas by remember { mutableStateOf("") }

    val sumaBilletes = denominaciones.sumOf { (cantidades[it]?.toLongOrNull() ?: 0L) * it }
    val sumaMonedas = totalMonedas.toLongOrNull() ?: 0L
    val granTotal = sumaBilletes + sumaMonedas

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Contador de Efectivo", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(denominaciones) { denom ->
                    FilaBillete(denom = denom, valor = cantidades[denom] ?: "") { nuevo ->
                        cantidades[denom] = nuevo
                    }
                }
                item {
                    FilaBillete(label = "Monedas", valor = totalMonedas) { totalMonedas = it }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    OutlinedButton(
                        onClick = {
                            cantidades.clear()
                            totalMonedas = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Borrar Datos")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Card(// tarjeta para el total
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("TOTAL", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = "$ ${String.format("%,d", granTotal)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun FilaBillete(denom: Int? = null, label: String? = null, valor: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label ?: "$ ${String.format("%,d", denom)}",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = valor,
            onValueChange = { if (it.all { char -> char.isDigit() }) onValueChange(it) },
            label = { Text("Cantidad") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}