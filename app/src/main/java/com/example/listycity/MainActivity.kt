package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import kotlin.collections.MutableList

class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )

    val cities: MutableList<String> get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CityRow(city: String, onClick: () -> Unit, isSelected: Boolean) {
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 19.dp, vertical = 14.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
fun CityListScreen(
    cities: MutableList<String>,
    onAddCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var isVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Button(onClick = { isVisible = true }) {
            Text("Add City")
        }

        if (isVisible) {
            Row(modifier = modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City name") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onAddCity(newCityName)
                            newCityName = ""
                        }
                    }
                ) {
                    Text("Confirm")
                }
            }
        }
        Button(
            onClick = {
                if (selectedCity != null) {
                    cities.remove(selectedCity)
                    selectedCity = null
                }
            },
            enabled = selectedCity != null,
        ) {
            Text(if (selectedCity != null) "Delete \"$selectedCity\"" else "Delete City")
        }
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(cities) { city ->
                val isSelected = (city == selectedCity)
                CityRow(
                    city = city,
                    isSelected = isSelected,
                    onClick = { selectedCity = city }
                )
            }
        }
    }
}
