package com.example.c001apk.compose.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.util.performConfiguredHapticFeedback

@Composable
fun rememberHapticClick(
    type: HapticFeedbackType = HapticFeedbackType.TextHandleMove,
    onClick: () -> Unit,
): () -> Unit {
    val prefs = LocalUserPreferences.current
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    return remember(context, prefs.hapticFeedback, prefs.hapticStrength, hapticFeedback, type, onClick) {
        {
            if (prefs.hapticFeedback) {
                context.performConfiguredHapticFeedback(
                    fallback = { hapticFeedback.performHapticFeedback(type) },
                    enabled = prefs.hapticFeedback,
                    strength = prefs.hapticStrength,
                )
            }
            onClick()
        }
    }
}

@Composable
fun MoreMenuButton(onClick: () -> Unit) {
    IconButton(onClick = rememberHapticClick(onClick = onClick)) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = Icons.Default.MoreVert.name
        )
    }
}
