package hr.foi.aitsg.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.Project_user
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
