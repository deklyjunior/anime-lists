package com.junior0028.assesstmentmobpro2.ui.theme.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.junior0028.assesstmentmobpro2.R
import com.junior0028.assesstmentmobpro2.model.Anime
import com.junior0028.assesstmentmobpro2.util.ViewModelFactory
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    animeId: Long,
    navController: NavHostController,
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val animeState by viewModel.animeState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(animeId == 0L) }
    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var episodeCount by remember { mutableStateOf("0") }
    var rating by remember { mutableFloatStateOf(0f) }
    var season by remember { mutableStateOf("") }
    var isWatched by remember { mutableStateOf(false) }

    LaunchedEffect(animeState) {
        animeState?.let { anime ->
            title = anime.title
            genre = anime.genre
            description = anime.description
            episodeCount = anime.episodeCount.toString()
            rating = anime.rating.toFloat()
            season = anime.season
            isWatched = anime.isWatched
        }
    }

    LaunchedEffect(animeId) {
        if (animeId > 0) {
            viewModel.getAnimeById(animeId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) {
                            if (animeId > 0) stringResource(R.string.edit_anime) else stringResource(R.string.add_anime)
                        } else {
                            animeState?.title ?: ""
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    if (!isEditMode && animeId > 0) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (animeId > 0 && !isEditMode) {
                FloatingActionButton(
                    onClick = { isEditMode = true }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_anime))
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = stringResource(R.string.error, error ?: ""),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                isEditMode -> {
                    EditAnimeContent(
                        title = title,
                        onTitleChange = { title = it },
                        genre = genre,
                        onGenreChange = { genre = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        episodeCount = episodeCount,
                        onEpisodeCountChange = { episodeCount = it },
                        rating = rating,
                        onRatingChange = { rating = it },
                        season = season,
                        onSeasonChange = { season = it },
                        isWatched = isWatched,
                        onWatchedChange = { isWatched = it },
                        onSave = {
                            val newAnime = if (animeId > 0) {
                                animeState?.copy(
                                    title = title,
                                    genre = genre,
                                    description = description,
                                    episodeCount = episodeCount.toIntOrNull() ?: 0,
                                    rating = rating.roundToInt(),
                                    season = season,
                                    isWatched = isWatched
                                )
                            } else {
                                Anime(
                                    id = 0L,
                                    title = title,
                                    genre = genre,
                                    description = description,
                                    episodeCount = episodeCount.toIntOrNull() ?: 0,
                                    rating = rating.roundToInt(),
                                    season = season,
                                    isWatched = isWatched,
                                    deletedAt = 0L
                                )
                            }

                            if (animeId > 0 && newAnime != null) {
                                viewModel.updateAnime(newAnime) {
                                    isEditMode = false
                                }
                            } else {
                                viewModel.insertAnime(
                                    Anime(
                                        id = 0L,
                                        title = title,
                                        genre = genre,
                                        description = description,
                                        episodeCount = episodeCount.toIntOrNull() ?: 0,
                                        rating = rating.roundToInt(),
                                        season = season,
                                        isWatched = isWatched,
                                        deletedAt = 0L
                                    )
                                ) {
                                    navController.navigateUp()
                                }
                            }
                        },
                        onCancel = {
                            if (animeId > 0) {
                                isEditMode = false
                            } else {
                                navController.navigateUp()
                            }
                        }
                    )
                }
                animeState != null -> {
                    AnimeDetailContent(
                        anime = animeState!!,
                        onWatchedChange = { watched -> viewModel.updateWatchStatus(watched) }
                    )
                }
                else -> {
                    if (animeId > 0) {
                        Text(
                            text = stringResource(R.string.not_found),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_confirmation)) },
            text = { Text(stringResource(R.string.delete_confirmation_message, animeState?.title ?: "")) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAnime {
                            showDeleteDialog = false
                            navController.navigateUp()
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun EditAnimeContent(
    title: String,
    onTitleChange: (String) -> Unit,
    genre: String,
    onGenreChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    episodeCount: String,
    onEpisodeCountChange: (String) -> Unit,
    rating: Float,
    onRatingChange: (Float) -> Unit,
    season: String,
    onSeasonChange: (String) -> Unit,
    isWatched: Boolean,
    onWatchedChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text(stringResource(R.string.title)) },
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = genre,
            onValueChange = onGenreChange,
            label = { Text(stringResource(R.string.genre)) },
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text(stringResource(R.string.desc)) },
            modifier = Modifier.fillMaxSize(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = episodeCount,
            onValueChange = onEpisodeCountChange,
            label = { Text(stringResource(R.string.eps)) },
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${stringResource(R.string.rating)}: ${rating.roundToInt()}",
            style = MaterialTheme.typography.bodyLarge
        )

        Slider(
            value = rating,
            onValueChange = onRatingChange,
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = season,
            onValueChange = onSeasonChange,
            label = { Text(stringResource(R.string.season)) },
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Checkbox(
                checked = isWatched,
                onCheckedChange = onWatchedChange
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.watched),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun AnimeDetailContent(
    anime: Anime,
    onWatchedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = anime.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = anime.season,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.eps, anime.episodeCount),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = stringResource(R.string.rating, anime.rating),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.genre, anime.genre),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.desc),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = anime.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = anime.isWatched,
                onCheckedChange = onWatchedChange
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(if (anime.isWatched) R.string.watch else R.string.watched),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}