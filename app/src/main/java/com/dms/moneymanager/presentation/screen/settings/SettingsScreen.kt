package com.dms.moneymanager.presentation.screen.settings

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.BuildConfig
import com.dms.moneymanager.R
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun SettingsScreen(
    onEvent: (BaseEvent) -> Unit,
    toastMessage: Int? = null
) {
    SettingsContent(
        onEvent = onEvent
    )

    toastMessage?.let { error ->
        Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        onEvent(BaseEvent.RemoveToast)
    }
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    onEvent: (SettingsEvent) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        Text(text = stringResource(R.string.settings))

        // TODO settings user
        SettingsItem(
            name = R.string.accounts
        ) {

        }

        if (BuildConfig.DEBUG) {
            Text(text = stringResource(R.string.debug))

            SettingsItem(
                name = R.string.settings_debug_save_state
            ) {
                onEvent(SettingsEvent.OnClickSaveState)
            }

            SettingsItem(
                name = R.string.settings_debug_restore_state
            ) {
                onEvent(SettingsEvent.OnClickRestoreState)
            }

            SettingsItem(
                name = R.string.settings_debug_remove_data
            ) {
                onEvent(SettingsEvent.OnClickRemoveData)
            }
        }
    }
}

@Composable
fun SettingsItem(
    @StringRes name: Int,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = onClick,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = name),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.surfaceTint
                        ),
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = ""
                )
            }

            HorizontalDivider()
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    MoneyManagerTheme {
        SettingsScreen(
            onEvent = { }
        )
    }
}