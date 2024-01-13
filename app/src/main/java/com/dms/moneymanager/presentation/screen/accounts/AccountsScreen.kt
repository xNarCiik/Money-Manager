package com.dms.moneymanager.presentation.screen.accounts

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
fun AccountsScreen(
    navController: NavHostController
) {
    AccountsContent(
    )
}

@Composable
private fun AccountsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        // TODO
        Text(text = stringResource(id = R.string.accounts))
    }
}

@Preview
@Composable
private fun AccountsPreview() {
    MoneyManagerTheme {
        AccountsScreen(
            navController = rememberNavController()
        )
    }
}