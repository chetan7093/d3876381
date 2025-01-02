package uk.ac.tees.mad.travelplanner.ui.screens

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.tees.mad.travelplanner.viewmodels.TripDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TripDetailsScreen(
    tripId: String,
    navController: NavHostController,
    viewModel: TripDetailsViewModel = hiltViewModel()
) {
    val trip by viewModel.trip.collectAsState(initial = null)
    val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())

    val tripPhotos = remember { mutableStateListOf<Bitmap>() }
    var showPhotoDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(tripId) {
        viewModel.loadTrip(tripId)
    }

    LaunchedEffect(trip) {
        if (trip != null && trip?.photoUrl?.isNotEmpty() == true) {
            launch(Dispatchers.IO) {
                tripPhotos.clear()
                val loader = ImageLoader(context)
                trip?.photoUrl?.forEach { url ->
                    val request = ImageRequest.Builder(context)
                        .data(url)
                        .allowHardware(false)
                        .build()

                    when (val result = loader.execute(request)) {
                        is ErrorResult -> {
                            result.throwable.printStackTrace()
                        }

                        is SuccessResult -> {
                            tripPhotos.add((result.drawable as BitmapDrawable).bitmap)
                        }
                    }
                }
            }
        }
    }

    val requestCameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            tripPhotos.add(it)
        }
    }

    // Permission for camera access
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA) {
        if (it) {
            requestCameraLauncher.launch(null)
        }
    }

    // Photo picker launcher for picking from gallery
    val requestGalleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            tripPhotos.add(bitmap)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.saveTrip(tripPhotos) }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "check")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { showPhotoDialog = true }) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Add Photo")
            }
        }
    ) { pad ->
        trip?.let { tripDetails ->
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                item {
                    Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .width(2.dp)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color.Green)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = tripDetails.startLocation,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = tripDetails.destination,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(
                                text = "From",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "To",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = dateFormat.format(tripDetails.startDate),
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = dateFormat.format(tripDetails.endDate),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Itinerary:", style = MaterialTheme.typography.titleMedium)

                    Text(
                        text = tripDetails.itinerary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(tripPhotos) { photo ->
                    Column(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .height(200.dp)
                            .fillMaxWidth()
                            .border(
                                1.5.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Image(
                            bitmap = photo.asImageBitmap(),
                            contentDescription = "New Trip Photo",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .background(Color.Red.copy(0.8f))
                                .fillMaxWidth()
                                .clickable {
                                    tripPhotos.remove(photo)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "delete",
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                }
            }

            if (showPhotoDialog) {
                AlertDialog(
                    onDismissRequest = { showPhotoDialog = false },
                    title = { Text("Add Photo") },
                    text = { Text("Choose an option to add a new photo.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showPhotoDialog = false
                            if (cameraPermissionState.status.isGranted) {
                                requestCameraLauncher.launch(null)
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }) {
                            Text("Camera")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showPhotoDialog = false
                            requestGalleryLauncher.launch("image/*")
                        }) {
                            Text("Gallery")
                        }
                    }
                )
            }
        }
    }
}