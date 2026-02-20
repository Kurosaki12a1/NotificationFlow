package com.kuro.notiflow.presentation.common.view

import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
fun PriorityIcon(
    level: Int
) {
    if (level >= IMPORTANCE_HIGH) {
        Icon(
            painter = painterResource(CommonR.drawable.ic_high_priority),
            contentDescription = "High Priority",
            tint = if (isSystemInDarkTheme()) Color(0xFFFF6659) else Color(0xFFD32F2F)
        )
    } else if (level == IMPORTANCE_DEFAULT) {
        Icon(
            painter = painterResource(CommonR.drawable.ic_priority),
            contentDescription = "Medium Priority",
            tint = if (isSystemInDarkTheme()) Color(0xFFFFD54F) else Color(0xFFF9A825)
        )
    } else {
        Icon(
            painter = painterResource(CommonR.drawable.ic_low_priority),
            contentDescription = "Low Priority",
            tint = if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF388E3C)
        )
    }
}

@Composable
fun PriorityInfo(level: Int) {
    if (level >= IMPORTANCE_HIGH) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSystemInDarkTheme()) Color(0xFFFF6659) else Color(0xFFD32F2F),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp)
        ) {
            Text(
                text = stringResource(CommonR.string.high_priority),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    } else if (level == IMPORTANCE_DEFAULT) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSystemInDarkTheme()) Color(0xFFFFD54F) else Color(0xFFF9A825),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp)
        ) {
            Text(
                text = stringResource(CommonR.string.medium_priority),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    } else {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF388E3C),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp)
        ) {
            Text(
                text = stringResource(CommonR.string.low_priority),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun getPriorityColor(
    level: Int
): Color {
    return if (level >= IMPORTANCE_HIGH) {
        if (isSystemInDarkTheme()) Color(0xFFFF6659) else Color(0xFFD32F2F)
    } else if (level == IMPORTANCE_DEFAULT) {
        if (isSystemInDarkTheme()) Color(0xFFFFD54F) else Color(0xFFF9A825)
    } else {
        if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF388E3C)
    }
}
