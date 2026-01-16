package com.takehomechallenge.arizona.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.takehomechallenge.arizona.R
import com.takehomechallenge.arizona.presentation.component.common.CharacterCard
import com.takehomechallenge.arizona.presentation.navigation.Screen
import com.takehomechallenge.arizona.presentation.theme.BackgroundDark
import com.takehomechallenge.arizona.presentation.theme.RickGreen
import com.takehomechallenge.arizona.presentation.theme.StatusRed
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(top = 16.dp)
    ) {

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

                IconButton(onClick = { showFilterSheet = true }) {
                    val isFilterActive = state.filterStatus != null ||
                            state.filterGender != null ||
                            state.filterSpecies.isNotEmpty() ||
                            state.filterType.isNotEmpty()

                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = if (isFilterActive) RickGreen else TextGray
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

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

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
                                    Icon(painter = painterResource(id = R.drawable.ic_history), contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
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
                    Text(
                        text = "Type name to search...",
                        color = TextGray
                    )
                }
            }

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    currentStatus: String?,
    currentGender: String?,
    currentSpecies: String,
    currentType: String,
    onApply: (String?, String?, String, String) -> Unit
) {
    var status by remember { mutableStateOf(currentStatus) }
    var gender by remember { mutableStateOf(currentGender) }
    var species by remember { mutableStateOf(currentSpecies) }
    var type by remember { mutableStateOf(currentType) }

    val commonSpecies = listOf("Human", "Alien", "Humanoid", "Robot", "Animal", "Cronenberg", "Mythological")
    val commonTypes = listOf("Genetic experiment", "Superhuman", "Parasite", "Human with antennae")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        dragHandle = {
            androidx.compose.material3.BottomSheetDefaults.DragHandle(
                width = 80.dp,
                height = 6.dp,
                color = TextGray
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Filters", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                TextButton(
                    onClick = {
                        status = null
                        gender = null
                        species = ""
                        type = ""
                    }
                ) {
                    Text("Reset", color = StatusRed)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text("Status", color = TextGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Alive", "Dead", "Unknown").forEach { item ->
                    val isSelected = status?.equals(item, ignoreCase = true) == true
                    FilterChip(
                        selected = isSelected,
                        onClick = { status = if (status == item) null else item },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RickGreen,
                            selectedLabelColor = BackgroundDark,
                            labelColor = Color.White,
                            containerColor = SurfaceDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = TextGray,
                            selectedBorderColor = RickGreen
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text("Gender", color = TextGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Male", "Female", "Genderless", "Unknown").forEach { item ->
                    val isSelected = gender?.equals(item, ignoreCase = true) == true
                    FilterChip(
                        selected = isSelected,
                        onClick = { gender = if (gender == item) null else item },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RickGreen,
                            selectedLabelColor = BackgroundDark,
                            labelColor = Color.White,
                            containerColor = SurfaceDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = TextGray,
                            selectedBorderColor = RickGreen
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text("Species", color = TextGray)
            Spacer(modifier = Modifier.height(8.dp))

            val isSpeciesFromChip = commonSpecies.any { it.equals(species, ignoreCase = true) }
            val isManualSpecies = species.isNotEmpty() && !isSpeciesFromChip

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                commonSpecies.forEach { item ->
                    val isSelected = species.equals(item, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { species = if (isSelected) "" else item },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RickGreen,
                            selectedLabelColor = BackgroundDark,
                            labelColor = Color.White,
                            containerColor = SurfaceDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = TextGray,
                            selectedBorderColor = RickGreen
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = if (isManualSpecies) species else "",
                onValueChange = { species = it },
                placeholder = { Text("Or type manually...") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = if (isManualSpecies) RickGreen else TextGray,
                    focusedBorderColor = RickGreen,
                    cursorColor = RickGreen
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text("Type", color = TextGray)
            Spacer(modifier = Modifier.height(8.dp))

            val isTypeFromChip = commonTypes.any { it.equals(type, ignoreCase = true) }
            val isManualType = type.isNotEmpty() && !isTypeFromChip

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                commonTypes.forEach { item ->
                    val isSelected = type.equals(item, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { type = if (isSelected) "" else item },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RickGreen,
                            selectedLabelColor = BackgroundDark,
                            labelColor = Color.White,
                            containerColor = SurfaceDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = TextGray,
                            selectedBorderColor = RickGreen
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = if (isManualType) type else "",
                onValueChange = { type = it },
                placeholder = { Text("Or type manually...") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = if (isManualType) RickGreen else TextGray,
                    focusedBorderColor = RickGreen,
                    cursorColor = RickGreen
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onApply(status, gender, species, type) },
                colors = ButtonDefaults.buttonColors(containerColor = RickGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Apply Filters", color = BackgroundDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}