package com.dms.moneymanager.presentation.screen.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.moneymanager.R
import com.dms.moneymanager.presentation.util.getCurrentDateString
import com.dms.moneymanager.presentation.util.getLastDayOfMonthDateString
import com.dms.moneymanager.presentation.util.getTextColor
import com.dms.moneymanager.presentation.util.toAmountString
import com.dms.moneymanager.ui.theme.MoneyManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBalance(
    modifier: Modifier = Modifier,
    currentBalance: Float,
    futureBalance: Float,
    isExpended: Boolean,
    onClick: () -> Unit,
    onExpendedClick: () -> Unit,
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier.padding(bottom = if (!isExpended) 8.dp else 0.dp),
            shape = RoundedCornerShape(size = 8.dp),
            border = BorderStroke(width = 1.dp, color = Color.Black),
            onClick = onClick
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (isExpended) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.current_balance),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.padding(start = 6.dp),
                            text = "(Le ${getCurrentDateString()})",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = currentBalance.toAmountString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = currentBalance.getTextColor()
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.future_balance),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.padding(start = 6.dp),
                            text = "(Le ${getLastDayOfMonthDateString()})",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = futureBalance.toAmountString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = futureBalance.getTextColor()
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Visualiser en détail",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )

                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "icon right"
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.current_balance),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = currentBalance.toAmountString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            color = currentBalance.getTextColor()
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.future_balance),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = futureBalance.toAmountString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            color = futureBalance.getTextColor()
                        )
                    }
                }
            }
        }

        // TODO REWORK UI COLOR
        OutlinedButton(
            modifier = Modifier
                .padding(
                    start = if (isExpended) 15.dp else 0.dp,
                    bottom = if (isExpended) 8.dp else 0.dp
                )
                .size(size = 30.dp)
                .align(alignment = if (isExpended) Alignment.BottomStart else Alignment.BottomCenter),
            shape = CircleShape,
            contentPadding = PaddingValues(all = 0.dp),
            onClick = onExpendedClick,
        ) {
            Icon(
                imageVector = if (isExpended) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = ""
            )
        }
    }
}

@Preview
@Composable
private fun InfoBalanceNotExpendedPreview() {
    MoneyManagerTheme {
        InfoBalance(
            currentBalance = 100f,
            futureBalance = 0f,
            isExpended = false,
            onClick = { },
            onExpendedClick = { }
        )
    }
}

@Preview
@Composable
private fun InfoBalanceExpendedPreview() {
    MoneyManagerTheme {
        InfoBalance(
            currentBalance = 100f,
            futureBalance = 0f,
            isExpended = true,
            onClick = { },
            onExpendedClick = { }
        )
    }
}
