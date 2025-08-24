package com.kuro.notiflow.presentation.common.view

import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import androidx.compose.foundation.background
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
import com.kuro.notiflow.presentation.R

@Composable
fun PriorityIcon(
    level: Int
) {
    if (level >= IMPORTANCE_HIGH) {
        Icon(
            painter = painterResource(R.drawable.ic_high_priority),
            contentDescription = "High Priority",
            tint = Color.Red
        )
    } else if (level == IMPORTANCE_DEFAULT) {
        Icon(
            painter = painterResource(R.drawable.ic_priority),
            contentDescription = "Medium Priority",
            tint = Color.Yellow
        )
    } else {
        Icon(
            painter = painterResource(R.drawable.ic_low_priority),
            contentDescription = "Low Priority",
            tint = Color.Green
        )
    }
}

@Composable
fun PriorityInfo(level : Int) {
    if (level >= IMPORTANCE_HIGH) {
        Box(modifier = Modifier.background(Color.Red, RoundedCornerShape(8.dp)).padding(4.dp)) {
            Text(
                text = stringResource(R.string.high_priority),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    } else if (level == IMPORTANCE_DEFAULT) {
        Box(modifier = Modifier.background(Color.Yellow, RoundedCornerShape(8.dp)).padding(4.dp)) {
            Text(
                text = stringResource(R.string.medium_priority),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    } else {
        Box(modifier = Modifier.background(Color.Green, RoundedCornerShape(8.dp)).padding(4.dp)) {
            Text(
                text = stringResource(R.string.low_priority),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
