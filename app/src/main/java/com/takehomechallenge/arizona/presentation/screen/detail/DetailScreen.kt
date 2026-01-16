package com.takehomechallenge.arizona.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.takehomechallenge.arizona.R
import com.takehomechallenge.arizona.presentation.component.common.CharacterCard
import com.takehomechallenge.arizona.presentation.navigation.Screen
import com.takehomechallenge.arizona.presentation.theme.BackgroundDark
import com.takehomechallenge.arizona.presentation.theme.RickGreen
import com.takehomechallenge.arizona.presentation.theme.StatusGray
import com.takehomechallenge.arizona.presentation.theme.StatusGreen
import com.takehomechallenge.arizona.presentation.theme.StatusRed
import com.takehomechallenge.arizona.presentation.theme.SurfaceDark
import com.takehomechallenge.arizona.presentation.theme.TextGray

@Composable
fun DetailScreen(
    characterId: Int,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.getCharacterDetail(characterId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = RickGreen
            )
        } else if (state.character != null) {
            val character = state.character!!

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                item {
                    Box(modifier = Modifier.height(350.dp).fillMaxWidth()) {
                        AsyncImage(
                            model = character.image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, BackgroundDark),
                                        startY = 400f
                                    )
                                )
                        )
                    }
                }


                item {
                    Column(
                        modifier = Modifier
                            .offset(y = (-40).dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        GlassCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = character.name,
                                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        StatusBadgeLarge(status = character.status.value, species = character.species)
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleFavorite(character) },
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(Color.Black.copy(alpha = 0.6f))
                                    ) {
                                        Icon(
                                            imageVector = if (state.isFavorite)
                                                Icons.Default.Favorite
                                            else
                                                Icons.Default.FavoriteBorder,
                                            contentDescription = "Favorite",
                                            tint = RickGreen,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                Divider(color = Color.Gray.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "BIO & APPEARANCE",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextGray
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        BioItem(
                                            icon = R.drawable.ic_gender,
                                            label = "Gender",
                                            value = character.gender.value
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Box(modifier = Modifier.weight(1f)) {
                                        BioItem(
                                            icon = R.drawable.ic_species,
                                            label = "Species",
                                            value = character.species
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Box(modifier = Modifier.weight(1f)) {
                                        BioItem(
                                            icon = R.drawable.ic_type,
                                            label = "Type",
                                            value = if(character.type.isEmpty()) "N/A" else character.type
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "LOCATIONS",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextGray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    LocationItem(
                                        modifier = Modifier.weight(1f),
                                        label = "Origin",
                                        value = character.originName,
                                        icon = R.drawable.ic_origin
                                    )
                                    LocationItem(
                                        modifier = Modifier.weight(1f),
                                        label = "Last Known Location",
                                        value = character.locationName,
                                        icon = R.drawable.ic_last_location
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "EPISODE APPEARANCES",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(character.episode) { episodeUrl ->

                                val episodeNum = episodeUrl.split("/").last()
                                EpisodeChip(episodeNum)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                item {
                    Column {
                        Text(
                            text = "YOU MIGHT ALSO LIKE",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextGray,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.recommendations) { recChar ->
                                Box(modifier = Modifier.width(160.dp)) {
                                    CharacterCard(
                                        character = recChar,
                                        onClick = { navController.navigate(Screen.Detail.createRoute(recChar.id)) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }
    }
}

@Composable
fun GlassCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SurfaceDark.copy(alpha = 0.8f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
    ) {
        content()
    }
}

@Composable
fun StatusBadgeLarge(status: String, species: String) {
    val color = when (status.lowercase()) {
        "alive" -> StatusGreen
        "dead" -> StatusRed
        else -> StatusGray
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$status | $species",
            color = color,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun BioItem(icon: Int, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = Color(0xFFC5C7D7),
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                color = TextGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )

            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: Int
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.3f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = RickGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, color = TextGray, fontSize = 12.sp, lineHeight = 18.sp)
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EpisodeChip(episodeNum: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2C2C2E))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Episode $episodeNum", color = Color.White, fontSize = 14.sp)
        }
    }
}