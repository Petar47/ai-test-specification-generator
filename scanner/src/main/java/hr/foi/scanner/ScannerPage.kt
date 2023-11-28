package hr.foi.scanner

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun ScannerPage(getTestData: (testData: String) -> Unit){
    val applicationContext = LocalContext.current
    var isScanned by remember { mutableStateOf(false) }
    var testData by remember { mutableStateOf("") }
    val controller = remember{
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    if(isScanned){
        getTestData(testData)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = {
                takePhoto(
                    applicationContext = applicationContext,
                    controller = controller,
                    onPhotoTaken = {photo ->
                        scanPhoto(photo, isScanned = {text ->
                            testData = text
                            isScanned = true
                        })

                    }
                    )
            },
            modifier = Modifier
                .padding(50.dp)
                .clip(shape = CircleShape)
                .size(70.dp)
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.primary)

        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = ImageVector.vectorResource(R.drawable.camera_snapshot),
                contentDescription = "Skeniraj",
                tint = MaterialTheme.colorScheme.background
            )
        }

    }
}

fun takePhoto(
    applicationContext: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
){
    controller.takePicture(
        ContextCompat.getMainExecutor(applicationContext),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                onPhotoTaken(image.toBitmap())
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Pogreska kod fotografiranja", exception)
            }
        }
    )
}

fun scanPhoto(photo: Bitmap, isScanned: (testData: String) -> Unit){
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = InputImage.fromBitmap(photo, 0)
    val result = recognizer.process(image)
        .addOnSuccessListener { visionText ->
            val scannedText = visionText.text
            Log.d("Scanner", "Ovo se skeniralo:  "+scannedText)
            isScanned(scannedText)
        }
        .addOnFailureListener { e ->
            Log.e("Scanner", "Pogreska kod skeniranja", e)
        }
}