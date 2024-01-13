package com.dms.moneymanager.presentation.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dms.moneymanager.R
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun SettingsScreen(
    onEvent: (SettingsEvent) -> Unit,
    navController: NavHostController
) {
    SettingsContent(
    )
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        // TODO
        Text(text = stringResource(id = R.string.settings))

        // SettingsClickableComp(
        //     name = R.string.title,
        //     icon = R.drawable.ic_icon,
        //     iconDesc = R.string.icon_description,
        // ) {
        //     // here you can do anything - navigate - open other settings, ...
        // }
    }
}
//
// @Composable
// fun SettingsClickableComp(
//     @DrawableRes icon: Int,
//     @StringRes iconDesc: Int,
//     @StringRes name: Int,
//     onClick: () -> Unit
// ) {
//     Surface(
//         color = Color.Transparent,
//         modifier = Modifier
//             .fillMaxWidth()
//             .padding(16.dp),
//         onClick = onClick,
//     ) {
//         Column {
//             Row(
//                 verticalAlignment = Alignment.CenterVertically,
//                 horizontalArrangement = Arrangement.SpaceBetween
//             ) {
//                 Row(verticalAlignment = Alignment.CenterVertically) {
//                     Icon(
//                         painterResource(id = icon),
//                         contentDescription = stringResource(id = iconDesc),
//                         modifier = Modifier
//                             .size(24.dp)
//                     )
//                     Spacer(modifier = Modifier.width(8.dp))
//                     Text(
//                         text = stringResource(id = name),
//                         style = MaterialTheme.typography.bodyMedium.copy(
//                             color = MaterialTheme.colorScheme.surfaceTint
//                         ),
//                         modifier = Modifier
//                             .padding(16.dp),
//                         textAlign = TextAlign.Start,
//                         overflow = TextOverflow.Ellipsis,
//                     )
//                 }
//                 Spacer(modifier = Modifier.weight(1.0f))
//                 Icon(
//                     Icons.Rounded.KeyboardArrowRight,
//                     tint = MaterialTheme.colorScheme.surfaceTint,
//                     contentDescription = stringResource(id = R.string.ic_arrow_forward)
//                 )
//             }
//             Divider()
//         }
//
//     }
// }

@Preview
@Composable
private fun SettingsScreenPreview() {
    MoneyManagerTheme {
        SettingsScreen(
            onEvent = { },
            navController = rememberNavController()
        )
    }
}