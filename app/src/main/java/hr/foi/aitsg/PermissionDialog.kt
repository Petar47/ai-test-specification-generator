package hr.foi.aitsg

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()){
                Divider()
                Text(
                   text = if(isPermanentlyDeclined){
                       "Dozvoli"
                   }else {
                       "U redu"
                   },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        .clickable{
                            if(isPermanentlyDeclined){
                                onGoToAppSettingsClick()
                            }else{
                                onOkClick()
                            }
                        }
                )
            }
        },
        title = {
                Text(text = "Potrebna dozvola")
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                )
            )
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean) : String{
        return ""
    }
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Izgleda da ste trajno zabranili ovoj aplikaciji pristup kameri." +
            "Da bi koristili ovu funkcionalnost, dozvolite pristup kameri u postavkama."
        } else {
            "Da bi koristili ovu funkcionalnost potrebna Vam je dozvola za pristup kameri."
        }

    }
}

class FileDialogPermissiontextProvider: PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Izgleda da ste trajno zabranili ovoj aplikaciji pristup datotečnom sustavu." +
                    "Da bi koristili ovu funkcionalnost, dozvolite pristup datotečnom sustavu u postavkama."
        } else {
            "Da bi koristili ovu funkcionalnost potrebna Vam je dozvola za pristup datotečnom sustavu."
        }

    }
}