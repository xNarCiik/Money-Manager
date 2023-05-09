package com.dms.moneymanager.presentation.screen.commun

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.dms.moneymanager.R

enum class MenuRoute {
    HOME, SETTING
}

data class MenuItem(
    val menuRoute: MenuRoute,
    @StringRes val titleId: Int,
    val imageVector: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun BottomBar(
    defaultSelectedItem: MenuRoute,
    onHomeClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    val bottomMenuItemsList = arrayListOf(
        MenuItem(
            menuRoute = MenuRoute.HOME,
            titleId = R.string.add_account,
            imageVector = Icons.Default.Home,
            onClick = onHomeClick
        ),
        MenuItem(
            menuRoute = MenuRoute.SETTING,
            titleId = R.string.add_account,
            imageVector = Icons.Default.Settings,
            onClick = onSettingClick
        )
    )

    var selectedItem: MenuItem by remember { mutableStateOf(bottomMenuItemsList.first { it.menuRoute == defaultSelectedItem }) }

    BottomAppBar(cutoutShape = CircleShape) {
        bottomMenuItemsList.forEachIndexed { _, menuItem ->
            BottomNavigationItem(
                selected = (selectedItem == menuItem),
                onClick = {
                    selectedItem = menuItem
                    menuItem.onClick()
                },
                label = {
                    Text(
                        text = stringResource(id = menuItem.titleId),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                icon = {
                    Icon(
                        imageVector = menuItem.imageVector,
                        contentDescription = stringResource(id = menuItem.titleId)
                    )
                },
                enabled = true
            )
        }
    }
}