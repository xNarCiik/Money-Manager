package com.dms.moneymanager.presentation.screen.commun

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dms.moneymanager.R
import com.dms.moneymanager.presentation.util.NavigationRoute

enum class MenuRoute {
    TRANSACTIONS, ACCOUNTS, SETTINGS
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
    navController: NavController
) {
    val bottomMenuItemsList = arrayListOf(
        MenuItem(
            menuRoute = MenuRoute.TRANSACTIONS,
            titleId = R.string.transactions,
            imageVector = Icons.AutoMirrored.Filled.List,
            onClick = { navController.navigate(NavigationRoute.TRANSACTIONS.route)}
        ),
        MenuItem(
            menuRoute = MenuRoute.ACCOUNTS,
            titleId = R.string.accounts,
            imageVector = Icons.Default.AccountBalance,
            onClick = { navController.navigate(NavigationRoute.ACCOUNTS.route)}
        ),
        MenuItem(
            menuRoute = MenuRoute.SETTINGS,
            titleId = R.string.settings,
            imageVector = Icons.Default.Settings,
            onClick = { navController.navigate(NavigationRoute.SETTINGS.route)}
        )
    )

    var selectedItem: MenuItem by remember { mutableStateOf(bottomMenuItemsList.first { it.menuRoute == defaultSelectedItem }) }

    BottomAppBar {
        bottomMenuItemsList.forEachIndexed { _, menuItem ->
            NavigationBarItem(
                selected = (selectedItem.titleId == menuItem.titleId),
                onClick = {
                    if (selectedItem.titleId != menuItem.titleId) {
                        selectedItem = menuItem
                        menuItem.onClick()
                    }
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
                }
            )
        }
    }
}