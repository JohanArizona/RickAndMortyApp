package com.takehomechallenge.arizona.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.takehomechallenge.arizona.presentation.component.common.CharacterCard
import com.takehomechallenge.arizona.presentation.navigation.Screen
import com.takehomechallenge.arizona.presentation.theme.BackgroundDark
import com.takehomechallenge.arizona.presentation.theme.RickGreen
import com.takehomechallenge.arizona.presentation.theme.SurfaceDark
import com.takehomechallenge.arizona.presentation.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    // Bottom Sheet untuk Filter
    if (showFilterSheet) {
        FilterBottomSheet(
            onDismiss = { showFilterSheet = false },
            sheetState = sheetState,
            currentStatus = state.filterStatus,
            currentGender = state.filterGender,
            currentSpecies = state.filterSpecies,
            currentType = state.filterType,
            onApply = { status, gender, species, type ->
                viewModel.applyFilter(status, gender, species, type)
                showFilterSheet = false
            },
            onClear = {
                viewModel.clearFilter()
                showFilterSheet = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(top = 16.dp)
    ) {
        // --- Header & Search Bar ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                // Tombol Filter
                IconButton(onClick = { showFilterSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Filter",
                        tint = if (state.filterStatus != null || state.filterGender != null) RickGreen else TextGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = state.query,
                onValueChange = { viewModel.onQueryChange(it) },
                placeholder = { Text("Find character...", color = TextGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = TextGray,
                            modifier = Modifier.clickable { viewModel.onQueryChange("") }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = RickGreen,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Content Area ---
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            // 1. Kondisi: Input Kosong (Tampilkan History atau Hint)
            if (state.query.isEmpty()) {
                if (state.history.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Recent Searches",
                            color = TextGray,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        LazyColumn {
                            items(state.history) { historyItem ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.onHistoryClicked(historyItem) }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(text = historyItem, color = Color.White, modifier = Modifier.weight(1f))
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Remove",
                                        tint = TextGray,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable { viewModel.deleteHistory(historyItem) }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // History Kosong & Query Kosong -> Tampilkan Hint di Tengah
                    Text(
                        text = "Type name to search...",
                        color = TextGray
                    )
                }
            }

            // 2. Kondisi: Ada Input (Tampilkan Hasil Search)
            else {
                if (state.isLoading) {
                    CircularProgressIndicator(color = RickGreen)
                } else if (state.characters.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.characters) { character ->
                            CharacterCard(
                                character = character,
                                isFavorite = character.isFavorite,
                                onFavoriteClick = {
                                    viewModel.toggleFavorite(character)
                                },
                                onClick = { navController.navigate(Screen.Detail.createRoute(character.id)) }
                            )
                        }
                    }
                } else if (state.isEmpty) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TextGray, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "No character found", color = TextGray)
                    }
                }

                if (state.error != null) {
                    Text(text = state.error ?: "Error", color = Color.Red)
                }
            }
        }
    }
}

// UI Bottom Sheet Filter (Sama seperti sebelumnya)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    currentStatus: String?,
    currentGender: String?,
    currentSpecies: String,
    currentType: String,
    onApply: (String?, String?, String, String) -> Unit,
    onClear: () -> Unit
) {
    var status by remember { mutableStateOf(currentStatus) }
    var gender by remember { mutableStateOf(currentGender) }
    var species by remember { mutableStateOf(currentSpecies) }
    var type by remember { mutableStateOf(currentType) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Filters", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Filter UI Components (Chip, TextField)
            Text("Status", color = TextGray)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Alive", "Dead", "Unknown").forEach { item ->
                    FilterChip(
                        selected = status?.equals(item, ignoreCase = true) == true,
                        onClick = { status = if (status == item) null else item },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RickGreen,
                            selectedLabelColor = BackgroundDark,
                            labelColor = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text("Gender", color = TextGray)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Male", "Female", "Genderless").forEach { item ->
                    FilterChip(
                        selected = gender?.equals(item, ignoreCase = true) == true,
                        onClick = { gender = if (gender == item) null else item },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RickGreen,
                            selectedLabelColor = BackgroundDark,
                            labelColor = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = species,
                onValueChange = { species = it },
                label = { Text("Species") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = RickGreen, unfocusedBorderColor = TextGray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = RickGreen, unfocusedBorderColor = TextGray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onClear,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                    modifier = Modifier.weight(1f)
                ) { Text("Reset") }

                Button(
                    onClick = { onApply(status, gender, species, type) },
                    colors = ButtonDefaults.buttonColors(containerColor = RickGreen),
                    modifier = Modifier.weight(1f)
                ) { Text("Apply", color = BackgroundDark) }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}