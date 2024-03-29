package com.sarahiris.todo.user

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.google.android.material.snackbar.Snackbar
import com.sarahiris.todo.data.Api
import com.sarahiris.todo.user.ui.theme.ToDoSarahIrisTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserActivity : ComponentActivity() {

    private val capturedUri by lazy {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues() )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var bitmap: Bitmap? by remember { mutableStateOf(null) }
            var uri: Uri? by remember { mutableStateOf(null) }


            val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) uri = capturedUri
            }
            val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                uri = it
                lifecycleScope.launch {
                    uri?.let { it1 -> Api.userWebService.updateAvatar(it1.toRequestBody()) }
                }
            }
            val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    showMessage("Permission denied. Cannot pick a photo.")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Optional padding for better spacing
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(.2f),
                    model = bitmap ?: uri,
                    contentDescription = null
                )
                Button(
                    onClick = {takePicture.launch(capturedUri)},
                    content = { Text("Take picture") }
                )
                Button(
                    onClick = {
                        pickPhotoWithPermission(requestPermissionLauncher, pickMedia)},
                        //requestPermissionLauncher.launch((Manifest.permission.READ_EXTERNAL_STORAGE)) },
                        content = { Text("Pick photo") }
                )
                Button(
                    onClick = { super.onBackPressed() },
                    content = { Text("Go back") }
                )
            }
        }
    }

    private fun pickPhotoWithPermission(requestPermissionLauncher: ActivityResultLauncher<String>, pickMedia: ActivityResultLauncher<PickVisualMediaRequest>) {
        val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val permissionStatus = checkSelfPermission(storagePermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(storagePermission)

        when {
            isAlreadyAccepted -> {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            isExplanationNeeded -> {
                showMessage("Permission needed to pick a photo.")
            }
            else -> {
                requestPermissionLauncher.launch(storagePermission)
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpg")
        tmpFile.outputStream().use { // *use*: open et close automatiquement
            this.compress(Bitmap.CompressFormat.JPEG, 100, it) // *this* est le bitmap ici
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }

    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = fileBody
        )
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoSarahIrisTheme {
        Greeting("Android")
    }
}

